package com.wangxp.exercise.recycleview.viewmodel.entity;

/**
 * Author : wangxp
 * Date : 2017/2/14
 * Desc : 当前城市title
 */

public class ItemTitleCurCity extends BaseFloorEntity {
    public ItemTitleCurCity() {
        setFloorType(TYPE_TITLE_CURCITY);
    }

    String leftTip;
    String rightTip;

    public String getLeftTip() {
        return leftTip;
    }

    public void setLeftTip(String leftTip) {
        this.leftTip = leftTip;
    }

    public String getRightTip() {
        return rightTip;
    }

    public void setRightTip(String rightTip) {
        this.rightTip = rightTip;
    }
}
