package com.uilib;

import android.app.Activity;
import android.os.Build;
import android.view.View;

/**
 * A utility class that helps with showing and hiding system UI such as the
 * status bar and navigation/system bar. This class uses backward-compatibility
 * techniques described in <a href=
 * "http://developer.android.com/training/backward-compatible-ui/index.html">
 * Creating Backward-Compatible UIs</a> to ensure that devices running any
 * version of android OS are supported. 
 * <p>
 * <b><em>if you use this feature, must not apply full-screen theme to the activty.</em><b>
 * <p>
 * For more on system bars, see <a href=
 * "http://developer.android.com/design/get-started/ui-overview.html#system-bars"
 * > System Bars</a>.
 * 
 * @see android.view.View#setSystemUiVisibility(int)
 * @see android.view.WindowManager.LayoutParams#FLAG_FULLSCREEN
 */
public class SystemUiHider {
    /**
     * The activity associated with this UI hider object.
     */
    protected Activity mActivity;

    /**
     * The view on which {@link View#setSystemUiVisibility(int)} will be called.
     */
    protected View mAnchorView;

    /**
     * Whether or not the system UI is currently visible. This is a cached value
     * from calls to {@link #hide()} and {@link #show()}.
     */
    private boolean mVisible = true;

    /**
     * The current visibility callback.
     */
    protected OnVisibilityChangeListener mOnVisibilityChangeListener;

    /**
     * Creates and returns an instance of {@link SystemUiHider} that is
     * appropriate for this device. The object will be either a
     * {@link SystemUiHiderBase} or {@link SystemUiHiderHoneycomb} depending on
     * the device.
     * 
     * @param activity The activity whose window's system UI should be
     *            controlled by this class.
     * @param anchorView The view on which
     *            {@link View#setSystemUiVisibility(int)} will be called.
     * @param flags Either 0 or any combination of {@link #FLAG_FULLSCREEN},
     *            {@link #FLAG_HIDE_NAVIGATION}, and
     *            {@link #FLAG_LAYOUT_IN_SCREEN_OLDER_DEVICES}.
     */
    public SystemUiHider(Activity activity) {
        mActivity = activity;
        activity.getWindow().setFlags(
        		android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        | android.view.WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR,
                android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        | android.view.WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR);
    	View anchor = activity.getWindow().getDecorView().findViewById(android.view.Window.ID_ANDROID_CONTENT);
    	StrictClickHelper.dealwith(anchor, new StrictClickHelper.OnClickListener() {
			
			@Override
			public void onClick(View view) {System.out.println("onClick");
				toggle();
			}
		});	//	因为click的触发范围为全view，所以不使用
//    	anchor.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {System.out.println("onClick");
//				toggle();
//			}
//		});
    	mAnchorView = anchor;System.out.println(anchor);
    	//	SDK Version: 11	Build.VERSION_CODES.HONEYCOMB	(3.0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        	anchor.setOnSystemUiVisibilityChangeListener(mSystemUiVisibilityChangeListener);
        }
    }


    private View.OnSystemUiVisibilityChangeListener mSystemUiVisibilityChangeListener
            = new View.OnSystemUiVisibilityChangeListener() {
    	/**
    	 * New Activity with same system ui visibility, will not receive this notify.
    	 * When back to pre-activity, the system ui will show again automatically, only if showed in the last activity.
    	 * When system ui visibility changes, all listener receives notify, no matter the view's visibility.
    	 * Receive this notify delays 15-30mms since trigger.
    	 */
        @Override
        public void onSystemUiVisibilityChange(int visibility) {
            System.out.println(this + " onSystemUiVisibilityChange visibility 0x" + Integer.toHexString(visibility));
            if ((visibility & (View.SYSTEM_UI_FLAG_LOW_PROFILE
            		| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            		| View.SYSTEM_UI_FLAG_FULLSCREEN)) != 0) {
            	//	SDK Version: 16	Build.VERSION_CODES.JELLY_BEAN	(4.1)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    // Pre-Jelly Bean, we must manually hide the action bar
                    // and use the old window flags API.
                    mActivity.getActionBar().hide();
                    mActivity.getWindow().setFlags(
                    		android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    		android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }

                // Trigger the registered listener and cache the visibility state.
                if (mOnVisibilityChangeListener != null) mOnVisibilityChangeListener.onVisibilityChange(false);
                mVisible = false;

            } else {
                mAnchorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    // Pre-Jelly Bean, we must manually show the action bar
                    // and use the old window flags API.
                    mActivity.getActionBar().show();
                    mActivity.getWindow().setFlags(
                            0,
                            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }

                // Trigger the registered listener and cache the visibility state.
                if (mOnVisibilityChangeListener != null) mOnVisibilityChangeListener.onVisibilityChange(true);
                mVisible = true;
            }
        }
    };

    /**
     * Returns whether or not the system UI is visible.
     */
    public boolean isVisible() {
        return mVisible;
    }

    /**
     * Hide the system UI.
     */
    public void hide() {System.out.println("hide");
    	//	SDK Version: 11	Build.VERSION_CODES.HONEYCOMB	(3.0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mAnchorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

                	//	SDK Version: 19	Build.VERSION_CODES.KITKAT	(4.4)
            		| (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            					? (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            							| View.SYSTEM_UI_FLAG_IMMERSIVE)
            					: 0));
        } else {
            mActivity.getWindow().setFlags(
            		android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
            		android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);

            if (mOnVisibilityChangeListener != null) mOnVisibilityChangeListener.onVisibilityChange(false);
        }
        mVisible = false;
    }

    /**
     * Show the system UI.
     */
    public void show() {System.out.println("show");
    	//	SDK Version: 11	Build.VERSION_CODES.HONEYCOMB	(3.0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mAnchorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            		| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            mActivity.getWindow().setFlags(
                    0,
                    android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);

            if (mOnVisibilityChangeListener != null) mOnVisibilityChangeListener.onVisibilityChange(true);
        }
        mVisible = true;
    }

    /**
     * Toggle the visibility of the system UI.
     */
    public void toggle() {
        if (isVisible()) {
            hide();
        } else {
            show();
        }
    }

    /**
     * Registers a callback, to be triggered when the system UI visibility
     * changes.
     */
    public void setOnVisibilityChangeListener(OnVisibilityChangeListener listener) {
        mOnVisibilityChangeListener = listener;
    }


    /**
     * A callback interface used to listen for system UI visibility changes.
     */
    public interface OnVisibilityChangeListener {
        /**
         * Called when the system UI visibility has changed.
         * 
         * @param visible True if the system UI is visible.
         */
        public void onVisibilityChange(boolean visible);
    }
}
