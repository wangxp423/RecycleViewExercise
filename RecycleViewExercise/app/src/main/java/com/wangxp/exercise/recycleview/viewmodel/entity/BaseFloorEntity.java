package com.wangxp.exercise.recycleview.viewmodel.entity;

import org.json.JSONObject;

/**
 * 行程推荐基类
 */
public class BaseFloorEntity {
    public static final int TYPE_CUR_CITY = 0;     // 当前城市
    public static final int TYPE_RECOMMEND_JINGDIAN = 1;   // 必去景点
    public static final int TYPE_RECOMMEND_FOOD = 2;      // 必吃美食
    public static final int TYPE_RECOMMEND_HOTEL = 3;   // 住宿推荐
    public static final int TYPE_RECOMMEND_GONGLVE = 4; // 精彩攻略
    public static final int TYPE_RECOMMEND_AROUND = 5; // 周边游推荐
    public static final int TYPE_TITLE_CURCITY = 6;
    public static final int TYPE_TITLE_RECOMMEND = 7;

    int type;
    int index;
    int sequenceId = -1;

    public int getFloorType() {
        return type;
    }

    public void setFloorType(int type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public BaseFloorEntity() {
    }

    ;

    public BaseFloorEntity(JSONObject obj) {
        if (obj != null) {
            type = obj.optInt("type");
            index = obj.optInt("index");
            sequenceId = obj.optInt("sequenceId");
        }
    }
}
