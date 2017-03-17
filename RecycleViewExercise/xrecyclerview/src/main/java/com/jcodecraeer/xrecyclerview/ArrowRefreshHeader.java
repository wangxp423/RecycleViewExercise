package com.jcodecraeer.xrecyclerview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Date;

public class ArrowRefreshHeader extends LinearLayout implements BaseRefreshHeader {

    private LinearLayout mContainer;
    private int mState = PULL_STATE_NONE;
    public int mMeasuredHeight;
    private XRecyclerView.HeaderAndFooterViewCallback mHeaderCallBack;

    public ArrowRefreshHeader(Context context, View view) {
        super(context);
        initView(view);
    }

    /**
     * @param context
     * @param attrs
     */
    public ArrowRefreshHeader(Context context, AttributeSet attrs, View view) {
        super(context, attrs);
        initView(view);
    }

    public void setHeaderCallBack(XRecyclerView.HeaderAndFooterViewCallback callBack) {
        this.mHeaderCallBack = callBack;
    }

    private void initView(View view) {
        // 初始情况，设置下拉刷新view高度为0
        mContainer = (LinearLayout) view;
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        this.setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);
        addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
        setGravity(Gravity.BOTTOM);

        //init the progress view
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();
    }

    public View getHeaderView() {
        return mContainer;
    }

    public void setState(int state) {
        if (state == mState) return;
        Log.d("Test", "setState.state = " + state);
        mHeaderCallBack.onStateChanged(this, state, mState);
        mState = state;
    }

    public int getState() {
        return mState;
    }

    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
        mHeaderCallBack.onHeightChanged(this, height, mState);
    }

    public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        return lp.height;
    }

    @Override
    public void onMove(float delta) {
//        Log.d("Test", "onMove.delta = " + delta + "  getVisibleHeight = " + getVisibleHeight() + "  state = " + getState());
        if (getVisibleHeight() > 0 || delta > 0) {
            setVisibleHeight((int) delta + getVisibleHeight());
            if (getVisibleHeight() > mMeasuredHeight) {
                setState(PULL_STATE_ENABLE);
            } else {
                setState(PULL_STATE_PULLING);
            }
        }
    }

    @Override
    public boolean releaseAction() {
//        Log.d("Test", "releaseAction.getVisibleHeight = " + getVisibleHeight() + "  state = " + getState());
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        if (getVisibleHeight() > mMeasuredHeight) {
//            if (mState < PULL_STATE_ENABLE) {
                setState(PULL_STATE_LOADING);
//            } else {
//                smoothScrollTo(mMeasuredHeight);
//            }
            isOnRefresh = true;
        } else {
            if (getState() < PULL_STATE_LOADING){
                smoothScrollTo(0);
            }
            isOnRefresh = false;
        }
        return isOnRefresh;
    }

    @Override
    public void refreshComplete() {

    }

    public void smoothScrollTo(final int destHeight) {
//        Log.d("Test", "smoothScrollTo.getVisibleHeight = " + getVisibleHeight() + "  destHeight = " + destHeight);
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                Log.d("Test", "onAnimationUpdate = " + animation.getAnimatedValue());
                int value = (int) animation.getAnimatedValue();
                setVisibleHeight(value);
            }

        });
        animator.start();
    }

    public static String friendlyTime(Date time) {
        //获取time距离当前的秒数
        int ct = (int) ((System.currentTimeMillis() - time.getTime()) / 1000);

        if (ct == 0) {
            return "刚刚";
        }

        if (ct > 0 && ct < 60) {
            return ct + "秒前";
        }

        if (ct >= 60 && ct < 3600) {
            return Math.max(ct / 60, 1) + "分钟前";
        }
        if (ct >= 3600 && ct < 86400)
            return ct / 3600 + "小时前";
        if (ct >= 86400 && ct < 2592000) { //86400 * 30
            int day = ct / 86400;
            return day + "天前";
        }
        if (ct >= 2592000 && ct < 31104000) { //86400 * 30
            return ct / 2592000 + "月前";
        }
        return ct / 31104000 + "年前";
    }

}