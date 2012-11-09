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

import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * Pavel Boldyrev
 * 18/09/12 10:05 PM
 */
public class DataProcessor implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DataProcessor.class.getSimpleName());

    private static final File SNAPSHOT_FILE = new File(System.getProperty("snapshot.file", "/var/lib/weartherstation/snapshot"));

    private final BlockingQueue<MeasurementDto> queue;

    public DataProcessor(BlockingQueue<MeasurementDto> queue) throws IOException {
        this.queue = queue;
        Files.createParentDirs(SNAPSHOT_FILE);
    }

    @Override
    public void run() {
        logger.info("Start processor");
        try {
            while (!Thread.currentThread().isInterrupted()) {
                MeasurementDto dto = queue.take();
                logger.info("{}", MeasurementFormatter.toString(dto));
                try {
                    Files.write(MeasurementFormatter.toValues(dto).getBytes("ASCII"), SNAPSHOT_FILE);
                } catch (IOException ex) {
                    logger.error("Unable to write snapshot information to " + SNAPSHOT_FILE, ex);
                }
            }
        } catch (InterruptedException ex) {
            logger.info("Processor has been terminated");
        } catch (Exception ex) {
            logger.warn("Processor has been terminated,  exception:", ex);
        }
    }
}
