package com.somenameofpackage.internetradiowithmosby.ui.fragments;

public enum Status {
    isPlay("Stop"), isStop("Play"), Error("Error");

    private final String status;

    Status(String s) {
        status = s;
    }

    public String toString() {
        return this.status;
    }
}
