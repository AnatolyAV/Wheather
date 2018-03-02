package ru.andreev_av.weather.data.listeners;

import android.database.sqlite.SQLiteDatabase;

public interface IDbTablesCreatedListener {

    void onDbTablesCreated(SQLiteDatabase db);
}
