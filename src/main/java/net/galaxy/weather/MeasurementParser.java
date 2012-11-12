/*
 * Copyright (C) 2012 Pavel Boldyrev <pboldyrev@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

    private static final Logger logger = LoggerFactory.getLogger(MeasurementParser.class.getSimpleName());

    private static final long DT_ADD = 946684800000L;

    //~192:644|0|404|1900^42=
    public static MeasurementDto parse(int src, String str) throws IllegalArgumentException {
        Preconditions.checkNotNull(str, "String is null");
        Preconditions.checkArgument(str.length() > 6, "Invalid string: [%s]", str);

        logger.debug("parsing: [{}]", str);
        try {
            String payload = str.substring(0, str.lastIndexOf('^'));
            logger.trace("  payload: `{}`", payload);
            String receivedCrcStr = str.substring(str.lastIndexOf('^') + 1, str.length());
            logger.trace("  CRC16: `{}`", receivedCrcStr);

            int calculatedCrc;
            try {
                calculatedCrc = Digest.crc16(payload.getBytes("ASCII")) & 0xFFFF;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            int receivedCrc = Integer.valueOf(receivedCrcStr, 16);
            if (calculatedCrc != receivedCrc) {
                throw new IllegalArgumentException("Checksum mismatch");
            }

            String[] parts = payload.split("\\|");
            if (parts.length != 5) {
                throw new IllegalArgumentException("Invalid format -- missing parts");
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
                    src,
                    timestamp,
                    temp,
                    humid,
                    Integer.valueOf(parts[4], 10) / 100.0,
                    MeasurementDto.BatteryStatus.values()[Integer.valueOf(parts[2], 10)],
                    Integer.valueOf(parts[3], 10) / 100.0
            );
        } catch (Exception ex) {
            throw new IllegalArgumentException(String.format("Error parsing string [%s]: %s", str, ex.getMessage()));
        }
    }
}
