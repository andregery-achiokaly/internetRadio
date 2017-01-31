package com.somenameofpackage.internetradiowithmosby.model.visualizer;


import android.media.audiofx.Visualizer;

import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModel;
import com.somenameofpackage.internetradiowithmosby.presenter.VisualizerListener;

public class VisualizerModel {
    private Visualizer mVisualizer;
    private RadioModel radioModel;
    private VisualizerListener visualizerListener;

    public VisualizerModel(RadioModel radioModel, VisualizerListener visualizerListener){
        this.radioModel = radioModel;
        this.visualizerListener = visualizerListener;
        setupVisualizerFxAndUI();
    }


    public void setupVisualizerFxAndUI() {
        if (mVisualizer != null) mVisualizer.release();
        mVisualizer = new Visualizer(radioModel.getMediaPlayer().getAudioSessionId());
        mVisualizer.setEnabled(false);
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(new AudioWaveDataCaptureListener(),
                Visualizer.getMaxCaptureRate(), true, false);
        mVisualizer.setEnabled(true);
    }

    private class AudioWaveDataCaptureListener implements Visualizer.OnDataCaptureListener {
        public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
            if (visualizerListener != null) visualizerListener.updateVisualizer(bytes);
        }

        @Override
        public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int i) {

        }
    }
}
