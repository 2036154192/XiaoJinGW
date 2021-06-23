package com.example.xiaojingw.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.xiaojingw.R;

public class LoadingView extends AppCompatImageView {

    private float mDrgres = 0;
    private boolean mNeedRotate = true;

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.drawable.loading);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startRotate();
    }

    private void startRotate() {
        mNeedRotate = true;
        post(new Runnable() {
            @Override
            public void run() {
                mDrgres += 10;
                if (mDrgres >= 360){
                    mDrgres = 0;
                }
                invalidate();
                if (getVisibility() != VISIBLE && !mNeedRotate){
                    removeCallbacks(this);
                }else {
                    postDelayed(this,30);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRotate();
    }

    private void stopRotate() {
        mNeedRotate = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(mDrgres,getWidth() / 2,getHeight() / 2);
        super.onDraw(canvas);
    }
}
