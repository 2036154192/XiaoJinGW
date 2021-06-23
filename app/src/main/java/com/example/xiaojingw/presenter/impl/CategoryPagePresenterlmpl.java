package com.example.xiaojingw.presenter.impl;

import android.util.Log;

import com.example.xiaojingw.model.doment.Api;
import com.example.xiaojingw.model.doment.HomePagerContent;
import com.example.xiaojingw.presenter.ICategoryPagerPresenter;
import com.example.xiaojingw.utils.RetrofitManager;
import com.example.xiaojingw.utils.UrlUtils;
import com.example.xiaojingw.view.ICategoryPagerCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagePresenterlmpl implements ICategoryPagerPresenter {

    private static final String TAG ="CategoryPagePresenterlmpl";

    private Map<Integer,Integer> pagesInfo = new HashMap<>();

    private static final int DEEAULT_PAGE = 1;
    private Integer currentPage;

    @Override
    public void getContentByCategoryId(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId()==categoryId) {
                callback.onLoading();
            }
        }
        Integer integer = pagesInfo.get(categoryId);
        if (integer == null){
            integer = DEEAULT_PAGE;
            pagesInfo.put(categoryId,integer);
        }
        Call<HomePagerContent> task = createTask(categoryId,integer);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK){
                    HomePagerContent body = response.body();
                    Log.d(TAG, "onResponse: ------------------"+body.toString());
                    //把数据给到UI
                    hoandleHomePageContentResult(body,categoryId);
                }else {
                    Log.d(TAG, "----------------数据获取失败-------------: ");
                    handleNetworkError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                Log.d(TAG, "onFailure: ----------------------------"+t.toString());
                handleNetworkError(categoryId);
            }
        });
    }

    private Call<HomePagerContent> createTask(int categoryId, Integer integer) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        String url = UrlUtils.createHomePagerUrl(categoryId,integer);
        return api.getHomePagerContent(url);
    }

    private void handleNetworkError(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId()==categoryId) {
                callback.onError();
            }
        }
    }

    private void hoandleHomePageContentResult(HomePagerContent body, int categoryId) {
        //通知UI层更新数据
        List<HomePagerContent.DataDTO> data = body.getData();
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId()==categoryId) {
                if (body == null || body.getData().size()==0){
                    callback.onEmpty();
                }else {
                    List<HomePagerContent.DataDTO> list = data.subList(data.size() - 5, data.size());
                    callback.onLooperListLoaded(list);
                    callback.onContentLoaded(data);
                }
            }
        }
    }

    @Override
    public void loaderMore(int categoryId) {
        //加载更多数据
        //拿到当前页面，页码++
        currentPage = pagesInfo.get(categoryId);
        if (currentPage != null) {
            currentPage++;
        }else {
            currentPage = 1;
        }
        //加载数据,处理数据结果
        Call<HomePagerContent> task = createTask(categoryId, currentPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK){
                    HomePagerContent result = response.body();
                    handleLoaderResult(result,categoryId);
                }else {
                    handleLoaderMoreError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                handleLoaderMoreError(categoryId);
            }
        });
    }

    private void handleLoaderResult(HomePagerContent result, int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (result == null || result.getData().size() == 0){
                    callback.onLoaderMoreEmpty();
                }else {
                    callback.onLoaderMoreLoader(result.getData());
                }
            }
        }
    }

    private void handleLoaderMoreError(int categoryId) {
        currentPage--;
        pagesInfo.put(categoryId,currentPage);
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId){
                callback.onLoaderMoreError();
            }
        }
    }

    @Override
    public void reload(int categoryId) {
        this.getContentByCategoryId(categoryId);
    }

    private List<ICategoryPagerCallback> callbacks = new ArrayList<>();

    @Override
    public void registerCallback(ICategoryPagerCallback callback) {
        if (!callbacks.contains(callback)){
            callbacks.add(callback);
        }
    }

    @Override
    public void unregisterCallback(ICategoryPagerCallback callback) {
        callbacks.remove(callback);
    }

}
