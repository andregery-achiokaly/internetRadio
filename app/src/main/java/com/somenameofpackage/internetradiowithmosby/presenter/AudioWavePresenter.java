package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModel;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.model.db.realmDB.StationsRelamDB;
import com.somenameofpackage.internetradiowithmosby.model.visualizer.VisualizerModel;
import com.somenameofpackage.internetradiowithmosby.presenter.listeners.RadioListener;
import com.somenameofpackage.internetradiowithmosby.ui.views.WaveView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class AudioWavePresenter extends MvpBasePresenter<WaveView> {
    private RadioModel radioModel;
    private VisualizerModel visualizerModel;

    private final RadioListener radioListener = new RadioListener() {
        @Override
        public void onPlay(String source) {
            visualizerModel.setupVisualizerFxAndUI();
        }

        @Override
        public void onPause() {
        }

        @Override
        public void onError(String message) {
            Log.v(getClass().getSimpleName(), message);
        }
    };

    public AudioWavePresenter(Context context) {
        StationsRelamDB stationsRelamDB = new StationsRelamDB(context);
        final String source = stationsRelamDB.getPlayingStationSource();

        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                radioModel = ((RadioService.RadioBinder) iBinder).getModel(radioListener, source);
                visualizerModel = new VisualizerModel(radioModel);
                visualizerModel.getVisualizerObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getStationsObserver());
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        context.bindService(new Intent(context, RadioService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private Observer<Byte[]> getStationsObserver() {
        return new DefaultObserver<Byte[]>() {
            @Override
            public void onNext(Byte[] value) {
                if (getView() != null) getView().updateVisualizer(value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }
}
