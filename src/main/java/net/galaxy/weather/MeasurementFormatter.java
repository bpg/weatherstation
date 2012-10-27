/*
 * Project: weatherstation, file: MeasurementFormatter.java
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

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created with IntelliJ IDEA.
 * Pavel Boldyrev
 * 16/09/12 7:32 PM
 */
public class MeasurementFormatter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZoneUTC();

    public static String toString(MeasurementDto dto) {
        return String.format("%s :: temp: %.2f°C, humid: %.2f%%, int temp: %.2f°C, batt voltage: %.2fv, batt status: %s",
                DATE_TIME_FORMATTER.print(dto.getTimestamp()), dto.getTemperature(), dto.getHumidity(),
                dto.getTemperatureInternal(), dto.getBatteryVoltage(), dto.getBatteryStatus());
    }

    /**
     * Munin-cpecific format
     *
     * @return
     */
    public static String toValues(MeasurementDto dto) {
        return String.format("temp_out.value %.2f\nhumid_out.value %.2f\ntemp_int.value %.2f\nbat_volt.value %.2f\nbat_stat.value %d\n",
                dto.getTemperature(), dto.getHumidity(),
                dto.getTemperatureInternal(), dto.getBatteryVoltage(), dto.getBatteryStatus().equals(MeasurementDto.BatteryStatus.CHARGING) ? 5 : 0);
    }
}
