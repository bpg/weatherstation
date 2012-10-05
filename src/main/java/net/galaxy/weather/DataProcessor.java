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
        } catch (Exception ex) {
            logger.warn("Processor terminated: exception", ex);
        }
    }
}
