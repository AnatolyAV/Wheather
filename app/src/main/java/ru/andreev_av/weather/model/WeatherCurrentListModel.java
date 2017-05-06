package ru.andreev_av.weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
        return weatherCurrentModelList;
    }

    public void setWeatherCurrentModelList(java.util.List<WeatherCurrentModel> weatherCurrentModelList) {
        this.weatherCurrentModelList = weatherCurrentModelList;
    }

}
