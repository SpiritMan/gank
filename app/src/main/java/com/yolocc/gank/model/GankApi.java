package com.yolocc.gank.model;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 */

public interface GankApi {

    @GET("data/{category}/{count}/{page}")
    Observable<GankWrap> getCategoryGankData(@Path("category") String category, @Path("count") int count, @Path("page") int page);
}
