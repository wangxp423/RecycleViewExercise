package com.wangxp.exercise.recycleview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.commonlib.util.AssetsUtil;
import com.google.gson.Gson;
import com.wangxp.exercise.recycleview.R;
import com.wangxp.exercise.recycleview.adapter.StaggerModelAdapter;
import com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemCurCity;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemDianpingCity;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemHotelCity;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemMeishiCity;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemMeishiColumn;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemTitleCategory;
import com.wangxp.exercise.recycleview.viewmodel.entity.ItemTitleCurCity;
import com.wangxp.exercise.recycleview.viewmodel.model.RouteAroundModel;
import com.wangxp.exercise.recycleview.viewmodel.model.RouteCurCityModel;
import com.wangxp.exercise.recycleview.viewmodel.model.RouteGonglveModel;
import com.wangxp.exercise.recycleview.viewmodel.model.RouteHotelModel;
import com.wangxp.exercise.recycleview.viewmodel.model.RouteJingdianModel;
import com.wangxp.exercise.recycleview.viewmodel.model.RouteMeishiColumnModel;
import com.wangxp.exercise.recycleview.viewmodel.model.RouteMeishiModel;
import com.wangxp.exercise.recycleview.viewmodel.model.RouteTitleCategoryModel;
import com.wangxp.exercise.recycleview.viewmodel.model.RouteTitleCurCityModel;
import com.wangxp.exercise.recycleview.widget.CompositeLayout;

import java.util.ArrayList;

import static com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity.TYPE_CUR_CITY;
import static com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity.TYPE_RECOMMEND_AROUND;
import static com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity.TYPE_RECOMMEND_FOOD;
import static com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity.TYPE_RECOMMEND_GONGLVE;
import static com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity.TYPE_RECOMMEND_HOTEL;
import static com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity.TYPE_RECOMMEND_JINGDIAN;
import static com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity.TYPE_TEST_FOOD;
import static com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity.TYPE_TITLE_CURCITY;
import static com.wangxp.exercise.recycleview.viewmodel.entity.BaseFloorEntity.TYPE_TITLE_RECOMMEND;

/**
 * Author : wangxp
 * Date : 2017/2/13
 * Desc : 复合布局 线性布局内嵌header和RecyclerView
 */

public class CompositeLayoutActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<BaseFloorEntity> data = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayoutId());
        initView();
        initData();
    }

    protected int getContentLayoutId() {
        return R.layout.activity_composite;
    }

    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 6, GridLayoutManager.VERTICAL, false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                final int type = data.get(position).getFloorType();
                if (type == TYPE_CUR_CITY) {
                    return 6;
                } else if (type == TYPE_RECOMMEND_JINGDIAN) {
                    return 3;
                } else if (type == TYPE_RECOMMEND_FOOD) {
                    return 2;
                } else if (type == TYPE_RECOMMEND_HOTEL) {
                    return 2;
                } else if (type == TYPE_RECOMMEND_GONGLVE) {
                    return 6;
                } else if (type == TYPE_RECOMMEND_AROUND) {
                    return 3;
                } else if (type == TYPE_TITLE_CURCITY) {
                    return 6;
                } else if (type == TYPE_TITLE_RECOMMEND) {
                    return 6;
                } else if (type == TYPE_TEST_FOOD) {
                    return 6;
                } else {
                    return 6;
                }
            }
        });
        int leftRight = getResources().getDimensionPixelOffset(R.dimen.big_margin);
