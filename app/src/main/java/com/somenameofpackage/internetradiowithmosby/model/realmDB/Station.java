package com.somenameofpackage.internetradiowithmosby.model.realmDB;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Station extends RealmObject {
    private boolean isPlay;

    @Required
    private String name;
    @Required
    @PrimaryKey
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

    public static String getSourceFieldName(){ return "source";}
    public static String getNameFieldName(){ return "name";}
    public static String getImageFieldName(){ return "image";}
    public static String getIsPlayFieldName(){ return "isPlay";}
}
