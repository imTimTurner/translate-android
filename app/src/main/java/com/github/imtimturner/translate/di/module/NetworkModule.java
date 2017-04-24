package com.github.imtimturner.translate.di.module;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    Cache provideOkHttpCache(Context context) {
        return new Cache(context.getCacheDir(), 10 * 1024 * 1024);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(final Context context, Cache cache) {
        return new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (isNetworkAvailable(context)) {
                            request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build();
                        } else {
                            request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                        }
                        return chain.proceed(request);
                    }

                    private boolean isNetworkAvailable(Context context) {
                        ConnectivityManager connectivityManager = (ConnectivityManager)
                                context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
                    }
                })
                .build();
    }
}
