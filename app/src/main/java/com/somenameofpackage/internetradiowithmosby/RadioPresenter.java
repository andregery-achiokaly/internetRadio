package com.somenameofpackage.internetradiowithmosby;


import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

class RadioPresenter extends MvpBasePresenter<RadioView> {
    RadioModel radioModel = new RadioModel(new RadioListener() {
        @Override
        public void onPlay(String message) {
            getView().showPlay(message);
        }

        @Override
        public void onPause(String message) {
            getView().showPause(message);
        }
    });

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
