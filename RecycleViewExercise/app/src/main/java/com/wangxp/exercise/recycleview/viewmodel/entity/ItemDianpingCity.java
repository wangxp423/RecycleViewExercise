package com.wangxp.exercise.recycleview.viewmodel.entity;

import java.util.ArrayList;

/**
 * Author : wangxp
 * Date : 2017/2/14
 * Desc : 大众点评城市推荐
 */

public class ItemDianpingCity extends BaseFloorEntity {
    int error_code;
    String error_msg;
    DianpingData data;

    public int getError_code() {
        return error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public DianpingData getData() {
        return data;
    }

    public static class DianpingData {
        String name;
        String link;
        String img;
        String poiLink;
        String gonglveLink;
        String localdealsLink;
        ArrayList<ItemJingdian> poiList;
        ArrayList<ItemGonglve> gonglveList;
        ArrayList<ItemAround> localdealsList;

        public String getName() {
            return name;
        }

        public String getLink() {
            return link;
        }

        public String getImg() {
            return img;
        }

        public String getPoiLink() {
            return poiLink;
        }

        public String getGonglveLink() {
            return gonglveLink;
        }

        public String getLocaldealsLink() {
            return localdealsLink;
        }

        public ArrayList<ItemJingdian> getPoiList() {
            return poiList;
        }

        public ArrayList<ItemGonglve> getGonglveList() {
            return gonglveList;
        }

        public ArrayList<ItemAround> getLocaldealsList() {
            return localdealsList;
        }
    }

    public static class ItemJingdian extends BaseFloorEntity{
        String name;
        String img;
        String num_comment;
        String link;

        public String getName() {
            return name;
        }

        public String getImg() {
            return img;
        }

        public String getNum_comment() {
            return num_comment;
        }

        public String getLink() {
            return link;
        }
    }
    public static class ItemGonglve extends BaseFloorEntity{
        String name;
        String img;
        String sold_num;
        String links;

        public String getName() {
            return name;
        }

        public String getImg() {
            return img;
        }

        public String getSold_num() {
            return sold_num;
        }

        public String getLinks() {
            return links;
        }
    }
    public static class ItemAround extends BaseFloorEntity{
        String name;
        String img;
        String price;
        String links;

        public String getName() {
            return name;
        }

        public String getImg() {
            return img;
        }

        public String getPrice() {
            return price;
        }

        public String getLinks() {
            return links;
        }
    }
}
