package com.github.imtimturner.translate.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

/**
 * Вспомогательный класс позволяющий следить за изменением состояния сети
 * в RX стиле
 * */
public class NetworkAvailableObservable {

    public static Observable<Boolean> create(final Context context){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                ConnectivityManager connectivityManager = (ConnectivityManager)
                        context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                e.onNext(activeNetworkInfo != null && activeNetworkInfo.isConnected());

                final BroadcastReceiver receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent data) {
                        if (data != null && data.hasExtra("networkInfo")) {
                            NetworkInfo info = data.getParcelableExtra("networkInfo");
                            e.onNext(info.isConnected());
                        }
                    }
                };
                context.registerReceiver(receiver,
                        new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                e.setDisposable(new Disposable() {

                    private boolean disposed;

                    @Override
                    public void dispose() {
                        disposed = true;
                        context.unregisterReceiver(receiver);
                    }

                    @Override
                    public boolean isDisposed() {
                        return disposed;
                    }
                });

            }
        });
    }

}
