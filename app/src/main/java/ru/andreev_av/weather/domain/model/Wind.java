
package ru.andreev_av.weather.domain.model;

public class Wind {

    private Float mSpeed;
    private Float mDeg;

    public Wind(Float speed) {
        mSpeed = speed;
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
}
