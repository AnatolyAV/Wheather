package ru.andreev_av.weather.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import ru.andreev_av.weather.model.City;
import ru.andreev_av.weather.processors.CityProcessor;
import ru.andreev_av.weather.processors.Processor;
import ru.andreev_av.weather.ui.activities.BaseActivity;

public class CityService extends IntentService {

    private static final String TAG = "CityService";

    public CityService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        String resultAction = extras.getString(Processor.Extras.RESULT_ACTION_EXTRA);
        Intent resultIntent = new Intent(resultAction);

        resultIntent.putExtra(BaseActivity.PARAM_STATUS, BaseActivity.STATUS_START);
        getBaseContext().sendBroadcast(resultIntent);

        City city = extras.getParcelable(ServiceHelper.Methods.PARAMETER_CITY);

        int methodId = extras.getInt(Processor.Extras.METHOD_EXTRA);
        switch (methodId) {
            case ServiceHelper.Methods.LOAD_CITY_TO_WATCH:
                new CityProcessor(getApplicationContext(), methodId, resultAction).loadCityToWatch(city);
                break;
        }

    }


}
