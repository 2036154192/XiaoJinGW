package com.example.xiaojingw.presenter.impl;

import android.util.Log;

import com.bumptech.glide.load.data.HttpUrlFetcher;
import com.example.xiaojingw.model.doment.Api;
import com.example.xiaojingw.model.doment.Category;
import com.example.xiaojingw.presenter.IHomePresenter;
import com.example.xiaojingw.utils.RetrofitManager;
import com.example.xiaojingw.view.IHomeCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class HomePresenterImpl implements IHomePresenter {

    private static final String TAG = "HomePresenterImpl";
    private IHomeCallback mCallback = null;

    @Override
    public void getCategories() {
        if (mCallback != null) {
            mCallback.onLoading();
        }
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<Category> task = api.getCategories();
        task.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                int code = response.code();
                Log.d(TAG, "code:-------- "+code);
                if (code == HttpURLConnection.HTTP_OK){
                    Category category = response.body();
                    if (mCallback != null) {
                        Log.d(TAG, "onResponse: a");
                        Log.d(TAG, "onResponse: "+category);
                        Log.d(TAG, "onResponse: "+category.getCode());
                        Log.d(TAG, "onResponse: b");
                        if (category.getCode() !=20005){
                            if (category == null || category.getData().size() == 0){
                                Log.d(TAG, "onResponse: b");
                                mCallback.onEmpty();
                            }else {
                                Log.d(TAG, "onResponse: c");
                                mCallback.onCategoriesLoaded(category);
                            }
                        }else {
                            mCallback.onEmpty();
                        }
                    }
                }else {
                    Log.d(TAG, "请求失败: ");
                    if (mCallback != null) {
                        mCallback.onError();
                    }
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Log.d(TAG, "请求失败1-------: "+t.toString());
                if (mCallback != null) {
                    mCallback.onError();
                }
            }
        });
    }

    @Override
    public void reLoad() {
        this.getCategories();
    }

    @Override
    public void registerCallback(IHomeCallback callback) {
        this.mCallback=callback;
    }

    @Override
    public void unregisterCallback(IHomeCallback callback) {
        mCallback = null;
    }
}
