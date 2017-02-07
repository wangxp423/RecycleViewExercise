package com.uilib;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

public class SlidingDrawerHelper implements View.OnClickListener, View.OnTouchListener {
	
    private Context mContext;
    private View mOpenHandle;
    private View mCloseHandle;
    private View mContent;
    private Animation mOpenHandleShowAnim, mCloseHandleShowAnim;
    private int mOpenHandleWidth, mOpenHandleHeight, mCloseHandleWidth, mCloseHandleHeight;
    private int mContentWidth, mContentHeight;
    private ViewScaleHelper mViewShrinker, mViewExpander;
    
    int mGravity = Gravity.BOTTOM;
    
    public SlidingDrawerHelper (Context context) {
    	mContext = context;
    	mViewExpander = new ViewScaleHelper(context);
    	mViewShrinker = new ViewScaleHelper(context);

    	mExpandCallback = new ExpandCallback();
    	mShrinkCallback = new ShrinkCallback();
    	initGesture (context);
    }
    
    public void setGravity (int gravity) {
    	if (gravity != mGravity) {
    		if (mContent != null) {
    			setContent(mContent, mContentWidth, mContentHeight);
    		}
    	}
    	mGravity = gravity;
    }

    public void setOpenHandle (View handle, int width, int height) {
    	mOpenHandle = handle;
    	mOpenHandle.setVisibility(View.VISIBLE);
    	mOpenHandle.setOnClickListener(this);
    	mOpenHandle.setOnTouchListener(this);
    }
    public void setCloseHandle (View handle, int width, int height) {
    	mCloseHandle = handle;
    	mCloseHandle.setVisibility(View.INVISIBLE);
    	mCloseHandle.setOnClickListener(this);
    	mCloseHandle.setOnTouchListener(this);
    }
    public void setOpenHandleShowAnimaiton (Animation animation) {
    	mOpenHandleShowAnim = animation;
    }
    public void setCloseHandleShowAnimaiton (Animation animation) {
    	mCloseHandleShowAnim = animation;
    }
    public void setContent (View content, int width, int height) {
    	mContent = content;
    	mContentWidth = width;
    	mContentHeight = height;
    	ViewGroup.LayoutParams params = mContent.getLayoutParams();
    	switch (mGravity) {
    	case Gravity.TOP:
    	case Gravity.BOTTOM:params.height = 0;break;
    	case Gravity.LEFT:
    	case Gravity.RIGHT:params.width = 0;break;
    	}
		mContent.setLayoutParams(params);
    }
    
    private void expand (int fromDimension, int toDimension, int orientation) {
        mViewExpander.stop(true);
    	mViewExpander.setView(mContent);
    	mViewExpander.startScale(fromDimension, toDimension, orientation);

    	mViewExpander.setCallback(mExpandCallback);
    }
    private void shrink (int fromDimension, int toDimension, int orientation) {
    	mViewShrinker.stop(true);
    	mViewShrinker.setView(mContent);
    	mViewShrinker.startScale(fromDimension, toDimension, orientation);

    	mViewShrinker.setCallback(mShrinkCallback);
    }
    
    @Override
    public void onClick (View v) {
    	if (v == mOpenHandle) {
    		if (mContent == null) return;
            
    		ViewGroup.LayoutParams params = mContent.getLayoutParams();
    		switch (mGravity) {
        	case Gravity.TOP:
        	case Gravity.BOTTOM:{
                params.height = 0;
                expand(0, mContentHeight, ViewScaleHelper.ORIENTATION_VERTICAL);
        	}break;
        	case Gravity.LEFT:
        	case Gravity.RIGHT:{
                params.width = 0;
                expand(0, mContentWidth, ViewScaleHelper.ORIENTATION_HORIZONTAL);
        	}break;
        	}
    	} else if (v == mCloseHandle) {
    		if (mContent == null) return;
            
    		ViewGroup.LayoutParams params = mContent.getLayoutParams();
    		switch (mGravity) {
        	case Gravity.TOP:
        	case Gravity.BOTTOM:{
                params.height = mContentHeight;
                shrink(mContentHeight, 0, ViewScaleHelper.ORIENTATION_VERTICAL);
        	}break;
        	case Gravity.LEFT:
        	case Gravity.RIGHT:{
                params.width = mContentWidth;
                shrink(mContentWidth, 0, ViewScaleHelper.ORIENTATION_HORIZONTAL);
        	}break;
        	}
    	}
    }

