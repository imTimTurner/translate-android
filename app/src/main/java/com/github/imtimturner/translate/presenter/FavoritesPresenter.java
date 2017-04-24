package com.github.imtimturner.translate.presenter;

import com.github.imtimturner.translate.data.model.Translation;
import com.github.imtimturner.translate.data.repository.TranslationRepository;
import com.github.imtimturner.translate.di.scope.FavoritesScope;
import com.github.imtimturner.translate.view.FavoritesView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;


/**
 * Реализует Presenter паттерна MVP.
 * Он описывает Presenter экрана "Избранное"
 * */
@FavoritesScope
public class FavoritesPresenter {

    private final BehaviorSubject<List<Translation>> favorites = BehaviorSubject.create();

    private final TranslationRepository translationRepository;
    private final FavoritesView view;
    private CompositeDisposable disposable;


    @Inject
    public FavoritesPresenter(TranslationRepository translationRepository,
                              FavoritesView view) {
        this.translationRepository = translationRepository;
        this.view = view;
    }

    public void onViewCreated() {
        // Подписываем View
        disposable = new CompositeDisposable();
        disposable.add(favorites.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Translation>>() {
                    @Override
                    public void accept(List<Translation> translations) throws Exception {
                        if (translations.size() > 0) {
                            view.showContent(true);
                            view.setTranslations(translations);
                        } else {
                            view.showEmpty(false);
                        }
                    }
                }));
        disposable.add(translationRepository.getTranslationFavorites()
                .subscribe(new Consumer<List<Translation>>() {
                    @Override
                    public void accept(List<Translation> translations) throws Exception {
                        favorites.onNext(translations);
                    }
                }));
    }

    public void onDestroyView() {
        disposable.dispose();
    }

    public void onFavoritesClick(Translation translation) {
        translationRepository.removeFromFavoritesTranslation(translation);
    }
}
