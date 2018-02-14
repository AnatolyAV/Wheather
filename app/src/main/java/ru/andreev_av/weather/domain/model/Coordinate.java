package ru.andreev_av.weather.domain.model;

public class Coordinate {

    private final Float mLatitude;
    private final Float mLongitude;

    public Coordinate(Float latitude, Float longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (mLatitude != null ? !mLatitude.equals(that.mLatitude) : that.mLatitude != null)
            return false;
        return mLongitude != null ? mLongitude.equals(that.mLongitude) : that.mLongitude == null;
    }

    @Override
    public int hashCode() {
        int result = mLatitude != null ? mLatitude.hashCode() : 0;
        result = 31 * result + (mLongitude != null ? mLongitude.hashCode() : 0);
        return result;
    }

    public static final class Builder {
        private Float mLatitude;
        private Float mLongitude;

        private Builder() {
        }

        public Builder latitude(Float latitude) {
            mLatitude = latitude;
            return this;
        }

        public Builder longitude(Float longitude) {
            mLongitude = longitude;
            return this;
        }

        public Coordinate build() {
            return new Coordinate(mLatitude, mLongitude);
        }
    }
}
