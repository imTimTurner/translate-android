package com.github.imtimturner.translate.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.imtimturner.translate.R;
import com.github.imtimturner.translate.util.TRAnimationUtil;

/**
 * Класс реализует базовый фрагмент списка приложения
 * */
public abstract class TRListFragment extends BaseFragment {

    private Toolbar supportActionbar;

    private SearchView searchView;
    private ListView listView;
    private ImageView emptyDrawable;
    private TextView emptyText;

    private View emptyLayout;
    private View loadingLayout;
    private View contentLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Инициализирую вручную чтобы потом в дочерних классах не запутаться
        // т.к. при использовании ButterKnife они доступны в дочерних классах
        View root = inflater.inflate(R.layout.fragment_list_tr, container, false);
        supportActionbar = (Toolbar) root.findViewById(R.id.support_actionbar);
        searchView = (SearchView) root.findViewById(R.id.search_view);
        listView = (ListView) root.findViewById(R.id.list_view);
        emptyDrawable = (ImageView) root.findViewById(R.id.empty_drawable);
        emptyText = (TextView) root.findViewById(R.id.empty_text);
        emptyLayout = root.findViewById(R.id.empty_layout);
        loadingLayout = root.findViewById(R.id.loading_layout);
        contentLayout = root.findViewById(R.id.content_layout);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showLoading(loadingLayout.getWindowToken() != null);
    }

    public Toolbar getToolbar(){
        return supportActionbar;
    }

    public ListView getListView() {
        return listView;
    }

    public ListAdapter getListAdapter() {
        return listView.getAdapter();
    }

    public void setListAdapter(ListAdapter listAdapter) {
        listView.setAdapter(listAdapter);
    }

    public SearchView getSearchView() {
        return searchView;
    }

    public void setEmptyText(CharSequence text) {
        emptyText.setText(text);
    }

    public void setEmptyText(@StringRes int textRes) {
        emptyText.setText(getText(textRes));
    }

    public void setEmptyDrawable(Drawable drawable) {
        emptyDrawable.setImageDrawable(drawable);
    }

    public void setEmptyDrawableRes(@DrawableRes int drawableRes) {
        emptyDrawable.setImageResource(drawableRes);
    }

    public void showEmpty(boolean animate) {
        TRAnimationUtil.defaultFadeIn(emptyLayout, animate);
        TRAnimationUtil.defaultFadeOut(contentLayout, animate);
    }

    public void showContent(boolean animate) {
        TRAnimationUtil.defaultFadeOut(emptyLayout, animate);
        TRAnimationUtil.defaultFadeIn(contentLayout, animate);
        TRAnimationUtil.defaultFadeIn(listView, animate);
        TRAnimationUtil.defaultFadeOut(loadingLayout, animate);
    }

    public void showLoading(boolean animate) {
        TRAnimationUtil.defaultFadeOut(emptyLayout, animate);
        TRAnimationUtil.defaultFadeIn(contentLayout, animate);
        TRAnimationUtil.defaultFadeIn(loadingLayout, animate);
        TRAnimationUtil.defaultFadeOut(listView, animate);
    }
}
