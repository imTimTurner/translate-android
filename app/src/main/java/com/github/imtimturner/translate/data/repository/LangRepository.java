package com.github.imtimturner.translate.data.repository;

import android.content.Context;

import com.github.imtimturner.translate.data.model.Lang;
import com.github.imtimturner.translate.data.network.YandexTranslateClient;
import com.github.imtimturner.translate.util.DefaultObserver;
import com.github.imtimturner.translate.util.LocaleObservable;
import com.github.imtimturner.translate.util.NetworkAvailableObservable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Класс реалирует Repository для доступа к языкам
 * */
@Singleton
public class LangRepository {

    private final YandexTranslateClient client;

    /**
     * InMemmory хранилище языков
     * т.к. языков не очень много мы можем их хранить в памяти для более быстрого доступа к ним.
     * также это позволяем нам запрашивать язык по его коду
     * */
    private final BehaviorSubject<Map<String, Lang>> inMemoryStore = BehaviorSubject.create();

    private Locale currentLocale;

    @Inject
    public LangRepository(final Context context,
                          final YandexTranslateClient client) {
        this.client = client;

        // Подписываемся на изменение Locale для автоматичской перезагрузки языков
        LocaleObservable.create(context).doOnNext(new Consumer<Locale>() {
            @Override
            public void accept(Locale locale) throws Exception {
                currentLocale = locale;
            }
        }).flatMap(new Function<Locale, ObservableSource<List<Lang>>>() {
            @Override
            public ObservableSource<List<Lang>> apply(Locale locale) throws Exception {
                return LangRepository.this.client.getLangs(locale);
            }
        }).subscribe(new DefaultObserver<List<Lang>>() {
            @Override
            public void onNext(List<Lang> langs) {
                // Сохраняем результат в память для более быстрого доступа к языкам
                Map<String, Lang> cache = new HashMap<>();
                for (Lang lang : langs) {
                    cache.put(lang.getCode(), lang);
                }
                inMemoryStore.onNext(cache);
            }

            @Override
            public void onError(Throwable e) {
                // В случае если загрузить не удалось подписываемся на появление сети
                // и повторяем попытку и т.д.
                NetworkAvailableObservable.create(context).skipWhile(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean aBoolean) throws Exception {
                        return !aBoolean;
                    }
                }).take(1).flatMap(new Function<Boolean, ObservableSource<List<Lang>>>() {
                    @Override
                    public ObservableSource<List<Lang>> apply(Boolean aBoolean) throws Exception {
                        return client.getLangs(currentLocale);
                    }
                }).subscribe();
            }
        });
    }

    public Observable<Lang> getLang(final String code) {
        return inMemoryStore.filter(new Predicate<Map<String, Lang>>() {
            @Override
            public boolean test(Map<String, Lang> cache) throws Exception {
                return cache.containsKey(code);
            }
        }).map(new Function<Map<String, Lang>, Lang>() {
            @Override
            public Lang apply(Map<String, Lang> cache) throws Exception {
                return cache.get(code);
            }
        });
    }

    public Observable<List<Lang>> getLangs() {
        return inMemoryStore.filter(new Predicate<Map<String, Lang>>() {
            @Override
            public boolean test(Map<String, Lang> cache) throws Exception {
                return !cache.isEmpty();
            }
        }).map(new Function<Map<String, Lang>, List<Lang>>() {
            @Override
            public List<Lang> apply(Map<String, Lang> cache) throws Exception {
                return new ArrayList<>(cache.values());
            }
        });
    }

}
