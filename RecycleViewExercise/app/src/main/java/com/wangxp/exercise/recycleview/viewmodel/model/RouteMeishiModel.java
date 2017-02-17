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
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemMeishiCity;

/**
 * Author : wangxp
 * Date : 2017/2/16
 * Desc : 美食
 */

public class RouteMeishiModel implements IViewModel<ItemMeishiCity.ItemMeishi> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meishi, parent, false);
        return new MeishiViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(ItemMeishiCity.ItemMeishi model, RecyclerView.ViewHolder holder, int position) {
        MeishiViewHolder meishiViewHolder = (MeishiViewHolder) holder;
        final Context context = meishiViewHolder.mContext;
        ImageLoader.loadUrlImage(context, model.getImg_url(), meishiViewHolder.meishiImg, context.getResources().getDrawable(R.drawable.zhanwei_224_224));
        meishiViewHolder.meishiName.setText(model.getShopname());
    }

    public class MeishiViewHolder extends RecyclerView.ViewHolder {
        public ImageView meishiImg;
        public TextView meishiName;
        public Context mContext;

        public MeishiViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            final int screenWidth = UiUtil.getScreenWidth(context);
            final int widthImg = (screenWidth - 4 * context.getResources().getDimensionPixelOffset(R.dimen.big_margin)) / 3;
            final int heightImg = widthImg;
            meishiImg = (ImageView) itemView.findViewById(R.id.meishi_img);
            ViewGroup.LayoutParams params = meishiImg.getLayoutParams();
            params.width = widthImg;
            params.height = heightImg;
            meishiImg.setLayoutParams(params);
            meishiName = (TextView) itemView.findViewById(R.id.meishi_name);
        }
    }
}
