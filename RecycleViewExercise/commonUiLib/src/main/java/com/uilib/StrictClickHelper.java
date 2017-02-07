package com.uilib;

import android.view.MotionEvent;
import android.view.View;

/**
 * 严格的click检测，手指不能移动过大距离
 * 因为使用了 OnTouchListener接口，所以对于已经set了touch listener的view不适用此方法
 * 
 * @author MasterWang
 *
 */
public class StrictClickHelper implements View.OnTouchListener {
	
	public static interface OnClickListener {
		public void onClick (View view);
	}
	
	private int mTouchSlopSquare;
	private View mView;
	OnClickListener mOnClickListener;

	public StrictClickHelper (View view, OnClickListener listener) {
		mView = view;
		mOnClickListener = listener;
		view.setOnTouchListener(this);
		ViewConfiguration config = ViewConfiguration.get(view.getContext());
		mTouchSlopSquare = config.getTouchSlopSquare();
	}
	
	public static void dealwith (View view, OnClickListener listener) {
		new StrictClickHelper(view, listener);
	}

	private float mDownX, mDownY;
	@Override
	public boolean onTouch(View v, MotionEvent event) {

		final View view = mView;
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            	if (view.isPressed()) {
            		performClick();

            		view.setPressed(false);
                }
                break;

            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                view.setPressed(true);
                return true;

            case MotionEvent.ACTION_CANCEL:
            	view.setPressed(false);
                break;

            case MotionEvent.ACTION_MOVE:
                final int deltax = (int)(event.getX() - mDownX);
                final int deltay = (int)(event.getY() - mDownY);

                // Be lenient about moving outside of buttons
                int slopSquare = mTouchSlopSquare;
                if (slopSquare < (deltax * deltax + deltay * deltay)) {
                	view.setPressed(false);
                }
                break;
        }
		return false;
	}
	
	private void performClick () {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(mView);
        }

	}
}
