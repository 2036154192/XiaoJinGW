package com.example.xiaojingw.presenter;

import com.example.xiaojingw.base.IBasePresenter;
import com.example.xiaojingw.view.ICategoryPagerCallback;

public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPagerCallback> {

    //根据分类id获取内容
    void getContentByCategoryId(int categoryId);

    void loaderMore(int categoryId);

    void reload(int categoryId);

}
