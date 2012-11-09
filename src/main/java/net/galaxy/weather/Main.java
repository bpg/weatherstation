/*
 * Copyright (C) 2012 Pavel Boldyrev <pboldyrev@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.galaxy.weather;

import com.google.common.base.Charsets;
import net.galaxy.rfbee.RFBee;
import net.galaxy.rfbee.ReceiveCallback;
import net.galaxy.rfbee.ReceivedMessage;
import net.galaxy.rfbee.SignalQuality;
import net.galaxy.rfbee.impl.RFBeeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Created with IntelliJ IDEA.
 * Pavel Boldyrev
 * 18/09/12 10:11 PM
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class.getSimpleName());

    public static final int MESSAGE_TERMINATOR = 0x0A;

    public static void main(String[] args) throws Exception {

        String version = "UNDEF";
        Enumeration<URL> resources = Main.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
        try {
            while (resources.hasMoreElements()) {
                Manifest manifest = new Manifest(resources.nextElement().openStream());
                Attributes attributes = manifest.getMainAttributes();
                if ("weatherstation".equals(attributes.getValue("Bundle-SymbolicName"))) {
                    version = attributes.getValue("Bundle-Version");
                }
            }
        } catch (IOException ex) {
            logger.warn("Error while reading MANIFEST: " + ex.getMessage());
            System.exit(255);
        }
        System.out.println("Weather Station v. " + version + "\nCopyright (C) 2012  Pavel Boldyrev\n" +
                "This program comes with ABSOLUTELY NO WARRANTY; see the Apache License Version 2.0 for more details <http://www.apache.org/licenses/LICENSE-2.0>\n");

        final BlockingQueue<MeasurementDto> queue = new LinkedBlockingDeque<>(4);
        final Map<Integer, SignalQuality> nodes = new HashMap<>();
        Thread processor = new Thread(new DataProcessor(queue), "PROCESSOR");
        processor.start();

        int exitStatus = 0;
        try {
            RFBee bee = new RFBeeImpl(MESSAGE_TERMINATOR);
            bee.registerReceiveCallback(new ReceiveCallback() {
                @Override
                public void receive(ReceivedMessage message) {
                    try {
                        int src = message.getSrc();
                        SignalQuality sq = message.getSignalQuality();
                        nodes.put(src, sq);
                        logger.info("Signal quality from SRC[{}] : {}", src, sq);

                        queue.put(MeasurementParser.parse(src, new String(message.getPayload(), Charsets.US_ASCII).trim()));
                    } catch (InterruptedException ex) {
                        logger.error("Interrupted", ex);
                    }
                }
            });

            logger.info("Listening...");
            synchronized (Main.class) {
                Main.class.wait();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            exitStatus = 255;
        }

        processor.interrupt();
        logger.info("FINISHED");
        System.exit(exitStatus);
    }
}
