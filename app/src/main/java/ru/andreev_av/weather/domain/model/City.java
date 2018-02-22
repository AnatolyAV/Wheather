
package ru.andreev_av.weather.domain.model;

import java.io.Serializable;

public class City implements Serializable {

    private Integer mId;
    private String mName;
    private Coordinate mCoordinate;
    private String mCountryCode;
    private Integer mWatch;

    public City() {
    }

    public City(Integer id, String name, String countryCode) {
        mId = id;
        mName = name;
        mCountryCode = countryCode;
    }

    public City(Integer id, String name, Coordinate coordinate, String countryCode) {
        mId = id;
        mName = name;
        mCoordinate = coordinate;
        mCountryCode = countryCode;
    }

    public City(Integer id, String name, Coordinate coordinate, String countryCode, Integer watch) {
        mId = id;
        mName = name;
        mCoordinate = coordinate;
        mCountryCode = countryCode;
        mWatch = watch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        if (mId != null ? !mId.equals(city.mId) : city.mId != null) return false;
        if (mName != null ? !mName.equals(city.mName) : city.mName != null) return false;
        if (mCoordinate != null ? !mCoordinate.equals(city.mCoordinate) : city.mCoordinate != null)
            return false;
        if (mCountryCode != null ? !mCountryCode.equals(city.mCountryCode) : city.mCountryCode != null)
            return false;
        return mWatch != null ? mWatch.equals(city.mWatch) : city.mWatch == null;
    }

    @Override
    public int hashCode() {
        int result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        result = 31 * result + (mCoordinate != null ? mCoordinate.hashCode() : 0);
        result = 31 * result + (mCountryCode != null ? mCountryCode.hashCode() : 0);
        result = 31 * result + (mWatch != null ? mWatch.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", mName, mCountryCode);
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Coordinate getCoordinate() {
        return mCoordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        mCoordinate = coordinate;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String countryCode) {
        mCountryCode = countryCode;
    }

    public Integer getWatch() {
        return mWatch;
    }

    public void setWatch(Integer watch) {
        mWatch = watch;
    }
}
