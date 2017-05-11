package ru.andreev_av.weather.processors;

import android.content.Context;
import android.util.Log;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.db.sqloperation.WeatherForecastSqlOperation;
import ru.andreev_av.weather.model.WeatherForecastModel;
import ru.andreev_av.weather.net.WeatherForecastHttpRequest;

public class WeatherForecastProcessor extends WeatherProcessor {

    private static final String TAG = "WeatherForecastProcesor";

    private WeatherForecastSqlOperation sqlOperation;

    public WeatherForecastProcessor(Context context, int methodId, String resultAction) {
        super(context, methodId, resultAction);
        sqlOperation = new WeatherForecastSqlOperation(context);
    }

    public void loadWeatherForecast(int cityId, int countDays) {
        new WeatherForecastHttpRequest(context, this).getWeatherForecast(cityId, countDays);
    }

    public void runProcessSuccessResponse(final WeatherForecastModel weatherForecastModel) {
        Log.d(TAG, "WeatherForecastProcesor onResponse success ");
        new Thread() {
            public void run() {
                fillDb(weatherForecastModel);
            }
        }.start();
    }

    private void fillDb(WeatherForecastModel weatherForecastModel) {
        try {
            sqlOperation.insertOrUpdate(weatherForecastModel);
            Log.d(TAG, "WeatherForecastProcesor fillDb success");
            sendResult(ACTION_RESULT_OK);
        } catch (Exception e) {
            Log.e(TAG, context.getResources().getString(R.string.error_fill_db) + e.getMessage());
            sendResult(ACTION_RESULT_FAIL);
        }

    }
}
