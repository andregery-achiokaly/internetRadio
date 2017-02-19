package com.somenameofpackage.internetradiowithmosby.model.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Station extends RealmObject {
    public static final String STATION = "Station";
    public static final String STATION_NAME = "name";
    public static final String STATION_SOURCE = "source";
    public static final String STATION_IS_PLAY = "isPlay";
    public static final String STATION_ID_KEY = "id_key";

    private boolean isPlay;
    @PrimaryKey
    private int id_key;
    @Required
    private String name;
    @Required
    private String source;

    public Station() {
    }

    public Station(String name, String source) {
        this.name = name;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public int getId_key() {
        return id_key;
    }

    public void setId_key(int id_key) {
        this.id_key = id_key;
    }
}
