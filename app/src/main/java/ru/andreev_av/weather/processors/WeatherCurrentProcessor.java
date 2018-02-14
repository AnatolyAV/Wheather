package ru.andreev_av.weather.processors;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.data.model.WeatherCurrentListModel;
import ru.andreev_av.weather.data.model.WeatherCurrentModel;
import ru.andreev_av.weather.db.sqloperation.WeatherCurrentSqlOperation;
import ru.andreev_av.weather.net.WeatherCurrentHttpRequest;

public class WeatherCurrentProcessor extends WeatherProcessor {

    private static final String TAG = "WeatherCurrentProcessor";

    private WeatherCurrentSqlOperation sqlOperation;

    public WeatherCurrentProcessor(Context context, int methodId, String resultAction) {
        super(context, methodId, resultAction);
        sqlOperation = new WeatherCurrentSqlOperation(context);
    }

    public void loadWeatherCurrent(int cityId) {
        new WeatherCurrentHttpRequest(context, this).getWeatherCurrent(cityId);
    }

    public void loadWeatherCurrentByCityIds(ArrayList<Integer> cityIds) {
        new WeatherCurrentHttpRequest(context, this).getWeatherCurrent(cityIds);
    }

    // TODO подумать как обобщить (проблема в разных моделях)
    public void runProcessSuccessResponse(final WeatherCurrentModel weatherCurrentModel) {
        Log.d(TAG, "WeatherCurrentProcessor onResponse success ");
        new Thread() {
            public void run() {
                fillDb(weatherCurrentModel);
            }
        }.start();
    }

    public void runProcessSuccessResponse(final WeatherCurrentListModel weatherCurrentListModel) {
        Log.d(TAG, "WeatherCurrentProcessor onResponse success ");
        new Thread() {
            public void run() {
                fillDb(weatherCurrentListModel);
            }
        }.start();
    }

    private void fillDb(WeatherCurrentModel weatherModel) {
        try {
            sqlOperation.insertOrUpdate(weatherModel);
            Log.d(TAG, "WeatherCurrentProcessor fillDb success");
            sendResult(ACTION_RESULT_OK);
        } catch (Exception e) {
            Log.e(TAG, context.getResources().getString(R.string.error_fill_db) + e.getMessage());
            sendResult(ACTION_RESULT_FAIL);
        }

    }

    private void fillDb(WeatherCurrentListModel weatherCurrentListModel) {
        try {
            sqlOperation.insertOrUpdate(weatherCurrentListModel);
            Log.d(TAG, "WeatherCurrentProcessor fillDb success");
            sendResult(ACTION_RESULT_OK);
        } catch (Exception e) {
            Log.e(TAG, context.getResources().getString(R.string.error_fill_db) + e.getMessage());
            sendResult(ACTION_RESULT_FAIL);
        }
    }
}
