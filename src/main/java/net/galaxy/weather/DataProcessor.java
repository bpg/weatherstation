package net.galaxy.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.concurrent.BlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * Pavel Boldyrev
 * 18/09/12 10:05 PM
 */
public class DataProcessor implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DataProcessor.class.getSimpleName());

    private final BlockingQueue<MeasurementDto> queue;

    public DataProcessor(BlockingQueue<MeasurementDto> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                MeasurementDto dto = queue.take();
                logger.info("Received: {}", MeasurementFormatter.toString(dto));
            }
        } catch (Exception ex) {
            logger.warn("Processor terminated: exception", ex);
        }
    }
}
