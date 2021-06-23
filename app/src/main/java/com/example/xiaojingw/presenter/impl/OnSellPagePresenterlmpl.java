package com.example.xiaojingw.presenter.impl;

import android.util.Log;

import com.example.xiaojingw.model.doment.Api;
import com.example.xiaojingw.model.doment.OnSellContent;
import com.example.xiaojingw.presenter.IOnSellPagePresenter;
import com.example.xiaojingw.utils.RetrofitManager;
import com.example.xiaojingw.utils.UrlUtils;
import com.example.xiaojingw.view.IOnSellPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OnSellPagePresenterlmpl implements IOnSellPagePresenter {

    private static final String TAG = "OnSellPagePresenterlmpl";
    public static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;
    private IOnSellPageCallback mOnSellPageCallback = null;
    private final Api api;
    private boolean isLoading = false;

    public OnSellPagePresenterlmpl(){
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
         api = retrofit.create(Api.class);
    }

    @Override
    public void getOnSellContext() {
        if (isLoading){
            return;
        }
        isLoading = true;
        if (mOnSellPageCallback != null) {
            mOnSellPageCallback.onLoading();
        }
        //获取特惠内容
        String url = UrlUtils.getOnSellPageUrl(mCurrentPage);
        Call<OnSellContent> task = api.getOnSellPageContext(url);
        Log.d(TAG, "getOnSellContext: ");
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                Log.d(TAG, "onResponse: ");
                isLoading = false;
                int code = response.code();
                Log.d(TAG, "onResponse: "+code);
                if (code == HttpURLConnection.HTTP_OK){
                    OnSellContent sellContext = response.body();
                    onSuccess(sellContext);
                }else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                onError();
            }
        });
    }

    private void onSuccess(OnSellContent sellContext) {
        if (mOnSellPageCallback != null) {
            try {
                if (isEmpty(sellContext)){
                    onEmpty();
                }else {
                    mOnSellPageCallback.onContextLoadedSeuccess(sellContext);
                }
            }catch (Exception e){
                e.printStackTrace();
                onEmpty();
            }
        }
    }

    private boolean isEmpty(OnSellContent context){
        int size = context.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        return size == 0;
    }

    private void  onEmpty(){
        if (mOnSellPageCallback != null) {
            mOnSellPageCallback.onEmpty();
        }
    }

    private void onError() {
        isLoading = false;
        if (mOnSellPageCallback != null) {
            mOnSellPageCallback.onError();
        }
        Log.d(TAG, "onError: ");
    }

    @Override
    public void reLoad() {
        //重载
        this.getOnSellContext();
    }

    @Override
    public void loaderMore() {
        if (isLoading){
            return;
        }
        isLoading = true;
        //加载更多
        mCurrentPage++;
        String url = UrlUtils.getOnSellPageUrl(mCurrentPage);
        Call<OnSellContent> task = api.getOnSellPageContext(url);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                isLoading = false;
                int code = response.code();
                if(code == HttpURLConnection.HTTP_OK){
                    OnSellContent body = response.body();
                    onLoaderMore(body);
                }else {
                    onMoreloaderError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                onMoreloaderError();
            }
        });
    }

    private void onMoreloaderError() {
        isLoading = false;
        mCurrentPage--;
        mOnSellPageCallback.onMoreLoadedError();
    }


    private void onLoaderMore(OnSellContent body) {
        if (mOnSellPageCallback != null){
            if(isEmpty(body)){
                mCurrentPage--;
                mOnSellPageCallback.onMoreLoadedEmpty();
            }else {
                mOnSellPageCallback.onMoreLoaded(body);
            }
        }
    }

    @Override
    public void registerCallback(IOnSellPageCallback callback) {
        this.mOnSellPageCallback = callback;
    }

    @Override
    public void unregisterCallback(IOnSellPageCallback callback) {
        mOnSellPageCallback = null;
    }
}
