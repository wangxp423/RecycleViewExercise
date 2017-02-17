package com.wangxp.exercise.recycleview.viewmodel.entity;

/**
 * Author : wangxp
 * Date : 2017/2/14
 * Desc : 当前城市
 */

public class ItemCurCity extends BaseFloorEntity {
    int error_code;
    String error_msg;
    CurCity data;

    public int getError_code() {
        return error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public CurCity getData() {
        return data;
    }

    public static class CurCity extends BaseFloorEntity{
        int id;
        String name;
        String img;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getImg() {
            return img;
        }
    }
}
