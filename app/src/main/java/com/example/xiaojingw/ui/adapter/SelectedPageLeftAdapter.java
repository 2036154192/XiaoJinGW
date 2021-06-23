package com.example.xiaojingw.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xiaojingw.R;
import com.example.xiaojingw.model.doment.SelectedPageCategory;

import java.util.ArrayList;
import java.util.List;


public class SelectedPageLeftAdapter extends RecyclerView.Adapter<SelectedPageLeftAdapter.InnerHolder> {

    private List<SelectedPageCategory.DataDTO> mData = new ArrayList<>();

    private int mCurrentseldctedPostion = 0;
    private OnLeftItemClickListener ItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_left, parent, false);
        return new InnerHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        TextView textTv = holder.itemView.findViewById(R.id.left_category_tv);
        if (mCurrentseldctedPostion == position){
            textTv.setBackgroundColor(Color.parseColor("#EEEEEE"));
        }else {
            textTv.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        SelectedPageCategory.DataDTO dataDTO = mData.get(position);
        textTv.setText(dataDTO.getFavorites_title());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ItemClickListener != null) {
                    if (mCurrentseldctedPostion != position){
                        mCurrentseldctedPostion = position;
                        ItemClickListener.onLeftItemClick(dataDTO);
                        notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(SelectedPageCategory categories) {
        List<SelectedPageCategory.DataDTO> data = categories.getData();
        if (data != null) {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }
        if (mData.size()>0){
            ItemClickListener.onLeftItemClick(mData.get(mCurrentseldctedPostion));
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setOnLeftItemClickListener(OnLeftItemClickListener listener){
        this.ItemClickListener = listener;
    }

    public interface OnLeftItemClickListener{
        void onLeftItemClick(SelectedPageCategory.DataDTO item);
    }

}
