package com.github.imtimturner.translate.view;

import android.content.Intent;

import com.github.imtimturner.translate.data.model.Lang;

import java.util.List;

/**
 * Интерфейс определяет View паттерна MVP.
 * Этот интерфейс описывает Экран "Выбора языка"
 * */
public interface SelectLangView {

    Intent getIntent();

    void showLoading(boolean animated);

    void showContent(boolean animated);

    void showEmpty(boolean animated);

    void setLangs(List<Lang> langs);

    void setChecked(Lang lang);

    void setSearchQuery(String query);

    void finish(Lang lang);

}
