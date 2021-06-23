package com.example.xiaojingw.presenter;


import com.example.xiaojingw.base.IBasePresenter;
import com.example.xiaojingw.view.ISearchViewCallback;

public interface ISearchPresenter extends IBasePresenter<ISearchViewCallback> {

    //获取搜索历史
    void getHistories();
    //删除搜索历史
    void deleteHistories();
    //搜索
    void doSearch(String keyword);
    //重新搜索
    void research();
    //获取更多的搜索结果
    void loaderMore();
    //推荐
    void getRecommendWords();

}
