package com.wangxp.exercise.recycleview.activity;

import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commonlib.util.WifiUtil;
import com.iwangfan.foundationlibary.utils.LogUtils;
import com.wangxp.exercise.recycleview.BaseNormalActivity;
import com.wangxp.exercise.recycleview.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @类描述：StateListDrawable 测试研究
 * @类描述：当前wifi链接人数 测试研究
 * @创建人：Wangxiaopan
 * @创建时间：2017/3/20 0020 11:56
 * @修改人：
 * @修改时间：2017/3/20 0020 11:56
 * @修改备注：
 */

public class StateListDrawableActivity extends BaseNormalActivity implements View.OnClickListener {
    private ImageView mImageView;
    private Button mPlusBt,mMinusBt;
    StateListDrawable mPullingDrawable;
    private int index = 0;
    /***********************************************/
    private TextView mWifiSsid,mWifiLinkNum;
    private Button mWifiLinkBtn;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_statelist_drawable;
    }

    @Override
    protected void initView() {
        StateListDrawable drawable = mPullingDrawable = new StateListDrawable();
        for (int i = DRAWABLE_PULL.length - 1; i >= 0; i--) {
            final int resid = DRAWABLE_PULL[i];
            drawable.addState(new int[]{i}, getResources().getDrawable(resid));
        }
        mImageView = (ImageView) findViewById(R.id.status_image);
        mPlusBt = (Button) findViewById(R.id.status_plus);
        mPlusBt.setOnClickListener(this);
        mMinusBt = (Button) findViewById(R.id.status_minus);
        mMinusBt.setOnClickListener(this);
        mImageView.setImageDrawable(mPullingDrawable);
        mWifiSsid = (TextView) findViewById(R.id.wifi_ssid);
        mWifiLinkNum = (TextView) findViewById(R.id.wifi_num);
        mWifiLinkBtn = (Button) findViewById(R.id.wifi_link_btn);
        mWifiLinkBtn.setOnClickListener(this);
    }

    private final int[] DRAWABLE_PULL = {
            R.drawable.refresh_pulling_bird_logo_01, R.drawable.refresh_pulling_bird_logo_02, R.drawable.refresh_pulling_bird_logo_03,
            R.drawable.refresh_pulling_bird_logo_04, R.drawable.refresh_pulling_bird_logo_05, R.drawable.refresh_pulling_bird_logo_06,
            R.drawable.refresh_pulling_bird_logo_07, R.drawable.refresh_pulling_bird_logo_08, R.drawable.refresh_pulling_bird_logo_09,
            R.drawable.refresh_pulling_bird_logo_10, R.drawable.refresh_pulling_bird_logo_11, R.drawable.refresh_pulling_bird_logo_12,
            R.drawable.refresh_pulling_bird_logo_13, R.drawable.refresh_pulling_bird_logo_14, R.drawable.refresh_pulling_bird_logo_15,
            R.drawable.refresh_pulling_bird_logo_16, R.drawable.refresh_pulling_bird_logo_17, R.drawable.refresh_pulling_bird_logo_18,
            R.drawable.refresh_pulling_bird_logo_19, R.drawable.refresh_pulling_bird_logo_20, R.drawable.refresh_pulling_bird_logo_21,
            R.drawable.refresh_pulling_bird_logo_22, R.drawable.refresh_pulling_bird_logo_23, R.drawable.refresh_pulling_bird_logo_24,
            R.drawable.refresh_pulling_bird_logo_25, R.drawable.refresh_pulling_bird_logo_26, R.drawable.refresh_pulling_bird_logo_27,
    };

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        final String ssid = WifiUtil.getSsid(this);
        mWifiSsid.setText(ssid);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.status_plus:
                index ++;
                if (index >= DRAWABLE_PULL.length) index = DRAWABLE_PULL.length-1;
                mPullingDrawable.setState(new int[]{index});
                mImageView.invalidate();
                break;
            case R.id.status_minus:
                index--;
                if (index < 0) index = 0;
                mPullingDrawable.setState(new int[]{index});
                mImageView.invalidate();
                break;
            case R.id.wifi_link_btn:
                int count = WifiUtil.getLinkDeviceNum();
                mWifiLinkNum.setText("wifi连接数 = " + count);
                break;
            default:
                break;
        }
    }

    private void setPlusBtHeight(float height){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPlusBt.getLayoutParams();
        params.height = Math.round(height);
        mPlusBt.setLayoutParams(params);
    }

    float downY,curY;
    float distance = 0.0f;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                curY = ev.getY() - downY;
//                downY = ev.getY();
                distance = distance + curY;
                Log.d("Test","curY = " + curY +  "   distance = " + distance);
                break;
            case MotionEvent.ACTION_UP:
                downY = ev.getY();
                distance = 0.0f;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
