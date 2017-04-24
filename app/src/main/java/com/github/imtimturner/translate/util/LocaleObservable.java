package com.github.imtimturner.translate.util;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

/**
 * Вспомогательный класс позволяющий следить за изменением языка устройства
 * в RX стиле
 * */
public class LocaleObservable {

    public static Observable<Locale> create(final Context context){
        return Observable.create(new ObservableOnSubscribe<Locale>() {
            @Override
            public void subscribe(final ObservableEmitter<Locale> e) throws Exception {
                e.onNext(getLocale(context.getResources().getConfiguration()));

                final ComponentCallbacks callback = new ComponentCallbacks() {
                    @Override
                    public void onConfigurationChanged(Configuration newConfig) {
                        if (getLocale(context.getResources().getConfiguration()) !=
                                getLocale(newConfig)) {
                            e.onNext(getLocale(newConfig));
                        }
                    }

                    @Override
                    public void onLowMemory() {
                    }
                };

                e.setDisposable(new Disposable() {
                    private boolean disposed;

                    @Override
                    public void dispose() {
                        disposed = true;
                        context.unregisterComponentCallbacks(callback);
                    }

                    @Override
                    public boolean isDisposed() {
                        return disposed;
                    }
                });
                context.registerComponentCallbacks(callback);
            }

            private Locale getLocale(Configuration config) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    return config.getLocales().get(0);
                } else {
                    //noinspection deprecation
                    return config.locale;
                }
            }
        });
    }

}
