package com.somenameofpackage.internetradiowithmosby.model.db;

import dagger.Module;
import dagger.Provides;

@Module
public class RadioStationsModule {
    @Provides
    RadioStations provideVisualizerModel() {
        return new RadioStations();
    }
}
