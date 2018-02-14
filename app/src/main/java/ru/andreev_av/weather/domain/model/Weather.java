
package ru.andreev_av.weather.domain.model;

// TODO может перевести на Builder, а также остальные классы в этом пакете
public class Weather {

    private Integer mId;
    private String mMain;
    private String mDescription;
    private String mIcon;

    public Weather(Integer id, String main, String description, String icon) {
        mId = id;
        mMain = main;
        mDescription = description;
        mIcon = icon;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public String getMain() {
        return mMain;
    }

    public void setMain(String main) {
        mMain = main;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

}
