
package ru.andreev_av.weather.domain.model;

public class Main {

    private Float mTemperature;
    private Float mPressure;
    private Float mHumidity;
    private Float mTemperatureMin;
    private Float mTemperatureMax;
    private Float mSeaLevel;
    private Float mGrndLevel;

    public Main(Float temperature, Float pressure, Float humidity, Float temperatureMin, Float temperatureMax) {
        mTemperature = temperature;
        mPressure = pressure;
        mHumidity = humidity;
        mTemperatureMin = temperatureMin;
        mTemperatureMax = temperatureMax;
    }

    public Float getTemperature() {
        return mTemperature;
    }

    public void setTemperature(Float temperature) {
        mTemperature = temperature;
    }

    public Float getPressure() {
        return mPressure;
    }

    public void setPressure(Float pressure) {
        mPressure = pressure;
    }

    public Float getHumidity() {
        return mHumidity;
    }

    public void setHumidity(Float humidity) {
        mHumidity = humidity;
    }

    public Float getTemperatureMin() {
        return mTemperatureMin;
    }

    public void setTemperatureMin(Float tempMin) {
        mTemperatureMin = tempMin;
    }

    public Float getTemperatureMax() {
        return mTemperatureMax;
    }

    public void setTemperatureMax(Float tempMax) {
        mTemperatureMax = tempMax;
    }

    public Float getSeaLevel() {
        return mSeaLevel;
    }

    public void setSeaLevel(Float seaLevel) {
        mSeaLevel = seaLevel;
    }

    public Float getGrndLevel() {
        return mGrndLevel;
    }

    public void setGrndLevel(Float grndLevel) {
        mGrndLevel = grndLevel;
    }
}
