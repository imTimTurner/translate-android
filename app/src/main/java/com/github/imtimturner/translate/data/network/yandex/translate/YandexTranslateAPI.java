package com.github.imtimturner.translate.data.network.yandex.translate;

import com.github.imtimturner.translate.data.network.yandex.translate.json.YandexTranslateJsonGetLangs;
import com.github.imtimturner.translate.data.network.yandex.translate.json.YandexTranslateJsonTranslate;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface YandexTranslateAPI {

    @GET("/api/v1.5/tr.json/getLangs")
    Observable<YandexTranslateJsonGetLangs> getLangs(@Query("key") String key,
                                                     @Query("ui") String ui);

    @FormUrlEncoded
    @POST("/api/v1.5/tr.json/translate")
    Observable<YandexTranslateJsonTranslate> translate(@Query("key") String key,
                                                       @Field("text") String text,
                                                       @Field("lang") String lang);
}
