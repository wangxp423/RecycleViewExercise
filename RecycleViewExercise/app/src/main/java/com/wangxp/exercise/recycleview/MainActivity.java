package com.wangxp.exercise.recycleview;

import android.content.Intent;
import android.view.View;

import com.wangxp.exercise.recycleview.activity.CompositeLayoutActivity;
import com.wangxp.exercise.recycleview.activity.PullRecyclerViewActivity;
import com.wangxp.exercise.recycleview.activity.StaggerTypeActivity;
import com.wangxp.exercise.recycleview.activity.StateListDrawableActivity;

public class MainActivity extends BaseNormalActivity implements View.OnClickListener {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        findViewById(R.id.bt_type_taggertype).setOnClickListener(this);
        findViewById(R.id.bt_composite_layout).setOnClickListener(this);
        findViewById(R.id.bt_pull_recyclerview).setOnClickListener(this);
        findViewById(R.id.bt_statuslist_drawable).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_type_taggertype:
                startActivity(new Intent(this, StaggerTypeActivity.class));
                break;
            case R.id.bt_composite_layout:
                startActivity(new Intent(this, CompositeLayoutActivity.class));
                break;
            case R.id.bt_pull_recyclerview:
                startActivity(new Intent(this, PullRecyclerViewActivity.class));
                break;
            case R.id.bt_statuslist_drawable:
                startActivity(new Intent(this, StateListDrawableActivity.class));
                break;
            default:
                break;
        }
    }
}
