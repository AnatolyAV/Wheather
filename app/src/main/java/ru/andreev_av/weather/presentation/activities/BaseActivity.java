package ru.andreev_av.weather.presentation.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import ru.andreev_av.weather.R;

public abstract class BaseActivity extends BaseDrawerActivity {

    public final static int STATUS_START = 100;
    public final static int STATUS_FINISH = 200;
    public final static int STATUS_CONNECTION_NOT_FOUND = 300;
    public final static String PARAM_STATUS = "status";
    protected Toolbar toolbar;
    protected MenuItem updateItem;
    private ProgressBar progressUpdate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        findComponents();

        initToolbar();
    }

    protected void findComponents() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressUpdate = (ProgressBar) findViewById(R.id.toolbar_progress_bar);
    }

    protected void initToolbar() {
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_city_list, menu);
        updateItem = menu.findItem(R.id.main_menu_refresh);
        return true;
    }

    protected void setUpdateButtonState(boolean isUpdate) {
        if (isUpdate) {
            if (updateItem != null) {
                updateItem.setVisible(false);
            }
            progressUpdate.setVisibility(View.VISIBLE);
        } else {
            progressUpdate.setVisibility(View.GONE);
            if (updateItem != null) {
                updateItem.setVisible(true);
            }
        }
    }
}
