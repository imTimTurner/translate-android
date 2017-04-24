package com.github.imtimturner.translate.di.component;

import com.github.imtimturner.translate.di.module.SelectLangModule;
import com.github.imtimturner.translate.di.scope.SelectLangScope;
import com.github.imtimturner.translate.ui.fragment.SelectLangFragment;

import dagger.Subcomponent;

@SelectLangScope
@Subcomponent(
        modules = {
                SelectLangModule.class
        }
)
public interface SelectLangComponent {
    void inject(SelectLangFragment fragment);
}
