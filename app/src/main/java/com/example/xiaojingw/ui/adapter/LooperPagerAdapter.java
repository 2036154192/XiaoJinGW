package com.example.xiaojingw.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.xiaojingw.model.doment.HomePagerContent;
import com.example.xiaojingw.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class LooperPagerAdapter  extends PagerAdapter {

    private List<HomePagerContent.DataDTO> mData = new ArrayList<>();
    private OnLooperPageItemClickListener mOnLooperPageItemClickListener = null;

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public int getDataSize(){
        return mData.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position % mData.size();
        HomePagerContent.DataDTO dataDTO = mData.get(realPosition);
        int measuredHeight = container.getMeasuredHeight();
        int measuredWidth = container.getMeasuredWidth();
        int ivSize = (measuredWidth > measuredHeight?measuredWidth:measuredHeight) / 2;
        String coverUrl = UrlUtils.getCoverPath(dataDTO.getPict_url(),ivSize);
        ImageView iv = new ImageView(container.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(layoutParams);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(container.getContext()).load(coverUrl).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnLooperPageItemClickListener != null) {
                    HomePagerContent.DataDTO item = mData.get(realPosition);
                    mOnLooperPageItemClickListener.onLooperItemClick(item);
                }
            }
        });
        container.addView(iv);
        return iv;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setData(List<HomePagerContent.DataDTO> contents) {
        mData.clear();
        mData.addAll(contents);
        notifyDataSetChanged();
    }

    public void setOnLooperPageItemClickListener(OnLooperPageItemClickListener listener){
        this.mOnLooperPageItemClickListener = listener;
    }

    public interface OnLooperPageItemClickListener {
        void onLooperItemClick(HomePagerContent.DataDTO item);
    }

}
