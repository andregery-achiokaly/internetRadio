package com.somenameofpackage.internetradiowithmosby.model.db.realmDB;

import android.support.test.rule.ActivityTestRule;

import com.somenameofpackage.internetradiowithmosby.model.db.Station;
import com.somenameofpackage.internetradiowithmosby.ui.RadioActivity;
import com.somenameofpackage.internetradiowithmosby.ui.RadioApplicationTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import io.realm.RealmResults;
import rx.Subscriber;

public class StationsRelamDBTest {
    @Rule
    public ActivityTestRule<RadioActivity> activityTestRule = new ActivityTestRule<>(RadioActivity.class);

    @Inject
    StationsRelamDB stationsRelamDB;

    @Before
    public void setUp() throws Exception {
        RadioApplicationTest.getComponent().injectRelamDBTest(this);
    }


    @Test
    public void setDefaultValues() throws Throwable {

    }

    @Test
    public void getCurrentStation() throws Exception {

    }

    @Test
    public void addStation() throws Exception {

    }

    @Test
    public void setPlayingStationSource() throws Exception {

    }

    @Test
    public void getStations() throws Throwable {
        activityTestRule.runOnUiThread(() -> {
            stationsRelamDB.addStation(new Station("test", "test"));

            stationsRelamDB.getStations().subscribe(new Subscriber<RealmResults<Station>>() {
                boolean isReady = false;

                @Override
                public void onCompleted() {
                    isReady = true;
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(RealmResults<Station> stations) {
                    if (isReady) Assert.assertNotEquals(0, stations.size());
                }
            });
        });
    }

    @Test
    public void closeBD() throws Exception {

    }

    @Test
    public void clearBD() throws Exception {

    }

    @Test
    public void deleteStation() throws Exception {

    }

}