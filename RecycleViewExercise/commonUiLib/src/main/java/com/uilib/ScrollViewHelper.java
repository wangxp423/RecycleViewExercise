package com.uilib;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ScrollView;

public class ScrollViewHelper {
	
	private static final int DURATION	= 500;
    private ScrollView mScrollView;
    private ScrollHelper mScroller;
    private ViewScaleHelper mViewShrinker, mViewExpander;
    private int mShrinkViewLayoutParamHeight, mExpandViewLayoutParamHeight;
    
    public ScrollViewHelper (ScrollView scrollView) {
    	mScrollView = scrollView;
    }
    
    public void showContentView (View title, View content, int contentTopInScrollView, int contentHeight) {
    	if (mScroller == null) {
        	mScroller = new ScrollHelper(mScrollView);
    	}
        if (content.getVisibility() != View.VISIBLE)content.setVisibility(View.VISIBLE);
    	final int paddingTop = mScrollView.getPaddingTop();
    	final int scrollViewHeight = mScrollView.getHeight() - paddingTop;

    	final int titleTop = UiUtil.getViewTopInParent(title, mScrollView);
    	final int bottom = contentTopInScrollView + contentHeight;
    	if (titleTop < paddingTop) {
        	mScroller.stop(false);
    		mScroller.startUsingDistance(titleTop - paddingTop, ScrollHelper.ORIENTATION_VERTICAL, DURATION);
    	} else if (titleTop > paddingTop && bottom > scrollViewHeight) {
        	mScroller.stop(false);
    		final int distance = bottom - scrollViewHeight;
    		final int adjust = titleTop - paddingTop - distance;
    		mScroller.startUsingDistance(adjust<0?(distance+adjust-paddingTop):distance, ScrollHelper.ORIENTATION_VERTICAL, DURATION);
    	} else {
        	if (mViewExpander == null) {
        		mViewExpander = new ViewScaleHelper(content.getContext());
        	}
        	mViewExpander.stop(true);

            if (content.getVisibility() != View.VISIBLE)content.setVisibility(View.VISIBLE);
        	mViewExpander.setView(content);
            LayoutParams params = content.getLayoutParams();
            mExpandViewLayoutParamHeight = params.height;
            
            params.height = 0;
            if (mExpandCallback == null) {
            	mExpandCallback = new ExpandCallback();
            }
        	mViewExpander.startScale(0, contentHeight);
        	mViewExpander.setCallback(mExpandCallback);
    	}
    }
    
    public void hideContentView (View title, View content) {
    	if (mViewShrinker == null) {
    		mViewShrinker = new ViewScaleHelper(content.getContext());
    	}
    	mViewShrinker.stop(true);

        if (content.getVisibility() != View.VISIBLE)content.setVisibility(View.VISIBLE);
    	mViewShrinker.setView(content);
    	final int fromDimension = content.getHeight();
        LayoutParams params = content.getLayoutParams();
        mShrinkViewLayoutParamHeight = params.height;
        
        params.height = fromDimension;
        if (mShrinkCallback == null) {
        	mShrinkCallback = new ShrinkCallback();
        }
    	mViewShrinker.startScale(fromDimension, 0);
    	mViewShrinker.setCallback(mShrinkCallback);
    }

    ExpandCallback mExpandCallback;
    class ExpandCallback implements ViewScaleHelper.Callback {
		@Override
		public void onFinished(View v) {
	        LayoutParams params = v.getLayoutParams();
	        params.height = mExpandViewLayoutParamHeight;
			v.setLayoutParams(params);
		}
    	
    }

    ShrinkCallback mShrinkCallback;
    class ShrinkCallback implements ViewScaleHelper.Callback {
		@Override
		public void onFinished(View v) {
			v.setVisibility(View.GONE);
	        LayoutParams params = v.getLayoutParams();
	        params.height = mShrinkViewLayoutParamHeight;
			v.setLayoutParams(params);
		}
    	
    }
}
