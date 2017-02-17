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
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemDianpingCity;

/**
 * Author : wangxp
 * Date : 2017/2/16
 * Desc : 周边游
 */

public class RouteAroundModel implements IViewModel<ItemDianpingCity.ItemAround> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_around, parent, false);
        return new AroundViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(ItemDianpingCity.ItemAround model, RecyclerView.ViewHolder holder, int position) {
        AroundViewHolder aroundViewHolder = (AroundViewHolder) holder;
        final Context context = aroundViewHolder.mContext;
        ImageLoader.loadUrlImage(context, model.getImg(), aroundViewHolder.aroundImg, context.getResources().getDrawable(R.drawable.zhanwei_345_230));
        aroundViewHolder.aroundName.setText(model.getName());

    }

    public class AroundViewHolder extends RecyclerView.ViewHolder {
        Context mContext;
        ImageView aroundImg;
        TextView aroundName;

        public AroundViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            final int screenWidth = UiUtil.getScreenWidth(context);
            final int widthImg = (screenWidth - 3 * context.getResources().getDimensionPixelOffset(R.dimen.big_margin)) / 2;
            final int heightImg = widthImg * 230 / 345;
            aroundImg = (ImageView) itemView.findViewById(R.id.around_img);
            ViewGroup.LayoutParams params = aroundImg.getLayoutParams();
            params.width = widthImg;
            params.height = heightImg;
            aroundImg.setLayoutParams(params);
            aroundName = (TextView) itemView.findViewById(R.id.around_name);
        }
    }
}
