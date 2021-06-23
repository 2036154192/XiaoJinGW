package com.example.xiaojingw.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xiaojingw.R;
import com.example.xiaojingw.base.BaseActivity;
import com.example.xiaojingw.model.doment.TicketResult;
import com.example.xiaojingw.presenter.ITicketPresenter;
import com.example.xiaojingw.utils.PresenterManager;
import com.example.xiaojingw.utils.StringUtils;
import com.example.xiaojingw.utils.ToastUtils;
import com.example.xiaojingw.utils.UrlUtils;
import com.example.xiaojingw.view.ITicketPagerCallBack;

public class TicketActivity extends BaseActivity implements ITicketPagerCallBack {

    private boolean hasTabaoApp = false;
    private ITicketPresenter ticketPresenterlmpl;
    private ImageView mCover;
    private EditText mTicketCode;
    private TextView mOpenOrCopyBtn;
    private ImageView mBreak;
    private String coverPath;

    @Override
    protected void initPresenter() {
        ticketPresenterlmpl = PresenterManager.getInstance().getTicketPresenterlmpl();
        if (ticketPresenterlmpl != null) {
            ticketPresenterlmpl.registerCallback(this);
        }
        //判断手机是否有淘宝 淘宝包名com.taobao.taobao
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            hasTabaoApp = packageInfo != null;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            hasTabaoApp = false;
        }
        mOpenOrCopyBtn.setText(hasTabaoApp ? "打开淘宝领券" : "复制淘口令" );
    }

    @Override
    protected void initView() {
        mCover = findViewById(R.id.ticker_cover);
        mTicketCode = findViewById(R.id.ticker_code);
        mOpenOrCopyBtn = findViewById(R.id.ticker_copy_or_open_btn);
        mBreak = findViewById(R.id.ticker_);
    }

    @Override
    protected void initEvent() {
        mBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mOpenOrCopyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //复制口令
                String trim = mTicketCode.getText().toString().trim();
                ClipboardManager systemService = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                //复制到粘贴板
                ClipData sob_taobao_ticket_code = ClipData.newPlainText("sob_taobao_ticket_code", trim);
                systemService.setPrimaryClip(sob_taobao_ticket_code);
                //有淘宝就打开
                if(hasTabaoApp){
                    //打开淘宝
                    Intent taobao = new Intent();
                    //打开选择器，会让用户进行选择
//                    taobao.setAction("android.intent.action.MAIN");
//                    taobao.addCategory("android.intent.category.LAUNCHER");
                    //直接打开淘宝
                    ComponentName componentName = new ComponentName("com.taobao.taobao","com.taobao.tao.TBMainActivity");
                    taobao.setComponent(componentName);
                    startActivity(taobao);
                }else {
                    ToastUtils.showToast("复制成功");
                }
            }
        });
    }

    @Override
    protected void release() {
        if (ticketPresenterlmpl != null) {
            ticketPresenterlmpl.unregisterCallback(this);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }


    @Override
    public void onTicketLoaded(String cover, String result) {
        //设置图片
        if (mCover != null && !TextUtils.isEmpty(cover)) {
            ViewGroup.LayoutParams layoutParams = mCover.getLayoutParams();
            if (cover.startsWith("https")){
                coverPath = cover;
            }else {
                coverPath = UrlUtils.getCoverPath(cover);
            }
            Glide.with(this).load(coverPath).into(mCover);
        }else {
            Glide.with(this).load(R.drawable.null_a).into(mCover);
        }
        //设置code
        if (result != null) {
            mTicketCode.setText(result);
        }
    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }
}
