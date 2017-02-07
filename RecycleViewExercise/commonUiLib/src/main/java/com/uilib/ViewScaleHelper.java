package com.uilib;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Scroller;

public class ViewScaleHelper implements Runnable{
	
	public static final int DURATION	= 300;
	public static final int ORIENTATION_HORIZONTAL	= 0;
	public static final int ORIENTATION_VERTICAL	= 1;
    /**
     * Tracks the decay of a fling scroll
     */
    protected Scroller mScroller;
    
    /**
     * X value reported by mScroller on the previous fling
     */
    private int mLastFlingX;
    
    private int mOrientation;	//	移动方向
    private int mToDimension;	//	最终尺寸
    /**
     * The view that is scrolled
     */
    private View mView;
    
    private Callback mCallback;
    
    public interface Callback {
    	void onFinished (View v);
    }
    
    public void setCallback (Callback callBack) {
    	mCallback = callBack;
    }
    
    public ViewScaleHelper (Context context) {
    	if (mScroller == null) {
            mScroller = new Scroller(context);
    	}
    }

    public ViewScaleHelper (View view) {
        setView(view);
    	if (mScroller == null) {
            mScroller = new Scroller(view.getContext());
    	}
    }
    
    public void setView (View view) {
        mView = view;
    }
    public View getView () {return mView;}
    
    public boolean isFinished () {
    	if (mScroller != null) {
            return mScroller.isFinished();
    	}
    	return false;
    }
    
    public void startScale(int fromDimension, int toDimension) {
    	startScale(fromDimension, toDimension, ORIENTATION_VERTICAL);
    }

    public void startScale(int fromDimension, int toDimension, int orientation) {
    	startScale(fromDimension, toDimension, orientation, DURATION);
    }

    /**
     * 
     * @param distance	目标点减去当前点的值
     * @param duration	动画所需的时间
     */
    public void startScale(int fromDimension, int toDimension, int orientation, int duration) {
        if (fromDimension == toDimension) return;
        if (toDimension < 0) toDimension = 0;
        mOrientation = orientation;
        mView.removeCallbacks(this);
        if (mView.getVisibility() != View.VISIBLE) mView.setVisibility(View.VISIBLE);

        LayoutParams params = mView.getLayoutParams();
        switch (mOrientation) {
        case ORIENTATION_HORIZONTAL:{
        	params.width = fromDimension;
        }break;
        case ORIENTATION_VERTICAL:{
        	params.height = fromDimension;
        }break;
        }
        mLastFlingX = 0;
        mScroller.startScroll(0, 0, toDimension - fromDimension, 0, duration);
        mView.post(this);
    }

    public void stop(boolean scrollIntoSlots) {
    	if (mView != null) {
        	mView.removeCallbacks(this);
        	if (!mScroller.isFinished()) endFling(scrollIntoSlots);
    	}
    }
    
    public void endFling(boolean force) {
        if (force) {
            final Scroller scroller = mScroller;
            mScroller.forceFinished(true);

        	final int x = scroller.getFinalX();
            final int delta = x - mLastFlingX;

        	if (delta != 0) {
        		if (mToDimension == 0) {
        			mView.setVisibility(View.GONE);
        		}
                LayoutParams params = mView.getLayoutParams();
                switch (mOrientation) {
                case ORIENTATION_HORIZONTAL:{
                	params.width += delta;
                }break;
                case ORIENTATION_VERTICAL:{
                	params.height += delta;
                }break;
                }
        		mView.setLayoutParams(params);
        	}
            if (mCallback != null) {
            	mCallback.onFinished(mView);
            }
        }
    }
	
	@Override
	public void run () {
        
        final Scroller scroller = mScroller;
        boolean more = scroller.computeScrollOffset();
        final int x = scroller.getCurrX();

        final int delta = x - mLastFlingX;

        LayoutParams params = mView.getLayoutParams();
        switch (mOrientation) {
        case ORIENTATION_HORIZONTAL:{
        	params.width += delta;
        }break;
        case ORIENTATION_VERTICAL:{
        	params.height += delta;
        }break;
        }

        mView.setLayoutParams(params);

        mLastFlingX = x;
        if (!more || (scroller.getCurrX() == scroller.getFinalX())) {
            endFling(true);
        } else if (more) {
        	mView.post(this);
        }
	}
}
