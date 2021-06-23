package com.example.xiaojingw.presenter.impl;

import android.util.Log;

import com.example.xiaojingw.model.doment.Api;
import com.example.xiaojingw.model.doment.TicketParams;
import com.example.xiaojingw.model.doment.TicketResult;
import com.example.xiaojingw.presenter.ITicketPresenter;
import com.example.xiaojingw.utils.RetrofitManager;
import com.example.xiaojingw.utils.StringUtils;
import com.example.xiaojingw.utils.UrlUtils;
import com.example.xiaojingw.view.ITicketPagerCallBack;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TicketPresenterlmpl implements ITicketPresenter {

    private static final String TAG = "TicketPresenterlmpl";

    private ITicketPagerCallBack mVIewCallback = null;
    private String mCover = null;
    private TicketResult ticketResult;

    enum LoadState{
        LOADING,SUCCESS,ERROR,NONE
    }

    private LoadState currentState = LoadState.NONE;

    @Override
    public void getTicket(String title, String url, String cover) {
        //去获取淘宝口令
        onTicketLoading();
        this.mCover = cover;
        String ticketUrl = UrlUtils.getTicketUrl(url);
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        TicketParams ticketParams = new TicketParams(ticketUrl,title);
        Call<TicketResult> ticket = api.getTicket(ticketParams);
        ticket.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK){
                    ticketResult = response.body();
                    //拿到数据通知UI更新
                    onTicketLoadedSuccess();
                }else {
                    onLoadedTicketError();
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                onLoadedTicketError();
            }
        });
    }

    private void onTicketLoadedSuccess() {
        if (mVIewCallback != null) {
            String results = StringUtils.results(ticketResult.getData().getTbk_tpwd_create_response().getData().getModel(), "￥");
            mVIewCallback.onTicketLoaded(mCover, results);
        }else {
            currentState = LoadState.SUCCESS;
        }
    }

    private void onLoadedTicketError() {
        if (mVIewCallback != null) {
            mVIewCallback.onError();
        }else {
            currentState = LoadState.ERROR;
        }
    }

    @Override
    public void registerCallback(ITicketPagerCallBack callback) {
        this.mVIewCallback = callback;
        if (currentState != LoadState.NONE){
            if (currentState == LoadState.SUCCESS){
                onTicketLoadedSuccess();
            }else if (currentState == LoadState.ERROR){
                onLoadedTicketError();
            }else if (currentState == LoadState.LOADING){
                onTicketLoading();
            }
        }
    }

    private void onTicketLoading() {
        if (mVIewCallback != null) {
            mVIewCallback.onLoading();
        }else {
            currentState=LoadState.LOADING;
        }
    }

    @Override
    public void unregisterCallback(ITicketPagerCallBack callback) {
        mVIewCallback = null;
    }
}