    private VelocityTracker mVelocityTracker;
    private boolean mDraging;
	private float mDownX, mDownY;
	private float mPointerMotionX0, mPointerMotionY0;
	private int mActivePointerId;
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumMinorVelocity;
    private int mVelocityUnits;
    
    private void initGesture (Context context) {
        ViewConfiguration config = ViewConfiguration.get(mContext);
        mTouchSlop = config.getTouchSlop();
        mMinimumVelocity = config.getMaximumMinorVelocity();	//	config.getMinimumFlingVelocity();
        mMaximumMinorVelocity = config.getMaximumMinorVelocity();
        mVelocityUnits = config.getVelocityUnits();
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        final float y = ev.getRawY();
        final float x = ev.getRawX();
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        switch (action) {
        case MotionEvent.ACTION_DOWN:{
            mActivePointerId = ev.getPointerId(0);
        	mDraging = false;
            mDownX = mPointerMotionX0 = x;
            mDownY = mPointerMotionY0 = y;
        }break;
        case MotionEvent.ACTION_MOVE:{
        	final int pointerIndex = ev.findPointerIndex(mActivePointerId);
        	if (pointerIndex < 0) {
        		mDraging = false;break;
        	}
        	
        	if (!mDraging) {
        		final int deltaX = (int) (x - mDownX);
        		final int deltaY = (int) (y - mDownY);
                final boolean vertical = mGravity==Gravity.TOP || mGravity==Gravity.BOTTOM;
                if (Math.abs(vertical?deltaY:deltaX) > mTouchSlop) {
            		dragContentView(mContent, deltaX, deltaY, mGravity);
            		mDraging = true;
    			}
        	} else {
        		final int deltaX = (int) (x - mPointerMotionX0);
        		final int deltaY = (int) (y - mPointerMotionY0);
        		dragContentView(mContent, deltaX, deltaY, mGravity);
        	}
        	mPointerMotionX0 = x;
        	mPointerMotionY0 = y;
        }break;
        case MotionEvent.ACTION_UP:{
            final VelocityTracker velocityTracker = mVelocityTracker;
            velocityTracker.computeCurrentVelocity(mVelocityUnits, mMaximumMinorVelocity);

            final int gravity = mGravity;
            float velocity;

            final boolean vertical = gravity==Gravity.TOP || gravity==Gravity.BOTTOM;
            if (vertical) {
            	velocity = velocityTracker.getYVelocity();
            } else {
            	velocity = velocityTracker.getXVelocity();
            }

            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
        	ViewGroup.LayoutParams params = mContent.getLayoutParams();
    		switch (gravity) {
        	case Gravity.TOP:
        	case Gravity.BOTTOM:{
        		final int height = params.height;
        		if (height > 0 && height < mContentHeight) {
        			if (Math.abs(velocity) < mMinimumVelocity) {
        				final int oneforuth = mContentHeight / 4;
            			if (height <= oneforuth) {
            				shrink(height, 0, ViewScaleHelper.ORIENTATION_VERTICAL);
            			} else if (height >= mContentHeight - oneforuth) {
            				expand(height, mContentHeight, ViewScaleHelper.ORIENTATION_VERTICAL);
            			} else {
                    		if (y > mDownY) {
                    			if (gravity == Gravity.TOP) {
                        			expand(height, mContentHeight, ViewScaleHelper.ORIENTATION_VERTICAL);
                    			} else {
                        			shrink(height, 0, ViewScaleHelper.ORIENTATION_VERTICAL);
                    			}
                    		} else {
                    			if (gravity == Gravity.TOP) {
                    				shrink(height, 0, ViewScaleHelper.ORIENTATION_VERTICAL);
                    			} else {
                    				expand(height, mContentHeight, ViewScaleHelper.ORIENTATION_VERTICAL);
                    			}
                    		}
            			}
        			} else {
        				if ((velocity > 0 && gravity == Gravity.TOP)
        						|| (velocity < 0 && gravity == Gravity.BOTTOM)) {
            	            params.height = height;
            	            expand(height, mContentHeight, ViewScaleHelper.ORIENTATION_VERTICAL);
            			} else {
            	            shrink(height, 0, ViewScaleHelper.ORIENTATION_VERTICAL);
            			}
        			}
        			return true;
        		}
        	}break;
        	case Gravity.LEFT:
        	case Gravity.RIGHT:{
        		final int width = params.width;
        		if (width > 0 && width < mContentWidth) {
        			if (Math.abs(velocity) < mMinimumVelocity) {
        				final int oneforuth = mContentHeight / 4;
            			if (width <= oneforuth) {
            				shrink(width, 0, ViewScaleHelper.ORIENTATION_HORIZONTAL);
            			} else if (width >= mContentHeight - oneforuth) {
            				expand(width, mContentHeight, ViewScaleHelper.ORIENTATION_HORIZONTAL);
            			} else {
                    		if (x > mDownX) {
                    			if (gravity == Gravity.LEFT) {
                        			expand(width, mContentHeight, ViewScaleHelper.ORIENTATION_HORIZONTAL);
                    			} else {
                        			shrink(width, 0, ViewScaleHelper.ORIENTATION_HORIZONTAL);
                    			}
                    		} else {
                    			if (gravity == Gravity.LEFT) {
                    				shrink(width, 0, ViewScaleHelper.ORIENTATION_HORIZONTAL);
                    			} else {
                    				expand(width, mContentHeight, ViewScaleHelper.ORIENTATION_HORIZONTAL);
                    			}
                    		}
            			}
        			} else {
        				if ((velocity > 0 && gravity == Gravity.LEFT)
        						|| (velocity < 0 && gravity == Gravity.RIGHT)) {
            	        	expand(width, mContentWidth, ViewScaleHelper.ORIENTATION_HORIZONTAL);
            			} else {
            	            shrink(width, 0, ViewScaleHelper.ORIENTATION_HORIZONTAL);
            			}
        			}
        			return true;
        		}
        	}break;
        	}
        }break;
        default:{
        }break;
        }
    	if (mDraging) return true;
		return false;
		
	}
    
