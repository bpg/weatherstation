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

    private final int source;
    private final long timestamp;
    private final double temperature;
    private final double humidity;
    private final double temperatureInternal;
    private final BatteryStatus batteryStatus;
    private final double batteryVoltage;

    public MeasurementDto(int source, long timestamp, double temperature, double humidity, double temperatureInternal, BatteryStatus batteryStatus, double voltage) {
        this.source = source;
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.humidity = humidity;
        this.temperatureInternal = temperatureInternal;
        this.batteryStatus = batteryStatus;
        this.batteryVoltage = voltage;
    }

    public int getSource() {
        return source;
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
