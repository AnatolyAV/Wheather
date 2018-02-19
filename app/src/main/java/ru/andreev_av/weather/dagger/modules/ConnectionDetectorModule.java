package ru.andreev_av.weather.dagger.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.andreev_av.weather.net.ConnectionDetector;

@Module(includes = {ContextModule.class})
public class ConnectionDetectorModule {

    @Provides
    @Singleton
    public ConnectionDetector provideConnectionDetector(Context context) {
        return ConnectionDetector.getInstance(context.getApplicationContext());
    }
}