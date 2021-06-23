package com.example.xiaojingw.view;

import com.example.xiaojingw.base.IBaseCallback;
import com.example.xiaojingw.model.doment.HomePagerContent;

import java.util.List;

public interface ICategoryPagerCallback extends IBaseCallback {

    //数据加载回来
    void onContentLoaded(List<HomePagerContent.DataDTO> contents);

    void onLoaderMoreError();

    void onLoaderMoreEmpty();

    void onLoaderMoreLoader(List<HomePagerContent.DataDTO> contents);

    void onLooperListLoaded(List<HomePagerContent.DataDTO> contents);

    int getCategoryId();

}
