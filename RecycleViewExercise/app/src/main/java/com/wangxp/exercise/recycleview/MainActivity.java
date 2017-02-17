package com.wangxp.exercise.recycleview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wangxp.exercise.recycleview.activity.StaggerTypeActivity;

public class MainActivity extends BaseNormalActivity implements View.OnClickListener {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        findViewById(R.id.bt_type_taggertype).setOnClickListener(this);
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
            default:
                break;
        }
    }
}
