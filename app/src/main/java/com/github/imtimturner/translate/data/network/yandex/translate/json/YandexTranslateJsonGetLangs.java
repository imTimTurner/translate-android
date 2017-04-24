package com.github.imtimturner.translate.data.network.yandex.translate.json;

import java.util.Map;

public class YandexTranslateJsonGetLangs extends YandexTranslaJsonBase {

    private Map<String, String> langs;

    public YandexTranslateJsonGetLangs() { }

    public Map<String, String> getLangs() {
        return langs;
    }

    public void setLangs(Map<String, String> langs) {
        this.langs = langs;
    }

}
