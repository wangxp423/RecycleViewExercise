package com.jcodecraeer.xrecyclerview;

/**
 * Created by jianghejie on 15/11/22.
 */
public interface BaseRefreshHeader {

    int PULL_STATE_NONE = -1;
    int PULL_STATE_NORMAL = 0; // 正常状态
    int PULL_STATE_PULLING = 1; // 触发了下拉
    int PULL_STATE_ENABLE = 2; // 激活状态(松开可以刷新)
    int PULL_STATE_LOADING = 3; // 刷新中
    int PULL_STATE_FINISH = 4; // 加载成功

    void onMove(float delta);

    boolean releaseAction();

    void refreshComplete();

}