package com.somenameofpackage.internetradiowithmosby.model.visualizer;

import android.media.audiofx.Visualizer;

import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModel;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class VisualizerModel {
    private Visualizer mVisualizer;
    private final RadioModel radioModel;
    private PublishSubject<Byte[]> source = PublishSubject.create();

    public Subject<Byte[]> getVisualizerObservable(){
        return source;
    }

    public VisualizerModel(RadioModel radioModel) {
        this.radioModel = radioModel;
    }

    public void setupVisualizerFxAndUI() {
        if (mVisualizer != null) mVisualizer.release();
        mVisualizer = new Visualizer(radioModel.getMediaPlayer().getAudioSessionId());
        mVisualizer.setEnabled(false);
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(new AudioWaveDataCaptureListener(),
                Visualizer.getMaxCaptureRate() / 2, true, false);
        mVisualizer.setEnabled(true);
    }

    private class AudioWaveDataCaptureListener implements Visualizer.OnDataCaptureListener {
        public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Byte[] result = new Byte[bytes.length];
            int i = 0;
            for(byte b: bytes) result[i++] = b;
            source.onNext(result);
        }

        @Override
        public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int i) {

        }
    }
}
