/*
 * Project: weatherstation, file: DataProcessor.java
 * Copyright (C) 2012 Pavel Boldyrev <pboldyrev@gmail.com>
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
