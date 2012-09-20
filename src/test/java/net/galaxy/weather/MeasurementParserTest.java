package net.galaxy.weather;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import static org.junit.Assert.*;

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
