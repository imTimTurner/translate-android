package com.github.imtimturner.translate.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.imtimturner.translate.R;

/**
 * Активити выбора языка
 * */
public class SelectLangActivity extends AppCompatActivity {

    public static final String EXTRA_CHECKED_LANG_CODE = "checkedLang";
    public static final String EXTRA_SELECTED_LANG_CODE = "selectedLang";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lang_select);
    }

}
