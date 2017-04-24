package com.github.imtimturner.translate.di.module;

import com.github.imtimturner.translate.di.scope.HistoryScope;
import com.github.imtimturner.translate.view.HistoryView;

import dagger.Module;
import dagger.Provides;

@Module
public class HistoryModule {

    private final HistoryView view;

    public HistoryModule(HistoryView view) {
        this.view = view;
    }

    @HistoryScope
    @Provides
    public HistoryView provideHistoryView(){
        return view;
    }

}
