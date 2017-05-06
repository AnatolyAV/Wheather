package ru.andreev_av.weather.processors;

import android.content.Context;
import android.content.Intent;

import ru.andreev_av.weather.ui.activities.BaseActivity;

public class Processor {

    public final static boolean ACTION_RESULT_OK = true;
    public final static boolean ACTION_RESULT_FAIL = false;
    public final static String CITY_ID = "cityId";
    public final static int NOT_CITY = -1;
    public final static int CITY_WATCH = 1;
    protected Context context;
    private int methodId;
    private String resultAction;

    public Processor(Context context, int methodId, String resultAction) {
        this.context = context;
        this.methodId = methodId;
        this.resultAction = resultAction;
    }

    //TODO Посмотреть может константы вынести в AppContext или в класс Constants
    protected void sendResult(boolean result) {
        sendResult(result, NOT_CITY);
    }

    protected void sendResult(boolean result, int newCityId) {
        Intent intent = new Intent(resultAction);
        intent.putExtra(Processor.Extras.METHOD_EXTRA, methodId);
        if (newCityId != NOT_CITY) {
            intent.putExtra(Processor.CITY_ID, newCityId);
        } else {
            intent.putExtra(Processor.CITY_ID, NOT_CITY);
        }
        if (result == ACTION_RESULT_OK) {
            intent.putExtra(Extras.RESULT_EXTRA, ACTION_RESULT_OK);
        } else if (result == ACTION_RESULT_FAIL) {
            intent.putExtra(Extras.RESULT_EXTRA, ACTION_RESULT_FAIL);
        }
        intent.putExtra(BaseActivity.PARAM_STATUS, BaseActivity.STATUS_FINISH);
        context.sendBroadcast(intent);
    }

    public static class Extras {
        public final static String METHOD_EXTRA = "METHOD_EXTRA";
        public static final String RESULT_ACTION_EXTRA = "RESULT_ACTION_EXTRA";
        public final static String RESULT_EXTRA = "RESULT_EXTRA";
    }
}
