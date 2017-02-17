package com.wangxp.exercise.recycleview.viewmodel.entity;

/**
 * Author : wangxp
 * Date : 2017/2/14
 * Desc : 分类title
 */

public class ItemTitleCategory extends BaseFloorEntity {
    public ItemTitleCategory() {
        setFloorType(TYPE_TITLE_RECOMMEND);
    }

    String title;
    String rightTip;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRightTip() {
        return rightTip;
    }

    public void setRightTip(String rightTip) {
        this.rightTip = rightTip;
    }
}
