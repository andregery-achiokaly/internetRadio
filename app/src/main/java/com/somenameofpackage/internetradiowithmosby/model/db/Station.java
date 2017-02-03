package com.somenameofpackage.internetradiowithmosby.model.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Station extends RealmObject {
    private boolean isPlay;

    @PrimaryKey
    private int id_key;
    @Required
    private String name;
    @Required

    private String source;
    @Required
    private byte[] image;

    Station(String name, String source, byte[] image) {
        this.name = name;
        this.source = source;
        this.image = image;
    }

    public Station() {
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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

    public static String getSourceFieldName() {
        return "source";
    }

    public static String getNameFieldName() {
        return "name";
    }

    public static String getImageFieldName() {
        return "image";
    }

    public static String getIsPlayFieldName() {
        return "isPlay";
    }
    public static String getsetId_keyFieldName() {
        return "id_key";
    }

    public static String getNameTable(){ return Station.class.getSimpleName();}
}
