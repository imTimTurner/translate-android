package com.github.imtimturner.translate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.imtimturner.translate.R;
import com.github.imtimturner.translate.data.model.Translation;
import com.github.imtimturner.translate.di.module.FavoritesModule;
import com.github.imtimturner.translate.presenter.FavoritesPresenter;
import com.github.imtimturner.translate.view.FavoritesView;

import javax.inject.Inject;

/**
 * Фрагмент реализующий вкладку Избранное
 * */
public class TabFavoritesFragment extends TranslationListFragment implements FavoritesView {

    @Inject
    FavoritesPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjections();
    }

    private void initializeInjections(){
        getApplicationComponent().getFavoritesComponent(new FavoritesModule(this)).inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Так как основной функционал в родительском классе
        // мы просто устанавливаем нужные значения
        getSearchView().setQueryHint(getString(R.string.search_in_favorites));
        getToolbar().setTitle(R.string.favorites_label);
        setEmptyDrawableRes(R.drawable.favorites_outline_light_48dp);
        setEmptyText(R.string.empty_favorites_text);
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
