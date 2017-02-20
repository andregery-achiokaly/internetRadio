package com.somenameofpackage.internetradiowithmosby.model.radio;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public class RadioModule {
    @Provides
    Radio provideRadioModel() {
        return new Radio();
    }
}
