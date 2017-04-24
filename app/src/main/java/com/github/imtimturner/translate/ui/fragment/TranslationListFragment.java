package com.github.imtimturner.translate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.github.imtimturner.translate.data.model.Translation;
import com.github.imtimturner.translate.ui.adapter.TranslationListAdapter;

import java.util.List;

/**
 * Базовый фрагмент реализующий список переводов
 * */
public abstract class TranslationListFragment extends TRListFragment
        implements TranslationListAdapter.OnFavoritesClickListener {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Подписываемся на изменение поля поиска
        getSearchView().setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                TranslationListAdapter adapter = getListAdapter();
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getSearchView().setOnQueryTextListener(null);
    }

    @Override
    public TranslationListAdapter getListAdapter() {
        return (TranslationListAdapter) super.getListAdapter();
    }

    public void setTranslations(List<Translation> translations) {
        TranslationListAdapter current = getListAdapter();
        // Если адаптер есть то устанавливаем в него значения иначе инициалиируем его
        if (current != null) {
            current.setItems(translations);
        } else {
            TranslationListAdapter adapter = new TranslationListAdapter(translations);
            adapter.setOnFavoritesClickListener(this);
            adapter.getFilter().filter(getSearchView().getQuery());
            setListAdapter(adapter);
        }
    }

    @Override
    public void onFavoritesClick(Translation translation) {
    }

}