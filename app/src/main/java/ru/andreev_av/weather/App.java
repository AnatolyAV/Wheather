package ru.andreev_av.weather;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.andreev_av.weather.data.network.OwmService;
import ru.andreev_av.weather.ownapi.OwmApi;

public class App extends Application {

    // TODO подумать над выносом в ApiFactory
    // TODO удалить один Own и один Retrofit после переноса всего функционала на Rx
    private static OwmApi owmApi;
    private static OwmService owmService;
    private Retrofit retrofit;
    private Retrofit retrofitWithRx;

    public static OwmApi getApi() {
        return owmApi;
    }

    public static OwmService getOwmService() {
        return owmService;
    }

    @Override
    public void onCreate() {
        super.onCreate();

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
