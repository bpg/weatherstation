/*
 * Project: weatherstation, file: MeasurementFormatterTest.java
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

/**
 * Created with IntelliJ IDEA.
 * Pavel Boldyrev
 * 16/09/12 7:39 PM
 */
public class MeasurementFormatterTest {

    @Test
    public void testFormatter() {
        MeasurementDto dto = new MeasurementDto(
                System.currentTimeMillis(),
                22.1,
                44.5,
                22.15,
                MeasurementDto.BatteryStatus.CHARGING,
                4.01);
        System.out.println(MeasurementFormatter.toString(dto));
        System.out.println(MeasurementFormatter.toValues(dto));
    }

}


