package com.uilib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uilib.R;
import com.uilib.ViewConfiguration;

/**
 * 通讯录效果
 * @author wangwenguan
 *
 */
public class AddressBookView extends RelativeLayout implements OnClickListener,
															AbsListView.OnScrollListener {
	
	private ListView mListView;
	private View mLabelView;
    private ViewGroup mLabelViewContainer;
	private ViewGroup mShortcutView;
	private Rect mTempRect;
	private boolean mShortcutEnable;
	private int mShortcutItemLayout;
	private int mShortcutItemLabelViewId;
	private int mListViewId, mLabelViewId, mShortcutViewId;
	private int mLabelMarginTop, mLabelMarginBottom, mLabelMarginRight;
	private int mTotalItemCount;
	private int mCurLabelPosition = -1;
	private int[] mLabelPostions;
	private int mLabelCount;

    public AddressBookView(Context context) {
        super(context);
        initGesture(context);
    }
    
    public AddressBookView(Context context, AttributeSet attrs) {
    	this(context, attrs, 0);
    }
    
    public AddressBookView(Context context, AttributeSet attrs, int defStyleAttr) {
    	super(context, attrs, defStyleAttr);
		if (attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AddressBookView);
			final int N = typedArray.getIndexCount();
	        for (int i = 0; i < N; i++) {
	            int attr = typedArray.getIndex(i);
	            if (attr == R.styleable.AddressBookView_listView) {
	            	mListViewId = typedArray.getResourceId(attr, View.NO_ID);
	            } else if (attr == R.styleable.AddressBookView_labelView) {
	            	mLabelViewId = typedArray.getResourceId(attr, View.NO_ID);
	            } else if (attr == R.styleable.AddressBookView_shortcutView) {
	            	mShortcutViewId = typedArray.getResourceId(attr, View.NO_ID);
	            } else if (attr == R.styleable.AddressBookView_shortcutItemLayout) {
	            	mShortcutItemLayout = typedArray.getResourceId(attr, View.NO_ID);
	            } else if (attr == R.styleable.AddressBookView_shortcutItemLabelViewId) {
	            	mShortcutItemLabelViewId = typedArray.getResourceId(attr, View.NO_ID);
	            } else if (attr == R.styleable.AddressBookView_shortcutEnable) {
	            	mShortcutEnable = typedArray.getBoolean(attr, false);
	            } else if (attr == R.styleable.AddressBookView_shortcutLayoutMarginTop) {
	            	mLabelMarginTop = typedArray.getDimensionPixelSize(attr, 0);
	            } else if (attr == R.styleable.AddressBookView_shortcutLayoutMarginBottom) {
	            	mLabelMarginBottom = typedArray.getDimensionPixelSize(attr, 0);
	            } else if (attr == R.styleable.AddressBookView_shortcutLayoutMarginRight) {
	            	mLabelMarginRight = typedArray.getDimensionPixelSize(attr, 0);
	            }
	        }
			typedArray.recycle();
		}
        initGesture(context);
    }
    
    @Override
    protected void onFinishInflate () {
    	if (mListView == null && mListViewId > 0) {
    		mListView = (ListView)findViewById(mListViewId);
    		mListView.setOnScrollListener(this);
    	}
    	if (mLabelViewId > 0) {
    		mLabelView = findViewById(mLabelViewId);
            final int index = indexOfChild(this);
            removeViewInLayout(mLabelView);
    		mLabelViewContainer = new FrameLayout(getContext());
    		addView(mLabelViewContainer, index, mLabelView.getLayoutParams());
    		mLabelViewContainer.addView(mLabelView);
    	}
    	if (mShortcutViewId > 0) {
			mShortcutView = (ViewGroup)findViewById(mShortcutViewId);
		}
    }
    
    public void setListView (ListView listView) {
    	mListView = listView;
		mListView.setOnScrollListener(this);
    }
    
    /**
     * 记录所有label项在listview中的position
     * @param postions
     * @param count	label项的总数
     */
    public void setLabelPositions (int[] postions, String[] labels, int count) {
    	mLabelPostions = postions;
    	mLabelCount = count;
    	if ((mShortcutEnable || mShortcutViewId > 0) && mShortcutItemLayout > 0 && mShortcutItemLabelViewId > 0) {
    		if (mShortcutView == null) {
        		if (mShortcutViewId > 0) {
        			mShortcutView = (ViewGroup)findViewById(mShortcutViewId);
        		} else {
            		mShortcutView = new LinearLayout(getContext());
            		((LinearLayout)mShortcutView).setOrientation(LinearLayout.VERTICAL);
            		((LinearLayout)mShortcutView).setGravity(1);
        			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -1);
        			params.topMargin = mLabelMarginTop;
        			params.bottomMargin = mLabelMarginBottom;
        			params.rightMargin = mLabelMarginRight;
        			params.alignWithParent = true;
        			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        			params.addRule(RelativeLayout.CENTER_VERTICAL);
        			addView(mShortcutView, params);
        		}
    		}
    		mShortcutView.removeAllViews();
    		for (int i=0;i<count;i++) {
    			LayoutInflater inflater = LayoutInflater.from(getContext());
    			View view = inflater.inflate(mShortcutItemLayout, mShortcutView, false);
    			mShortcutView.addView(view);
    			TextView label = (TextView)view.findViewById(mShortcutItemLabelViewId);
    			label.setText(labels[i]);
    			
    			view.setTag(postions[i]);
    			view.setOnClickListener(this);
    		}
    	}
    }
    
    @Override
    public void onClick (View v) {
    	Object tag = v.getTag();
    	if (tag == null || !(tag instanceof Integer)) return;
    	
    	int position = (Integer)tag;
    	if (mListView != null) {
            mListView.setSelectionFromTop(position, 0);
    	}
    }

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
//		System.out.println("onScrollStateChanged : " + scrollState);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//		System.out.println("onScroll: " + firstVisibleItem + ", " + visibleItemCount + ", " + totalItemCount + "; " + mCurLabelPosition + ", " + mTotalItemCount);
		final int realFirst = firstVisibleItem;
        if (mLabelCount <= 0 || mLabelPostions == null) return;
		if (totalItemCount > 0 && mLabelView != null 
				&& mOnLabelChangedListener != null) {
	        int index = 0;
	        do {
	            View child = view.getChildAt(index);
	            if (child == null) break;
	            final int bottom = child.getBottom();
	            if (bottom < 0) {
	                index ++;
	            } else {
	                firstVisibleItem += index;
	                break;
	            }
	        } while (true);
	        int labelPostion = -1;
	        int nextLabelPostion = mCurLabelPosition;
	        for (int i=1;i<mLabelCount;i++) {
	            if (firstVisibleItem >= mLabelPostions[i-1] && firstVisibleItem < mLabelPostions[i]) {
	                labelPostion = mLabelPostions[i-1];
	                nextLabelPostion = mLabelPostions[i];break;
	            }
	        }
	        if (labelPostion == -1) {
                labelPostion = mLabelPostions[mLabelCount - 1];
                nextLabelPostion = totalItemCount;
            }
	        if (labelPostion >= 0 && (mCurLabelPosition != labelPostion || mTotalItemCount != totalItemCount)) {
	            mOnLabelChangedListener.onLabelChanged(mLabelView, labelPostion);
	            mCurLabelPosition = labelPostion;
	        }
	        if (mLabelViewContainer != null) {
                View child = null;
                if (nextLabelPostion >= realFirst && nextLabelPostion < realFirst + visibleItemCount) {
                    child = view.getChildAt(nextLabelPostion - realFirst);
                }
                if (child == null || child.getTop() >= mLabelViewContainer.getHeight()) {
                    mLabelViewContainer.scrollTo(0, 0);
                } else {
                    mLabelViewContainer.scrollTo(0, mLabelViewContainer.getHeight() - child.getTop());
                }
	        }
	        mTotalItemCount = totalItemCount;
		}
	}
	
	public static interface OnLabelChangedListener {
		void onLabelChanged (View label, int position);
	}
	OnLabelChangedListener mOnLabelChangedListener;
	public void setOnLabelChangedListener (OnLabelChangedListener listener) {
		mOnLabelChangedListener = listener;
	}
    
