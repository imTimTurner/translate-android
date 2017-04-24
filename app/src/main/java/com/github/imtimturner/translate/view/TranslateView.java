package com.github.imtimturner.translate.view;

import com.github.imtimturner.translate.data.model.Lang;

/**
 * Интерфейс определяет View паттерна MVP.
 * Этот интерфейс описывает Экран "Перевода"
 * */
public interface TranslateView {

    void setOriginLang(Lang originLang);

    void setTargetLang(Lang targetLang);

    void setOriginText(String originText);

    void setTargetText(String targetText);

    void setInFavorites(boolean inFavorites);

    void showLoading(boolean animate);

    void showContent(boolean animate);

    void showError(boolean animate);

    void showSelectOriginLangView(Lang checked);

    void showSelectTargetLangView(Lang checked);
}
