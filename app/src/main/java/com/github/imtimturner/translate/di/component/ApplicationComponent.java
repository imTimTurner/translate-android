package com.github.imtimturner.translate.di.component;

import android.content.Context;

import com.github.imtimturner.translate.di.module.ApplicationModule;
import com.github.imtimturner.translate.di.module.FavoritesModule;
import com.github.imtimturner.translate.di.module.HistoryModule;
import com.github.imtimturner.translate.di.module.NetworkModule;
import com.github.imtimturner.translate.di.module.SelectLangModule;
import com.github.imtimturner.translate.di.module.TranslateModule;
import com.github.imtimturner.translate.ui.fragment.SelectLangFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                ApplicationModule.class,
                NetworkModule.class
        }
)
public interface ApplicationComponent {
    Context getApplicationContext();
    TranslateComponent getTranslateComponent(TranslateModule module);
    FavoritesComponent getFavoritesComponent(FavoritesModule module);
    HistoryComponent getHistoryComponent(HistoryModule module);
    SelectLangComponent getSelectLangComponent(SelectLangModule module);
}
