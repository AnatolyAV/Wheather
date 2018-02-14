package ru.andreev_av.weather.processors;

import android.content.Context;
import android.util.Log;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.data.model.City;
import ru.andreev_av.weather.db.sqloperation.CitySqlOperation;

public class CityProcessor extends Processor {

    private static final String TAG = "CityProcessor";

    public CityProcessor(Context context, int methodId, String resultAction) {
        super(context, methodId, resultAction);
    }

    public void loadCityToWatch(final City city) {
        fillDb(city);
    }

    private void fillDb(City city) {
        try {
            new CitySqlOperation(context).update(city);
            Log.d(TAG, "CityProcessor fillDb success");
            sendResult(ACTION_RESULT_OK, city.getId());
        } catch (Exception e) {
            Log.e(TAG, context.getResources().getString(R.string.error_fill_db) + e.getMessage());
            sendResult(ACTION_RESULT_FAIL, NOT_CITY);
        }
    }


}
