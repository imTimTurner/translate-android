package com.github.imtimturner.translate.data.mapper;

import com.github.imtimturner.translate.data.model.Lang;
import com.github.imtimturner.translate.data.network.yandex.translate.json.YandexTranslateJsonGetLangs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Класс преобразование из JSON -> Lang
 * */
@Singleton
public class LangJsonMapper {

    @Inject
    public LangJsonMapper() {
    }

    public List<Lang> transform(YandexTranslateJsonGetLangs json){
        final List<Lang> result = new ArrayList<>();
        if (json != null){
            Map<String, String> langs = json.getLangs();
            if (langs != null){
                for (String code : langs.keySet()){
                    result.add(new Lang(code, langs.get(code)));
                }
            }
        }
        return result;
    }

}
