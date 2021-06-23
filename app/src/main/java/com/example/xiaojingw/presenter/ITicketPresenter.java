package com.example.xiaojingw.presenter;

import com.example.xiaojingw.base.IBasePresenter;
import com.example.xiaojingw.view.ITicketPagerCallBack;

public interface ITicketPresenter extends IBasePresenter<ITicketPagerCallBack> {

    void getTicket(String title,String url,String cover);

}
