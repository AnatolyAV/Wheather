package ru.andreev_av.weather.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.preferences.AppPreference;

public abstract class BaseDrawerActivity extends MvpAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected boolean drawerClosed;
    //TODO перевести все Activity на Fragment
    // TODO добавить анимацию
    private TextView tvCityAndCountryCode;
    private String[] cityAndCountryCode;
    private Intent cityListIntent;
    private Intent weatherCurrentIntent;
    private Intent weatherForecastListIntent;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        cityListIntent = new Intent(this, CitiesListActivity.class);
        weatherCurrentIntent = new Intent(this, WeatherCurrentActivity.class);
        weatherForecastListIntent = new Intent(this, WeatherForecastActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO рефакторинг нужен
        // TODO удалить после перевода на Fragment
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        int menuSize = navigationView.getMenu().size();
        for (int i = 0; i < menuSize; i++)
            navigationView.getMenu().getItem(i).setChecked(false);

        // TODO рефакторинг нужен
        cityAndCountryCode = AppPreference.getCityAndCode(this);
        View headerLayout = navigationView.getHeaderView(0);
        tvCityAndCountryCode = (TextView) headerLayout.findViewById(R.id.tv_city);
        tvCityAndCountryCode.setText(cityAndCountryCode[0] + ", " + cityAndCountryCode[1]);
    }

    protected void initDrawer(Toolbar toolbar) {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
        };
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);// слушатель нажатия на пункты бокового меню
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_current_city:
                startActivity(weatherCurrentIntent);
                break;
            case R.id.action_cities:
                startActivity(cityListIntent);
                break;
            case R.id.action_weather_forecast:
                startActivity(weatherForecastListIntent);
                break;
        }
        item.setChecked(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            drawerClosed = true;
        } else {
            drawerClosed = false;
        }

    }
}
