package com.example.xiaojingw.ui.activity;

import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.xiaojingw.R;
import com.example.xiaojingw.model.doment.SelectedContent;
import com.example.xiaojingw.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectedPageContentAdapter extends RecyclerView.Adapter<SelectedPageContentAdapter.InnerHolder> {

    private List<SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.result_listBean.UatmTbkItemBean> mData = new ArrayList<>();
    private static final String TAG = "SelectedPageContentAdapter";
    private ImageView cover;
    private TextView offPrise;
    private TextView title;
    private TextView buyBtn;
    private TextView originalPrise;
    private OnSelectedPageContentItemClickListener mContentItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_content, parent, false);
        return new InnerHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        cover = holder.itemView.findViewById(R.id.selected_cover);
        offPrise = holder.itemView.findViewById(R.id.selected_off_prise);
        title = holder.itemView.findViewById(R.id.selected_title);
        buyBtn = holder.itemView.findViewById(R.id.selected_buy_btn);
        originalPrise = holder.itemView.findViewById(R.id.selected_original_prise);
        SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.result_listBean.UatmTbkItemBean uatmTbkItemBean = mData.get(position);
        title.setText(uatmTbkItemBean.getTitle());
        Log.d(TAG, "onBindViewHolder: "+uatmTbkItemBean.getPict_url());
        Glide.with(holder.itemView.getContext()).load(UrlUtils.getCoverPath(uatmTbkItemBean.getPict_url())).into(cover);
        if (TextUtils.isEmpty(uatmTbkItemBean.getCoupon_click_url())){
            originalPrise.setText("没优惠了");
            buyBtn.setVisibility(View.GONE);
        }else {
            originalPrise.setText("原价"+uatmTbkItemBean.getZk_final_price()+"元");
            buyBtn.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(uatmTbkItemBean.getCoupon_info())){
            offPrise.setVisibility(View.GONE);
        }else {
            offPrise.setVisibility(View.VISIBLE);
            offPrise.setText(uatmTbkItemBean.getCoupon_info());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContentItemClickListener != null) {
                    mContentItemClickListener.onContentItemClick(uatmTbkItemBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(SelectedContent content) {
        if (content.getCode() == 10000){
            List<SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.result_listBean.UatmTbkItemBean> uatmTbkItemBeans = content.getData().gettbk_dg_optimus_material_response().getresult_list().getmap_data();
            mData.clear();
            mData.addAll(uatmTbkItemBeans);
            notifyDataSetChanged();
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setOnSelectedPageContentItemClickListener(OnSelectedPageContentItemClickListener listener){
        this.mContentItemClickListener = listener;
    }

    public interface OnSelectedPageContentItemClickListener{
        void onContentItemClick(SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.result_listBean.UatmTbkItemBean item);
    }
}
