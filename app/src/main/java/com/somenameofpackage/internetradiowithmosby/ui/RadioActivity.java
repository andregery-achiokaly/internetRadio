package com.somenameofpackage.internetradiowithmosby.ui;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.model.db.RadioStation;
import com.somenameofpackage.internetradiowithmosby.model.db.RadioStations;
import com.somenameofpackage.internetradiowithmosby.model.db.SQLiteHelpDB.StationsDBHelper;
import com.somenameofpackage.internetradiowithmosby.model.db.realmDB.StationsRelamDB;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.AudioWaveFragment;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.ControlFragment;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.dialogs.AddStationDialog;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.StationsListFragment;

import butterknife.ButterKnife;

public class RadioActivity extends AppCompatActivity implements AddStation {
    SharedPreferences sharedPreferences;
    final private static String INITIAL_DB = "INITIAL_DB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createBD();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_radio_layout, new ControlFragment())
                    .replace(R.id.fragment_audio_wave_layout, new AudioWaveFragment())
                    .replace(R.id.list_container, new StationsListFragment())
                    .commit();
        }

        ButterKnife.bind(this);
    }

    public void addStationToBD(String name, String source, Bitmap bitmap) {
        StationsListFragment stationsListFragment = (StationsListFragment)
                getSupportFragmentManager().findFragmentById(R.id.list_container);

        if (stationsListFragment != null) {
            stationsListFragment.addStationToBD(name, source, bitmap);
        } else {
            Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        }
    }

    public void createStationDialog(String tag) {
        DialogFragment dialogFragment = new AddStationDialog();
        dialogFragment.show(getSupportFragmentManager(), tag);
    }

    void createBD() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        Boolean isCreated = sharedPreferences.getBoolean(INITIAL_DB, false);
        if (!isCreated) {
            firstInitial();
        }
    }

    void firstInitial() {
        RadioStations stationsDB = new RadioStations(getApplicationContext());
        stationsDB.addStation(getString(R.string.best_fm_name),
                getString(R.string.best_fm_source),
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round));

        stationsDB.addStation(getString(R.string.jam_fm_name),
                getString(R.string.jam_fm_source),
                BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher));

        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putBoolean(INITIAL_DB, true);
        ed.apply();
    }
}
