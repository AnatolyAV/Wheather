
package ru.andreev_av.weather.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City implements Parcelable {

    @SuppressWarnings("unchecked")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public Object[] newArray(int size) {
            return new City[size];
        }
    };
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("coord")
    @Expose
    private Coordinate coordinate;
    @SerializedName("country")
    @Expose
    private String countryCode;
    private Integer watch;

    public City() {
    }

    public City(Integer id, String name, String countryCode) {
        this.id = id;
        this.name = name;
        this.countryCode = countryCode;
    }

    public City(Parcel in) {
        readFromParcel(in);
    }

    public City(Integer id, String name, Coordinate coordinate, String countryCode) {
        this.id = id;
        this.name = name;
        this.coordinate = coordinate;
        this.countryCode = countryCode;
    }

    public City(Integer id, String name, Coordinate coordinate, String countryCode, Integer watch) {
        this.id = id;
        this.name = name;
        this.coordinate = coordinate;
        this.countryCode = countryCode;
        this.watch = watch;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getWatch() {
        return watch;
    }

    public void setWatch(Integer watch) {
        this.watch = watch;
    }

    private void readFromParcel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        coordinate = (Coordinate) in.readSerializable();
        countryCode = in.readString();
        if (watch != null)
            watch = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeSerializable(coordinate);
        dest.writeString(countryCode);
        if (watch != null)
            dest.writeInt(watch);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, countryCode);
    }
}
