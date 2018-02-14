package ru.andreev_av.weather.db.sqloperation;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;

import ru.andreev_av.weather.data.model.City;
import ru.andreev_av.weather.db.entry.CityEntry;
import ru.andreev_av.weather.processors.Processor;

public class CitySqlOperation {

    private Context context;

    public CitySqlOperation(Context context) {
        this.context = context;
    }

    public void update(City city) throws IllegalArgumentException {
        ContentValues cv = new ContentValues();
        cv.put(CityEntry.COLUMN_WATCHED, Processor.CITY_WATCH);

        String selection = CityEntry.COLUMN_CITY_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf((int) city.getId()),};
        context.getContentResolver().update(CityEntry.CONTENT_URI, cv, selection, selectionArgs);
    }

    // TODO реализовать в след. версиях
    public void delete(int cityId) throws IllegalArgumentException {
        context.getContentResolver().delete(ContentUris.withAppendedId(CityEntry.CONTENT_URI, cityId), null, null);
    }

}
