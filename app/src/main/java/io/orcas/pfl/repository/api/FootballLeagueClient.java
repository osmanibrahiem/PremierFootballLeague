package io.orcas.pfl.repository.api;

import android.content.Context;

import io.orcas.pfl.helper.Constants;
import io.orcas.pfl.helper.NetworkUtils;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FootballLeagueClient {

    private static FootballLeagueClient instance;
    private final Retrofit retrofit;
    private FootballLeagueApi service;

    private FootballLeagueClient(Context context) {
        long cacheSize = (5 * 1024 * 1024);
        Cache myCache = new Cache(context.getCacheDir(), cacheSize);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.cache(myCache);

        httpClient.addInterceptor(chain -> {
            Request.Builder requestBuilder = chain.request().newBuilder();
            requestBuilder.addHeader(Constants.API_KEY_PARAM, Constants.API_KEY);

            if (!NetworkUtils.isNetworkConnected(context)) {
                requestBuilder.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7);
            } else {
                requestBuilder.addHeader("Cache-Control", "public, max-stale=" + 60);
            }

            return chain.proceed(requestBuilder.build());
        });

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(loggingInterceptor);

        this.retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
                .client(httpClient.build())
                .baseUrl(Constants.FOOTBALL_LEAGUE_BASE_URL)
                .build();
    }

    public synchronized static FootballLeagueClient getInstance(Context context) {
        if (instance == null) {
            instance = new FootballLeagueClient(context);
        }
        return instance;
    }

    public FootballLeagueApi getService() {
        if (service == null) {
            this.service = retrofit.create(FootballLeagueApi.class);
        }
        return service;
    }
}
