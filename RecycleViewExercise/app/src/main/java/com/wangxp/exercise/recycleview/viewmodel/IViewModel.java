package com.wangxp.exercise.recycleview.viewmodel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Author : wangxp
 * Date : 2017/2/16
 * Desc :base ViewModel for M-V-Vm
 */

public interface IViewModel<T> {
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    public void onBindViewHolder(T model, RecyclerView.ViewHolder holder, int position);
}
