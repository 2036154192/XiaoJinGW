package com.example.xiaojingw.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.xiaojingw.R;
import com.example.xiaojingw.base.BaseFragment;
import com.example.xiaojingw.model.doment.Category;
import com.example.xiaojingw.model.doment.HomePagerContent;
import com.example.xiaojingw.presenter.ICategoryPagerPresenter;
import com.example.xiaojingw.presenter.ITicketPresenter;
import com.example.xiaojingw.presenter.impl.CategoryPagePresenterlmpl;
import com.example.xiaojingw.presenter.impl.TicketPresenterlmpl;
import com.example.xiaojingw.ui.activity.TicketActivity;
import com.example.xiaojingw.ui.adapter.HomePageContentAdapter;
import com.example.xiaojingw.ui.adapter.LooperPagerAdapter;
import com.example.xiaojingw.ui.custom.AutoLoopViewPager;
import com.example.xiaojingw.utils.Constants;
import com.example.xiaojingw.utils.PresenterManager;
import com.example.xiaojingw.utils.SizeUtils;
import com.example.xiaojingw.utils.TicketUtil;
import com.example.xiaojingw.utils.ToastUtils;
import com.example.xiaojingw.view.ICategoryPagerCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.views.TbNestScrollView;

import java.util.List;

public class HomePacketFragment extends BaseFragment implements ICategoryPagerCallback, HomePageContentAdapter.OnListeItemClickListener, LooperPagerAdapter.OnLooperPageItemClickListener {

    private static final String TAG = "HomePacketFragment";
    private ICategoryPagerPresenter categoryPagePresenterlmpl;
    private int materialId;
    private RecyclerView mContentList;
    private HomePageContentAdapter contentAdapter;
    private AutoLoopViewPager looperPager;
    private LooperPagerAdapter looperPagerAdapter;
    private LinearLayout looperPointContent;
    private TwinklingRefreshLayout twinklingRefreshLayout;
    private LinearLayout homePagerParent;
    private TbNestScrollView homePagerScroller;
    private RelativeLayout homePagerHeaderContainer;

    @Override
    public void onResume() {
        super.onResume();
        //可见的时候调用开始轮播
        looperPager.starLoop();
    }

    @Override
    public void onPause() {
        super.onPause();
        looperPager.stopLoop();
    }

