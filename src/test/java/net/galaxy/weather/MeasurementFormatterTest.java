package net.galaxy.weather;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created with IntelliJ IDEA.
 * Pavel Boldyrev
 * 16/09/12 7:39 PM
 */
public class MeasurementFormatterTest {

    @Test
    public void testFormatter() {
        String str = MeasurementFormatter.toString(new MeasurementDto(
                System.currentTimeMillis(),
                22.1,
                44.5,
                22.15,
                MeasurementDto.BatteryStatus.CHARGING,
                4.01));
        System.out.println(str);

    }

}


