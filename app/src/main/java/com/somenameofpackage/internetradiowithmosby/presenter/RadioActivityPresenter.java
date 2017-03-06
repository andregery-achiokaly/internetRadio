package com.somenameofpackage.internetradiowithmosby.presenter;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.somenameofpackage.internetradiowithmosby.model.db.Station;
import com.somenameofpackage.internetradiowithmosby.model.db.sqliteDB.SqliteDBHelper;
import com.somenameofpackage.internetradiowithmosby.ui.RadioApplication;
import com.somenameofpackage.internetradiowithmosby.ui.views.RadioActivityView;

import javax.inject.Inject;

public class RadioActivityPresenter  implements MvpPresenter<RadioActivityView> {
    @Inject
    SqliteDBHelper dataBase;

    public RadioActivityPresenter() {
        RadioApplication.getComponent().injectsRadioActivityPresenter(this);
    }



    public void addStationToBD(String name, String source) {
        dataBase.addStation(new Station(name, source));
    }

    @Override
    public void attachView(RadioActivityView view) {

    }

    @Override
    public void detachView(boolean retainInstance) {

    }
}
