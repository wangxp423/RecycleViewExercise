package com.wangxp.exercise.recycleview.viewmodel.entity;

import java.util.ArrayList;

/**
 * Author : wangxp
 * Date : 2017/2/24
 * Desc : 美食一行三列
 */

public class ItemMeishiColumn extends BaseFloorEntity {
    ArrayList<ItemMeishiCity.ItemMeishi> columnList;

    public ArrayList<ItemMeishiCity.ItemMeishi> getColumnList() {
        return columnList;
    }

    public void setColumnList(ArrayList<ItemMeishiCity.ItemMeishi> columnList) {
        this.columnList = columnList;
    }
}
