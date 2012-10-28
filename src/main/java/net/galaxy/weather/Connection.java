/*
 * Project: weatherstation, file: Connection.java
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

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
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
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Pavel Boldyrev
 * 16/09/12 8:53 PM
 */
public class Connection {

    private static final Logger logger = LoggerFactory.getLogger(Connection.class.getSimpleName());

    private SerialPort serialPort;

    private static final String PORT_OWNER = "WeatherStation";
    private static final String PORT_NAME = System.getProperty("serial.port.name", "/dev/ttyUSB*");
    private static final int PORT_BAUD_RATE = Integer.getInteger("serial.port.baud", 9600);

    private InputStream inputStream;
    private OutputStream outputStream;

    public void open() {
        logger.info("Opening port...");
        Preconditions.checkArgument(!PORT_NAME.isEmpty());
        List<String> ports = new ArrayList<>();
        if (PORT_NAME.endsWith("*")) {
            for (int i = 0; i <= 9; i++) {
                ports.add(PORT_NAME.replace("*", String.valueOf(i)));
            }
        } else {
            ports.add(PORT_NAME);
        }

        for (String port : ports) {
            try {
                serialPort = openPort(port);
                break;
            } catch (Exception ex) {
                logger.info("Port {} is unavailable: " + ex.getMessage(), port);
            }
        }
        if (serialPort == null) {
            throw new RuntimeException("Unable to open serial port");
        }

        try {
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            initializeSerialPort(serialPort);

            logger.debug("Serial port {} is ready", serialPort.getName());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private SerialPort openPort(String portName) throws IOException {
        Preconditions.checkNotNull(portName);

        logger.debug("Opening serial port: {}", portName);
        File file = new File(portName);
        if (!file.exists()) {
            throw new IOException("Port " + portName + " does not exist");
        }

        System.setProperty("gnu.io.rxtx.SerialPorts", portName);
        try {
            Enumeration<?> identifiers = CommPortIdentifier.getPortIdentifiers();
            logger.trace("Identifiers: {}", Joiner.on(";").join(Collections.list(identifiers)));
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            if (portIdentifier.isCurrentlyOwned()) {
                throw new IOException("Port is currently in use");
            }
            CommPort commPort = portIdentifier.open(PORT_OWNER, 0);
            if (!(commPort instanceof SerialPort)) {
                throw new IOException("Not a serial port");
            }
            return (SerialPort) commPort;
        } catch (Exception ex) {
            Throwables.propagateIfPossible(ex, IOException.class);
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
        logger.debug("Set baud rate: {}", PORT_BAUD_RATE);
    }


}
