package net.galaxy.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * Pavel Boldyrev
 * 18/09/12 9:39 PM
 */
public class DataReader implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DataReader.class.getSimpleName());

    private final InputStream inputStream;
    private final BlockingQueue<MeasurementDto> queue;

    public DataReader(InputStream inputStream, BlockingQueue<MeasurementDto> queue) {
        this.inputStream = inputStream;
        this.queue = queue;
    }

    @Override
    public void run() {
        ByteBuffer buffer = ByteBuffer.allocate(128);

        while (!Thread.currentThread().isInterrupted()) {
            try {
                int ch = -1;
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

            buffer.rewind();
        }

        logger.warn("Reader terminated: Thread was interrupted");
    }
}
