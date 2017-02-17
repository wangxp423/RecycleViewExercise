package com.wangxp.exercise.recycleview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonlib.imageloader.ImageLoader;
import com.commonlib.util.UiUtil;
import com.wangxp.exercise.recycleview.R;
import com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemCurCity;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemDianpingCity;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemHotelCity;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemMeishiCity;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemTitleCategory;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemTitleCurCity;

import java.util.ArrayList;

import static com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity.TYPE_CUR_CITY;
import static com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity.TYPE_RECOMMEND_AROUND;
import static com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity.TYPE_RECOMMEND_FOOD;
import static com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity.TYPE_RECOMMEND_GONGLVE;
import static com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity.TYPE_RECOMMEND_HOTEL;
import static com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity.TYPE_RECOMMEND_JINGDIAN;
import static com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity.TYPE_TITLE_CURCITY;
import static com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity.TYPE_TITLE_RECOMMEND;

/**
 * Author : wangxp
 * Date : 2017/2/14
 * Desc : 复杂布局adapter
 */

public class StaggerAdapter extends RecyclerView.Adapter {
    private int screenWidth;
    private ArrayList<BaseFloorEntity> list;

    public StaggerAdapter(ArrayList<BaseFloorEntity> datas) {
        this.list = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        screenWidth = UiUtil.getScreenWidth(parent.getContext());
        if (viewType == TYPE_CUR_CITY) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_curcity, parent, false)) {};
        } else if (viewType == TYPE_RECOMMEND_JINGDIAN) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jingdian, parent, false)) {};
        } else if (viewType == TYPE_RECOMMEND_FOOD) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meishi, parent, false)) {};
        } else if (viewType == TYPE_RECOMMEND_HOTEL) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel, parent, false)) {};
        } else if (viewType == TYPE_RECOMMEND_GONGLVE) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gonglve, parent, false)) {};
        } else if (viewType == TYPE_RECOMMEND_AROUND) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_around, parent, false)) {};
        } else if (viewType == TYPE_TITLE_CURCITY) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_curcity, parent, false)) {};
        } else if (viewType == TYPE_TITLE_RECOMMEND) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_category, parent, false)) {};
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        BaseFloorEntity entity = list.get(position);
        final int type = getItemViewType(position);
        if (type == TYPE_CUR_CITY) {
            ItemCurCity.CurCity city = (ItemCurCity.CurCity) entity;
            ImageView cityImg = (ImageView) holder.itemView.findViewById(R.id.arrive_city_img);
            TextView cityName = (TextView) holder.itemView.findViewById(R.id.arrive_city_name);
            cityName.setText(city.getName());
            ImageLoader.loadUrlImage(context,city.getImg(),cityImg,context.getResources().getDrawable(R.drawable.zhanwei_750_200));
        } else if (type == TYPE_RECOMMEND_JINGDIAN) {
            final  int widthImg = (screenWidth - 3 * context.getResources().getDimensionPixelOffset(R.dimen.big_margin)) / 2;
            final  int heightImg = widthImg * 230 / 345;
            ItemDianpingCity.ItemJingdian jingdian = (ItemDianpingCity.ItemJingdian) entity;
            ImageView jingdianImg = (ImageView) holder.itemView.findViewById(R.id.jingdian_img);
            ViewGroup.LayoutParams params = jingdianImg.getLayoutParams();
            params.width = widthImg;
            params.height = heightImg;
            jingdianImg.setLayoutParams(params);
            ImageLoader.loadUrlImage(context,jingdian.getImg(),jingdianImg,context.getResources().getDrawable(R.drawable.zhanwei_345_230));
            TextView jingdianName = (TextView) holder.itemView.findViewById(R.id.jingdian_name);
            TextView jingdianComment = (TextView) holder.itemView.findViewById(R.id.jingdian_comment);
            final String name = jingdian.getName();
            int index = name.indexOf("(");
            jingdianName.setText(index > 0 ? name.substring(0, index) : name);
            StringBuilder builder = new StringBuilder();
            builder.append("<font color='#FF7733'>")
                    .append(jingdian.getNum_comment())
                    .append("</font>")
                    .append("<font color='#b2b0af'>")
                    .append(context.getResources().getString(R.string.notice_route_jingdian_comment))
                    .append("</font>");
            jingdianComment.setText(Html.fromHtml(builder.toString()));
        } else if (type == TYPE_RECOMMEND_FOOD) {
            final  int widthImg = (screenWidth - 4 * context.getResources().getDimensionPixelOffset(R.dimen.big_margin)) / 3;
            final  int heightImg = widthImg;
            ItemMeishiCity.ItemMeishi meishi = (ItemMeishiCity.ItemMeishi) entity;
            ImageView meishiImg = (ImageView) holder.itemView.findViewById(R.id.meishi_img);
            ViewGroup.LayoutParams params = meishiImg.getLayoutParams();
            params.width = widthImg;
            params.height = heightImg;
            meishiImg.setLayoutParams(params);
            ImageLoader.loadUrlImage(context,meishi.getImg_url(),meishiImg,context.getResources().getDrawable(R.drawable.zhanwei_224_224));
            TextView meishiName = (TextView) holder.itemView.findViewById(R.id.meishi_name);
            meishiName.setText(meishi.getShopname());
        } else if (type == TYPE_RECOMMEND_HOTEL) {
            final  int widthImg = (screenWidth - 4 * context.getResources().getDimensionPixelOffset(R.dimen.big_margin)) / 3;
            final  int heightImg = widthImg;
            ItemHotelCity.ItemHotel hotel = (ItemHotelCity.ItemHotel) entity;
            ImageView hotelImg = (ImageView) holder.itemView.findViewById(R.id.hotel_img);
            ViewGroup.LayoutParams params = hotelImg.getLayoutParams();
            params.width = widthImg;
            params.height = heightImg;
            hotelImg.setLayoutParams(params);
            ImageLoader.loadUrlImage(context,hotel.getImg_url(),hotelImg,context.getResources().getDrawable(R.drawable.zhanwei_224_224));
            TextView hotelName = (TextView) holder.itemView.findViewById(R.id.hotel_name);
            hotelName.setText(hotel.getShopname());
        } else if (type == TYPE_RECOMMEND_GONGLVE) {
            ItemDianpingCity.ItemGonglve gonglve = (ItemDianpingCity.ItemGonglve) entity;
            ImageView gonglveImg = (ImageView) holder.itemView.findViewById(R.id.gonglve_img);
            TextView gonglveName = (TextView) holder.itemView.findViewById(R.id.gonglve_name);
            TextView gonglveGo = (TextView) holder.itemView.findViewById(R.id.gonglve_go);
            TextView gonglveDesc = (TextView) holder.itemView.findViewById(R.id.gonglve_desc);
            ImageLoader.loadUrlImage(context,gonglve.getImg(),gonglveImg,context.getResources().getDrawable(R.drawable.zhanwei_224_168));
            gonglveName.setText(gonglve.getName());
            StringBuilder builder = new StringBuilder();
            builder.append("<font color='#FF7733'>")
                    .append(gonglve.getSold_num())
                    .append("</font>")
                    .append("<font color='#b2b0af'>")
                    .append(context.getResources().getString(R.string.notice_route_gonglve_enjoy))
                    .append("</font>");
            gonglveGo.setText(Html.fromHtml(builder.toString()));
            gonglveDesc.setText(context.getString(R.string.notice_other_gonglve_canot_dismiss));
        } else if (type == TYPE_RECOMMEND_AROUND) {
            final  int widthImg = (screenWidth - 3 * context.getResources().getDimensionPixelOffset(R.dimen.big_margin)) / 2;
            final  int heightImg = widthImg * 230 / 345;
            ItemDianpingCity.ItemAround around = (ItemDianpingCity.ItemAround) entity;
            ImageView aroundImg = (ImageView) holder.itemView.findViewById(R.id.around_img);
            ViewGroup.LayoutParams params = aroundImg.getLayoutParams();
            params.width = widthImg;
            params.height = heightImg;
            aroundImg.setLayoutParams(params);
            ImageLoader.loadUrlImage(context,around.getImg(),aroundImg,context.getResources().getDrawable(R.drawable.zhanwei_345_230));
            TextView aroundName = (TextView) holder.itemView.findViewById(R.id.around_name);
            aroundName.setText(around.getName());
        } else if (type == TYPE_TITLE_CURCITY) {
            ItemTitleCurCity city = (ItemTitleCurCity) entity;
            TextView cityLifeTip = (TextView) holder.itemView.findViewById(R.id.arrive_city_title);
            TextView cityRightTip = (TextView) holder.itemView.findViewById(R.id.arrive_city_detail);
            cityLifeTip.setText(city.getLeftTip());
            cityRightTip.setText(city.getRightTip());
        } else if (type == TYPE_TITLE_RECOMMEND) {
            ItemTitleCategory category = (ItemTitleCategory) entity;
            TextView title = (TextView) holder.itemView.findViewById(R.id.recommend_title);
            title.setText(category.getTitle());
        } else {
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getFloorType();
    }

}
