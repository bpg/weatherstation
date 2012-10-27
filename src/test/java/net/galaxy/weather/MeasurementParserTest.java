/*
 * Project: weatherstation, file: MeasurementParserTest.java
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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * Pavel Boldyrev
 * 16/09/12 7:53 PM
 */
public class MeasurementParserTest {

    @Test
    public void testParse() throws Exception {
        MeasurementDto dto = MeasurementParser.parse("~401317542|242:431|0|402|2725^1DE8=");
        assertNotNull(dto);
        System.out.println(MeasurementFormatter.toString(dto));
        assertEquals(24.2, dto.getTemperature(), 0.001);
        assertEquals(43.1, dto.getHumidity(), 0.001);
        assertEquals(MeasurementDto.BatteryStatus.SLEEPING, dto.getBatteryStatus());
        assertEquals(4.02, dto.getBatteryVoltage(), 0.001);
        assertEquals(27.25, dto.getTemperatureInternal(), 0.001);
    }
}
