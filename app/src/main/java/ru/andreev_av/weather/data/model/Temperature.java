
package ru.andreev_av.weather.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Temperature {

    @SerializedName("day")
    @Expose
    private Float day;
    @SerializedName("min")
    @Expose
    private Float min;
    @SerializedName("max")
    @Expose
    private Float max;
    @SerializedName("night")
    @Expose
    private Float night;
    @SerializedName("eve")
    @Expose
    private Float evening;
    @SerializedName("morn")
    @Expose
    private Float morning;

    public Temperature() {
    }

    public Temperature(Float day, Float min, Float max, Float night, Float evening, Float morning) {
        this.day = day;
        this.min = min;
        this.max = max;
        this.night = night;
        this.evening = evening;
        this.morning = morning;
    }

    public Float getDay() {
        return day;
    }

    public void setDay(Float day) {
        this.day = day;
    }

    public Float getMin() {
        return min;
    }

    public void setMin(Float min) {
        this.min = min;
    }

    public Float getMax() {
        return max;
    }

    public void setMax(Float max) {
        this.max = max;
    }

    public Float getNight() {
        return night;
    }

    public void setNight(Float night) {
        this.night = night;
    }

    public Float getEvening() {
        return evening;
    }

    public void setEvening(Float evening) {
        this.evening = evening;
    }

    public Float getMorning() {
        return morning;
    }

    public void setMorning(Float morning) {
        this.morning = morning;
    }

}