//        int topBottom = getResources().getDimensionPixelOffset(R.dimen.big_margin);
//        mRecyclerView.addItemDecoration(new SpacesItemDecoration(leftRight, 0));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.offsetChildrenHorizontal(leftRight);
    }

    protected void initData() {
        initArriveCityData();
        initCityMeishiData();
        initCityMeishiColumnData();
//        initCityHotelData();
        initDianpingData();
//        StaggerAdapter adapter = new StaggerAdapter(data);
//        mRecyclerView.setAdapter(adapter);
        StaggerModelAdapter adapter = new StaggerModelAdapter();
        adapter.addViewModelClass(BaseFloorEntity.TYPE_CUR_CITY, RouteCurCityModel.class);
        adapter.addViewModelClass(BaseFloorEntity.TYPE_RECOMMEND_JINGDIAN, RouteJingdianModel.class);
        adapter.addViewModelClass(BaseFloorEntity.TYPE_RECOMMEND_FOOD, RouteMeishiModel.class);
        adapter.addViewModelClass(BaseFloorEntity.TYPE_RECOMMEND_HOTEL, RouteHotelModel.class);
        adapter.addViewModelClass(BaseFloorEntity.TYPE_RECOMMEND_GONGLVE, RouteGonglveModel.class);
        adapter.addViewModelClass(BaseFloorEntity.TYPE_RECOMMEND_AROUND, RouteAroundModel.class);
        adapter.addViewModelClass(BaseFloorEntity.TYPE_TITLE_CURCITY, RouteTitleCurCityModel.class);
        adapter.addViewModelClass(BaseFloorEntity.TYPE_TITLE_RECOMMEND, RouteTitleCategoryModel.class);
        adapter.addViewModelClass(BaseFloorEntity.TYPE_TEST_FOOD, RouteMeishiColumnModel.class);
        mRecyclerView.setAdapter(adapter);
        adapter.setList(data);
    }

    private void initArriveCityData() {
        ItemTitleCurCity titleCurCity = new ItemTitleCurCity();
        titleCurCity.setLeftTip("目的地");
        titleCurCity.setRightTip("详情");
        data.add(titleCurCity);
        String cityData = AssetsUtil.getFromAssets(this, "arriveCity.json");
        ItemCurCity curCity = new Gson().fromJson(cityData, ItemCurCity.class);
        ItemCurCity.CurCity city = curCity.getData();
        city.setFloorType(TYPE_CUR_CITY);
        data.add(city);

    }

    private void initCityMeishiData() {
        ItemTitleCategory categoryTitle = new ItemTitleCategory();
        categoryTitle.setTitle("美食推荐");
        data.add(categoryTitle);
        String meishiData = AssetsUtil.getFromAssets(this, "meishiCity.json");
        ItemMeishiCity meishiCity = new Gson().fromJson(meishiData, ItemMeishiCity.class);
        ArrayList<ItemMeishiCity.ItemMeishi> meishiList = meishiCity.getList();
        for (ItemMeishiCity.ItemMeishi meishi : meishiList) {
            meishi.setFloorType(TYPE_RECOMMEND_FOOD);
            data.add(meishi);
        }
    }

    private void initCityHotelData() {
        ItemTitleCategory categoryTitle = new ItemTitleCategory();
        categoryTitle.setTitle("住宿推荐");
        data.add(categoryTitle);
        String meishiData = AssetsUtil.getFromAssets(this, "hotelCity.json");
        ItemHotelCity hotelCity = new Gson().fromJson(meishiData, ItemHotelCity.class);
        ArrayList<ItemHotelCity.ItemHotel> hotelList = hotelCity.getList();
        for (ItemHotelCity.ItemHotel hotel : hotelList) {
            hotel.setFloorType(TYPE_RECOMMEND_HOTEL);
            data.add(hotel);
        }
    }

    private void initDianpingData() {
        String dianpingData = AssetsUtil.getFromAssets(this, "dianpingCity.json");
        ItemDianpingCity dianpingCity = new Gson().fromJson(dianpingData, ItemDianpingCity.class);
        ItemTitleCategory jingidanTitle = new ItemTitleCategory();
        jingidanTitle.setTitle("必去景点");
        data.add(jingidanTitle);
        ArrayList<ItemDianpingCity.ItemJingdian> jingdianList = dianpingCity.getData().getPoiList();
        for (ItemDianpingCity.ItemJingdian jingdian : jingdianList) {
            jingdian.setFloorType(TYPE_RECOMMEND_JINGDIAN);
            data.add(jingdian);
        }
        initCityHotelData();
        ItemTitleCategory gonglveTitle = new ItemTitleCategory();
        gonglveTitle.setTitle("精彩攻略");
        data.add(gonglveTitle);
        ArrayList<ItemDianpingCity.ItemGonglve> gonglveList = dianpingCity.getData().getGonglveList();
        for (ItemDianpingCity.ItemGonglve gonglve : gonglveList) {
            gonglve.setFloorType(TYPE_RECOMMEND_GONGLVE);
            data.add(gonglve);
        }
        ItemTitleCategory aroundTitle = new ItemTitleCategory();
        aroundTitle.setTitle("周边游推荐");
        data.add(aroundTitle);
        ArrayList<ItemDianpingCity.ItemAround> aroundList = dianpingCity.getData().getLocaldealsList();
        for (ItemDianpingCity.ItemAround around : aroundList) {
            around.setFloorType(TYPE_RECOMMEND_AROUND);
            data.add(around);
        }
    }

    //三列样式的美食
    private void initCityMeishiColumnData() {
        final int COLUMN = 3;
        ItemTitleCategory categoryTitle = new ItemTitleCategory();
        categoryTitle.setTitle("美食推荐");
        data.add(categoryTitle);
        String meishiData = AssetsUtil.getFromAssets(this, "meishiCity.json");
        ItemMeishiCity meishiCity = new Gson().fromJson(meishiData, ItemMeishiCity.class);
        ArrayList<ItemMeishiCity.ItemMeishi> meishiList = meishiCity.getList();
        int line = meishiList.size()/COLUMN;
        for (int i = 0; i < line; i++) {
            ItemMeishiColumn meishiColumn = new ItemMeishiColumn();
            meishiColumn.setFloorType(TYPE_TEST_FOOD);
            ArrayList<ItemMeishiCity.ItemMeishi> columnList = new ArrayList<ItemMeishiCity.ItemMeishi>();
            for (int j = 0; j < COLUMN; j++) {
                columnList.add(meishiList.get(i*COLUMN + j));
            }
            meishiColumn.setColumnList(columnList);
            data.add(meishiColumn);
        }
    }
}
