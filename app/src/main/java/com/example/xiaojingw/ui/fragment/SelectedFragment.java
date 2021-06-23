package com.example.xiaojingw.ui.fragment;

import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xiaojingw.R;
import com.example.xiaojingw.base.BaseFragment;
import com.example.xiaojingw.model.doment.SelectedContent;
import com.example.xiaojingw.model.doment.SelectedPageCategory;
import com.example.xiaojingw.presenter.ISelectdPagePresenter;
import com.example.xiaojingw.ui.activity.SelectedPageContentAdapter;
import com.example.xiaojingw.ui.adapter.SelectedPageLeftAdapter;
import com.example.xiaojingw.utils.PresenterManager;
import com.example.xiaojingw.utils.SizeUtils;
import com.example.xiaojingw.utils.TicketUtil;
import com.example.xiaojingw.view.ISelectedPageCallback;

public class SelectedFragment extends BaseFragment implements ISelectedPageCallback, SelectedPageLeftAdapter.OnLeftItemClickListener {

    private static final String TAG = "SelectedFragment";
    private ISelectdPagePresenter selectedPagePresenter;
    private RecyclerView leftCategoryList;
    private RecyclerView rightContentList;
    private SelectedPageLeftAdapter selectedPageLeftAdapter;
    private SelectedPageContentAdapter selectedPageContentAdapter;
    private TextView fragment_bar_title;

    @Override
    protected View loadRootViewe(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout,container,false);
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        selectedPagePresenter = PresenterManager.getInstance().getSelectedPagePresenter();
        selectedPagePresenter.registerCallback(this);
        selectedPagePresenter.getCategories();
    }

    @Override
    protected void release() {
        super.release();
        if (selectedPagePresenter != null) {
            selectedPagePresenter.unregisterCallback(this);
        }
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_selected;
    }

    @Override
    protected void initView(View view) {

        fragment_bar_title = view.findViewById(R.id.fragment_bar_title);
        fragment_bar_title.setText("精选宝贝");
        setUpState(State.SUCCESS);
        leftCategoryList = view.findViewById(R.id.left_category_list);
        rightContentList = view.findViewById(R.id.right_content_list);

        leftCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        selectedPageLeftAdapter = new SelectedPageLeftAdapter();
        leftCategoryList.setAdapter(selectedPageLeftAdapter);

        rightContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        selectedPageContentAdapter = new SelectedPageContentAdapter();
        rightContentList.setAdapter(selectedPageContentAdapter);
        rightContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(),4);
                outRect.bottom = SizeUtils.dip2px(getContext(),4);
                outRect.right = SizeUtils.dip2px(getContext(),6);
                outRect.left = SizeUtils.dip2px(getContext(),6);
            }
        });
    }

    @Override
    protected void initListener() {
        selectedPageLeftAdapter.setOnLeftItemClickListener(this);
        selectedPageContentAdapter.setOnSelectedPageContentItemClickListener(new SelectedPageContentAdapter.OnSelectedPageContentItemClickListener() {
            @Override
            public void onContentItemClick(SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.result_listBean.UatmTbkItemBean item) {
//                String title = item.getTitle();
//                String url = item.getCoupon_click_url();
//                if (TextUtils.isEmpty(url)){
//                    url = item.getClick_url();
//                }
//                String cover = item.getPict_url();
//                ITicketPresenter ticketPresenterlmpl = PresenterManager.getInstance().getTicketPresenterlmpl();
//                ticketPresenterlmpl.getTicket(title,url,cover);
//                startActivity(new Intent(getContext(), TicketActivity.class));
                TicketUtil.toTicketPage(getContext(),item);
            }
        });
    }

    @Override
    public void onCategoriesLoaded(SelectedPageCategory categories) {
        setUpState(State.SUCCESS);
        //分类数据返回
        Log.d(TAG, "onCategoriesLoaded: "+categories.toString());
        //更新UI
        selectedPageLeftAdapter.setData(categories);
    }

    @Override
    public void onContentLoaded(SelectedContent content) {
        //根据当前选中的分类，获取分类内容
        selectedPageContentAdapter.setData(content);
        rightContentList.scrollToPosition(0);
    }

    @Override
    protected void onRetryClick() {
        if (selectedPagePresenter != null) {
            selectedPagePresenter.reloadContent();
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

    }

    @Override
    public void onLeftItemClick(SelectedPageCategory.DataDTO item) {
        //单击事件
        selectedPagePresenter.getContentByCategory(item);
    }
}