    private void dragContentView (View content, final int deltaX, final int deltaY, final int gravity) {
    	ViewGroup.LayoutParams params = content.getLayoutParams();
		switch (gravity) {
    	case Gravity.TOP:{
    		int height = params.height;
    		height += deltaY;
    		if (height < 0) height = 0;
    		params.height = height;
    	}break;
    	case Gravity.BOTTOM:{
    		int height = params.height;
    		height -= deltaY;
    		if (height < 0) height = 0;
    		params.height = height;
    	}break;
    	case Gravity.LEFT:{
    		int width = params.width;
    		width += deltaX;
    		if (width < 0) width = 0;
    		params.width = width;
    	}break;
    	case Gravity.RIGHT:{
    		int width = params.width;
    		width -= deltaX;
    		if (width < 0) width = 0;
    		params.width = width;
    	}break;
    	}
    	if (content.getVisibility() != View.VISIBLE) content.setVisibility(View.VISIBLE);
    	content.setLayoutParams(params);
    }
    
    ExpandCallback mExpandCallback;
    class ExpandCallback implements ViewScaleHelper.Callback {
		@Override
		public void onFinished(View v) {
    		ViewGroup.LayoutParams params = v.getLayoutParams();
    		switch (mGravity) {
        	case Gravity.TOP:
        	case Gravity.BOTTOM:{
        		params.height = mContentHeight;
        	}break;
        	case Gravity.LEFT:
        	case Gravity.RIGHT:{
        		params.width = mContentWidth;
        	}break;
        	}
    		v.setLayoutParams(params);
    		mOpenHandle.setVisibility(View.GONE);
    		mCloseHandle.setVisibility(View.VISIBLE);
    		if (mCloseHandleShowAnim != null && mCloseHandle.getVisibility() != View.VISIBLE) {
    			mCloseHandle.startAnimation(mCloseHandleShowAnim);
    		}
		}
    	
    }

    ShrinkCallback mShrinkCallback;
    class ShrinkCallback implements ViewScaleHelper.Callback {
		@Override
		public void onFinished(View v) {
			v.setVisibility(View.GONE);
    		ViewGroup.LayoutParams params = v.getLayoutParams();
    		switch (mGravity) {
        	case Gravity.TOP:
        	case Gravity.BOTTOM:{
        		params.height = 0;
        	}break;
        	case Gravity.LEFT:
        	case Gravity.RIGHT:{
        		params.width = 0;
        	}break;
        	}
    		v.setLayoutParams(params);
    		mOpenHandle.setVisibility(View.VISIBLE);
    		mCloseHandle.setVisibility(View.GONE);
    		if (mOpenHandleShowAnim != null && mOpenHandle.getVisibility() != View.VISIBLE) {
    			mOpenHandle.startAnimation(mOpenHandleShowAnim);
    		}
		}
    	
    }
}
