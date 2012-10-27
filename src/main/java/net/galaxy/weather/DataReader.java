/*
 * Project: weatherstation, file: DataReader.java
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;

/**
 * Created with IntelliJ IDEA.
 * Pavel Boldyrev
 * 18/09/12 9:39 PM
 */
public class DataReader implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DataReader.class.getSimpleName());

    private final InputStream inputStream;
    private final BlockingQueue<MeasurementDto> queue;
    private final CyclicBarrier barrier;

    public DataReader(InputStream inputStream, BlockingQueue<MeasurementDto> queue, CyclicBarrier barrier) {
        this.inputStream = inputStream;
        this.queue = queue;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        ByteBuffer buffer = ByteBuffer.allocate(128);

        try {
            int ch;
            // skip all bytes until find a new message
            while (true) {
                ch = inputStream.read();
                if (ch == -1) {
                    throw new IOException("No more data");
                }
                if (ch == (int) '~') {
                    buffer.put((byte) ch);
                    break;
                }
            }

            // start reading the message
            int count = 1; // '~' already in buffer
            while (true) {
                ch = inputStream.read();
                if (ch == -1) {
                    throw new IOException("No more data");
                }
                if (ch == (int) '~') {
                    throw new IOException("Data format exception");
                }
                buffer.put((byte) ch);
                count++;
                if (ch == (int) '=') {
                    break;
                }
            }

            try {
                queue.put(MeasurementParser.parse(new String(buffer.array(), 0, count, "ASCII").trim()));
            } catch (IllegalArgumentException ex) {
                logger.error("Data error", ex);
            }
        } catch (Exception ex) {
            logger.error("Error reading data. Reset", ex);
        }

        try {
            barrier.await();
        } catch (Exception ex) {
            logger.error("Barrier exception", ex);
        }

        logger.warn("Reader terminated");
    }
}
