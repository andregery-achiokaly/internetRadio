package com.somenameofpackage.internetradiowithmosby.model.visualizer;

import dagger.Module;
import dagger.Provides;

@Module
public class VisualizerModule {
    @Provides
    VisualizerModel provideVisualizerModel() {
        return new VisualizerModel();
    }
}
