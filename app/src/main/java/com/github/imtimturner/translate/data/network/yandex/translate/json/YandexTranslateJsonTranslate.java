package com.github.imtimturner.translate.data.network.yandex.translate.json;

import java.util.List;

public class YandexTranslateJsonTranslate extends YandexTranslaJsonBase {

    private String lang;
    private List<String> text;

    public YandexTranslateJsonTranslate() { }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

}
