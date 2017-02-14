package com.somenameofpackage.internetradiowithmosby.model.visualizer;

import android.media.audiofx.Visualizer;

import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModel;

import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class VisualizerModel {
    private Visualizer mVisualizer;
    private PublishSubject<Byte[]> source = PublishSubject.create();

    public Subject<Byte[], Byte[]> getVisualizerObservable() {
        return source;
    }

    public void setupVisualizerFxAndUI(int id) {
        if (id != -1) {
            if (mVisualizer != null) mVisualizer.release();
            mVisualizer = new Visualizer(id);
            mVisualizer.setEnabled(false);
            mVisualizer.setCaptureSize(16);
            mVisualizer.setDataCaptureListener(new AudioWaveDataCaptureListener(),
                    Visualizer.getMaxCaptureRate() / 2, true, false);
            mVisualizer.setEnabled(true);
        }
    }

    private class AudioWaveDataCaptureListener implements Visualizer.OnDataCaptureListener {
        public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
            Byte[] result = new Byte[bytes.length];
            int i = 0;
            for (byte b : bytes) result[i++] = b;

            source.onNext(result);
        }

        @Override
        public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int i) {

        }
    }
}
