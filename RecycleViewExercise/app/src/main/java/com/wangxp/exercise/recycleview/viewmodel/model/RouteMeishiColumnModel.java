package com.wangxp.exercise.recycleview.viewmodel.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.commonlib.imageloader.ImageLoader;
import com.commonlib.util.UiUtil;
import com.wangxp.exercise.recycleview.R;
import com.wangxp.exercise.recycleview.viewmodel.IViewModel;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemMeishiCity;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemMeishiColumn;

import java.util.ArrayList;

/**
 * Author : wangxp
 * Date : 2017/2/24
 * Desc : 三列美食viewModel
 */

public class RouteMeishiColumnModel implements IViewModel<ItemMeishiColumn>,View.OnClickListener {
    private int MAX_ITEM_ROW = 3;
    private int[] valueIds = {R.id.item_meishi_left, R.id.item_meishi_minddle, R.id.item_meishi_right};

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meishi_column3, parent, false);
        return new MeishiColunmViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(ItemMeishiColumn model, RecyclerView.ViewHolder holder, int position) {
        ArrayList<ItemMeishiCity.ItemMeishi> columnList = model.getColumnList();
        MeishiColunmViewHolder meishiHolder = (MeishiColunmViewHolder) holder;
        final Context context = meishiHolder.mContext;
        final int screenWidth = UiUtil.getScreenWidth(context);
        final int widthImg = (screenWidth - 4 * context.getResources().getDimensionPixelOffset(R.dimen.big_margin)) / 3;
        final int heightImg = widthImg;
        for (int i = 0; i < MAX_ITEM_ROW; i++) {
            View view = null;
            MeishiChildViewHolder childHolder = null;
            if (null == view) {
                view = meishiHolder.views[i];
                view.setOnClickListener(this);
                childHolder = new MeishiChildViewHolder();
                childHolder.meishiImg = (ImageView) view.findViewById(R.id.meishi_img);
                ViewGroup.LayoutParams params = childHolder.meishiImg.getLayoutParams();
                params.width = widthImg;
                params.height = heightImg;
                childHolder.meishiImg.setLayoutParams(params);
                childHolder.meishiName = (TextView) view.findViewById(R.id.meishi_name);
                view.setTag(childHolder);
            } else {
                childHolder = (MeishiChildViewHolder) view.getTag();
            }
            ItemMeishiCity.ItemMeishi meishi = columnList.get(i);
            childHolder.meishi = meishi;
            childHolder.context = context;
            ImageLoader.loadUrlImage(context, meishi.getImg_url(), childHolder.meishiImg, context.getResources().getDrawable(R.drawable.zhanwei_224_224));
            childHolder.meishiName.setText(meishi.getShopname());
        }
    }

    @Override
    public void onClick(View v) {
        Object obj = v.getTag();
        if (obj instanceof MeishiChildViewHolder) {
            MeishiChildViewHolder holder = (MeishiChildViewHolder) obj;
            Toast.makeText(holder.context,holder.meishi.getShopname(),Toast.LENGTH_SHORT).show();
        }
    }

    public class MeishiColunmViewHolder extends RecyclerView.ViewHolder {
        public View[] views = new View[MAX_ITEM_ROW];
        public Context mContext;

        public MeishiColunmViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            for (int i = 0; i < MAX_ITEM_ROW; i++) {
                views[i] = itemView.findViewById(valueIds[i]);
            }
        }
    }

    public class MeishiChildViewHolder {
        public Context context;
        public ItemMeishiCity.ItemMeishi meishi;
        public ImageView meishiImg;
        public TextView meishiName;
    }
}
