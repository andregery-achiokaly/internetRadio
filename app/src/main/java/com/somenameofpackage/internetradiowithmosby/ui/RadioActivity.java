package com.somenameofpackage.internetradiowithmosby.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.presenter.RadioActivityPresenter;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.AudioWaveFragment;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.ControlFragment;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.StationsRecyclerViewFragment;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.dialogs.AddStationDialog;
import com.somenameofpackage.internetradiowithmosby.ui.views.RadioActivityView;

import butterknife.ButterKnife;
import fresh.Test;

public class RadioActivity extends MvpActivity<RadioActivityView, RadioActivityPresenter> implements RadioActivityView, AddStation {
    final private static String CREATE_STATION = "CREATE_STATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Test t = new Test();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_radio_layout, ControlFragment.newInstance())
                    .replace(R.id.fragment_audio_wave_layout, AudioWaveFragment.newInstance())
                    .replace(R.id.list_container, StationsRecyclerViewFragment.newInstance())
                    .commit();
        }
        ButterKnife.bind(this);
    }

    @NonNull
    @Override
    public RadioActivityPresenter createPresenter() {
        return new RadioActivityPresenter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_station_menu_btn:
                openDialogCreateStation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addStationToBD(String name, String source) {
           presenter.addStationToBD(name, source);
    }

    public void openDialogCreateStation() {
        DialogFragment dialogFragment = new AddStationDialog();
        dialogFragment.show(getSupportFragmentManager(), CREATE_STATION);
    }
}
