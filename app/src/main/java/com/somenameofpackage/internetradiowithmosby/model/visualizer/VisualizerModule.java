package com.somenameofpackage.internetradiowithmosby.model.visualizer;

import dagger.Module;
import dagger.Provides;

@Module
public class VisualizerModule {
    @Provides
    RadioVisualizer provideVisualizerModel() {
        return new RadioVisualizer();
    }
}
