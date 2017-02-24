package com.wangxp.exercise.recycleview.viewmodel.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wangxp.exercise.recycleview.R;
import com.wangxp.exercise.recycleview.viewmodel.IViewModel;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemTitleCategory;

/**
 * Author : wangxp
 * Date : 2017/2/16
 * Desc : 行程推荐分类title
 */

public class RouteTitleCategoryModel implements IViewModel<ItemTitleCategory> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_category, parent, false);
        return new TitleCategoryViewholder(view);
    }

    @Override
    public void onBindViewHolder(ItemTitleCategory model, RecyclerView.ViewHolder holder, int position) {
        TitleCategoryViewholder titleCategoryViewholder = (TitleCategoryViewholder) holder;
        titleCategoryViewholder.title.setText(model.getTitle());
    }

    public class TitleCategoryViewholder extends RecyclerView.ViewHolder {
        public TextView title;

        public TitleCategoryViewholder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.recommend_title);
        }
    }
}
