package com.yolocc.gank.model;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 */

public interface GankApi {

    @GET("data/{category}/{count}/{page}")
    Observable<GankWrap> getCategoryGankData(@Path("category") String category, @Path("count") int count, @Path("page") int page);

    @GET
    Observable<ResponseBody> downloadPic(@Url String imageUrl);
}
