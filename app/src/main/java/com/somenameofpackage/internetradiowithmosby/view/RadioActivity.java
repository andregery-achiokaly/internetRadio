package com.somenameofpackage.internetradiowithmosby.view;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.model.realmDB.StationsDB;
import com.somenameofpackage.internetradiowithmosby.view.controlUI.ControlFragment;
import com.somenameofpackage.internetradiowithmosby.view.radioList.StationsListFragment;

import butterknife.ButterKnife;

public class RadioActivity extends AppCompatActivity {
    StationsDB stationsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //temporary
        stationsDB = new StationsDB(getApplicationContext());
        stationsDB.clearBD();
        stationsDB.addStation("BestFM", "http://radio.bestfm.fm:8080/bestfm", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round));
        stationsDB.addStation("JamFM", "http://cast.radiogroup.com.ua:8000/jamfm", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_radio_layout, new ControlFragment())
                    .replace(R.id.list_container, new StationsListFragment())
                    .commit();
        }

        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //temporary
        stationsDB.clearBD();
        stationsDB.closeBD();
    }
}
