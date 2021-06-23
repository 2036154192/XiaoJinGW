package com.example.xiaojingw.view;

import com.example.xiaojingw.base.IBaseCallback;
import com.example.xiaojingw.model.doment.Category;

public interface IHomeCallback extends IBaseCallback {

    void onCategoriesLoaded(Category category);


}
