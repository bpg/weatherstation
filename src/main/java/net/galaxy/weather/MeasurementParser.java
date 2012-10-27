/*
 * Project: weatherstation, file: MeasurementParser.java
 * Copyright (C) 2012 Pavel Boldyrev
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

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.MathContext;

/**
 * Created with IntelliJ IDEA.
 * Pavel Boldyrev
 * 16/09/12 7:51 PM
 */
public class MeasurementParser {

    private static final Logger logger = LoggerFactory.getLogger(DataReader.class.getSimpleName());

    private static final long DT_ADD = 946684800000L;

    //~192:644|0|404|1900^42=
    public static MeasurementDto parse(String str) throws IllegalArgumentException {
        Preconditions.checkNotNull(str, "String is null");
        Preconditions.checkArgument(str.length() > 6, "Invalid string: [%s]", str);
        Preconditions.checkArgument(str.charAt(0) == '~', "Invalid string: [%s]", str);
        Preconditions.checkArgument(str.charAt(str.length() - 1) == '=', "Invalid string: [%s]", str);

        logger.debug("parsing: [{}]", str);

        String payload = str.substring(1, str.lastIndexOf('^'));
        logger.trace("  payload: `{}`", payload);
        String receivedCrcStr = str.substring(str.lastIndexOf('^') + 1, str.length() - 1);
        logger.trace("  CRC16: `{}`", receivedCrcStr);

        int calculatedCrc;
        try {
            calculatedCrc = Digest.crc16(payload.getBytes("ASCII")) & 0xFFFF;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        int receivedCrc = Integer.valueOf(receivedCrcStr, 16);
        if (calculatedCrc != receivedCrc) {
            throw new IllegalArgumentException(String.format("Checksum mismatch for string [%s]", str));
        }

        String[] parts = payload.split("\\|");
        if (parts.length != 5) {
            throw new IllegalArgumentException(String.format("Invalid format -- missing parts in string [%s]", str));
        }

        long timestamp = Long.parseLong(parts[0]) * 1000 + DT_ADD;

        String[] thParts = parts[1].split(":");
        double temp = 0;
        double humid = 0;
        if (thParts.length == 2) {
            temp = Integer.valueOf(thParts[0], 10) / 10.0;
            humid = Integer.valueOf(thParts[1], 10) / 10.0;
            // temperature sensor error
        }

        MathContext mc = new MathContext(2);
        return new MeasurementDto(
                timestamp,
                temp,
                humid,
                Integer.valueOf(parts[4], 10) / 100.0,
                MeasurementDto.BatteryStatus.values()[Integer.valueOf(parts[2], 10)],
                Integer.valueOf(parts[3], 10) / 100.0
        );
    }
}
