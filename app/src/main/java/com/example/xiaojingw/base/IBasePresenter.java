package com.example.xiaojingw.base;

import com.example.xiaojingw.view.IHomeCallback;

public interface IBasePresenter<T>  {

    //注册UI通知的接口
    void registerCallback(T callback);
    //取消UI通知的接口
    void unregisterCallback(T callback);

}
