package com.somenameofpackage.internetradiowithmosby;


import com.somenameofpackage.internetradiowithmosby.model.db.realmDB.DataBaseModule;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModule;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.model.visualizer.VisualizerModule;
import com.somenameofpackage.internetradiowithmosby.presenter.AudioWavePresenter;
import com.somenameofpackage.internetradiowithmosby.presenter.ControlPresenter;
import com.somenameofpackage.internetradiowithmosby.presenter.StationsListPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RadioModule.class, VisualizerModule.class, DataBaseModule.class})
public interface AppComponent {
    void injectsRadioService(RadioService radioService);
    void injectsAudioWavePresenter(AudioWavePresenter audioWavePresenter);
    void injectsControlPresenter(ControlPresenter controlPresenter);
    void injectsStationsListPresenter(StationsListPresenter stationsListPresenter);
}
