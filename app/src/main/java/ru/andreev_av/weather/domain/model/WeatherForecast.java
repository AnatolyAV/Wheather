
package ru.andreev_av.weather.domain.model;

import java.util.List;

public class WeatherForecast {

    private Long mDateTime;
    private Temperature mTemperature;
    private Float mPressure;
    private Float mHumidity;
    private java.util.List<Weather> mWeather;
    private Float mSpeed;
    private Float mDeg;
    private Integer mClouds;
    private Float mSnow;
    private Float mRain;

    public WeatherForecast(Long dateTime, Temperature temperature, Float pressure, Float humidity, List<Weather> weather, Float speed, Float deg, Integer clouds, Float snow, Float rain) {
        mDateTime = dateTime;
        mTemperature = temperature;
        mPressure = pressure;
        mHumidity = humidity;
        mWeather = weather;
        mSpeed = speed;
        mDeg = deg;
        mClouds = clouds;
        mSnow = snow;
        mRain = rain;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getDateTime() {
        return mDateTime;
    }

    public void setDateTime(Long dateTime) {
        mDateTime = dateTime;
    }

    public Temperature getTemperature() {
        return mTemperature;
    }

    public void setTemperature(Temperature temperature) {
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

    public java.util.List<Weather> getWeather() {
        return mWeather;
    }

    public void setWeather(java.util.List<Weather> weather) {
        mWeather = weather;
    }

    public Float getSpeed() {
        return mSpeed;
    }

    public void setSpeed(Float speed) {
        mSpeed = speed;
    }

    public Float getDeg() {
        return mDeg;
    }

    public void setDeg(Float deg) {
        mDeg = deg;
    }

    public Integer getClouds() {
        return mClouds;
    }

    public void setClouds(Integer clouds) {
        mClouds = clouds;
    }

    public Float getSnow() {
        return mSnow;
    }

    public void setSnow(Float snow) {
        mSnow = snow;
    }

    public Float getRain() {
        return mRain;
    }

    public void setRain(Float rain) {
        mRain = rain;
    }

    public static final class Builder {
        private Long mDateTime;
        private Temperature mTemperature;
        private Float mPressure;
        private Float mHumidity;
        private java.util.List<Weather> mWeather;
        private Float mSpeed;
        private Float mDeg;
        private Integer mClouds;
        private Float mSnow;
        private Float mRain;

        private Builder() {
        }

        public Builder dateTime(Long dateTime) {
            mDateTime = dateTime;
            return this;
        }

        public Builder temperature(Temperature temperature) {
            mTemperature = temperature;
            return this;
        }

        public Builder pressure(Float pressure) {
            mPressure = pressure;
            return this;
        }

        public Builder humidity(Float humidity) {
            mHumidity = humidity;
            return this;
        }

        public Builder weathers(List<Weather> weathers) {
            mWeather = weathers;
            return this;
        }

        public Builder speed(Float speed) {
            mSpeed = speed;
            return this;
        }

        public Builder deg(Float deg) {
            mDeg = deg;
            return this;
        }

        public Builder clouds(Integer clouds) {
            mClouds = clouds;
            return this;
        }

        public Builder snow(Float snow) {
            mSnow = snow;
            return this;
        }

        public Builder rain(Float rain) {
            mRain = rain;
            return this;
        }

        public WeatherForecast build() {
            return new WeatherForecast(mDateTime, mTemperature, mPressure,
                    mHumidity, mWeather, mSpeed,
                    mDeg, mClouds, mSnow, mRain);
        }
    }
}