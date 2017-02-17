package com.wangxp.exercise.recycleview.viewmodel.entity;

import java.util.ArrayList;

/**
 * Author : wangxp
 * Date : 2017/2/14
 * Desc : 当前城市美食推荐
 */

public class ItemMeishiCity extends BaseFloorEntity {
    int error_code;
    String error_msg;
    ArrayList<ItemMeishi> list;

    public int getError_code() {
        return error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public ArrayList<ItemMeishi> getList() {
        return list;
    }

    public static class ItemMeishi extends BaseFloorEntity{
        String img_url;
        String shop_id;
        String shopname;

        public String getImg_url() {
            return img_url;
        }

        public String getShop_id() {
            return shop_id;
        }

        public String getShopname() {
            return shopname;
        }
    }
}
