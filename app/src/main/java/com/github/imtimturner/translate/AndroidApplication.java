package com.github.imtimturner.translate;

import android.app.Application;
import android.content.Context;

import com.github.imtimturner.translate.di.component.ApplicationComponent;
import com.github.imtimturner.translate.di.component.DaggerApplicationComponent;
import com.github.imtimturner.translate.di.module.ApplicationModule;

import io.realm.Realm;

public class AndroidApplication extends Application {

    public static AndroidApplication get(Context context){
        return (AndroidApplication) context.getApplicationContext();
    }

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeRealm();
        initializeComponent();
    }

    private void initializeComponent(){
        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    private void initializeRealm(){
        Realm.init(this);
    }

    public ApplicationComponent getComponent(){
        return component;
    }

}
