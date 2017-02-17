package com.wangxp.exercise.recycleview.viewmodel;

import android.support.v7.widget.RecyclerView;

import com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity;

import java.util.ArrayList;

/**
 * Author : wangxp
 * Date : 2017/2/16
 * Desc : 基类RecyclerAdapter
 */

public abstract class BaseListRecyclerAdapter<T> extends RecyclerView.Adapter {
    ArrayList<T> mList = null;

    @Override
    public int getItemCount() {
        return (null == mList) ? 0 : mList.size();
    }

    /**
     * 用于设置List的方法，也可以不用这个方法，进行设置值，可以在子类的构造方法里直接进行设置
     *
     * @param group
     */
    public void setList(ArrayList<T> group) {
        mList = group;
        notifyDataSetChanged(); // 如果改变了group，则显示控件则要更新显示数据
    }

    public final ArrayList<T> getList() {
        return mList;
    }

    public final void addList(ArrayList<T> group) {
        if (group == null) return;

        if (mList == null) {
            mList = group;
        } else {
            if (group != null) mList.addAll(group);
        }

        notifyDataSetChanged(); // 如果改变了group，则显示控件则要更新显示数据
    }

    public final void addListFromHeader(ArrayList<T> group) {
        if (group == null) return;

        if (mList == null) {
            mList = group;
        } else {
            if (group != null) mList.addAll(0, group);
        }

        notifyDataSetChanged(); // 如果改变了group，则显示控件则要更新显示数据
    }

    public void addItem(int index, T item) {
        if (mList != null) {
            mList.add(index, item);
            notifyDataSetChanged();
        }
    }


    public void removeAt(int index) {
        if (mList != null) {
            mList.remove(index);
            notifyDataSetChanged();
        }
    }

    public void remove(T item) {
        if (mList != null) {
            mList.remove(item);
            notifyDataSetChanged();
        }
    }

    public void removeAll() {
        if (mList != null) {
            mList.clear();
            notifyDataSetChanged();
        }
    }

    public void destroy() {
        removeAll();
        mList = null;
    }
}
