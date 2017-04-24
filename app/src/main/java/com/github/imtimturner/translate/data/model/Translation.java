package com.github.imtimturner.translate.data.model;

/**
 * Описывает Перевод
 * */
public class Translation {

    private Lang originLang;
    private String originText;
    private Lang targetLang;
    private String targetText;
    private boolean inFavorites;

    // вспомогательное поле для сохранения истории
    private long created;

    public Translation() { }

    public Lang getOriginLang() {
        return originLang;
    }

    public void setOriginLang(Lang originLang) {
        this.originLang = originLang;
    }

    public String getOriginText() {
        return originText;
    }

    public void setOriginText(String originText) {
        this.originText = originText;
    }

    public Lang getTargetLang() {
        return targetLang;
    }

    public void setTargetLang(Lang targetLang) {
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

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    @Override
    public int hashCode() {
        int result = 42;
        result = 37 * result + (originLang == null ? 0 : originLang.hashCode());
        result = 37 * result + (originText == null ? 0 : originText.hashCode());
        result = 37 * result + (targetLang == null ? 0 : targetLang.hashCode());
        result = 37 * result + (targetText == null ? 0 : targetText.hashCode());
        result = 37 * result + (inFavorites ? 1 : 0 );
        return result;
    }

    @Override
    public String toString() {
        return originText + " -> " + targetText;
    }
}
