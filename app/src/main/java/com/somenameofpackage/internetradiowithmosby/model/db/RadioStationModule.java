package com.somenameofpackage.internetradiowithmosby.model.db;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class RadioStationModule {
    @Provides
    RadioStations provideRadioStations(Context context) {
        return new RadioStations(context);
    }
}
