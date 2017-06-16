package com.commonlib.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.commomlib.R;
import com.iwangfan.foundationlibary.utils.ActivityManagerUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;

/**
 * @类描述：activity基类
 * @创建人：Wangxiaopan
 * @创建时间：2017/4/26 0026 17:58
 * @修改人：
 * @修改时间：2017/4/26 0026 17:58
 * @修改备注：
 */

public abstract class BaseActivity extends RxAppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();
    /***
     * 是否沉浸状态栏
     **/
    private boolean isSetStatusBar = false;
    /***
     * 是否允许全屏
     **/
    private boolean mAllowFullScreen = false;

    /***
     * overridePendingTransition mode
     */
    public enum TransitionMode {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setOverridePendingTransition();
        super.onCreate(savedInstanceState);
        getExtras(getIntent());
        setStatusBar();
        setFullScreen();
        ActivityManagerUtils.getActivityManager().addActivity(this);
        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }
        initViews();
        initData();
    }

    protected void setOverridePendingTransition() {
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.activity_top_in, R.anim.activity_top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.activity_bottom_in, R.anim.activity_bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.activity_scale_in, R.anim.activity_scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    protected void setFullScreen() {
        if (mAllowFullScreen) {
            this.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
    }

    @Override
    public void finish() {
        super.finish();
        ActivityManagerUtils.getActivityManager().removeActivity(this);
        setOverridePendingTransition();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void setStatusBar() {
        if (!isSetStatusBar) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * [是否允许全屏]
     *
     * @param allowFullScreen
     */
    public void setAllowFullScreen(boolean allowFullScreen) {
        this.mAllowFullScreen = allowFullScreen;
    }

    /**
     * [是否设置沉浸状态栏]
     *
     * @param isSetStatusBar
     */
    public void setSteepStatusBar(boolean isSetStatusBar) {
        this.isSetStatusBar = isSetStatusBar;
    }

    /**
     * toggle overridePendingTransition
     *
     * @return
     */
    protected abstract boolean toggleOverridePendingTransition();

    /**
     * get the overridePendingTransition mode
     */
    protected abstract TransitionMode getOverridePendingTransitionMode();

    /**
     * get intent extras
     */
    protected abstract void getExtras(Intent intent);

    /**
     * bind layout resource file
     *
     * @return id of layout resource
     */
    protected abstract int getContentViewLayoutID();

    /**
     * init views
     */
    protected abstract void initViews();

    /**
     * init datas
     */
    protected abstract void initData();

    /**
     * startActivity
     *
     * @param clazz
     */
    protected void startActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * startActivity then finish
     *
     * @param clazz
     */
    protected void startActivityThenFinish(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    /**
     * startActivity with bundle then finish
     *
     * @param clazz
     * @param bundle
     */
    protected void startActivityThenFinish(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected void startActivityForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void startActivityForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }
}
