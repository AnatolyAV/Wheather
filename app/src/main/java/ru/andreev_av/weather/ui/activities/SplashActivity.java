package ru.andreev_av.weather.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.db.entry.CityEntry;
import ru.andreev_av.weather.preferences.AppPreference;
import ru.andreev_av.weather.processors.Processor;
import ru.andreev_av.weather.services.ServiceHelper;

import static ru.andreev_av.weather.ui.activities.BaseActivity.STATUS_CONNECTION_NOT_FOUND;
import static ru.andreev_av.weather.ui.activities.BaseActivity.STATUS_FINISH;

public class SplashActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String BROADCAST_ACTION = "ru.andreev_av.weather.SplashActivity.ActionResult";
    private final static int CITY_LOADER = 1;
    private final IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
    private ServiceHelper serviceHelper;
    private BroadcastReceiver weatherBroadcastReceiver;
    private ArrayList<Integer> cityIds = new ArrayList<>();
    private boolean dbCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initWeatherReceiver();
        serviceHelper = new ServiceHelper(SplashActivity.this, BROADCAST_ACTION);

        if (!AppPreference.isDbCreated(this)) {
            serviceHelper.createDb();
        } else {
            dbCreated = true;
        }
        getSupportLoaderManager().initLoader(CITY_LOADER, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(weatherBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(weatherBroadcastReceiver);
    }

    private void initWeatherReceiver() {
        weatherBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Intent cityListIntent = new Intent(SplashActivity.this, CityListActivity.class);
                int status = intent.getIntExtra(BaseActivity.PARAM_STATUS, 0);

                if (status == STATUS_CONNECTION_NOT_FOUND) {
                    Toast.makeText(SplashActivity.this, R.string.connection_not_found, Toast.LENGTH_SHORT).show();
                    startActivity(cityListIntent);
                }

                if (status == STATUS_FINISH) {
                    Bundle extras = intent.getExtras();

                    boolean success = extras.getBoolean(Processor.Extras.RESULT_EXTRA);

                    int method = extras.getInt(Processor.Extras.METHOD_EXTRA);
                    switch (method) {
                        case ServiceHelper.Methods.CREATE_DATABASE:
                            if (success) {
                                AppPreference.setDbCreated(SplashActivity.this, true);
                                dbCreated = true;
                                getSupportLoaderManager().getLoader(CITY_LOADER).forceLoad();
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case ServiceHelper.Methods.LOAD_WEATHER_CITIES:
                            startActivity(cityListIntent);
                            break;
                    }
                }
            }
        };
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = null;
        switch (id) {
            case CITY_LOADER:
                String selection = CityEntry.COLUMN_WATCHED + " = " + Processor.CITY_WATCH;
                cursorLoader = new CursorLoader(this, CityEntry.CONTENT_URI, null, selection, null, null);
                break;
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (dbCreated) {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        int cityId = cursor.getInt(cursor.getColumnIndex(CityEntry.COLUMN_CITY_ID));
                        cityIds.add(cityId);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                AppPreference.saveCityIds(SplashActivity.this, cityIds);
                serviceHelper.loadWeather(cityIds);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
