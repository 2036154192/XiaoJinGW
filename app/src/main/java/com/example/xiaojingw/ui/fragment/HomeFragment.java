package com.example.xiaojingw.ui.fragment;


import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.xiaojingw.R;
import com.example.xiaojingw.base.BaseFragment;
import com.example.xiaojingw.model.doment.Category;
import com.example.xiaojingw.presenter.impl.HomePresenterImpl;
import com.example.xiaojingw.ui.activity.IMainActivity;
import com.example.xiaojingw.ui.activity.MainActivity;
import com.example.xiaojingw.ui.activity.ScanQrCodeActivity;
import com.example.xiaojingw.ui.adapter.HomePagerAdapter;
import com.example.xiaojingw.utils.PresenterManager;
import com.example.xiaojingw.view.IHomeCallback;
import com.google.android.material.tabs.TabLayout;
import com.vondear.rxfeature.activity.ActivityScanerCode;


public class HomeFragment extends BaseFragment implements IHomeCallback {

    private String TAG = "HomeFragment";
    private HomePresenterImpl homePresenter;

    public TabLayout mTabLayout;
    public ViewPager homepager;
    private HomePagerAdapter homePagerAdapter;
    private TextView mSearchInputBox;
    private ImageView mScanIcon;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        //初始化控件
        homepager = view.findViewById(R.id.home_pager);
        mTabLayout = view.findViewById(R.id.home_indicator);
        mTabLayout.setupWithViewPager(homepager);
        mSearchInputBox = view.findViewById(R.id.home_search_input_box);
        mScanIcon = view.findViewById(R.id.scan_icon);
        //给viewpager创建适配器
        homePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        homepager.setAdapter(homePagerAdapter);
        homepager.setOffscreenPageLimit(3);
    }

    @Override
    protected void initPresenter() {
        homePresenter = (HomePresenterImpl) PresenterManager.getInstance().getHomePresenter();
        homePresenter.registerCallback(this);

        mSearchInputBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity instanceof IMainActivity){
                    ((IMainActivity)activity).switch2Search();
                }
            }
        });

    }

    @Override
    protected void initListener() {
        mScanIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到扫码界面
                startActivity(new Intent(getActivity(), ScanQrCodeActivity.class));
            }
        });
    }

    @Override
    protected View loadRootViewe(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_home_fragment_layout,container,false);
    }

    @Override
    protected void loadData() {
        //加载数据
        homePresenter.getCategories();
    }

    @Override
    public void onCategoriesLoaded(Category category) {
        setUpState(State.SUCCESS);
        //加载的数据从这里
        if (category != null) {
            homePagerAdapter.setTitleData(category);
        }
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
    protected void release() {
        //取消注册
        if (homePresenter != null) {
            homePresenter.unregisterCallback(this);
        }
    }

    @Override
    protected void onRetryClick() {
        //网络错误点击实现功能
        Log.d(TAG, "onRetryClick: ");
        if (homePresenter != null) {
            homePresenter.getCategories();
        }
    }

}