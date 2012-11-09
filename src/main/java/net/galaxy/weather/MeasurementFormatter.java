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
        return String.format("SRC[%d] - %s :: temp: %.2f°C, humid: %.2f%%, int temp: %.2f°C, batt voltage: %.2fv, batt status: %s",
                dto.getSource(), DATE_TIME_FORMATTER.print(dto.getTimestamp()), dto.getTemperature(), dto.getHumidity(),
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
