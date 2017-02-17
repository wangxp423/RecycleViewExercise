package com.wangxp.exercise.recycleview.adapter;

import com.wangxp.exercise.recycleview.viewmodel.BaseViewModelAdapter;
import com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity;

/**
 * Author : wangxp
 * Date : 2017/2/16
 * Desc : 复杂布局Adapter
 */

public class StaggerModelAdapter<T extends BaseFloorEntity> extends BaseViewModelAdapter<T> {
    @Override
    public int getItemViewType(int position) {
        BaseFloorEntity entity = getList().get(position);
        return entity.getFloorType();
    }
}
