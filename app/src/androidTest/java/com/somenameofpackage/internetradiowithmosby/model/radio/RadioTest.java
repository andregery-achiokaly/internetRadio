package com.somenameofpackage.internetradiowithmosby.model.radio;

import com.somenameofpackage.internetradiowithmosby.ui.fragments.RadioStatus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class RadioTest {
    private static final String radioStation = "http://cast.radiogroup.com.ua:8000/jamfm";
    private Radio radioMock;

    @Before
    public void setUp() throws Exception {
        radioMock = Mockito.spy(new Radio());
    }

    @Test
    public void getRadioModelStatusObservable() throws Exception {

    }

    @Test
    public void getRadioIdObservable() throws Exception {
        BehaviorSubject<String> changePlayStateSubject = BehaviorSubject.create();
        radioMock.setChangePlaySubject(changePlayStateSubject);
        changePlayStateSubject.onNext(radioStation);
        radioMock.getRadioIdObservable().subscribe(id -> Assert.assertEquals(true, id >= 0));
    }

    @Test
    public void getRadioIdObservableError() throws Exception {
        BehaviorSubject<String> changePlayStateSubject = BehaviorSubject.create();
        radioMock.setChangePlaySubject(changePlayStateSubject);
        changePlayStateSubject.onNext("it isn't radioMock");
        radioMock.getRadioIdObservable().subscribe(id -> Assert.assertEquals(false, id >= 0));
    }

    @Test
    public void setChangePlaySubject() throws Exception {
        BehaviorSubject<String> changePlayStateSubject = BehaviorSubject.create();
        radioMock.setChangePlaySubject(changePlayStateSubject);

        Action1<RadioStatus> idSubscriber1 = status -> Assert.assertEquals(false, status == RadioStatus.isPlay);
        Action1<RadioStatus> idSubscriber2 = status -> Assert.assertEquals(false, status == RadioStatus.isStop);

        Subscription subscription = radioMock.getRadioModelStatusObservable().subscribe(idSubscriber1);
        changePlayStateSubject.onNext(radioStation);
        subscription.unsubscribe();
        radioMock.getRadioModelStatusObservable().subscribe(idSubscriber2);
        changePlayStateSubject.onNext(radioStation);
        subscription.unsubscribe();
    }

    @Test
    public void closeMediaPlayer() throws Exception {
        radioMock.closeMediaPlayer();
        verify(radioMock).closeMediaPlayer();
    }


    @Test
    public void onPrepared() throws Exception {
        BehaviorSubject<String> changePlayStateSubject = BehaviorSubject.create();
        radioMock.setChangePlaySubject(changePlayStateSubject);
        changePlayStateSubject.onNext(radioStation);
        new Thread(() -> verify(radioMock, timeout(5000)).onPrepared(any())).start();
    }

    @Test
    public void onError() throws Exception {
        BehaviorSubject<String> changePlayStateSubject = BehaviorSubject.create();
        radioMock.setChangePlaySubject(changePlayStateSubject);
        changePlayStateSubject.onNext("");
        new Thread(() -> verify(radioMock, timeout(5000)).onError(any(), anyInt(), anyInt())).start();
    }
}