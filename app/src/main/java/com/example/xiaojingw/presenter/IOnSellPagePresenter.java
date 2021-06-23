package com.example.xiaojingw.presenter;

import com.example.xiaojingw.base.IBasePresenter;
import com.example.xiaojingw.view.IOnSellPageCallback;

public interface IOnSellPagePresenter extends IBasePresenter<IOnSellPageCallback> {

    //加载特惠内容
    void getOnSellContext();
    //重新加载内容
    void reLoad();
    //加载更多
    void loaderMore();

}
