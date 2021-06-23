package com.example.xiaojingw.presenter.impl;

import android.util.Log;

import com.example.xiaojingw.model.doment.Api;
import com.example.xiaojingw.model.doment.SelectedContent;
import com.example.xiaojingw.model.doment.SelectedPageCategory;
import com.example.xiaojingw.presenter.ISelectdPagePresenter;
import com.example.xiaojingw.utils.RetrofitManager;
import com.example.xiaojingw.utils.UrlUtils;
import com.example.xiaojingw.view.ISelectedPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedPagePresenterImpl implements ISelectdPagePresenter {

    private static final String TAG = "SelectedPagePresenterImpl";

    private final Api api;

    public SelectedPagePresenterImpl(){
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        api = retrofit.create(Api.class);
    }

    private ISelectedPageCallback mViewCallback = null;

    @Override
    public void getCategories() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        }
        Call<SelectedPageCategory> task = api.getSelectedPageCategories();
        task.enqueue(new Callback<SelectedPageCategory>() {
            @Override
            public void onResponse(Call<SelectedPageCategory> call, Response<SelectedPageCategory> response) {
                int code = response.code();
                if(code == HttpURLConnection.HTTP_OK){
                    SelectedPageCategory body = response.body();
                    //通知UI更新
                    if (mViewCallback != null) {
                        mViewCallback.onCategoriesLoaded(body);
                    }
                }else {
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(Call<SelectedPageCategory> call, Throwable t) {
                onLoadedError();
            }
        });
    }

    private void onLoadedError() {
        if (mViewCallback != null) {
            mViewCallback.onError();
        }
    }

    @Override
    public void getContentByCategory(SelectedPageCategory.DataDTO item) {
            String targetUrl = UrlUtils.getSelectedPageContentUrl(item.getFavorites_id());
            Call<SelectedContent> task = api.getSelectedContent(targetUrl);
            task.enqueue(new Callback<SelectedContent>() {
                @Override
                public void onResponse(Call<SelectedContent> call, Response<SelectedContent> response) {
                    int code = response.code();
                    if(code == HttpURLConnection.HTTP_OK){
                        SelectedContent body = response.body();
                        //通知UI更新
                        if (mViewCallback != null) {
                            Log.d(TAG, "body: "+body.toString());
                            mViewCallback.onContentLoaded(body);
                        }
                    }else {
                        onLoadedError();
                    }
                }

                @Override
                public void onFailure(Call<SelectedContent> call, Throwable t) {
                    onLoadedError();
                }
            });
        }

    @Override
    public void reloadContent() {
        this.getCategories();
    }

    @Override
    public void registerCallback(ISelectedPageCallback callback) {
        this.mViewCallback = callback;
    }

    @Override
    public void unregisterCallback(ISelectedPageCallback callback) {
        mViewCallback = null;
    }
}
