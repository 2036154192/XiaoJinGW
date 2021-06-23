package com.example.xiaojingw.view;

import com.example.xiaojingw.base.IBaseCallback;
import com.example.xiaojingw.model.doment.TicketResult;

public interface ITicketPagerCallBack extends IBaseCallback {

    void onTicketLoaded(String cover, String  result);

}
