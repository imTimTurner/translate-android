package com.github.imtimturner.translate.data.mapper;

import com.github.imtimturner.translate.data.model.Lang;
import com.github.imtimturner.translate.data.model.Translation;
import com.github.imtimturner.translate.data.network.yandex.translate.json.YandexTranslateJsonTranslate;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Класс преобразование из JSON -> Translation
 * */
@Singleton
public class TranslationJsonMapper {

    private static final String LANG_PAIR_SEPARATOR = "-";

    @Inject
    public TranslationJsonMapper() {
    }

    public Translation transform(YandexTranslateJsonTranslate json) {
        if (json == null) {
            return null;
        }
        Translation entity = new Translation();

        String lang = json.getLang();
        if (lang != null && lang.length() > 0) {
            if (lang.contains(LANG_PAIR_SEPARATOR)) {
                String strItems[] = lang.split(LANG_PAIR_SEPARATOR);
                entity.setOriginLang(strItems[0].length() > 0 ? new Lang(strItems[0]) : null);
                entity.setTargetLang(strItems[1].length() > 0 ? new Lang(strItems[1]) : null);
            } else {
                entity.setTargetLang(new Lang(lang));
            }
        }

        List<String> text = json.getText();
        if (text != null && text.size() > 0) {
            entity.setTargetText(json.getText().get(0));
        }

        return entity;
    }

}
