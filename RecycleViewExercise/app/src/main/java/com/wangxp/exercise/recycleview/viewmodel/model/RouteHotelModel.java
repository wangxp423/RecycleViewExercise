package com.wangxp.exercise.recycleview.viewmodel.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonlib.imageloader.ImageLoader;
import com.commonlib.util.UiUtil;
import com.wangxp.exercise.recycleview.R;
import com.wangxp.exercise.recycleview.viewmodel.IViewModel;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemHotelCity;

/**
 * Author : wangxp
 * Date : 2017/2/16
 * Desc : 酒店
 */

public class RouteHotelModel implements IViewModel<ItemHotelCity.ItemHotel> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel, parent, false);
        return new HotelModelViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(ItemHotelCity.ItemHotel model, RecyclerView.ViewHolder holder, int position) {
        HotelModelViewHolder hotelModelViewHolder = (HotelModelViewHolder) holder;
        final Context context = hotelModelViewHolder.mContext;
        ImageLoader.loadUrlImage(context, model.getImg_url(), hotelModelViewHolder.hotelImg, context.getResources().getDrawable(R.drawable.zhanwei_224_224));
        hotelModelViewHolder.hotelName.setText(model.getShopname());


    }

    public class HotelModelViewHolder extends RecyclerView.ViewHolder {
        public Context mContext;
        public ImageView hotelImg;
        public TextView hotelName;

        public HotelModelViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            final int screenWidth = UiUtil.getScreenWidth(context);
            final int widthImg = (screenWidth - 4 * context.getResources().getDimensionPixelOffset(R.dimen.big_margin)) / 3;
            final int heightImg = widthImg;
            hotelImg = (ImageView) itemView.findViewById(R.id.hotel_img);
            ViewGroup.LayoutParams params = hotelImg.getLayoutParams();
            params.width = widthImg;
            params.height = heightImg;
            hotelImg.setLayoutParams(params);
            hotelName = (TextView) itemView.findViewById(R.id.hotel_name);
        }
    }
}
