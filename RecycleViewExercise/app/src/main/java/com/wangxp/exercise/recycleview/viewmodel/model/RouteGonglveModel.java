package com.wangxp.exercise.recycleview.viewmodel.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonlib.imageloader.ImageLoader;
import com.wangxp.exercise.recycleview.R;
import com.wangxp.exercise.recycleview.viewmodel.IViewModel;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemDianpingCity;

/**
 * Author : wangxp
 * Date : 2017/2/16
 * Desc : 攻略
 */

public class RouteGonglveModel implements IViewModel<ItemDianpingCity.ItemGonglve> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gonglve, parent, false);
        return new GonglveViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(ItemDianpingCity.ItemGonglve model, RecyclerView.ViewHolder holder, int position) {
        GonglveViewHolder gonglveViewHolder = (GonglveViewHolder) holder;
        final Context context = gonglveViewHolder.mContext;
        ImageLoader.loadUrlImage(context, model.getImg(), gonglveViewHolder.gonglveImg, context.getResources().getDrawable(R.drawable.zhanwei_224_168));
        gonglveViewHolder.gonglveName.setText(model.getName());
        StringBuilder builder = new StringBuilder();
        builder.append("<font color='#FF7733'>")
                .append(model.getSold_num())
                .append("</font>")
                .append("<font color='#b2b0af'>")
                .append(context.getResources().getString(R.string.notice_route_gonglve_enjoy))
                .append("</font>");
        gonglveViewHolder.gonglveGo.setText(Html.fromHtml(builder.toString()));
        gonglveViewHolder.gonglveDesc.setText(context.getString(R.string.notice_other_gonglve_canot_dismiss));
    }

    public class GonglveViewHolder extends RecyclerView.ViewHolder {
        Context mContext;
        ImageView gonglveImg;
        TextView gonglveName;
        TextView gonglveGo;
        TextView gonglveDesc;

        public GonglveViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            gonglveImg = (ImageView) itemView.findViewById(R.id.gonglve_img);
            gonglveName = (TextView) itemView.findViewById(R.id.gonglve_name);
            gonglveGo = (TextView) itemView.findViewById(R.id.gonglve_go);
            gonglveDesc = (TextView) itemView.findViewById(R.id.gonglve_desc);

        }
    }
}
