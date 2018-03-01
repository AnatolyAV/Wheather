
package ru.andreev_av.weather.domain.model;

public class Temperature {

    private Float mDay;
    private Float mMin;
    private Float mMax;
    private Float mNight;
    private Float mEvening;
    private Float mMorning;

    public Temperature(Float day, Float min, Float max, Float night, Float evening, Float morning) {
        mDay = day;
        mMin = min;
        mMax = max;
        mNight = night;
        mEvening = evening;
        mMorning = morning;
    }

    public Float getDay() {
        return mDay;
    }

    public void setDay(Float day) {
        this.mDay = day;
    }

    public Float getMin() {
        return mMin;
    }

    public void setMin(Float min) {
        this.mMin = min;
    }

    public Float getMax() {
        return mMax;
    }

    public void setMax(Float max) {
        this.mMax = max;
    }

    public Float getNight() {
        return mNight;
    }

    public void setNight(Float night) {
        this.mNight = night;
    }

    public Float getEvening() {
        return mEvening;
    }

    public void setEvening(Float mEvening) {
        this.mEvening = mEvening;
    }

    public Float getMorning() {
        return mMorning;
    }

    public void setMorning(Float morning) {
        this.mMorning = morning;
    }

}
