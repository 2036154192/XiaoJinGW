package com.example.xiaojingw.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xiaojingw.R;
import com.example.xiaojingw.base.BaseFragment;
import com.example.xiaojingw.model.doment.IBaseInfo;
import com.example.xiaojingw.model.doment.OnSellContent;
import com.example.xiaojingw.presenter.IOnSellPagePresenter;
import com.example.xiaojingw.ui.adapter.OnSellContentAdapter;
import com.example.xiaojingw.utils.PresenterManager;
import com.example.xiaojingw.utils.TicketUtil;
import com.example.xiaojingw.utils.ToastUtils;
import com.example.xiaojingw.view.IOnSellPageCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

public class RedPacketFragment extends BaseFragment implements IOnSellPageCallback, OnSellContentAdapter.OnSellPageItmeClickListenter {

    private static final String TAG = "RedPacketFragment";
    private IOnSellPagePresenter onSellPagePresenter;
    private RecyclerView mContentRv;
    private OnSellContentAdapter onSellContentAdapter;
    private TwinklingRefreshLayout twinklingRefreshLayout;

    @Override
    protected View loadRootViewe(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout,container,false);
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        onSellPagePresenter = PresenterManager.getInstance().getOnSellPagePresenter();
        onSellPagePresenter.registerCallback(this);
        onSellPagePresenter.getOnSellContext();
    }

    @Override
    protected void release() {
        super.release();
        if (onSellPagePresenter != null) {
            onSellPagePresenter.unregisterCallback(this);
        }
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_red_packet;
    }

    @Override
    protected void initView(View view) {
        setUpState(State.SUCCESS);
        mContentRv = view.findViewById(R.id.on_sell_context_list);
        onSellContentAdapter = new OnSellContentAdapter();
        mContentRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mContentRv.setAdapter(onSellContentAdapter);

        twinklingRefreshLayout = view.findViewById(R.id.on_sell_refresh_layout);
    }

    @Override
    protected void initListener() {
        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            //??????????????????
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (onSellPagePresenter != null) {
                    onSellPagePresenter.loaderMore();
                }
            }
            //????????????
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                if (onSellPagePresenter != null) {
                    onSellPagePresenter.reLoad();
                    ToastUtils.showToast("????????????");
                    refreshLayout.finishRefreshing();
                }
            }
        });
        onSellContentAdapter.setOnSellPageItmeClickListenter(this);
    }

    @Override
    protected void onRetryClick() {
        onSellPagePresenter.reLoad();
    }

    @Override
    public void onContextLoadedSeuccess(OnSellContent result) {
        //???????????????UI
        setUpState(State.SUCCESS);
        onSellContentAdapter.setData(result);
    }

    @Override
    public void onMoreLoaded(OnSellContent moreResult) {
        twinklingRefreshLayout.finishLoadmore();
        ToastUtils.showToast("????????????");
        //????????????????????????
        onSellContentAdapter.onMoreLoaded(moreResult);
    }

    @Override
    public void onMoreLoadedError() {
        twinklingRefreshLayout.finishLoadmore();
        ToastUtils.showToast("???????????????????????????.....");
    }

    @Override
    public void onMoreLoadedEmpty() {
        twinklingRefreshLayout.finishLoadmore();
        ToastUtils.showToast("??????????????????.....");
    }

    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onSellItemClick(IBaseInfo item) {
        //??????????????????
        TicketUtil.toTicketPage(getContext(),item);
    }
}