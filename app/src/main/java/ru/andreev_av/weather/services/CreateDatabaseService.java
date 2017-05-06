package ru.andreev_av.weather.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import ru.andreev_av.weather.db.entry.CityEntry;
import ru.andreev_av.weather.processors.Processor;
import ru.andreev_av.weather.ui.activities.BaseActivity;

import static ru.andreev_av.weather.processors.Processor.ACTION_RESULT_OK;

public class CreateDatabaseService extends IntentService {

    private static final String TAG = "CreateDatabaseService";

    public CreateDatabaseService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        String resultAction = extras.getString(Processor.Extras.RESULT_ACTION_EXTRA);
        Intent resultIntent = new Intent(resultAction);

        getContentResolver().query(CityEntry.CONTENT_URI, null, null, null, null);

        resultIntent.putExtra(BaseActivity.PARAM_STATUS, BaseActivity.STATUS_FINISH);
        resultIntent.putExtra(Processor.Extras.RESULT_EXTRA, ACTION_RESULT_OK);
        resultIntent.putExtras(extras);
        getBaseContext().sendBroadcast(resultIntent);
    }
}
