package ru.andreev_av.weather.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import ru.andreev_av.weather.net.ConnectionDetector;
import ru.andreev_av.weather.processors.Processor;
import ru.andreev_av.weather.processors.WeatherCurrentProcessor;
import ru.andreev_av.weather.ui.activities.BaseActivity;

public class WeatherCurrentService extends IntentService {

    private static final String TAG = "WeatherCurrentService";

    public WeatherCurrentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        String resultAction = extras.getString(Processor.Extras.RESULT_ACTION_EXTRA);
        Intent resultIntent = new Intent(resultAction);

        // TODO подумать куда это анализ лучше вынести во всех сервисах
        ConnectionDetector connectionDetector = ConnectionDetector.getInstance(this);
        if (!connectionDetector.isNetworkAvailableAndConnected()) {
            resultIntent.putExtra(BaseActivity.PARAM_STATUS, BaseActivity.STATUS_CONNECTION_NOT_FOUND);
            getBaseContext().sendBroadcast(resultIntent);
            return;
        }

        resultIntent.putExtra(BaseActivity.PARAM_STATUS, BaseActivity.STATUS_START);
        getBaseContext().sendBroadcast(resultIntent);

        int methodId = extras.getInt(Processor.Extras.METHOD_EXTRA);
        switch (methodId) {
            case ServiceHelper.Methods.LOAD_WEATHER_CITY:
                int cityId = extras.getInt(ServiceHelper.Methods.PARAMETER_CITY_ID);
                new WeatherCurrentProcessor(getApplicationContext(), methodId, resultAction).loadWeatherCurrent(cityId);
                break;
            case ServiceHelper.Methods.LOAD_WEATHER_CITIES:
                ArrayList<Integer> cityIds = extras.getIntegerArrayList(ServiceHelper.Methods.PARAMETER_CITY_IDS);
                new WeatherCurrentProcessor(getApplicationContext(), methodId, resultAction).loadWeatherCurrentByCityIds(cityIds);
                break;
        }


    }


}
