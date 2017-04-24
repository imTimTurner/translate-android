package com.github.imtimturner.translate.data.repository;

import com.github.imtimturner.translate.data.database.TranslationRealmDataStore;
import com.github.imtimturner.translate.data.model.Lang;
import com.github.imtimturner.translate.data.model.Translation;
import com.github.imtimturner.translate.data.network.YandexTranslateClient;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * Класс реалирует Repository для доступа к переводам
 * */
@Singleton
public class TranslationRepository {

    private final YandexTranslateClient client;
    private final TranslationRealmDataStore translationRealmDataStore;

    @Inject
    public TranslationRepository(YandexTranslateClient client,
                                 TranslationRealmDataStore translationRealmDataStore) {
        this.client = client;
        this.translationRealmDataStore = translationRealmDataStore;
    }

    public Observable<Translation> getTranslation(String originText, Lang targetLang) {
        // Загружаем сначала из бд и если там ничего нет то загружаем из сети
        return translationRealmDataStore.getTranslation(originText, targetLang)
                .switchIfEmpty(client.translate(originText, targetLang).observeOn(AndroidSchedulers.mainThread())
                        .flatMap(new Function<Translation, ObservableSource<Translation>>() {
                            @Override
                            public ObservableSource<Translation> apply(Translation translation) throws Exception {
                                // при загрузке из сети сохраняем результат в бд
                                translation.setCreated(System.currentTimeMillis());
                                return translationRealmDataStore.createOrUpdateTranslation(translation);
                            }
                        }));
    }

    public Observable<Translation> getTranslation(String originText, Lang originLang, Lang targetLang) {
        // Загружаем сначала из бд и если там ничего нет то загружаем из сети
        return translationRealmDataStore.getTranslation(originText, originLang, targetLang)
                .switchIfEmpty(client.translate(originText, originLang, targetLang).observeOn(AndroidSchedulers.mainThread())
                        .flatMap(new Function<Translation, ObservableSource<Translation>>() {
                            @Override
                            public ObservableSource<Translation> apply(Translation translation) throws Exception {
                                // при загрузке из сети сохраняем результат в бд
                                translation.setCreated(System.currentTimeMillis());
                                return translationRealmDataStore.createOrUpdateTranslation(translation);
                            }
                        }));
    }

    public Observable<List<Translation>> getTranslationFavorites() {
        return translationRealmDataStore.getTranslationFavorites();
    }

    public Observable<List<Translation>> getTranslationHistory() {
        return translationRealmDataStore.getTranslationHistory();
    }

    public void addToFavoritesTranslation(Translation translation) {
        translation.setInFavorites(true);
        translationRealmDataStore.createOrUpdateTranslation(translation).subscribe();
    }

    public void removeFromFavoritesTranslation(Translation translation) {
        translation.setInFavorites(false);
        translationRealmDataStore.createOrUpdateTranslation(translation).subscribe();
    }

}
