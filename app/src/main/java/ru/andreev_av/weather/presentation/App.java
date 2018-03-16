package ru.andreev_av.weather.presentation;

import android.app.Application;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.andreev_av.weather.dagger.components.AppComponent;
import ru.andreev_av.weather.dagger.components.DaggerAppComponent;
import ru.andreev_av.weather.dagger.modules.AppModule;
import ru.andreev_av.weather.data.network.OwmService;

public class App extends Application {

    private static App mInstance;
    // TODO подумать над выносом в ApiFactory
    // TODO тоже попробывать через Dagger
    private static OwmService owmService;
    private AppComponent mAppComponent;
    private Retrofit retrofit;

    public static App getInstance() {
        return mInstance;
    }

    public static OwmService getOwmService() {
        return owmService;
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        retrofit = new Retrofit.Builder()
                // TODO можно вынести в buildConfigField
                .baseUrl(OwmService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();


        owmService = retrofit.create(OwmService.class);
    }
}
