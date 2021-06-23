package com.example.xiaojingw.presenter;

import com.example.xiaojingw.base.IBasePresenter;
import com.example.xiaojingw.view.IHomeCallback;

public interface IHomePresenter extends IBasePresenter<IHomeCallback> {
    //获取商品分类
    void getCategories();

    //重新加载内容
    void reLoad();

}
