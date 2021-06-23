package com.example.xiaojingw.view;

import com.example.xiaojingw.base.IBaseCallback;
import com.example.xiaojingw.model.doment.OnSellContent;

public interface IOnSellPageCallback extends IBaseCallback {
    //特惠内容
    void onContextLoadedSeuccess(OnSellContent result);
    //加载更多
    void onMoreLoaded(OnSellContent moreResult);

    void onMoreLoadedError();

    void onMoreLoadedEmpty();

}
