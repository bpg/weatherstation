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
        return String.format("temp.value %.2f\nhumid.value %.2f\ninttemp.value %.2f\nbvoltage.value %.2f\nbstatus.value %d",
                dto.getTemperature(), dto.getHumidity(),
                dto.getTemperatureInternal(), dto.getBatteryVoltage(), dto.getBatteryStatus().equals(MeasurementDto.BatteryStatus.CHARGING) ? 1 : 0);
    }
}
