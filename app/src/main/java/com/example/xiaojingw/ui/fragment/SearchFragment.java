package com.example.xiaojingw.ui.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xiaojingw.R;
import com.example.xiaojingw.base.BaseFragment;
import com.example.xiaojingw.model.doment.Histories;
import com.example.xiaojingw.model.doment.SearchRecommend;
import com.example.xiaojingw.model.doment.SearchResult;
import com.example.xiaojingw.presenter.ISearchPresenter;
import com.example.xiaojingw.ui.adapter.SearchResultAdapter;
import com.example.xiaojingw.ui.custom.TextFlowLayout;
import com.example.xiaojingw.utils.KeyboardUtil;
import com.example.xiaojingw.utils.PresenterManager;
import com.example.xiaojingw.utils.SizeUtils;
import com.example.xiaojingw.utils.TicketUtil;
import com.example.xiaojingw.utils.ToastUtils;
import com.example.xiaojingw.view.ISearchViewCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;



public class SearchFragment extends BaseFragment implements ISearchViewCallback, TextFlowLayout.OnFlowTextItemClickListener {

    private static final String TAG = "SearchFragment";
    private ISearchPresenter searchPresenter;
    private TextFlowLayout mHistoriesView;
    private TextFlowLayout mRecommendView;
    private LinearLayout mHistoryContainer;
    private LinearLayout mRecommendContainer;
    private ImageView mHistoryDelete;
    private AlertDialog dialog;
    private RecyclerView mResultList;
    private SearchResultAdapter searchResultAdapter;
    private TwinklingRefreshLayout mResultContainer;
    private TextView mSearchBth;
    private ImageView mSearchCleanBth;
    private EditText mSearchInputBox;

    @Override
    protected View loadRootViewe(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout,container,false);
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        searchPresenter = PresenterManager.getInstance().getSearchPresenter();
        searchPresenter.registerCallback(this);
        //?????????????????????
        searchPresenter.getRecommendWords();
        searchPresenter.getHistories();

