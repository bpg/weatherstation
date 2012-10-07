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
