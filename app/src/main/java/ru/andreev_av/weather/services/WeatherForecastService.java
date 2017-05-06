package ru.andreev_av.weather.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import ru.andreev_av.weather.net.ConnectionDetector;
import ru.andreev_av.weather.processors.Processor;
import ru.andreev_av.weather.processors.WeatherForecastProcessor;
import ru.andreev_av.weather.ui.activities.BaseActivity;

public class WeatherForecastService extends IntentService {

    private static final String TAG = "WeatherForecastService";

    public WeatherForecastService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        String resultAction = extras.getString(Processor.Extras.RESULT_ACTION_EXTRA);
        Intent resultIntent = new Intent(resultAction);

        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        if (!connectionDetector.isNetworkAvailableAndConnected()) {
            resultIntent.putExtra(BaseActivity.PARAM_STATUS, BaseActivity.STATUS_CONNECTION_NOT_FOUND);
            getBaseContext().sendBroadcast(resultIntent);
            return;
        }

        resultIntent.putExtra(BaseActivity.PARAM_STATUS, BaseActivity.STATUS_START);
        getBaseContext().sendBroadcast(resultIntent);

        int cityId = extras.getInt(ServiceHelper.Methods.PARAMETER_CITY_ID);
        int countDays = extras.getInt(ServiceHelper.Methods.PARAMETER_COUNT_DAYS);
        int methodId = extras.getInt(Processor.Extras.METHOD_EXTRA);
        new WeatherForecastProcessor(this, methodId, resultAction).loadWeatherForecast(countDays, cityId);
    }
}
