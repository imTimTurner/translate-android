package com.github.imtimturner.translate.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;

import com.github.imtimturner.translate.R;
import com.github.imtimturner.translate.data.model.Lang;
import com.github.imtimturner.translate.di.module.SelectLangModule;
import com.github.imtimturner.translate.presenter.SelectLangPresenter;
import com.github.imtimturner.translate.ui.activity.SelectLangActivity;
import com.github.imtimturner.translate.ui.adapter.SelectLangListAdapter;
import com.github.imtimturner.translate.view.SelectLangView;

import java.util.List;

import javax.inject.Inject;

import butterknife.OnItemClick;

public class SelectLangFragment extends TRListFragment implements SelectLangView {

    @Inject
    SelectLangPresenter presenter;

    public SelectLangFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjections();
    }

    private void initializeInjections() {
        getApplicationComponent().getSelectLangComponent(new SelectLangModule(this)).inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSupportActionBar(getToolbar());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.select_lang_label));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getSearchView().setQueryHint(getString(R.string.search_language));
        getSearchView().setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.onSearchQueryChanged(newText);
                return true;
            }
        });
        setEmptyText(getString(R.string.empty_langs_text));

        presenter.onViewCreated();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Activity activity = getActivity();
            if (activity != null) {
                activity.finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getSearchView().setOnQueryTextListener(null);
        presenter.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public SelectLangListAdapter getListAdapter() {
        return (SelectLangListAdapter) super.getListAdapter();
    }

    @Override
    public void setLangs(List<Lang> langs) {
        setListAdapter(new SelectLangListAdapter(getContext(), langs));
    }

    @OnItemClick(R.id.list_view)
    public void onListItemClick(int position) {
        SelectLangListAdapter adapter = getListAdapter();
        if (adapter != null) {
            presenter.onListItemClick(adapter.getItem(position));
        }
    }

    @Override
    public void setChecked(Lang lang) {
        SelectLangListAdapter adapter = getListAdapter();
        if (adapter != null) {
            adapter.setChecked(lang);
            int position = adapter.getPosition(lang);
            if (position > 0) {
                if (getListView().getFirstVisiblePosition() > position ||
                        getListView().getLastVisiblePosition() < position) {
                    getListView().setSelection(position);
                }
            }
        }
    }

    @Override
    public void setSearchQuery(String query) {
        SelectLangListAdapter adapter = getListAdapter();
        if (adapter != null) {
            adapter.getFilter().filter(query);
        }
    }

    @Override
    public void finish(Lang lang) {
        Activity activity = getActivity();

        Intent data = activity.getIntent();
        data.putExtra(SelectLangActivity.EXTRA_SELECTED_LANG_CODE, lang.getCode());

        activity.setResult(Activity.RESULT_OK, data);
        activity.finish();
    }
}
