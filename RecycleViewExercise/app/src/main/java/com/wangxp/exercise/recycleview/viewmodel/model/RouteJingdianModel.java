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
import com.commonlib.util.UiUtil;
import com.wangxp.exercise.recycleview.R;
import com.wangxp.exercise.recycleview.viewmodel.IViewModel;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemDianpingCity;

/**
 * Author : wangxp
 * Date : 2017/2/16
 * Desc : 景点
 */

public class RouteJingdianModel implements IViewModel<ItemDianpingCity.ItemJingdian> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jingdian, parent, false);
        return new JingdianViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(ItemDianpingCity.ItemJingdian model, RecyclerView.ViewHolder holder, int position) {
        JingdianViewHolder jingdianViewHolder = (JingdianViewHolder) holder;
        final Context context = jingdianViewHolder.mContext;
        ImageLoader.loadUrlImage(context, model.getImg(), jingdianViewHolder.jingdianImg, context.getResources().getDrawable(R.drawable.zhanwei_345_230));
        final String name = model.getName();
        int index = name.indexOf("(");
        jingdianViewHolder.jingdianName.setText(index > 0 ? name.substring(0, index) : name);
        StringBuilder builder = new StringBuilder();
        builder.append("<font color='#FF7733'>")
                .append(model.getNum_comment())
                .append("</font>")
                .append("<font color='#b2b0af'>")
                .append(context.getResources().getString(R.string.notice_route_jingdian_comment))
                .append("</font>");
        jingdianViewHolder.jingdianComment.setText(Html.fromHtml(builder.toString()));
    }

    public class JingdianViewHolder extends RecyclerView.ViewHolder {
        Context mContext;
        ImageView jingdianImg;
        TextView jingdianName;
        TextView jingdianComment;

        public JingdianViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            final int screenWidth = UiUtil.getScreenWidth(context);
            final int widthImg = (screenWidth - 3 * context.getResources().getDimensionPixelOffset(R.dimen.big_margin)) / 2;
            final int heightImg = widthImg * 230 / 345;
            jingdianImg = (ImageView) itemView.findViewById(R.id.jingdian_img);
            ViewGroup.LayoutParams params = jingdianImg.getLayoutParams();
            params.width = widthImg;
            params.height = heightImg;
            jingdianImg.setLayoutParams(params);
            jingdianName = (TextView) itemView.findViewById(R.id.jingdian_name);
            jingdianComment = (TextView) itemView.findViewById(R.id.jingdian_comment);

        }
    }
}
