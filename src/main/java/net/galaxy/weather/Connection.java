package net.galaxy.weather;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 * Created with IntelliJ IDEA.
 * Pavel Boldyrev
 * 16/09/12 8:53 PM
 */
public class Connection {

    private static final Logger logger = LoggerFactory.getLogger(Connection.class.getSimpleName());

    private SerialPort serialPort;

    private static final String PORT_OWNER = "WeatherStation";
    private static final String PORT_NAME = System.getProperty("serial.port.name", "/dev/ttyUSB0");
    private static final int PORT_BAUD_RATE = 9600;

    private InputStream inputStream;
    private OutputStream outputStream;

    public void open()  {
        Preconditions.checkArgument(!PORT_NAME.isEmpty());
        logger.info("Opening serial port `{}`", PORT_NAME);

        String ports = System.getProperty("gnu.io.rxtx.SerialPorts", "");
        ports += PORT_NAME + File.pathSeparator; // pathSeparator -- is it required??
        System.setProperty("gnu.io.rxtx.SerialPorts", ports);
        logger.debug("Port has been added to RxTx system properties. New `gnu.io.rxtx.SerialPorts`: `{}`", ports);

        try {
            Enumeration<?> identifiers = CommPortIdentifier.getPortIdentifiers();
            logger.trace("Identifiers: {}", Joiner.on(";").join(Collections.list(identifiers)));
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(PORT_NAME);
            if (portIdentifier.isCurrentlyOwned()) {
                throw new IOException("Port is currently in use");
            }
            CommPort commPort = portIdentifier.open(PORT_OWNER, 0);
            if (!(commPort instanceof SerialPort)) {
                throw new RuntimeException("Not a serial port");
            }
            serialPort = (SerialPort) commPort;

            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();

            initializeSerialPort(serialPort);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void close() {
        if (serialPort == null) {
            return;
        }

        try {
            serialPort.close();
        } catch (Exception e) {
            logger.debug("", e);
        } finally {
            serialPort = null;
        }
    }

    public InputStream getInputStream() throws IOException {
        if (serialPort == null) {
            throw new IOException("serial port is closed");
        }
        return inputStream;
    }

    public OutputStream getOutputStream() throws IOException {
        if (serialPort == null) {
            throw new IOException("serial port is closed");
        }
        return outputStream;
    }

    private void initializeSerialPort(final SerialPort serialPort)
            throws UnsupportedCommOperationException, TooManyListenersException, IOException {
        serialPort.setSerialPortParams(PORT_BAUD_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
    }


}
