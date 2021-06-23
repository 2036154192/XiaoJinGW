package com.example.xiaojingw.presenter.impl;

import android.util.Log;

import com.example.xiaojingw.model.doment.Api;
import com.example.xiaojingw.model.doment.Histories;
import com.example.xiaojingw.model.doment.SearchRecommend;
import com.example.xiaojingw.model.doment.SearchResult;
import com.example.xiaojingw.presenter.ISearchPresenter;
import com.example.xiaojingw.utils.JsonCacheUtil;
import com.example.xiaojingw.utils.RetrofitManager;
import com.example.xiaojingw.view.ISearchViewCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPresenter implements ISearchPresenter {

    private static final String TAG = "SearchPresenter";
    public static final int DEFAULT_HISTORIES_SIZE = 10;
    private final Api api;
    private ISearchViewCallback mSearchViewCallback = null;
    private static final int DEFAULT_PAGE = 0;
    private int mCurrentPage = DEFAULT_PAGE;
    private String mCurrentKeyword = null;
    private final JsonCacheUtil jsonCacheUtil;

    public SearchPresenter() {
        RetrofitManager instance = RetrofitManager.getInstance();
        Retrofit retrofit = instance.getRetrofit();
        api = retrofit.create(Api.class);
        jsonCacheUtil = JsonCacheUtil.getInstance();
    }

    @Override
    public void getHistories() {
        Histories histories = jsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        if (mSearchViewCallback != null){
            mSearchViewCallback.onHistoriesLoaded(histories);
        }
    }

    @Override
    public void deleteHistories() {
        jsonCacheUtil.delCache(KEY_HISTORIES);
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onHistoriesDelete();
        }
    }

    public static final String KEY_HISTORIES = "key_histories";
    private int historiesMaxSize = DEFAULT_HISTORIES_SIZE;

    //添加历史记录
    private void saveHistory(String history){
        //如果已经有了就删除再添加，对个数进行限制
        Histories histories = jsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        List<String> historiesList = null;
        if (histories != null && histories.getHistories() != null) {
            historiesList = histories.getHistories();
            if (historiesList.contains(history)){
                historiesList.remove(history);
            }
        }
        //去重完成
        //处理没有数据的情况
        if (historiesList == null) {
            historiesList = new ArrayList<>();
        }
        if (histories == null) {
            histories = new Histories();
        }
        histories.setHistories(historiesList);
        //对个数进行限制
        if (historiesList.size() > historiesMaxSize){
            historiesList = historiesList.subList(0,historiesMaxSize);
        }
        //添加记录
        historiesList.add(history);
        //保存记录
        jsonCacheUtil.saveCache(KEY_HISTORIES,histories);
    }

    @Override
    public void doSearch(String keyword) {
        if (mCurrentKeyword == null || !mCurrentKeyword.equals(keyword)){
            this.saveHistory(keyword);
            this.mCurrentKeyword = keyword;
        }
        //更新UI
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onLoading();
        }
            Call<SearchResult> task = api.doSearch(mCurrentPage, keyword);
            task.enqueue(new Callback<SearchResult>() {
                @Override
                public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                    int code = response.code();
                    Log.d(TAG, "doSearch: "+code);
                    if (code == HttpURLConnection.HTTP_OK){
                        handleSearchResult(response.body());
                    }else {
                        onError();
                    }
                }

                @Override
                public void onFailure(Call<SearchResult> call, Throwable t) {
                    t.printStackTrace();
                    onError();
                }
            });
    }

    private void onError() {
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onError();
        }
    }

    private void handleSearchResult(SearchResult data) {
        if (mSearchViewCallback != null) {
            if (isResultEmpty(data)){
                //这里数据为空
                mSearchViewCallback.onEmpty();
            }else {
                Log.d(TAG, "handleSearchResult: df3s1f3dsf3dsf3");
                mSearchViewCallback.onSearchSuccess(data);
            }
        }
    }

    private boolean isResultEmpty(SearchResult data){
        try {
            return data == null || data.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size() == 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public void research() {
        if (mCurrentKeyword == null) {
            if (mSearchViewCallback != null) {
                mSearchViewCallback.onEmpty();
            }
        }else {
            this.doSearch(mCurrentKeyword);
        }
    }

    @Override
    public void loaderMore() {
        mCurrentPage++;
        //进行搜索
        if (mCurrentKeyword == null){
            if (mSearchViewCallback != null) {
                mSearchViewCallback.onEmpty();
            }
        }else {
            doSearchMore();
        }
    }

    private void doSearchMore() {
        Call<SearchResult> satk = api.doSearch(mCurrentPage, mCurrentKeyword);
        satk.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                Log.d(TAG, "doSearchMore: "+code);
                if (code == HttpURLConnection.HTTP_OK){
                    handleMoreSearchResult(response.body());
                }else {
                    onLoaderMoreError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
                onLoaderMoreError();
            }
        });
    }

    private void handleMoreSearchResult(SearchResult body) {
        if (mSearchViewCallback != null) {
            if (isResultEmpty(body)){
                mSearchViewCallback.onMoreLoadedEmpty();
            }else {
                mSearchViewCallback.onMoreLoaded(body);
            }
        }
    }

    private void onLoaderMoreError() {
        mCurrentPage--;
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onMoreLoadedEmpty();
        }
    }

    @Override
    public void getRecommendWords() {
        Call<SearchRecommend> task = api.getRecommendWords();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                int code = response.code();
                Log.d(TAG, "getRecommendWords: "+code);
                if (code == HttpURLConnection.HTTP_OK){
                    if (response.body().getCode()!=20005){
                        mSearchViewCallback.onRecommendWordsLoaded(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void registerCallback(ISearchViewCallback callback) {
        this.mSearchViewCallback = callback;
    }

    @Override
    public void unregisterCallback(ISearchViewCallback callback) {
        mSearchViewCallback = null;
    }

}
