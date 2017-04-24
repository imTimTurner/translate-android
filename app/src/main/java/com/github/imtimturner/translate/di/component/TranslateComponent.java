package com.github.imtimturner.translate.di.component;

import com.github.imtimturner.translate.di.module.TranslateModule;
import com.github.imtimturner.translate.di.scope.TranslateScope;
import com.github.imtimturner.translate.ui.fragment.TabTranslateFragment;

import dagger.Subcomponent;

@TranslateScope
@Subcomponent(
        modules = {
                TranslateModule.class
        }
)
public interface TranslateComponent {
    void inject(TabTranslateFragment fragment);
}
