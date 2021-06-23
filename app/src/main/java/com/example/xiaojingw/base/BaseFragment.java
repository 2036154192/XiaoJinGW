package com.example.xiaojingw.base;


import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.xiaojingw.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public abstract class BaseFragment extends Fragment {

    private Unbinder bind;
    private FrameLayout baseContainer;
    private State currentState = State.NONE;
    private View loadingView;
    private View view;
    private View errorView;
    private View emptyView;
    private View rootView;

    public enum State{
        NONE,LOADING,SUCCESS,ERROR,EMPTY
    }

    public void retry(View rootView){
        ImageView imageView = rootView.findViewById(R.id.network_error_tips);
        //点击重新加载内容
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRetryClick();
            }
        });
    }

    //如果子类需要网络错误点击覆盖
    protected void onRetryClick() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = loadRootViewe(inflater,container);
        baseContainer = rootView.findViewById(R.id.base_contaile);
        loadStatesView(inflater,container);
        bind = ButterKnife.bind(this, rootView);
        initView(rootView);
        retry(rootView);
        initListener();
        initPresenter();
        loadData();
        return rootView;
    }

    protected void initListener(){

    }

    protected View loadRootViewe(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_fragment_layout,container,false);
    }

    protected void loadStatesView(LayoutInflater inflater, ViewGroup container){
        //加载各种状态的view
        //成功的view
        view = loadRootView(inflater, container);
        baseContainer.addView(view);
        //加载页面
        loadingView = loadLoadingView(inflater,container);
        baseContainer.addView(loadingView);
        //错误页面
        errorView = loadErrorView(inflater,container);
        baseContainer.addView(errorView);
        //内容为空的页面
        emptyView = loadEmptyView(inflater,container);
        baseContainer.addView(emptyView);
        setUpState(State.NONE);
    }

    protected View loadErrorView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_error,container,false);
    }

    protected View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty,container,false);
    }

    //子类通过方法来切换状态页面
    public void setUpState(State state){
        currentState = state;
        view.setVisibility(currentState == State.SUCCESS ? View.VISIBLE : View.GONE);
        loadingView.setVisibility(currentState == State.LOADING ? View.VISIBLE : View.GONE);
        errorView.setVisibility(currentState == State.ERROR ? View.VISIBLE : View.GONE);
        emptyView.setVisibility(currentState == State.EMPTY ? View.VISIBLE : View.GONE);
    }

    protected View loadLoadingView(LayoutInflater inflater, ViewGroup container){
        return  inflater.inflate(R.layout.fragment_loading,container,false);
    }

    protected void initView(View view){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bind != null) {
            bind.unbind();
        }
        release();
    }

    protected void release(){
        //释放资源

    }

    protected void initPresenter(){
        //创建Presenter
    }

    protected void loadData(){
        //加载数据
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container){
        int resId = getRootViewResId();
        return inflater.inflate(resId, container, false);
    }

    protected abstract int getRootViewResId();

}
