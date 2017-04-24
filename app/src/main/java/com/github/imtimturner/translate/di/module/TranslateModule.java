package com.github.imtimturner.translate.di.module;

import com.github.imtimturner.translate.di.scope.TranslateScope;
import com.github.imtimturner.translate.view.TranslateView;

import dagger.Module;
import dagger.Provides;

@Module
public class TranslateModule {

    private final TranslateView view;

    public TranslateModule(TranslateView view) {
        this.view = view;
    }

    @TranslateScope
    @Provides
    public TranslateView provideTranslateView(){
        return view;
    }

}
