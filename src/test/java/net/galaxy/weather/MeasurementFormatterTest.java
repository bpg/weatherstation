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

/**
 * Created with IntelliJ IDEA.
 * Pavel Boldyrev
 * 16/09/12 7:39 PM
 */
public class MeasurementFormatterTest {

    @BeforeClass
    public static void setUp() throws Exception {
        //turn off logging
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.getLogger("root").setLevel(Level.OFF);
    }


    @Test
    public void testFormatter() {
        MeasurementDto dto = new MeasurementDto(
                0,
                System.currentTimeMillis(),
                22.1,
                44.5,
                22.15,
                MeasurementDto.BatteryStatus.CHARGING,
                4.01);
        String str1 = MeasurementFormatter.toString(dto);
        //System.out.println(str1);
        String str2 = MeasurementFormatter.toValues(dto);
        //System.out.println(str2);
    }

}


