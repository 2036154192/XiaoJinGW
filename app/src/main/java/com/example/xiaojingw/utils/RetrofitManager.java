package com.example.xiaojingw.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static final RetrofitManager ourInstance = new RetrofitManager();
    private final Retrofit retrofit;

    public static RetrofitManager getInstance(){
        return ourInstance;
    }

    private RetrofitManager(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.GET_HOME)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit(){
        return retrofit;
    }

}
