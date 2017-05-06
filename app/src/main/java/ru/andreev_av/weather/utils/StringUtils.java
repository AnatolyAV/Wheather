package ru.andreev_av.weather.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    public static String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public static String joinIds(List<Integer> ids) {
        return TextUtils.join(",", ids);
    }

    public static ArrayList<Integer> convertStringToIntList(String str) {
        ArrayList<Integer> intList = new ArrayList<>();
        String[] strMas = TextUtils.split(str, ",");
        for (String str2 : strMas) {
            intList.add(Integer.valueOf(str2));
        }
        return intList;
    }

}
