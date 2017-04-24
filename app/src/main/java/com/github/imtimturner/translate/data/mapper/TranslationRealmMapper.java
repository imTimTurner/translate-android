package com.github.imtimturner.translate.data.mapper;

import com.github.imtimturner.translate.data.model.Lang;
import com.github.imtimturner.translate.data.model.Translation;
import com.github.imtimturner.translate.data.model.realm.TranslationRealm;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Класс преобразование из TranslationRealm -> Translation и обратно
 * */
@Singleton
public class TranslationRealmMapper {

    @Inject
    public TranslationRealmMapper() {
    }

    public Translation transform(TranslationRealm entity) {
        Translation translation = new Translation();
        if (entity != null) {
            translation.setOriginLang(new Lang(entity.getOriginLang()));
            translation.setTargetLang(new Lang(entity.getTargetLang()));
            translation.setOriginText(entity.getOriginText());
            translation.setTargetText(entity.getTargetText());
            translation.setInFavorites(entity.isInFavorites());
            translation.setCreated(entity.getCreated());
        }
        return translation;
    }

    public TranslationRealm transform(Translation translation) {
        TranslationRealm entity = new TranslationRealm();
        if (translation != null) {
            entity.setId(generateTranslationId(translation));
            entity.setOriginLang(translation.getOriginLang().getCode());
            entity.setTargetLang(translation.getTargetLang().getCode());
            entity.setOriginText(translation.getOriginText());
            entity.setTargetText(translation.getTargetText());
            entity.setInFavorites(translation.isInFavorites());
            entity.setCreated(translation.getCreated());
        }
        return entity;
    }

    public int generateTranslationId(Translation translation){
        int result = 42;
        result = 37 * result + (translation.getOriginLang() == null ? 0 : translation.getOriginLang().hashCode());
        result = 37 * result + (translation.getTargetLang() == null ? 0 : translation.getTargetLang().hashCode());
        result = 37 * result + (translation.getOriginText() == null ? 0 : translation.getOriginText().hashCode());
        return result;
    }
}
