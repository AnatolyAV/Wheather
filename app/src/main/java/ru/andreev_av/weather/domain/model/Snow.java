
package ru.andreev_av.weather.domain.model;

public class Snow {

    private Float mValue;

    public Snow(Float value) {
        this.mValue = value;
    }

    public Float getValue() {
        return mValue;
    }

    public void setValue(Float value) {
        mValue = value;
    }

}
