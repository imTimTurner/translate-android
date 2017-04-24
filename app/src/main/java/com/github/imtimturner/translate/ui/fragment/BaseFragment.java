package com.github.imtimturner.translate.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.imtimturner.translate.AndroidApplication;
import com.github.imtimturner.translate.di.component.ApplicationComponent;

import butterknife.ButterKnife;

/**
 * Базовый фрагмент содержит общие методы для фрагментов
 * */
public abstract class BaseFragment extends Fragment {

    public BaseFragment(){
        setRetainInstance(true);
    }

    public AndroidApplication getAndroidApplication(){
        return AndroidApplication.get(getContext());
    }

    public ApplicationComponent getApplicationComponent(){
        return getAndroidApplication().getComponent();
    }

    public Intent getIntent(){
        return getActivity() != null ? getActivity().getIntent() : null;
    }

    public AppCompatActivity getActivityCompat(){
        return  getActivity() instanceof AppCompatActivity ?
                (AppCompatActivity) getActivity() : null;
    }

    public ActionBar getSupportActionBar(){
        AppCompatActivity activity = getActivityCompat();
        return activity != null ? activity.getSupportActionBar() : null;
    }

    public void setSupportActionBar(Toolbar toolbar){
        AppCompatActivity activity = getActivityCompat();
        if (activity != null){
            activity.setSupportActionBar(toolbar);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Вызываем ButterKnife здесь благодаря чему
        // во всех дочерних фрагментах нам этого делать будет уже не нужно
        ButterKnife.bind(this, view);
    }

    public void setTitle(@StringRes int titleRes){
        Activity activity = getActivity();
        if (activity != null){
            activity.setTitle(titleRes);
        }
    }

}
