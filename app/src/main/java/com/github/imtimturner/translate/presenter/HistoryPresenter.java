package com.github.imtimturner.translate.presenter;

import com.github.imtimturner.translate.data.model.Translation;
import com.github.imtimturner.translate.data.repository.TranslationRepository;
import com.github.imtimturner.translate.di.scope.HistoryScope;
import com.github.imtimturner.translate.view.HistoryView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Реализует Presenter паттерна MVP.
 * Он описывает Presenter экрана "Истории"
 * */
@HistoryScope
public class HistoryPresenter {

    private final BehaviorSubject<List<Translation>> history = BehaviorSubject.create();

    private final TranslationRepository translationRepository;
    private final HistoryView view;
    private CompositeDisposable disposable;


    @Inject
    public HistoryPresenter(TranslationRepository translationRepository,
                            HistoryView view) {
        this.translationRepository = translationRepository;
        this.view = view;
    }

    public void onViewCreated() {
        // Подписываем View
        disposable = new CompositeDisposable();
        disposable.add(history.observeOn(AndroidSchedulers.mainThread())
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
        disposable.add(translationRepository.getTranslationHistory()
                .subscribe(new Consumer<List<Translation>>() {
                    @Override
                    public void accept(List<Translation> translations) throws Exception {
                        history.onNext(translations);
                    }
                }));
    }

    public void onDestroyView() {
        disposable.dispose();
    }

    public void onFavoritesClick(Translation translation) {
        if (translation.isInFavorites()) {
            translationRepository.removeFromFavoritesTranslation(translation);
        } else {
            translationRepository.addToFavoritesTranslation(translation);
        }
    }
}
