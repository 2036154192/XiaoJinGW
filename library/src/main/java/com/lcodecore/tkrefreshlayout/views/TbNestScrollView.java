package com.lcodecore.tkrefreshlayout.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

public class TbNestScrollView extends NestedScrollView {

    private static final String TAG="TbNestScrollView";

    private int mHeaderHeight = 0;
    private int originScroll= 0;
    private RecyclerView mRecyclerView;

    public TbNestScrollView(@NonNull Context context) {
        super(context);
    }

    public TbNestScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TbNestScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHeaderHeight(int headerHeight){
        this.mHeaderHeight = headerHeight;
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (target instanceof RecyclerView){
            this.mRecyclerView = (RecyclerView) target;
        }
        if (originScroll < mHeaderHeight){
            scrollBy(dx,dy);
            consumed[0] = dx;
            consumed[1] = dy;
        }
        super.onNestedPreScroll(target, dx, dy, consumed, type);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.originScroll = t;
        super.onScrollChanged(l, t, oldl, oldt);

    }

    public boolean isInBottom() {
        if (mRecyclerView != null) {
            boolean canScroll = !mRecyclerView.canScrollVertically(1);
            Log.d(TAG, "canScroll:--------------"+canScroll);
            return canScroll;
        }
        return false;
    }
}
