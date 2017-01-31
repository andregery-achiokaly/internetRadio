package com.somenameofpackage.internetradiowithmosby.ui;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.model.SQLiteHelpDB.StationsDBHelper;
import com.somenameofpackage.internetradiowithmosby.model.realmDB.RadioStation;
import com.somenameofpackage.internetradiowithmosby.model.realmDB.StationsDB;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.AudioWaveFragment;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.ControlFragment;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.dialogs.AddStationDialog;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.StationsListFragment;

import java.io.ByteArrayOutputStream;

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
            firstInitialBD();
        }
    }


    void firstInitialBD() {
        StationsDB stationsDB = new StationsDB(getApplicationContext());
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
