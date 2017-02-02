package com.somenameofpackage.internetradiowithmosby.ui;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.model.db.RadioStations;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.AudioWaveFragment;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.ControlFragment;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.dialogs.AddStationDialog;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.StationsListFragment;

import butterknife.ButterKnife;

public class RadioActivity extends AppCompatActivity implements AddStation {
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
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        Boolean isCreated = sharedPreferences.getBoolean(INITIAL_DB, false);
        if (!isCreated) {
            new RadioStations(getApplicationContext()).firstInitial(getApplicationContext());
            sharedPreferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor ed = sharedPreferences.edit();
            ed.putBoolean(INITIAL_DB, true);
            ed.apply();
        }
    }
}
