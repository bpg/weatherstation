/*
 * Project: weatherstation, file: Main.java
 * Copyright (C) 2012 Pavel Boldyrev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.galaxy.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
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
                "This program comes with ABSOLUTELY NO WARRANTY; see the GNU General Public License for more details <http://www.gnu.org/licenses/>\n");

        BlockingQueue<MeasurementDto> queue = new LinkedBlockingDeque<>(4);
        Thread processor = new Thread(new DataProcessor(queue), "PROCESSOR");
        processor.start();

        int extitStatus = 0;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                queue.clear();

                Connection connection = new Connection();
                connection.open();

                logger.info("Starting threads...");
                CyclicBarrier barrier = new CyclicBarrier(2);
                new Thread(new DataReader(connection.getInputStream(), queue, barrier), "READER").start();
                logger.info("Listening...");
                barrier.await();

                logger.info("Stop listening");
                connection.close();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            extitStatus = 255;
        }

        processor.interrupt();
        logger.info("FINISHED");
        System.exit(extitStatus);
    }
}
