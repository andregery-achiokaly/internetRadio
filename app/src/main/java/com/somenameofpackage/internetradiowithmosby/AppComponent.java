package com.somenameofpackage.internetradiowithmosby;


import com.somenameofpackage.internetradiowithmosby.model.db.RadioStationsModule;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModule;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.model.visualizer.VisualizerModule;
import com.somenameofpackage.internetradiowithmosby.presenter.AudioWavePresenter;
import com.somenameofpackage.internetradiowithmosby.presenter.ControlPresenter;
import com.somenameofpackage.internetradiowithmosby.presenter.StationsListPresenter;

import dagger.Component;

@Component(modules = {RadioModule.class, VisualizerModule.class, RadioStationsModule.class})
public interface AppComponent {
    void injectsRadioService(RadioService radioService);
    void injectsAudioWavePresenter(AudioWavePresenter audioWavePresenter);
    void injectsControlPresenter(ControlPresenter controlPresenter);
    void injectsStationsListPresenter(StationsListPresenter stationsListPresenter);
}
