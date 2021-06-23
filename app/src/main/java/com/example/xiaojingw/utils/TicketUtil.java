package com.example.xiaojingw.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.xiaojingw.model.doment.IBaseInfo;
import com.example.xiaojingw.presenter.ITicketPresenter;
import com.example.xiaojingw.ui.activity.TicketActivity;


public class TicketUtil {
    public static void toTicketPage(Context context, IBaseInfo item){
        String title = item.getTitle();
        String url = item.getUrl();
        if (TextUtils.isEmpty(url)){
            url = item.getUrl();
        }
        String cover = item.getCover();
        ITicketPresenter ticketPresenterlmpl = PresenterManager.getInstance().getTicketPresenterlmpl();
        ticketPresenterlmpl.getTicket(title,url,cover);
        context.startActivity(new Intent(context, TicketActivity.class));
    }

}
