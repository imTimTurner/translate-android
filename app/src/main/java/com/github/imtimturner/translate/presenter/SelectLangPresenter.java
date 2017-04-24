package com.github.imtimturner.translate.presenter;

import android.content.Intent;

import com.github.imtimturner.translate.data.model.Lang;
import com.github.imtimturner.translate.data.repository.LangRepository;
import com.github.imtimturner.translate.di.scope.SelectLangScope;
import com.github.imtimturner.translate.ui.activity.SelectLangActivity;
import com.github.imtimturner.translate.view.SelectLangView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Реализует Presenter паттерна MVP.
 * Он описывает Presenter экрана "Выбора языка"
 * */
@SelectLangScope
public class SelectLangPresenter {

    private final BehaviorSubject<List<Lang>> langs = BehaviorSubject.create();
    private Lang checked;

    private final SelectLangView view;
    private CompositeDisposable viewDisposables;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public SelectLangPresenter(LangRepository langRepository,
                               SelectLangView view) {
        this.view = view;

        // Получаем из Intent язык который нужно отметить
        Intent intent = view.getIntent();
        if (intent != null && intent.hasExtra(SelectLangActivity.EXTRA_CHECKED_LANG_CODE)) {
            this.checked = new Lang(intent
                    .getStringExtra(SelectLangActivity.EXTRA_CHECKED_LANG_CODE));
        }

        // загружаем языки
        disposables.add(langRepository.getLangs()
                .subscribe(new Consumer<List<Lang>>() {
                    @Override
                    public void accept(List<Lang> langs) throws Exception {
                        SelectLangPresenter.this.langs.onNext(langs);
                    }
                }));
    }

    public void onViewCreated() {
        view.showLoading(false);

        // Подписываем View
        viewDisposables = new CompositeDisposable();
        viewDisposables.add(langs.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Lang>>() {
                    @Override
                    public void accept(List<Lang> langModels) throws Exception {
                        if (langModels.size() > 0) {
                            view.showContent(true);
                        } else {
                            view.showEmpty(true);
                        }

                        // Сортируем языки в алфавитном порядке
                        Collections.sort(langModels, new Comparator<Lang>() {
                            @Override
                            public int compare(Lang o1, Lang o2) {
                                return o1.getName().compareTo(o2.getName());
                            }
                        });
                        view.setLangs(langModels);
                        view.setChecked(checked);
                    }
                }));

    }

    public void onDestroyView() {
        viewDisposables.dispose();
    }

    public void onDestroy() {
        disposables.dispose();
    }

    public void onListItemClick(Lang selected) {
        view.finish(selected);
    }

    public void onSearchQueryChanged(String newQuery) {
        view.setSearchQuery(newQuery);
    }

}
