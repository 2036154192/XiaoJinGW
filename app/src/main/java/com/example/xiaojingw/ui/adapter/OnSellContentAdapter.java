package com.example.xiaojingw.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.xiaojingw.R;
import com.example.xiaojingw.model.doment.IBaseInfo;
import com.example.xiaojingw.model.doment.OnSellContent;
import com.example.xiaojingw.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class OnSellContentAdapter extends RecyclerView.Adapter<OnSellContentAdapter.InnerHolder> {

    private String TAG = "OnSellContentAdapter";
    private List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mData = new ArrayList<>();
    private OnSellPageItmeClickListenter mContentItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_on_sell_content, parent, false);
        return new InnerHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        //绑定数据
        ImageView cover = holder.itemView.findViewById(R.id.on_sell_cover);
        TextView title = holder.itemView.findViewById(R.id.on_sell_context_title_tv);
        TextView originPriesTv = holder.itemView.findViewById(R.id.on_sell_origin_pries_tv);
        TextView offPriseTv = holder.itemView.findViewById(R.id.on_sell_off_prise_tv);
        OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean mapDataBean = mData.get(position);
        Glide.with(cover.getContext()).load(UrlUtils.getCoverPath(mapDataBean.getPict_url())).into(cover);
        title.setText(mapDataBean.getTitle());
        originPriesTv.setText("￥"+mapDataBean.getZk_final_price());
        int coupon_amount = mapDataBean.getCoupon_amount();
        String zk_final_price = mapDataBean.getZk_final_price();
        float ZFP = Float.parseFloat(zk_final_price);
        float finalPrise = ZFP - coupon_amount;
        offPriseTv.setText("卷后价"+String.format("%.2f",finalPrise));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContentItemClickListener != null) {
                    mContentItemClickListener.onSellItemClick(mapDataBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(OnSellContent result) {
            mData.clear();
            mData.addAll(result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data());
            notifyDataSetChanged();
    }

    public void onMoreLoaded(OnSellContent moreResult) {
        List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> moreData = moreResult.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        int oldDataSize = mData.size();
        this.mData.addAll(moreData);
        notifyItemChanged(oldDataSize -1,moreData.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setOnSellPageItmeClickListenter(OnSellPageItmeClickListenter listenter){
        this.mContentItemClickListener = listenter;
    }

    public interface OnSellPageItmeClickListenter{
        void onSellItemClick(IBaseInfo dataBean);
    }
}
