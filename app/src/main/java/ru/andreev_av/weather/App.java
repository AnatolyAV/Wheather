package ru.andreev_av.weather;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.andreev_av.weather.dagger.components.AppComponent;
import ru.andreev_av.weather.dagger.components.DaggerAppComponent;
import ru.andreev_av.weather.dagger.modules.AppModule;
import ru.andreev_av.weather.data.network.OwmService;
import ru.andreev_av.weather.ownapi.OwmApi;

public class App extends Application {

    private static App mInstance;
    // TODO подумать над выносом в ApiFactory
    // TODO удалить один Own и один Retrofit после переноса всего функционала на Rx
    // TODO тоже попробывать через Dagger
    private static OwmApi owmApi;
    private static OwmService owmService;
    private AppComponent mAppComponent;
    private Retrofit retrofit;
    private Retrofit retrofitWithRx;

    public static App getInstance() {
        return mInstance;
    }

    public static OwmApi getApi() {
        return owmApi;
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
                .baseUrl(OwmService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitWithRx = new Retrofit.Builder()
                // TODO можно вынести в buildConfigField
                .baseUrl(OwmService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();


        owmApi = retrofit.create(OwmApi.class);
        owmService = retrofitWithRx.create(OwmService.class);
    }
}
