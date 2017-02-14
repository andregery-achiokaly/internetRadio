package com.somenameofpackage.internetradiowithmosby;


import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModule;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;

import dagger.Component;

@Component(modules = {RadioModule.class})
public interface AppComponent {
    void injectsRadioService(RadioService radioService);
    void injectsRa(RadioService radioService);
}
