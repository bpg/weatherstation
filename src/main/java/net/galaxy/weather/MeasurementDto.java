/*
 * Project: weatherstation, file: MeasurementDto.java
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
        ERROR
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
