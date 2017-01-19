package com.somenameofpackage.internetradiowithmosby;


import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

class RadioPresenter extends MvpBasePresenter<RadioView> {
    RadioModel radioModel = new RadioModel();

    void startPlaying(String source) {
        radioModel.startPlay(source);
    }

    void stopPlaying() {
        radioModel.stopPlay();
    }

    public void detachView(boolean retainPresenterInstance) {
        super.detachView(retainPresenterInstance);
        if (!retainPresenterInstance) {
            radioModel.closePlayer();
        }
    }
}
