package ru.andreev_av.weather.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherCurrentListModel {

    @SerializedName("cnt")
    @Expose
    private Integer cnt;
    @SerializedName("list")
    @Expose
    private java.util.List<WeatherCurrentModel> weatherCurrentModelList = null;

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public java.util.List<WeatherCurrentModel> getWeatherCurrentModelList() {
        if (weatherCurrentModelList == null) {
            return new ArrayList<>();
        }
        return weatherCurrentModelList;
    }

    public void setWeatherCurrentModelList(java.util.List<WeatherCurrentModel> weatherCurrentModelList) {
        this.weatherCurrentModelList = weatherCurrentModelList;
    }

}