    public static HomePacketFragment newInstance(Category.DataBean category){
        HomePacketFragment homePacketFragment = new HomePacketFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE,category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID,category.getId());
        homePacketFragment.setArguments(bundle);
        return homePacketFragment;
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_packet;
    }

    @Override
    protected void initListener() {

        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int measuredHeight1 = homePagerHeaderContainer.getMeasuredHeight();
                Log.d(TAG, "measuredHeight1: "+measuredHeight1);
                homePagerScroller.setHeaderHeight(measuredHeight1);
                int measuredHeight = homePagerParent.getMeasuredHeight();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentList.getLayoutParams();
                layoutParams.height = measuredHeight;
                mContentList.setLayoutParams(layoutParams);
                if (measuredHeight !=0){
                    homePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        looperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (looperPagerAdapter.getDataSize() == 0){
                    return;
                }
                int targetPosition = position %looperPagerAdapter.getDataSize();
                updateLooperIndicator(targetPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            //上拉加载更多
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (categoryPagePresenterlmpl != null) {
                    categoryPagePresenterlmpl.loaderMore(materialId);
                }
            }
            //下拉刷新
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                if (categoryPagePresenterlmpl != null) {
                    categoryPagePresenterlmpl.reload(materialId);
                }
                ToastUtils.showToast("刷新成功");
                refreshLayout.finishLoadmore();
            }
        });

        contentAdapter.setOnListeItemClickListener(this);

        looperPagerAdapter.setOnLooperPageItemClickListener(this);

    }

    //切换指示器
    private void updateLooperIndicator(int targetPosition) {
        for (int i = 0; i < looperPointContent.getChildCount(); i++) {
            View childAt = looperPointContent.getChildAt(i);
            if (i == targetPosition){
                childAt.setBackgroundResource(R.drawable.shape_indicator_point);
            }else {
                childAt.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
        }
    }

    @Override
    protected void initView(View view) {
        mContentList = view.findViewById(R.id.home_pager_content_list);
        looperPager = view.findViewById(R.id.looper_pager);
        looperPointContent = view.findViewById(R.id.looper_parent);
        twinklingRefreshLayout = view.findViewById(R.id.home_pager_refresh);
        homePagerParent = view.findViewById(R.id.home_pager_parent);
        homePagerScroller = view.findViewById(R.id.home_pager_scroller);
        homePagerHeaderContainer = view.findViewById(R.id.home_pager_header_container);
        //listview
        //设置布局管理器
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        //给布局设置外边距
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(),1.5f);
                outRect.bottom = SizeUtils.dip2px(getContext(),1.5f);
            }
        });
        //创建适配器List列表
        contentAdapter = new HomePageContentAdapter();
        //设置适配器
        mContentList.setAdapter(contentAdapter);

        //viewpager轮播图
        //创建适配器
        looperPagerAdapter = new LooperPagerAdapter();
        looperPager.setAdapter(looperPagerAdapter);

        //刷新
        twinklingRefreshLayout.setEnableRefresh(true);
        twinklingRefreshLayout.setEnableLoadmore(true);
    }

    @Override
    protected void loadData() {
        Bundle arguments =  getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        materialId = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
        if (categoryPagePresenterlmpl != null) {
            categoryPagePresenterlmpl.getContentByCategoryId(materialId);
        }
    }

    @Override
    protected void initPresenter() {
        categoryPagePresenterlmpl = PresenterManager.getInstance().getCategoryPagePresenterlmpl();
        categoryPagePresenterlmpl.registerCallback(this);
    }

    @Override
    public void onContentLoaded(List<HomePagerContent.DataDTO> contents) {
        //数据列表加载到了
        contentAdapter.setData(contents);
        setUpState(State.SUCCESS);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onError() {
        //网络错误
        setUpState(State.ERROR);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onLoaderMoreError() {
        ToastUtils.showToast("网络异常—请稍后重试");
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLoaderMoreEmpty() {
        ToastUtils.showToast("没有更多的宝贝了");
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLoaderMoreLoader(List<HomePagerContent.DataDTO> contents) {
        //添加到适配器的底部
        contentAdapter.addData(contents);
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }
        ToastUtils.showToast("加载完成");
    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataDTO> contents) {
        looperPagerAdapter.setData(contents);
        int i1 = (Integer.MAX_VALUE / 2) % contents.size();
        looperPager.setCurrentItem((Integer.MAX_VALUE / 2) - i1);
        //添加点
        looperPointContent.removeAllViews();
        for (int i = 0; i < contents.size(); i++) {
            View view = new View(getContext());
            int size = SizeUtils.dip2px(getContext(), 8);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size,size);
            layoutParams.leftMargin = SizeUtils.dip2px(getContext(),5);
            layoutParams.rightMargin = SizeUtils.dip2px(getContext(),5);
            view.setLayoutParams(layoutParams);
            if (i == 0){
                view.setBackgroundResource(R.drawable.shape_indicator_point);
            }else {
                view.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
            looperPointContent.addView(view);
        }
    }

    @Override
    public int getCategoryId() {
        return materialId;
    }

    @Override
    protected void release() {
        if (categoryPagePresenterlmpl != null) {
            categoryPagePresenterlmpl.unregisterCallback(this);
        }
    }

    @Override
    public void onItemClick(HomePagerContent.DataDTO item) {
        //列表内容被点击了
        handleItemClick(item);
    }

    @Override
    public void onLooperItemClick(HomePagerContent.DataDTO item) {
        //轮播图被点击了
        handleItemClick(item);
    }

    private void handleItemClick(HomePagerContent.DataDTO item) {
        TicketUtil.toTicketPage(getContext(),item);
    }
}