package com.example.xiaojingw.view;

import com.example.xiaojingw.base.IBaseCallback;
import com.example.xiaojingw.model.doment.Histories;
import com.example.xiaojingw.model.doment.SearchRecommend;
import com.example.xiaojingw.model.doment.SearchResult;

import java.util.List;

public interface ISearchViewCallback extends IBaseCallback {

    //搜索历史结果
    void onHistoriesLoaded(Histories histories);

    //历史记录删除
    void onHistoriesDelete();

    //搜索成功结果
    void onSearchSuccess(SearchResult result);

    //加载更多内容
    void onMoreLoaded(SearchResult result);

    //加载更多网络出错
    void onMoreLoadedError();

    //没有更多内容
    void onMoreLoadedEmpty();

    //获取推荐词
    void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords);
}
