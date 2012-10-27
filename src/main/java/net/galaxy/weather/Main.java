package net.galaxy.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created with IntelliJ IDEA.
 * Pavel Boldyrev
 * 18/09/12 10:11 PM
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class.getSimpleName());


    public static void main(String[] args) throws Exception {

        BlockingQueue<MeasurementDto> queue = new LinkedBlockingDeque<>(4);
        Thread processor = new Thread(new DataProcessor(queue), "PROCESSOR");
        processor.start();

        int extirStatus = 0;
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
            extirStatus = 255;
        }

        processor.interrupt();
        logger.info("FINISHED");
        System.exit(extirStatus);
    }
}
