package com.github.imtimturner.translate.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.Pair;
import android.support.v7.preference.PreferenceManager;

import com.github.imtimturner.translate.data.model.Lang;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Класс реалирует Repository для доступа к настройкам приложения
 * */
@Singleton
public class SettingsRepository {

    private static final String LAST_ORIGIN_LANG_KEY = "last_origin_lang";
    private static final String LAST_TARGET_LANG_KEY = "last_target_lang";
    private static final String LIVE_TRANSLATION_KEY = "live_translation";
    private static final String AUTO_DETECT_LANG_KEY = "auto_detect_lang";

    private static final String defaultOriginLang = "en";
    private static final String defaultTargetLang = "ru";

    private final SharedPreferences preferences;

    @Inject
    public SettingsRepository(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Pair<Lang, Lang> getLastTranslationDirection() {
        Lang originLang = new Lang(preferences.getString(LAST_ORIGIN_LANG_KEY, defaultOriginLang));
        Lang targetLang = new Lang(preferences.getString(LAST_TARGET_LANG_KEY, defaultTargetLang));
        return Pair.create(originLang, targetLang);
    }

    public void saveLastTranslationDirection(Pair<Lang, Lang> langPair) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LAST_ORIGIN_LANG_KEY, langPair.first.getCode());
        editor.putString(LAST_TARGET_LANG_KEY, langPair.second.getCode());
        editor.apply();
    }

    public boolean isLiveTranslateEnabled() {
        return preferences.getBoolean(LIVE_TRANSLATION_KEY, true);
    }

    public boolean isAutoDetectLangEnabled(){
        return preferences.getBoolean(AUTO_DETECT_LANG_KEY, true);
    }

}
