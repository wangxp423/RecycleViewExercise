package com.commonlib.retrofit_rx.listener;

import com.commonlib.retrofit_rx.exception.ApiException;

/**
 * @类描述：成功回调处理
 * @创建人：Wangxiaopan
 * @创建时间：2017/6/5 09:51
 * @修改人：
 * @修改时间：2017/6/5 09:51
 * @修改备注：
 */
public interface HttpOnNextListener {
    /**
     * 成功后回调方法
     *
     * @param tag    tag标记
     * @param result 返回结果
     */
    void onNext(int tag, String result);

    /**
     * 失败
     * 失败或者错误方法
     * 自定义异常处理
     *
     * @param e
     */
    void onError(ApiException e);
}
