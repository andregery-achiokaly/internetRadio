package com.somenameofpackage.internetradiowithmosby.model.db;

public class Station{
    public static final String TABLE_NAME = "Station";
    public static final String STATION_NAME = "name";
    public static final String STATION_SOURCE = "source";
    public static final String STATION_IS_PLAY = "isPlay";
    public static final String STATION_ID_KEY = "id_key";

    private int id_key;
    private String name;
    private String source;
    private boolean isPlay;

    public Station() {
    }

    public Station(String name, String source) {
        this.name = name;
        this.source = source;
    }

    public Station(String name, String source, int isPlay, int id) {
        this.name = name;
        this.source = source;
        this.isPlay = isPlay != 0;
        this.id_key = id;
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
