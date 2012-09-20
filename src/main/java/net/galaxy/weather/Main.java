package net.galaxy.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created with IntelliJ IDEA.
 * Pavel Boldyrev
 * 18/09/12 10:11 PM
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) throws Exception {
        Connection connection = new Connection();
        logger.info("Opening connection...");
        connection.open();

        BlockingQueue<MeasurementDto> queue = new LinkedBlockingDeque<MeasurementDto>(4);

        logger.info("Starting threads...");
        new Thread(new DataReader(connection.getInputStream(), queue), "READER").start();
        new Thread(new DataProcessor(queue), "PROCESSOR").start();

        while (true) {

        }
    }
}
