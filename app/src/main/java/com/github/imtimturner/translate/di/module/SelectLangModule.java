package com.github.imtimturner.translate.di.module;

import com.github.imtimturner.translate.di.scope.SelectLangScope;
import com.github.imtimturner.translate.view.SelectLangView;

import dagger.Module;
import dagger.Provides;

@Module
public class SelectLangModule {

    private SelectLangView view;

    public SelectLangModule(SelectLangView view) {
        this.view = view;
    }

    @SelectLangScope
    @Provides
    public SelectLangView provideSelectLangView(){
        return view;
    }

}
