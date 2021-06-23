package com.example.xiaojingw.ui.adapter;

import android.content.Context;
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
import com.example.xiaojingw.model.doment.HomePagerContent;
import com.example.xiaojingw.model.doment.SearchResult;
import com.example.xiaojingw.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.InnerHolder> {

    private String TAG ="SearchResultAdapter";
    private List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> mData = new ArrayList<>();
    private OnListeItemClickListener mOnListeItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itme_home_pager_content, parent, false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        //设置数据
        SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean dataDTO = mData.get(position);
        holder.setData(dataDTO);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean item = mData.get(position);
                mOnListeItemClickListener.onItemClick(item);
            }
        });
    }

    public void addData(List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> contents) {
        int size = mData.size();
        mData.addAll(contents);
        //更新UI
        notifyItemRangeChanged(size,contents.size());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(SearchResult result) {
        List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> data = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        private ImageView cover;
        private TextView title;
        private TextView offPriseTv;
        private TextView finalPrise;
        private TextView yuanjia;
        private TextView sellCount;
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.goods_cover);
            title = itemView.findViewById(R.id.goods_title);
            offPriseTv = itemView.findViewById(R.id.goods_off_prise);
            finalPrise = itemView.findViewById(R.id.goods_after_off_prise);
            yuanjia = itemView.findViewById(R.id.goods_original_prise);
            sellCount = itemView.findViewById(R.id.goods_sell_count);
        }

        public void setData(SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean dataDTO) {
            Context context = itemView.getContext();
            title.setText(dataDTO.getTitle());
            String pict_url = dataDTO.getPict_url();
            if (!TextUtils.isEmpty(pict_url)){
                String coverPath = dataDTO.getPict_url();
                Log.d(TAG, "setData: "+coverPath);
                Glide.with(context).load(coverPath).into(cover);
            }else {
                Glide.with(context).load(R.drawable.null_a).into(cover);
            }
            String zkFinalPrice = dataDTO.getZk_final_price();
            long couponAmount = dataDTO.getCoupon_amount();
            finalPrise.setText(String.format("%.2f",Float.parseFloat(zkFinalPrice) - couponAmount));
            offPriseTv.setText(String.format(context.getString(R.string.text_goods_off_prise),couponAmount));
            yuanjia.setText(String.format(context.getString(R.string.text_goods_original_prise),zkFinalPrice));
            sellCount.setText(String.format(context.getString(R.string.text_goods_sell_prise),dataDTO.getVolume()));
        }

    }

    public void setOnListeItemClickListener(OnListeItemClickListener listener){
        this.mOnListeItemClickListener = listener;
    }

    public interface OnListeItemClickListener{
        void onItemClick(SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean item);
    }
}
