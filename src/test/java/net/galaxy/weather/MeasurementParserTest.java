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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * Pavel Boldyrev
 * 16/09/12 7:53 PM
 */
public class MeasurementParserTest {

    @BeforeClass
    public static void setUp() throws Exception {
        //turn off logging
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.getLogger("root").setLevel(Level.OFF);
    }


    @Test
    public void testParse() throws Exception {
        MeasurementDto dto = MeasurementParser.parse(0, "401317542|242:431|0|402|2725^1DE8");
        assertNotNull(dto);
        //System.out.println(MeasurementFormatter.toString(dto));
        assertEquals(24.2, dto.getTemperature(), 0.001);
        assertEquals(43.1, dto.getHumidity(), 0.001);
        assertEquals(MeasurementDto.BatteryStatus.SLEEPING, dto.getBatteryStatus());
        assertEquals(4.02, dto.getBatteryVoltage(), 0.001);
        assertEquals(27.25, dto.getTemperatureInternal(), 0.001);
    }
}