//    @Override
//    public void onLayout (boolean changed, int left, int top, int right, int bottom) {
//    	System.out.println("onLayout : " + changed + "; " + left + "," + top + "," + right + "," + bottom);
//    }
    
    private boolean startFromShortcut;
    private float mDownX, mDownY;
    private int mDownPointerId;
    private int mTouchSlop;
    
    private void initGesture (Context context) {
        ViewConfiguration config = ViewConfiguration.get(getContext());
        mTouchSlop = config.getTouchSlop();
        mTempRect = new Rect();
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final float y = ev.getY();
        final float x = ev.getX();
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;

        switch (action) {
        case MotionEvent.ACTION_DOWN:{
            mDownX = x;
            mDownY = y;
            mDownPointerId = ev.getPointerId(0);
            startFromShortcut = false;
    		if (mShortcutView != null) {
    	        final Rect frame = mTempRect;
    	        mShortcutView.getHitRect(frame);
    	        if (frame.contains((int)x, (int)y)) {
    	        	startFromShortcut = true;
    	        }
    		}
        }break;
        case MotionEvent.ACTION_MOVE:{
            if (!startFromShortcut) break;
            final int pointerIndex = ev.findPointerIndex(mDownPointerId);
            if (pointerIndex >= 0) {
                final int deltaY = (int) Math.abs(ev.getY(pointerIndex) - mDownY);
                if (deltaY > mTouchSlop) {
                    requestDisallowInterceptTouchEvent(true);
                    return true;
                }
            }
        }break;
        }
    	return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final float y = ev.getY();
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;

        switch (action) {
        case MotionEvent.ACTION_MOVE:
        case MotionEvent.ACTION_UP:{
            if (startFromShortcut) {
                final Rect frame = mTempRect;
                final int count = mShortcutView.getChildCount();
                for (int i=count-1;i>=0;i--) {
                    View child = mShortcutView.getChildAt(i);
                    child.getHitRect(frame);
                    final int py = (int)y - mShortcutView.getTop();
                    if (py > frame.top && py < frame.bottom) {
                        Object tag = child.getTag();
                        if (tag == null || !(tag instanceof Integer)) return false;
                        
                        int position = (Integer)tag;
                        if (mListView != null) {
                            mListView.setSelectionFromTop(position, 0);
                        }
                        return true;
                    }
                }
            }
        }break;
        }
		return false;
	}
    
}
