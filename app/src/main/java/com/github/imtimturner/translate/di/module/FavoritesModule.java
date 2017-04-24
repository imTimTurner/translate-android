package com.github.imtimturner.translate.di.module;

import com.github.imtimturner.translate.di.scope.FavoritesScope;
import com.github.imtimturner.translate.view.FavoritesView;

import dagger.Module;
import dagger.Provides;

@Module
public class FavoritesModule {

    private final FavoritesView view;

    public FavoritesModule(FavoritesView view) {
        this.view = view;
    }

    @FavoritesScope
    @Provides
    public FavoritesView provideFavoritesView(){
        return view;
    }

}
