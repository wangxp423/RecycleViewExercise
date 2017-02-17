package com.wangxp.exercise.recycleview.viewmodel.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonlib.imageloader.ImageLoader;
import com.wangxp.exercise.recycleview.R;
import com.wangxp.exercise.recycleview.viewmodel.IViewModel;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemCurCity;

/**
 * Author : wangxp
 * Date : 2017/2/16
 * Desc : 当前城市
 */

public class RouteCurCityModel implements IViewModel<ItemCurCity.CurCity> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_curcity, parent, false);
        return new CurCityViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(ItemCurCity.CurCity model, RecyclerView.ViewHolder holder, int position) {
        CurCityViewHolder cityViewHolder = (CurCityViewHolder) holder;
        final Context context = cityViewHolder.mContext;
        cityViewHolder.cityName.setText(model.getName());
        ImageLoader.loadUrlImage(context, model.getImg(), cityViewHolder.cityImg, context.getResources().getDrawable(R.drawable.zhanwei_750_200));
    }

    public class CurCityViewHolder extends RecyclerView.ViewHolder {
        Context mContext;
        ImageView cityImg;
        TextView cityName;

        public CurCityViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            cityImg = (ImageView) itemView.findViewById(R.id.arrive_city_img);
            cityName = (TextView) itemView.findViewById(R.id.arrive_city_name);
        }
    }
}
