package com.iwangfan.foundationlibary.recyclerview;

/**
 * Created by wuyulong on 2017/3/3.
 * RecyclerView 条目类型
 */

public interface IRecyclerViewItemTypeModle<T> {
    int getItemViewLayoutId();

    void onBindViewHoler(ViewHolder holder, T t, int position);
}
