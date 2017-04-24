package com.github.imtimturner.translate.data.model.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * Описывает объект Перевода в БД Realm
 * */
public class TranslationRealm extends RealmObject {

    @PrimaryKey
    private int id;
    private long created = 0;

    private String originLang;
    private String originText;
    private String targetLang;
    private String targetText;
    private boolean inFavorites;

    public TranslationRealm() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long index) {
        this.created = index;
    }

    public String getOriginLang() {
        return originLang;
    }

    public void setOriginLang(String originLang) {
        this.originLang = originLang;
    }

    public String getOriginText() {
        return originText;
    }

    public void setOriginText(String originText) {
        this.originText = originText;
    }

    public String getTargetLang() {
        return targetLang;
    }

    public void setTargetLang(String targetLang) {
        this.targetLang = targetLang;
    }

    public String getTargetText() {
        return targetText;
    }

    public void setTargetText(String targetText) {
        this.targetText = targetText;
    }

    public boolean isInFavorites() {
        return inFavorites;
    }

    public void setInFavorites(boolean inFavorites) {
        this.inFavorites = inFavorites;
    }

}
