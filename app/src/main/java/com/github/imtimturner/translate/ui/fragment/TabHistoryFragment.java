package com.github.imtimturner.translate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.github.imtimturner.translate.R;
import com.github.imtimturner.translate.data.model.Translation;
import com.github.imtimturner.translate.di.module.HistoryModule;
import com.github.imtimturner.translate.presenter.HistoryPresenter;
import com.github.imtimturner.translate.view.HistoryView;

import javax.inject.Inject;

/**
 * Фрагмент реализующий вкладку История
 * */
public class TabHistoryFragment extends TranslationListFragment implements HistoryView {

    @Inject
    HistoryPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjections();
    }

    private void initializeInjections() {
        getApplicationComponent().getHistoryComponent(new HistoryModule(this)).inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Таже история. Так как основной функционал в родительском классе
        // мы просто устанавливаем нужные значения
        getToolbar().setTitle(R.string.history_label);
        getSearchView().setQueryHint(getString(R.string.search_in_history));
        setEmptyDrawableRes(R.drawable.history_light_48dp);
        setEmptyText(R.string.empty_history_text);
        presenter.onViewCreated();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

    @Override
    public void onFavoritesClick(Translation translation) {
        presenter.onFavoritesClick(translation);
    }

}
