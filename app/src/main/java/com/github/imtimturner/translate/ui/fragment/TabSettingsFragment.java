package com.github.imtimturner.translate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.imtimturner.translate.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Фрагмент реализующий вкладку Настройки
 */
public class TabSettingsFragment extends PreferenceFragmentCompat {

    @BindView(R.id.support_actionbar)
    Toolbar supportActionBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.app_preferences);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Делаем не мудреный трюк чтобы добавить к стандартному фрагменту из
        // библиотеки поддержки toolbar

        View contentView = super.onCreateView(inflater, container, savedInstanceState);
        View fragmentView = inflater.inflate(R.layout.fragment_settings_tab, container, false);

        FrameLayout contentFrame = (FrameLayout) fragmentView.findViewById(R.id.content);
        contentFrame.addView(contentView);

        return fragmentView;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        supportActionBar.setTitle(R.string.settings_label);
    }

}
