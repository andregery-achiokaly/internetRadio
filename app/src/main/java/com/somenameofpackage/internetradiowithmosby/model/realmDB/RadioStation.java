package com.somenameofpackage.internetradiowithmosby.model.realmDB;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RadioStation extends RealmObject {
    private boolean isPlay;

    @PrimaryKey
    private int id;
    @Required
    private String name;
    @Required
    private String source;
    @Required
    private byte[] image;

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

    public static String getIdFieldName() {
        return "id";
    }

    public static String getNameTable(){ return RadioStation.class.getSimpleName();}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
