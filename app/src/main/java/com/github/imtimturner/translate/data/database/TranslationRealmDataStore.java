package com.github.imtimturner.translate.data.database;

import com.github.imtimturner.translate.data.mapper.TranslationRealmMapper;
import com.github.imtimturner.translate.data.model.Lang;
import com.github.imtimturner.translate.data.model.Translation;
import com.github.imtimturner.translate.data.model.realm.TranslationRealm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Реализовывает класс доступа в БД Realm
 * */
@Singleton
public class TranslationRealmDataStore {

    private final TranslationRealmMapper translationRealmMapper;

    @Inject
    public TranslationRealmDataStore(TranslationRealmMapper translationRealmMapper) {
        this.translationRealmMapper = translationRealmMapper;
    }

    public Observable<Translation> getTranslation(final String originText, final Lang targetLang) {
        return Observable.create(new ObservableOnSubscribe<Translation>() {
            @Override
            public void subscribe(final ObservableEmitter<Translation> e) throws Exception {
                Realm database = Realm.getDefaultInstance();

                // Запрашиваем объект
                final TranslationRealm entity = database.where(TranslationRealm.class)
                        .equalTo("originText", originText)
                        .equalTo("targetLang", targetLang.getCode())
                        .findFirst();
                if (entity != null) {
                    // если объект получили то подписываемся на его обновления
                    database.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            entity.setCreated(System.currentTimeMillis());
                        }
                    });
                    final RealmChangeListener<TranslationRealm> listener = new RealmChangeListener<TranslationRealm>() {
                        @Override
                        public void onChange(TranslationRealm element) {
                            e.onNext(translationRealmMapper.transform(element));
                        }
                    };
                    entity.addChangeListener(listener);
                    e.setDisposable(new Disposable() {
                        boolean disposed;

                        @Override
                        public void dispose() {
                            disposed = true;
                            entity.removeChangeListener(listener);
                        }

                        @Override
                        public boolean isDisposed() {
                            return disposed;
                        }
                    });
                    e.onNext(translationRealmMapper.transform(entity));
                } else {
                    e.onComplete();
                }
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Translation> getTranslation(final String originText, final Lang originLang, final Lang targetLang) {
        return Observable.create(new ObservableOnSubscribe<Translation>() {
            @Override
            public void subscribe(final ObservableEmitter<Translation> e) throws Exception {
                Realm database = Realm.getDefaultInstance();
                // Запрашиваем объект
                final TranslationRealm entity = database.where(TranslationRealm.class)
                        .equalTo("originText", originText)
                        .equalTo("originLang", originLang.getCode())
                        .equalTo("targetLang", targetLang.getCode())
                        .findFirst();
                if (entity != null) {
                    // если объект получили то подписываемся на его обновления
                    database.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            entity.setCreated(System.currentTimeMillis());
                        }
                    });

                    final RealmChangeListener<TranslationRealm> listener = new RealmChangeListener<TranslationRealm>() {
                        @Override
                        public void onChange(TranslationRealm element) {
                            e.onNext(translationRealmMapper.transform(element));
                        }
                    };
                    entity.addChangeListener(listener);
                    e.setDisposable(new Disposable() {
                        boolean disposed;

                        @Override
                        public void dispose() {
                            disposed = true;
                            entity.removeChangeListener(listener);
                        }

                        @Override
                        public boolean isDisposed() {
                            return disposed;
                        }
                    });
                    e.onNext(translationRealmMapper.transform(entity));
                } else {
                    e.onComplete();
                }
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Translation>> getTranslationHistory() {
        return Observable.create(new ObservableOnSubscribe<List<Translation>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Translation>> e) throws Exception {
                Realm database = Realm.getDefaultInstance();
                // Запрашиваем объекты
                final RealmChangeListener<RealmResults<TranslationRealm>> listener =
                        new RealmChangeListener<RealmResults<TranslationRealm>>() {
                            @Override
                            public void onChange(RealmResults<TranslationRealm> element) {
                                final List<Translation> translations = new ArrayList<Translation>();
                                for (TranslationRealm entity : element) {
                                    translations.add(translationRealmMapper.transform(entity));
                                }
                                Collections.reverse(translations);
                                e.onNext(translations);
                            }
                        };
                final RealmResults<TranslationRealm> results = database.where(TranslationRealm.class)
                        .findAll().sort("created");
                e.setDisposable(new Disposable() {

                    boolean disposed;

                    @Override
                    public void dispose() {
                        disposed = true;
                        results.removeChangeListener(listener);
                    }

                    @Override
                    public boolean isDisposed() {
                        return disposed;
                    }
                });
                // если объект получили то подписываемся на его обновления
                listener.onChange(results);
                results.addChangeListener(listener);
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Translation>> getTranslationFavorites() {
        return Observable.create(new ObservableOnSubscribe<List<Translation>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Translation>> e) throws Exception {
                Realm database = Realm.getDefaultInstance();
                // Запрашиваем объекты
                final RealmChangeListener<RealmResults<TranslationRealm>> listener =
                        new RealmChangeListener<RealmResults<TranslationRealm>>() {
                            @Override
                            public void onChange(RealmResults<TranslationRealm> element) {
                                final List<Translation> translations = new ArrayList<Translation>();
                                for (TranslationRealm entity : element) {
                                    translations.add(translationRealmMapper.transform(entity));
                                }
                                Collections.reverse(translations);
                                e.onNext(translations);
                            }
                        };
                // если объект получили то подписываемся на его обновления
                final RealmResults<TranslationRealm> results = database.where(TranslationRealm.class)
                        .equalTo("inFavorites", true)
                        .findAll().sort("created");
                e.setDisposable(new Disposable() {

                    boolean disposed;

                    @Override
                    public void dispose() {
                        disposed = true;
                        results.removeChangeListener(listener);
                    }

                    @Override
                    public boolean isDisposed() {
                        return disposed;
                    }
                });
                listener.onChange(results);
                results.addChangeListener(listener);
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Translation> createOrUpdateTranslation(final Translation translation) {
        return Observable.create(new ObservableOnSubscribe<Translation>() {
            @Override
            public void subscribe(final ObservableEmitter<Translation> e) throws Exception {
                try {
                    Realm database = Realm.getDefaultInstance();
                    // Создаем или обновляем объект
                    database.beginTransaction();
                    final TranslationRealm entity = database.copyToRealmOrUpdate(
                            translationRealmMapper.transform(translation));
                    database.commitTransaction();

                    final RealmChangeListener<TranslationRealm> listener = new RealmChangeListener<TranslationRealm>() {
                        @Override
                        public void onChange(TranslationRealm element) {
                            e.onNext(translationRealmMapper.transform(element));
                        }
                    };
                    // подписываемся на его обновления
                    entity.addChangeListener(listener);
                    e.setDisposable(new Disposable() {
                        boolean disposed;

                        @Override
                        public void dispose() {
                            disposed = true;
                            entity.removeChangeListener(listener);
                        }

                        @Override
                        public boolean isDisposed() {
                            return disposed;
                        }
                    });
                    e.onNext(translationRealmMapper.transform(entity));
                } catch (Exception error){
                    e.onComplete();
                }
            }

        }).subscribeOn(AndroidSchedulers.mainThread());
    }
}
