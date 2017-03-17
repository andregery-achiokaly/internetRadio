package com.somenameofpackage.internetradiowithmosby.model.db.realmDB;

import android.content.SharedPreferences;
import android.support.test.rule.ActivityTestRule;

import com.somenameofpackage.internetradiowithmosby.model.db.Station;
import com.somenameofpackage.internetradiowithmosby.ui.RadioActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.realm.RealmResults;
import rx.Subscriber;

import static android.content.Context.MODE_PRIVATE;

public class StationsRelamDBTest {
    @Rule
    public ActivityTestRule<RadioActivity> activityTestRule = new ActivityTestRule<>(RadioActivity.class);
    final private static String INITIAL_DB = "DB_IS_INITIAL";
    final private static String REALM_DB_PREFERENCES = "REALM_DB_PREFERENCES";
    private StationsRelamDB stationsRelamDB;

    @Before
    public void setUp() throws Exception {
        stationsRelamDB = new StationsRelamDB();
    }

    private Subscriber<RealmResults<Station>> getSubscriberCheckerCountOfStations(int i) {
        return new Subscriber<RealmResults<Station>>() {
            boolean isReady = false;

            @Override
            public void onCompleted() {
                isReady = true;
            }

            @Override
            public void onError(Throwable e) {
                Assert.fail(e.getMessage());
            }

            @Override
            public void onNext(RealmResults<Station> stations) {
                if (isReady) Assert.assertNotEquals(i, stations.size());
            }
        };
    }

    @Test
    public void setDefaultValues() throws Throwable {
        resetSharedPreferences();
        activityTestRule.runOnUiThread(() -> {
            stationsRelamDB.clearBD();
            stationsRelamDB.setDefaultValues(activityTestRule.getActivity());
            stationsRelamDB.getStations()
                    .filter(stations -> stations.isValid() && stations.isLoaded())
                    .subscribe(getSubscriberCheckerCountOfStations(2));
        });
    }

    private void resetSharedPreferences() {
        SharedPreferences sharedPreferences = activityTestRule.getActivity().getSharedPreferences(REALM_DB_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putBoolean(INITIAL_DB, false);
        ed.apply();
    }

    @Test
    public void getCurrentStation() throws Throwable {
        activityTestRule.runOnUiThread(() -> {
            stationsRelamDB.getCurrentStation().subscribe(station -> {
                Assert.assertNotEquals(null, station);
            });
        });
    }

    @Test
    public void addStation() throws Throwable {
        activityTestRule.runOnUiThread(() -> {
            final Integer[] i = new Integer[1];
            stationsRelamDB.getStations()
                    .filter(stations -> stations.isValid() && stations.isLoaded())
                    .subscribe(new Subscriber<RealmResults<Station>>() {
                        boolean isReady = false;

                        @Override
                        public void onCompleted() {
                            isReady = true;
                        }

                        @Override
                        public void onError(Throwable e) {
                            Assert.fail(e.getMessage());
                        }

                        @Override
                        public void onNext(RealmResults<Station> stations) {
                            if (isReady) {
                                i[0] = stations.size();
                                stationsRelamDB.addStation(new Station(stationName, stationName));
                                stationsRelamDB.getStations()
                                        .filter(s -> s.isValid() && s.isLoaded())
                                        .subscribe(s -> Assert.assertEquals(i[0] + 1, s.size()));
                            }
                        }
                    });
        });
    }

    @Test
    public void setPlayingStationSource() throws Throwable {
        activityTestRule.runOnUiThread(() -> {
            stationsRelamDB.addStation(new Station(stationName, stationName));
            stationsRelamDB.getStations()
                    .filter(stations -> stations.isLoaded() && stations.isValid())
                    .subscribe(stations -> {
                        setSubscriberForSetAndCheckCurrentStation();
                    });

            stationsRelamDB.deleteStation(stationName);
        });
    }

    private void setSubscriberForSetAndCheckCurrentStation() {
        new Subscriber<RealmResults<Station>>() {
            boolean isReady = false;

            @Override
            public void onCompleted() {
                isReady = true;
            }

            @Override
            public void onError(Throwable e) {
                Assert.fail(e.getMessage());
            }

            @Override
            public void onNext(RealmResults<Station> stations) {
                if (isReady) {
                    for (Station s : stations) {
                        if (stationName.equals(s.getSource())) {
                            stationsRelamDB.setPlayingStationSource(s);
                            break;
                        }
                    }
                    checkCurrentStation();
                }
            }
        };
    }

    private void checkCurrentStation() {
        stationsRelamDB.getCurrentStation()
                .filter(s -> s.isLoaded() && s.isValid())
                .subscribe(new Subscriber<Station>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Assert.fail(e.getMessage());
                    }

                    @Override
                    public void onNext(Station station) {
                        Assert.assertEquals(stationName, station.getSource());
                    }
                });
    }

    private static final String stationName = "test1";

    @Test
    public void getStations() throws Throwable {
        activityTestRule.runOnUiThread(() -> {
            stationsRelamDB.addStation(new Station("test", "test"));

            stationsRelamDB.getStations()
                    .filter(stations -> stations.isLoaded() && stations.isValid())
                    .subscribe(getSubscriberCheckerCountOfStations(0));

            stationsRelamDB.deleteStation("test");
        });
    }

    @Test
    public void clearBD() throws Throwable {
        activityTestRule.runOnUiThread(() -> {
            stationsRelamDB.addStation(new Station(stationName, stationName));
            stationsRelamDB.clearBD();
            stationsRelamDB.getStations().subscribe(getSubscriberCheckerCountOfStations(0));
        });
    }

    @Test
    public void deleteStation() throws Throwable {
        activityTestRule.runOnUiThread(() -> {
            final Integer[] i = new Integer[1];
            stationsRelamDB.getStations()
                    .filter(stations -> stations.isValid() && stations.isLoaded())
                    .subscribe(new Subscriber<RealmResults<Station>>() {
                        boolean isReady = false;

                        @Override
                        public void onCompleted() {
                            isReady = true;
                        }

                        @Override
                        public void onError(Throwable e) {
                            Assert.fail(e.getMessage());
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(RealmResults<Station> stations) {
                            if (isReady) {
                                i[0] = stations.size();
                                stationsRelamDB.deleteStation(stationName);
                                stationsRelamDB.getStations()
                                        .filter(s -> s.isValid() && s.isLoaded())
                                        .subscribe(s -> Assert.assertEquals(i[0] - 1, s.size()));
                            }
                        }
                    });
        });
    }
}