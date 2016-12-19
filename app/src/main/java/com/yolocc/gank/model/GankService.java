package com.yolocc.gank.model;

import android.os.Build;
import android.support.compat.BuildConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 */

public class GankService {

    public static Retrofit defaultInstance() {
        return new Retrofit.Builder()
                .client(defaultOkHttpClient())
                .baseUrl("http://gank.io/api/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    private static OkHttpClient defaultOkHttpClient() {

        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new HeaderInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    final static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls()
            .create();

    public static class HeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            request = request.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("User-Agent", "Android")
                    .addHeader("X-Os", "Android")
//                .addHeader("Authorization", "Bearer " + UserSession.getInstance().getTokenData().getToken())
                    .addHeader("X-Os-Version", Build.VERSION.RELEASE)
                    .addHeader("X-Client", BuildConfig.APPLICATION_ID)
                    .addHeader("X-Client-Version", BuildConfig.VERSION_NAME)
                    .addHeader("X-Device", Build.DEVICE)
                    .addHeader("X-Model", Build.MODEL)
                    .addHeader("X-Product", Build.PRODUCT)
                    .build();
            return chain.proceed(request);
        }

    }
}
