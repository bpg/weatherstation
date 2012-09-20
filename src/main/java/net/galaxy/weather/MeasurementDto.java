package net.galaxy.weather;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created with IntelliJ IDEA.
 * Pavel Boldyrev
 * 16/09/12 7:27 PM
 */
public class MeasurementDto {

    public enum BatteryStatus {
        SLEEPING,
        CHARGING,
        CHARGED,
        ERROR;
    }

    private final long timestamp;
    private final double temperature;
    private final double humidity;
    private final double temperatureInternal;
    private final BatteryStatus batteryStatus;
    private final double batteryVoltage;

    public MeasurementDto(long timestamp, double temperature, double humidity, double temperatureInternal, BatteryStatus batteryStatus, double voltage) {
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.humidity = humidity;
        this.temperatureInternal = temperatureInternal;
        this.batteryStatus = batteryStatus;
        this.batteryVoltage = voltage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getTemperatureInternal() {
        return temperatureInternal;
    }

    public BatteryStatus getBatteryStatus() {
        return batteryStatus;
    }

    public double getBatteryVoltage() {
        return batteryVoltage;
    }

    @Override
    public String toString() {
        return "MeasurementDto{" +
                "timestamp=" + timestamp +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", temperatureInternal=" + temperatureInternal +
                ", batteryStatus=" + batteryStatus +
                ", batteryVoltage=" + batteryVoltage +
                '}';
    }
}
