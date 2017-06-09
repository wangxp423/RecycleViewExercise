package com.commonlib.retrofit_rx.api;

import android.app.Activity;

import com.commonlib.retrofit_rx.http.HttpManager;
import com.commonlib.retrofit_rx.listener.HttpOnNextListener;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.Retrofit;

/**
 * @类描述：请求数据统一封装类
 * @创建人：Wangxiaopan
 * @创建时间：2017/6/5 09:51
 * @修改人：
 * @修改时间：2017/6/5 09:51
 * @修改备注：
 */
public class HttpManagerApi extends BaseApi {
    private HttpManager manager;

    public HttpManagerApi() {
        manager = new HttpManager();
    }

    /**
     * 获取Retrofit
     *
     * @return
     */
    protected Retrofit getRetrofit() {
        return manager.getReTrofit(getConnectionTime(), getBaseUrl());
    }

    /**
     * 配置工作流
     *
     * @param flowable
     * @return
     */
    protected Flowable getFlowable(Flowable flowable) {
        return manager.configFlowable(flowable, this, 0);
    }

    /**
     * 获取订阅者
     *
     * @param tag      标记
     * @param listener 回调
     * @return
     */
    protected ResourceSubscriber getSubscriber(int tag, HttpOnNextListener listener) {
        return manager.getResourceSubscriber(tag, listener, this);
    }

    /**
     * 直接请求 无回调
     *
     * @param flowable
     */
    protected void doHttpDeal(Flowable flowable) {
        manager.httpDeal(flowable, this, 0);
    }

    /**
     * 直接请求 无回调
     *
     * @param flowable
     * @param seconds  延迟时间S
     */
    protected void doHttpDealDelay(Flowable flowable, int seconds) {
        manager.httpDeal(flowable, this, seconds);
    }

    /**
     * http请求
     *
     * @param tag      标记
     * @param listener 回调
     * @param flowable 工作流
     */
    protected void doHttpDeal(int tag, HttpOnNextListener listener, Flowable flowable) {
        manager.httpDeal(tag, listener, flowable, this, 0);
    }

    /**
     * http请求
     *
     * @param tag      标记
     * @param listener 回调
     * @param flowable 工作流
     * @param seconds  延迟时间S
     */
    protected void doHttpDealDelay(int tag, HttpOnNextListener listener, Flowable flowable, int seconds) {
        manager.httpDeal(tag, listener, flowable, this, seconds);
    }

    /**
     * http请求
     *
     * @param tag      标记
     * @param listener 回调
     * @param flowable 工作流
     * @param activity
     */
    protected void doHttpDealDialog(int tag, HttpOnNextListener listener, Flowable flowable, Activity activity) {
        manager.httpDealDialog(tag, listener, flowable, this, 0, activity);
    }

    /**
     * http请求
     *
     * @param tag      标记
     * @param listener 回调
     * @param flowable 工作流
     * @param seconds  延迟时间S
     * @param activity
     */
    protected void doHttpDealDialogDelay(int tag, HttpOnNextListener listener, Flowable flowable, int seconds, Activity activity) {
        manager.httpDealDialog(tag, listener, flowable, this, seconds, activity);
    }

    /**
     * 取消http请求
     *
     * @param tag      标记
     * @param listener 回调
     */
    @Override
    public void cancelHttpDeal(int tag, HttpOnNextListener listener) {
        super.cancelHttpDeal(tag, listener);
    }

    @Override
    public Flowable getObservable(Retrofit retrofit) {
        return null;
    }
}
