package com.example.xiaojingw.ui.adapter;

import android.content.Context;
import android.media.Image;
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
import com.example.xiaojingw.utils.UrlUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HomePageContentAdapter extends RecyclerView.Adapter<HomePageContentAdapter.InnerHolder> {

    private List<HomePagerContent.DataDTO> data = new ArrayList<>();
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
        HomePagerContent.DataDTO dataDTO = data.get(position);
        holder.setData(dataDTO);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePagerContent.DataDTO item = data.get(position);
                mOnListeItemClickListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<HomePagerContent.DataDTO> contents) {
        data.clear();
        data.addAll(contents);
        notifyDataSetChanged();
    }

    public void addData(List<HomePagerContent.DataDTO> contents) {
        int size = data.size();
        data.addAll(contents);
        //更新UI
        notifyItemRangeChanged(size,contents.size());
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

        public void setData(HomePagerContent.DataDTO dataDTO) {
            Context context = itemView.getContext();
            title.setText(dataDTO.getTitle());
            ViewGroup.LayoutParams layoutParams = cover.getLayoutParams();
            int width = layoutParams.width;
            int height = layoutParams.height;
            int coverSize = (width >height ?width:height) / 2 ;
            Glide.with(context).load(UrlUtils.getCoverPath(dataDTO.getPict_url(),coverSize)).into(cover);
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
        void onItemClick(HomePagerContent.DataDTO item);
    }

}
