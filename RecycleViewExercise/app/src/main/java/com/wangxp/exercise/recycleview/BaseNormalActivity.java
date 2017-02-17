package com.wangxp.exercise.recycleview;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Author : wangxp
 * Date : 2017/2/13
 * Desc : 常规基类Activity
 */

public abstract class BaseNormalActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContentLayoutId() != 0){
            setContentView(getContentLayoutId());
            initView();
            initData();
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }
    }

    protected abstract int getContentLayoutId();

    protected abstract void initView();

    protected abstract void initData();
}
