package com.github.imtimturner.translate.util;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Вспомогательный класс который просто реализует интерфейс Observer
 * */
public class DefaultObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T value) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }

}
