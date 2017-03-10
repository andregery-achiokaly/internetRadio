package com.somenameofpackage.internetradiowithmosby.ui.fragments;

public enum RadioStatus {
    isPlay("Stop"), isStop("Play"), Error("Error"), Wait("Wait");

    private final String status;

    RadioStatus(String s) {
        status = s;
    }

    public String toString() {
        return this.status;
    }
}
