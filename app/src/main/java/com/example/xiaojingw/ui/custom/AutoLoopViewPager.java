package com.example.xiaojingw.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.xiaojingw.R;


public class AutoLoopViewPager extends ViewPager {

    public static final long DEFAULT_DURATION = 3000;

    private long mDuration = DEFAULT_DURATION;

    public AutoLoopViewPager(@NonNull Context context) {
        this(context,null);
    }

    public AutoLoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //
        inti(context,attrs);
    }

    private void inti(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoLoopViewPager);
        mDuration = a.getInteger(R.styleable.AutoLoopViewPager_duration, (int) DEFAULT_DURATION);
        a.recycle();
    }

    private boolean isLoop = false;

    public void starLoop(){
        isLoop = true;
        //先拿到当前的位置
        post(mTask);
    }

    public void setDuration(long duration){
        this.mDuration = duration;
    }

    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            int currentItem = getCurrentItem();
            currentItem++;
            setCurrentItem(currentItem);
            if (isLoop){
                postDelayed(this,mDuration);
            }
        }
    };

    public void stopLoop(){
        isLoop = false;
        removeCallbacks(mTask);
    }
}
