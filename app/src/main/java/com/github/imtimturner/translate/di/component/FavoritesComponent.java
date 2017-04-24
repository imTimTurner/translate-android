package com.github.imtimturner.translate.di.component;

import com.github.imtimturner.translate.di.module.FavoritesModule;
import com.github.imtimturner.translate.di.scope.FavoritesScope;
import com.github.imtimturner.translate.ui.fragment.TabFavoritesFragment;

import dagger.Subcomponent;

@FavoritesScope
@Subcomponent(
        modules = {
                FavoritesModule.class
        }
)
public interface FavoritesComponent {
    void inject(TabFavoritesFragment fragment);
}
