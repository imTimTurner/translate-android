package com.github.imtimturner.translate.view;

import com.github.imtimturner.translate.data.model.Translation;

import java.util.List;

/**
 * Интерфейс определяет View паттерна MVP.
 * Этот интерфейс описывает Экран "История"
 * */
public interface HistoryView {

    void showLoading(boolean animated);

    void showContent(boolean animated);

    void showEmpty(boolean animated);

    void setTranslations(List<Translation> translations);

}
