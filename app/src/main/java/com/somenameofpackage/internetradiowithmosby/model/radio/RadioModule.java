package com.somenameofpackage.internetradiowithmosby.model.radio;

import dagger.Module;
import dagger.Provides;

@Module
public class RadioModule {
    @Provides
    RadioModel provideRadioModel() {
        return new RadioModel();
    }
}
