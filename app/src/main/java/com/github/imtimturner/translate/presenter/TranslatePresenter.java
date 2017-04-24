package com.github.imtimturner.translate.presenter;

import android.content.Context;
import android.support.v4.util.Pair;

import com.github.imtimturner.translate.data.model.Lang;
import com.github.imtimturner.translate.data.model.Translation;
import com.github.imtimturner.translate.data.repository.LangRepository;
import com.github.imtimturner.translate.data.repository.SettingsRepository;
import com.github.imtimturner.translate.data.repository.TranslationRepository;
import com.github.imtimturner.translate.di.scope.TranslateScope;
import com.github.imtimturner.translate.util.NetworkAvailableObservable;
import com.github.imtimturner.translate.view.TranslateView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;
import retrofit2.HttpException;

/**
 * Реализует Presenter паттерна MVP.
 * Он описывает Presenter экрана "Переввод"
 * */
@TranslateScope
public class TranslatePresenter {

    // Используемы репозитории для доступа к данным
    private final SettingsRepository settingsRepository;
    private final LangRepository langRepository;
    private final TranslationRepository translationRepository;

    // Subject используемые чтобы не зависить от жизненого цикла View
    private final BehaviorSubject<Translation> translation = BehaviorSubject.create();
    private final BehaviorSubject<Lang> originLang;
    private final BehaviorSubject<Lang> targetLang;
    private final BehaviorSubject<String> originText = BehaviorSubject.create();
    private final BehaviorSubject<String> targetText = BehaviorSubject.create();
    private final BehaviorSubject<Boolean> inFavorites = BehaviorSubject.create();
    private final BehaviorSubject<Boolean> error = BehaviorSubject.create();

    private final Context context;
    private final TranslateView view;
    private CompositeDisposable viewDisposable;
    private final CompositeDisposable disposable = new CompositeDisposable();

    // Обработчик перевода
    private final Consumer<Translation> translationConsumer = new Consumer<Translation>() {
        @Override
        public void accept(Translation translation) throws Exception {
            TranslatePresenter.this.translation.onNext(translation);
        }
    };

