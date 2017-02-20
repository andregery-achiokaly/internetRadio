package com.somenameofpackage.internetradiowithmosby.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.AudioWaveFragment;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.ControlFragment;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.StationsRecyclerViewFragment;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.dialogs.AddStationDialog;

import butterknife.ButterKnife;

public class RadioActivity extends AppCompatActivity implements AddStation {
    final private static String CREATE_STATION = "CREATE_STATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_radio_layout, ControlFragment.newInstance())
                    .replace(R.id.fragment_audio_wave_layout, AudioWaveFragment.newInstance())
                    .replace(R.id.list_container, StationsRecyclerViewFragment.newInstance())
                    .commit();
        }
        ButterKnife.bind(this);
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
        StationsRecyclerViewFragment stationsListFragment = (StationsRecyclerViewFragment)
                getSupportFragmentManager().findFragmentById(R.id.list_container);

        if (stationsListFragment != null) {
            stationsListFragment.addStationToBD(name, source);
        } else {
            Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        }
    }

    public void openDialogCreateStation() {
        DialogFragment dialogFragment = new AddStationDialog();
        dialogFragment.show(getSupportFragmentManager(), CREATE_STATION);
    }
}
