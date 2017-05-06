package ru.andreev_av.weather.processors;

import android.content.Context;
import android.util.Log;

import ru.andreev_av.weather.R;

public class WeatherProcessor extends Processor implements IWeatherProcessor {

    private static final String TAG = "WeatherProcessor";

    public WeatherProcessor(Context context, int methodId, String resultAction) {
        super(context, methodId, resultAction);
    }

    @Override
    public void runProcessErrorResponse(Throwable e) {
        sendResult(ACTION_RESULT_FAIL);
        Log.e(TAG, context.getResources().getString(R.string.error_response) + e.getMessage());
    }

}