        mResultContainer.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (searchPresenter != null) {
                    searchPresenter.loaderMore();
                }
            }
        });
    }

    @Override
    protected void release() {
        super.release();
        if (searchPresenter != null) {
            searchPresenter.unregisterCallback(this);
        }
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View view) {
        setUpState(State.SUCCESS);
        mRecommendView =  view.findViewById(R.id.search_history_view);
        mHistoriesView = view.findViewById(R.id.search_lishi_view);
        mHistoryContainer = view.findViewById(R.id.search_history_container);
        mRecommendContainer = view.findViewById(R.id.search_recommend_container);
        mHistoryDelete = view.findViewById(R.id.search_history_delete);
        mResultList = view.findViewById(R.id.search_result_list);
        mResultContainer = view.findViewById(R.id.search_result_container);
        mSearchBth = view.findViewById(R.id.search_bth);
        mSearchCleanBth = view.findViewById(R.id.search_clean_bth);
        mSearchInputBox = view.findViewById(R.id.search_input_box);

        mResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultAdapter = new SearchResultAdapter();
        mResultList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(),1.5f);
                outRect.bottom = SizeUtils.dip2px(getContext(),1.5f);
            }
        });
        mResultList.setAdapter(searchResultAdapter);

        mResultContainer.setEnableLoadmore(true);
        mResultContainer.setEnableRefresh(false);
        mResultContainer.setEnableOverScroll(true);
    }

    @Override
    protected void initListener() {
        mHistoryDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 dialog = new AlertDialog.Builder(getContext())
                        .setTitle("????????????????????????")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                searchPresenter.deleteHistories();
                            }
                        })
                        .setNegativeButton("??????",null)
                        .create();
                 dialog.show();
            }
        });

        //??????????????????????????????
        searchResultAdapter.setOnListeItemClickListener(new SearchResultAdapter.OnListeItemClickListener() {
            @Override
            public void onItemClick(SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean item) {
                TicketUtil.toTicketPage(getContext(),item);
            }
        });

        //????????????????????????
        mHistoriesView.setOnFlowTextItemClickListener(this);
        //??????????????????
        mRecommendView.setOnFlowTextItemClickListener(this);

        //????????????????????????
        mSearchInputBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && searchPresenter != null){
                    //?????????????????????
                    String trim = v.getText().toString().trim();
                    if (TextUtils.isEmpty(trim)) {
                        return false;
                    }
                    //????????????
                    toSearch(trim);
                }
                return true;
            }
        });

        //???????????????
        mSearchInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //?????????????????????

                //??????????????????0????????????
                boolean b = s.toString().trim().length() > 0;
                boolean a = s.toString().length() > 0;
                mSearchCleanBth.setVisibility(a ? View.VISIBLE : View.GONE );
                mSearchBth.setText(b?"??????":"??????");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //???????????????
        mSearchCleanBth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchInputBox.setText("");
                //????????????????????????
                switch2HistoryPage();
            }
        });

        //?????????????????????
        mSearchBth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = mSearchBth.getText().toString().trim();
                if (trim.equals("??????")){
                    //??????
                    if (searchPresenter != null) {
                        toSearch(mSearchInputBox.getText().toString().trim());
                        KeyboardUtil.hide(getContext(),v);
                    }
                }else {
                    //??????
                    KeyboardUtil.hide(getContext(),v);
                }
            }
        });
    }

    private void switch2HistoryPage() {
        if (searchPresenter != null) {
            searchPresenter.getHistories();
        }
        //????????????????????????
        mHistoryContainer.setVisibility(mHistoriesView.getContentSize()!=0?View.VISIBLE:View.GONE);
        mRecommendContainer.setVisibility(mRecommendView.getContentSize()!=0?View.VISIBLE:View.GONE);
        mResultList.setVisibility(View.GONE);
    }

    @Override
    protected void onRetryClick() {
        //????????????
        if (searchPresenter != null) {
            searchPresenter.research();
        }
    }

    @Override
    public void onHistoriesLoaded(Histories histories) {
            if ( histories == null || histories.getHistories().size() == 0){
                mHistoryContainer.setVisibility(View.GONE);
            }else {
                mHistoryContainer.setVisibility(View.VISIBLE);
                 mHistoriesView.setTextList(histories.getHistories());
            }
    }

    @Override
    public void onHistoriesDelete() {
        if (searchPresenter != null){
            searchPresenter.getHistories();
        }
    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        setUpState(State.SUCCESS);
        //???????????????????????????
        mRecommendContainer.setVisibility(View.GONE);
        mHistoryContainer.setVisibility(View.GONE);
        //??????????????????
        mResultContainer.setVisibility(View.VISIBLE);
        //????????????
        try {
            searchResultAdapter.setData(result);
        }catch (Exception e){
            e.printStackTrace();
            setUpState(State.EMPTY);
        }
    }

    @Override
    public void onMoreLoaded(SearchResult result) {
        mResultContainer.finishLoadmore();
        //????????????
        List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> moreData = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
        searchResultAdapter.addData(moreData);
        ToastUtils.showToast("????????????");
    }

    @Override
    public void onMoreLoadedError() {
        mResultContainer.finishLoadmore();
        ToastUtils.showToast("???????????????????????????......");
    }

    @Override
    public void onMoreLoadedEmpty() {
        mResultContainer.finishLoadmore();
        ToastUtils.showToast("??????????????????");
    }
    
    private List<String> recoment = new ArrayList<>();

    @Override
    public void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords) {
        for (SearchRecommend.DataBean word : recommendWords) {
            recoment.add(word.getKeyword());
        }
        if (recommendWords == null || recommendWords.size()==0){
            mRecommendContainer.setVisibility(View.GONE);
        }else {
            mRecommendView.setTextList(recoment);
            mRecommendContainer.setVisibility(View.VISIBLE);
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
    public void onFlowItemClick(String text) {
        toSearch(text);
    }

    private void toSearch(String text) {
        if (searchPresenter != null) {
            mResultList.scrollToPosition(0);
            mSearchInputBox.setText(text);
            mSearchInputBox.setFocusable(true);
            mSearchInputBox.requestFocus();
            mSearchInputBox.setSelection(text.length(),text.length());
            searchPresenter.doSearch(text);
        }
    }
}