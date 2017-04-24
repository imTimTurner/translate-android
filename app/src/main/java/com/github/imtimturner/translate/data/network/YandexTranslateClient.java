package com.github.imtimturner.translate.data.network;

import com.github.imtimturner.translate.data.mapper.LangJsonMapper;
import com.github.imtimturner.translate.data.mapper.TranslationJsonMapper;
import com.github.imtimturner.translate.data.model.Lang;
import com.github.imtimturner.translate.data.model.Translation;
import com.github.imtimturner.translate.data.network.yandex.translate.YandexTranslateAPI;
import com.github.imtimturner.translate.data.network.yandex.translate.json.YandexTranslateJsonGetLangs;
import com.github.imtimturner.translate.data.network.yandex.translate.json.YandexTranslateJsonTranslate;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Класс реалирует клиента Yandex Translate
 * */
@Singleton
public class YandexTranslateClient {

    private static final String BASE_URL = "https://translate.yandex.net";
    private static final String API_KEY = "API_KEY";

    private final YandexTranslateAPI translateAPI;
    private final LangJsonMapper langJsonMapper;
    private final TranslationJsonMapper translationJsonMapper;

    @Inject
    public YandexTranslateClient(OkHttpClient httpClient,
                                 LangJsonMapper langJsonMapper,
                                 TranslationJsonMapper translationJsonMapper) {
        this.translateAPI = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build().create(YandexTranslateAPI.class);
        this.langJsonMapper = langJsonMapper;
        this.translationJsonMapper = translationJsonMapper;
    }

    public Observable<List<Lang>> getLangs(Locale locale) {
        return translateAPI.getLangs(API_KEY, locale.getLanguage())
                .map(new Function<YandexTranslateJsonGetLangs, List<Lang>>() {
                    @Override
                    public List<Lang> apply(YandexTranslateJsonGetLangs json) throws Exception {
                        return langJsonMapper.transform(json);
                    }
                });
    }

    public Observable<Translation> translate(final String originText, final Lang targetLang) {
        return translate(originText, null, targetLang);
    }

    public Observable<Translation> translate(final String originText, final Lang originLang, final Lang targetLang) {
        return translateAPI.translate(API_KEY, originText, originLang != null ?
                originLang.getCode() + "-" + targetLang.getCode() : targetLang.getCode())
                .map(new Function<YandexTranslateJsonTranslate, Translation>() {
                    @Override
                    public Translation apply(YandexTranslateJsonTranslate json) throws Exception {
                        return translationJsonMapper.transform(json);
                    }
                }).doOnNext(new Consumer<Translation>() {
                    @Override
                    public void accept(Translation entity) throws Exception {
                        entity.setOriginText(originText);
                    }
                });
    }

}
