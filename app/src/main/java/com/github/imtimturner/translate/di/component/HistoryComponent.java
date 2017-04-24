package com.github.imtimturner.translate.di.component;

import com.github.imtimturner.translate.di.module.HistoryModule;
import com.github.imtimturner.translate.di.scope.HistoryScope;
import com.github.imtimturner.translate.ui.fragment.TabHistoryFragment;

import dagger.Subcomponent;

@HistoryScope
@Subcomponent(
        modules = {
                HistoryModule.class
        }
)
public interface HistoryComponent {
    void inject(TabHistoryFragment fragment);
}
