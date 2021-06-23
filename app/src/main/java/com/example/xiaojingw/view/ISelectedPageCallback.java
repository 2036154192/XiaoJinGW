package com.example.xiaojingw.view;

import com.example.xiaojingw.base.IBaseCallback;
import com.example.xiaojingw.model.doment.SelectedContent;
import com.example.xiaojingw.model.doment.SelectedPageCategory;

public interface ISelectedPageCallback extends IBaseCallback {

    //分类内容结果
    void onCategoriesLoaded(SelectedPageCategory categories);

    //内容
    void onContentLoaded(SelectedContent content);
}
