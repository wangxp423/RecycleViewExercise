package com.wangxp.exercise.recycleview.viewmodel.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wangxp.exercise.recycleview.R;
import com.wangxp.exercise.recycleview.viewmodel.IViewModel;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemTitleCurCity;

/**
 * Author : wangxp
 * Date : 2017/2/16
 * Desc : 当前城市标题栏
 */

public class RouteTitleCurCityModel implements IViewModel<ItemTitleCurCity> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_curcity, parent, false);
        return new TitleCurCityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemTitleCurCity model, RecyclerView.ViewHolder holder, int position) {
        TitleCurCityViewHolder curCityViewHolder = (TitleCurCityViewHolder) holder;
        curCityViewHolder.cityLifeTip.setText(model.getLeftTip());
        curCityViewHolder.cityRightTip.setText(model.getRightTip());
    }

    public class TitleCurCityViewHolder extends RecyclerView.ViewHolder {
        public TextView cityLifeTip;
        public TextView cityRightTip;

        public TitleCurCityViewHolder(View itemView) {
            super(itemView);
            cityLifeTip = (TextView) itemView.findViewById(R.id.arrive_city_title);
            cityRightTip = (TextView) itemView.findViewById(R.id.arrive_city_detail);
        }
    }
}