    // Обработчик ошибок при запросе перевода
    private final Consumer<Throwable> translationErrorConsumer = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            if (throwable instanceof HttpException) {
                // если ошибка сети показываем сообщение об ошибке
                error.onNext(true);

                // если установлен автоперевод то подписываемся на изменение сети
                // чтобы когда сеть появиться сразу загрузить перевод
                if (settingsRepository.isLiveTranslateEnabled()) {
                    translationDisposable.add(NetworkAvailableObservable.create(context)
                            .skipWhile(new Predicate<Boolean>() {
                                @Override
                                public boolean test(Boolean aBoolean) throws Exception {
                                    return !aBoolean;
                                }
                            }).subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) throws Exception {
                                    loadTranslation();
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                }
                            }));
                }
            }
        }
    };
    private CompositeDisposable translationDisposable;

    @Inject
    public TranslatePresenter(SettingsRepository settingsRepository,
                              LangRepository langRepository,
                              TranslationRepository translationRepository,
                              Context context,
                              TranslateView view) {
        this.settingsRepository = settingsRepository;
        this.langRepository = langRepository;
        this.translationRepository = translationRepository;
        this.context = context;
        this.view = view;

        // Получем последнее напровление перевода
        Pair<Lang, Lang> langPair = settingsRepository.getLastTranslationDirection();
        originLang = BehaviorSubject.createDefault(langPair.first);
        targetLang = BehaviorSubject.createDefault(langPair.second);

        // и подписываемся обработку получения перевода
        this.disposable.add(translation.subscribe(new Consumer<Translation>() {
            @Override
            public void accept(Translation translation) throws Exception {
                originLang.onNext(translation.getOriginLang());
                targetText.onNext(translation.getTargetText());
                inFavorites.onNext(translation.isInFavorites());
            }
        }));
    }

    public void onViewCreated() {
        view.setOriginText(originText.getValue());

        // Подписываем view на изменение состояния
        viewDisposable = new CompositeDisposable();
        viewDisposable.add(originLang
                .flatMap(new Function<Lang, ObservableSource<Lang>>() {
                    @Override
                    public ObservableSource<Lang> apply(Lang lang) throws Exception {
                        return langRepository.getLang(lang.getCode());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Lang>() {
                    @Override
                    public void accept(Lang lang) throws Exception {
                        view.setOriginLang(lang);
                    }
                }));
        viewDisposable.add(originText.skip(1)
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (s.length() < 1) {
                            targetText.onNext("");
                        }
                    }
                })
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (s.length() > 0 && settingsRepository.isLiveTranslateEnabled()) {
                            loadTranslation();
                        }
                    }
                }));
        viewDisposable.add(targetLang
                .flatMap(new Function<Lang, ObservableSource<Lang>>() {
                    @Override
                    public ObservableSource<Lang> apply(Lang lang) throws Exception {
                        return langRepository.getLang(lang.getCode());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Lang>() {
                    @Override
                    public void accept(Lang lang) throws Exception {
                        view.setTargetLang(lang);

                        if (settingsRepository.isLiveTranslateEnabled() &&
                                originText.getValue().length() > 0) {
                            loadTranslation();
                        }
                    }
                }));
        viewDisposable.add(targetText
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        view.setTargetText(s);
                        view.showContent(true);
                    }
                }));
        viewDisposable.add(inFavorites
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        view.setInFavorites(aBoolean);
                    }
                }));
        viewDisposable.add(error.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean s) throws Exception {
                        if (s) {
                            view.showError(true);
                        }
                    }
                }));
    }

    public void onDestroyView() {
        // Сохраняем направление перевода и отписываемся от всего
        settingsRepository.saveLastTranslationDirection(Pair.create(originLang.getValue(),
                targetLang.getValue()));
        viewDisposable.dispose();
        if (translationDisposable != null && !translationDisposable.isDisposed()) {
            translationDisposable.dispose();
        }
    }

    public void destroy() {
        disposable.dispose();
    }

    public void onOriginTextChanged(String newText) {
        if (!newText.equals(originText.getValue())) {
            originText.onNext(newText);
        }
    }

    public void onEnterAction() {
        if (originText.getValue().length() > 0) {
            loadTranslation();
        }
    }

    public void onInFavoritesClick() {
        if (inFavorites.getValue()) {
            translationRepository.removeFromFavoritesTranslation(translation.getValue());
        } else {
            translationRepository.addToFavoritesTranslation(translation.getValue());
        }
    }

    public void onOriginLangClick() {
        view.showSelectOriginLangView(originLang.getValue());
    }

    public void onOriginLangSelected(Lang lang) {
        originLang.onNext(lang);
    }

    public void onTargetLangClick() {
        view.showSelectTargetLangView(targetLang.getValue());
    }

    public void onTargetLangSelected(Lang lang) {
        targetLang.onNext(lang);
    }

    public void onSwapButtonClick() {
        Lang buffer = originLang.getValue();
        originLang.onNext(targetLang.getValue());
        targetLang.onNext(buffer);
    }

    private void loadTranslation() {
        // отменяем предыдущую загрузку перевода если есть
        if (translationDisposable != null && !translationDisposable.isDisposed()) {
            translationDisposable.dispose();
        }
        translationDisposable = new CompositeDisposable();

        // отображаем прогрессбар и загружаем
        error.onNext(false);
        view.showLoading(true);
        if (settingsRepository.isAutoDetectLangEnabled()) {
            translationDisposable.add(translationRepository
                    .getTranslation(originText.getValue(), targetLang.getValue())
                    .subscribe(translationConsumer, translationErrorConsumer));
        } else {
            translationDisposable.add(translationRepository
                    .getTranslation(originText.getValue(), originLang.getValue(), targetLang.getValue())
                    .subscribe(translationConsumer, translationErrorConsumer));
        }
    }
}
