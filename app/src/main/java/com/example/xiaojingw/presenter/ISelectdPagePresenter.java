package com.example.xiaojingw.presenter;

import com.example.xiaojingw.base.IBasePresenter;
import com.example.xiaojingw.model.doment.SelectedPageCategory;
import com.example.xiaojingw.view.ISelectedPageCallback;

public interface ISelectdPagePresenter extends IBasePresenter<ISelectedPageCallback> {

    //获取分类
    void getCategories();

    //根据分类获取分类获取内容
    void getContentByCategory(SelectedPageCategory.DataDTO item);

    //重新加载内容
    void reloadContent();
}
