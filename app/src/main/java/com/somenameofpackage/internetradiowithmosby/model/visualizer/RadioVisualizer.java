package com.somenameofpackage.internetradiowithmosby.model.visualizer;

import android.media.audiofx.Visualizer;

import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class RadioVisualizer {
    private Visualizer visualizer;
    private PublishSubject<byte[]> source = PublishSubject.create();

    public Subject<byte[], byte[]> getVisualizerObservable() {
        return source;
    }

    public void setupVisualizerFxAndUI(int id) {
        if (id != -1) {
            if (visualizer != null) visualizer.release();
            visualizer = new Visualizer(id);
            visualizer.setEnabled(false);
            visualizer.setCaptureSize(16);
            visualizer.setDataCaptureListener(new AudioWaveDataCaptureListener(),
                    Visualizer.getMaxCaptureRate() / 2, true, false);
            visualizer.setEnabled(true);
        }
    }

    public void stop() {
        visualizer.setEnabled(false);
        visualizer.release();
    }

    private class AudioWaveDataCaptureListener implements Visualizer.OnDataCaptureListener {
        public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
            source.onNext(bytes);
        }

        @Override
        public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int i) {

        }
    }
}
