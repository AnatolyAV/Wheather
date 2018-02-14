package ru.andreev_av.weather.domain.model;

import java.io.Serializable;
import java.util.List;

public class WeatherCurrent implements Serializable {
    private final Coordinate mCoordinate;
    private final List<Weather> mWeathers;
    private final Main mMain;
    private final Integer mVisibility;
    private final Wind mWind;
    private final Clouds mClouds;
    private final Long mDateTime;
    private final Sys mSys;
    private final Integer mCityId;
    private final String mCityName;
    private final Integer mCod;
    private final Snow mSnow;
    private final Rain mRain;

    WeatherCurrent(Coordinate coordinate, List<Weather> weathers, Main main, Integer visibility,
                   Wind wind, Clouds clouds, Long dateTime, Sys sys, Integer cityId,
                   String cityName, Integer cod, Snow snow, Rain rain) {
        mCoordinate = coordinate;
        mWeathers = weathers;
        mMain = main;
        mVisibility = visibility;
        mWind = wind;
        mClouds = clouds;
        mDateTime = dateTime;
        mSys = sys;
        mCityId = cityId;
        mCityName = cityName;
        mCod = cod;
        mSnow = snow;
        mRain = rain;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Coordinate getCoordinate() {
        return mCoordinate;
    }

    public List<Weather> getWeathers() {
        return mWeathers;
    }

    public Main getMain() {
        return mMain;
    }

    public Integer getVisibility() {
        return mVisibility;
    }

    public Wind getWind() {
        return mWind;
    }

    public Clouds getClouds() {
        return mClouds;
    }

    public Long getDateTime() {
        return mDateTime;
    }

    public Sys getSys() {
        return mSys;
    }

    public Integer getCityId() {
        return mCityId;
    }

    public String getCityName() {
        return mCityName;
    }

    public Integer getCod() {
        return mCod;
    }

    public Snow getSnow() {
        return mSnow;
    }

    public Rain getRain() {
        return mRain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeatherCurrent that = (WeatherCurrent) o;

        if (mCoordinate != null ? !mCoordinate.equals(that.mCoordinate) : that.mCoordinate != null)
            return false;
        if (mWeathers != null ? !mWeathers.equals(that.mWeathers) : that.mWeathers != null)
            return false;
        if (mMain != null ? !mMain.equals(that.mMain) : that.mMain != null) return false;
        if (mVisibility != null ? !mVisibility.equals(that.mVisibility) : that.mVisibility != null)
            return false;
        if (mWind != null ? !mWind.equals(that.mWind) : that.mWind != null) return false;
        if (mClouds != null ? !mClouds.equals(that.mClouds) : that.mClouds != null) return false;
        if (mDateTime != null ? !mDateTime.equals(that.mDateTime) : that.mDateTime != null)
            return false;
        if (mSys != null ? !mSys.equals(that.mSys) : that.mSys != null) return false;
        if (mCityId != null ? !mCityId.equals(that.mCityId) : that.mCityId != null) return false;
        if (mCityName != null ? !mCityName.equals(that.mCityName) : that.mCityName != null)
            return false;
        if (mCod != null ? !mCod.equals(that.mCod) : that.mCod != null) return false;
        if (mSnow != null ? !mSnow.equals(that.mSnow) : that.mSnow != null) return false;
        return mRain != null ? mRain.equals(that.mRain) : that.mRain == null;
    }

    @Override
    public int hashCode() {
        int result = mCoordinate != null ? mCoordinate.hashCode() : 0;
        result = 31 * result + (mWeathers != null ? mWeathers.hashCode() : 0);
        result = 31 * result + (mMain != null ? mMain.hashCode() : 0);
        result = 31 * result + (mVisibility != null ? mVisibility.hashCode() : 0);
        result = 31 * result + (mWind != null ? mWind.hashCode() : 0);
        result = 31 * result + (mClouds != null ? mClouds.hashCode() : 0);
        result = 31 * result + (mDateTime != null ? mDateTime.hashCode() : 0);
        result = 31 * result + (mSys != null ? mSys.hashCode() : 0);
        result = 31 * result + (mCityId != null ? mCityId.hashCode() : 0);
        result = 31 * result + (mCityName != null ? mCityName.hashCode() : 0);
        result = 31 * result + (mCod != null ? mCod.hashCode() : 0);
        result = 31 * result + (mSnow != null ? mSnow.hashCode() : 0);
        result = 31 * result + (mRain != null ? mRain.hashCode() : 0);
        return result;
    }

    public static final class Builder {
        private Coordinate mCoordinate;
        private List<Weather> mWeathers;
        private Main mMain;
        private Integer mVisibility;
        private Wind mWind;
        private Clouds mClouds;
        private Long mDateTime;
        private Sys mSys;
        private Integer mCityId;
        private String mCityName;
        private Integer mCod;
        private Snow mSnow;
        private Rain mRain;

        private Builder() {
        }

        public Builder coordinate(Coordinate coordinate) {
            mCoordinate = coordinate;
            return this;
        }

        public Builder weathers(List<Weather> weathers) {
            mWeathers = weathers;
            return this;
        }

        public Builder main(Main main) {
            mMain = main;
            return this;
        }

        public Builder visibility(Integer visibility) {
            mVisibility = visibility;
            return this;
        }

        public Builder wind(Wind wind) {
            mWind = wind;
            return this;
        }

        public Builder clouds(Clouds clouds) {
            mClouds = clouds;
            return this;
        }

        public Builder dateTime(Long dateTime) {
            mDateTime = dateTime;
            return this;
        }

        public Builder sys(Sys sys) {
            mSys = sys;
            return this;
        }

        public Builder cityId(Integer cityId) {
            mCityId = cityId;
            return this;
        }

        public Builder cityName(String cityName) {
            mCityName = cityName;
            return this;
        }

        public Builder cod(Integer cod) {
            mCod = cod;
            return this;
        }

        public Builder snow(Snow snow) {
            mSnow = snow;
            return this;
        }

        public Builder rain(Rain rain) {
            mRain = rain;
            return this;
        }

        public WeatherCurrent build() {
            return new WeatherCurrent(mCoordinate, mWeathers, mMain, mVisibility,
                    mWind, mClouds, mDateTime, mSys,
                    mCityId, mCityName, mCod, mSnow, mRain);
        }
    }
}
