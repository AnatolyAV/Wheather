
package ru.andreev_av.weather.domain.model;

public class Rain {

    private Float mValue;

    public Rain(Float value) {
        this.mValue = value;
    }

    public Float getValue() {
        return mValue;
    }

    public void setValue(Float value) {
        mValue = value;
    }

}
