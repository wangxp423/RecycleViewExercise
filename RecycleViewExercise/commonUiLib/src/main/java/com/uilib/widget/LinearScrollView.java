package com.uilib.widget;

import com.uilib.UiUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

public class LinearScrollView extends ScrollView {
	private int mFirstVisibleItem, mLastVisibleItem, mTotalItemCount;
	private LinearScrollView.OnScrollListener mScrollListener;
    
    public static interface OnScrollListener {
    	void onScroll(LinearScrollView view, int firstVisibleItem, int lastVisibleItem, int totalItemCount);
    	void onScroll(LinearScrollView view, int scrollY, int oldScrollY);
    }

    public LinearScrollView(Context context) {
        super(context);
    }

    public LinearScrollView(Context context, AttributeSet attrs) {
    	super(context, attrs);
    }

    public LinearScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
	
	public void setOnScrollListener (LinearScrollView.OnScrollListener listener) {
		mScrollListener = listener;
		mFirstVisibleItem = -1;
		mLastVisibleItem = -1;
		mTotalItemCount = -1;
	}
	
	public View getContentChild (int index) {
		if (getChildCount() == 0) return null;
		ViewGroup content = (ViewGroup)getChildAt(0);

		final int count = content.getChildCount();
		if (index < 0 || index >= count) return null;
		
		return content.getChildAt(index);
	}

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
		ViewGroup content = null;
		View child = null;
		int top = 0;
        if (getChildCount() > 0) {
        	content = (ViewGroup)getChildAt(0);

			final int count = content.getChildCount();
        	if (mFirstVisibleItem >= 0 && mFirstVisibleItem < count) {
        		if (mLastVisibleItem > mFirstVisibleItem && mFirstVisibleItem < count) {
            		child = content.getChildAt(mFirstVisibleItem + 1);
            		int topInScroollView = UiUtil.getViewTopInParent(child, content) - getScrollY();
            		if (topInScroollView > getHeight() >> 1) {
            			child = content.getChildAt(mFirstVisibleItem);
            		}
        		} else {
            		child = content.getChildAt(mFirstVisibleItem);
        		}
        		top = UiUtil.getViewTopInParent(child, content);
        	}
        }
//        if (top > 0) {
//        	System.out.println("index = " + content.indexOfChild(child) + "; top = " + top + "; scrollY = " + getScrollY());
//        }
        super.onLayout(changed, l, t, r, b);
//        System.out.println("onLayout: " + changed + "; " + l + "," + t + "," + r + "," + b);
        //	处理滚动的同时，又更改内存大小，导致位置跳跃的问题
        if (top > 0) {
			final int newTop = UiUtil.getViewTopInParent(child, content);
			if (top < newTop) {
				scrollBy(0, newTop - top);
			}
//			System.out.println("index = " + content.indexOfChild(child) + "; newTop = " + newTop + "; scrollY = " + getScrollY());
        }
        
		compute(getScrollY(), getScrollY());
    }
    
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		compute(t, oldt);
	}
	
	private void compute (int scrollY, int oldScrollY) {
		if (mScrollListener != null) {
			if (getChildCount() == 0) return;
			ViewGroup content = (ViewGroup)getChildAt(0);

			final int count = content.getChildCount();
			if (count == 0) return;
			mScrollListener.onScroll(this, scrollY, oldScrollY);
			
			final int scrollTop = scrollY, scrollBottom = scrollY + getHeight();
//			System.out.println("scrollTop: " + scrollTop + "; scrollBottom: " + scrollBottom);
			int firstVisibleItem = -1, lastVisibleItem = 0;
			for (int i=0;i<count;i++) {
				View child = content.getChildAt(i);
				final int top = UiUtil.getViewTopInParent(child, content);
				final int bottom = top + child.getHeight();
//				System.out.println(i + "; top = " + top + "; bottom = " + bottom);
				if (top < bottom && bottom >= scrollTop && top <= scrollBottom && child.getVisibility() != View.GONE) {
					if (firstVisibleItem < 0) firstVisibleItem = i;
					lastVisibleItem = i;
				}
			}
//			System.out.println("onScrollChanged: firstVisibleItem = " + firstVisibleItem + "; lastVisibleItem = " + lastVisibleItem + "; totalItemCount = " + count);
			
			if (mFirstVisibleItem != firstVisibleItem
				|| mLastVisibleItem != lastVisibleItem
				|| mTotalItemCount != count) {
				mScrollListener.onScroll(this, firstVisibleItem, lastVisibleItem, count);
			}
			mFirstVisibleItem = firstVisibleItem;
			mLastVisibleItem = lastVisibleItem;
			mTotalItemCount = count;
		}
	}
	
	public void endScroll () {
        final long now = android.os.SystemClock.uptimeMillis();
		MotionEvent event = MotionEvent.obtain(now, now, MotionEvent.ACTION_DOWN, 0.0f, 0.0f, 0);
//        event.setSource(android.view.InputDevice.SOURCE_TOUCHSCREEN);
        super.onTouchEvent(event);
        event.recycle();
        mBeingTouched = false;
	}

	private boolean mBeingTouched;
	
	public boolean isBeingTouched () {
		return mBeingTouched;
	}

	@Override
    public boolean onTouchEvent(MotionEvent e) {
		switch (e.getActionMasked()) {
		case MotionEvent.ACTION_MOVE:{
			mBeingTouched = true;
		}break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL: {
			mBeingTouched = false;
			if (mScrollListener != null) {
				mScrollListener.onScroll(this, getScrollY(), getScrollY());
			}
		}break;
		}
		return super.onTouchEvent(e);
	}
}
