package com.commonlib.retrofit_rx.http;

import android.app.Activity;

import com.commonlib.retrofit_rx.RxRetrofitApp;
import com.commonlib.retrofit_rx.api.BaseApi;
import com.commonlib.retrofit_rx.exception.RetryWhenNetworkException;
import com.commonlib.retrofit_rx.http.func.ExceptionFunc;
import com.commonlib.retrofit_rx.http.func.ResulteFunc;
import com.commonlib.retrofit_rx.listener.HttpOnNextListener;
import com.commonlib.retrofit_rx.subscribers.NormalSubscriber;
import com.commonlib.retrofit_rx.subscribers.ProgressSubscriber;
import com.iwangfan.foundationlibary.utils.LogUtils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @类描述：http交互处理
 * @创建人：Wangxiaopan
 * @创建时间：2017/6/5 09:51
 * @修改人：
 * @修改时间：2017/6/5 09:51
 * @修改备注：
 */
public class HttpManager {
    private static final HashMap<String, Disposable> mRequestQue = new HashMap<String, Disposable>(10);

    public HttpManager() {

    }

    /**
     * 处理http请求
     *
     * @param baseApi 封装的请求数据
     */
    public void doHttpDeal(int tag, HttpOnNextListener listener, final BaseApi baseApi) {
        Retrofit retrofit = getReTrofit(baseApi.getConnectionTime(), baseApi.getBaseUrl());
        httpDeal(tag, listener, baseApi.getObservable(retrofit), baseApi, 0);
    }

    /**
     * 处理http请求
     *
     * @param baseUrl 单个Service配置的url
     * @param baseApi base封装的请求数据
     */
    public void doHttpDeal(int tag, HttpOnNextListener listener, String baseUrl, final BaseApi baseApi) {
        Retrofit retrofit = getReTrofit(baseApi.getConnectionTime(), baseUrl);
        httpDeal(tag, listener, baseApi.getObservable(retrofit), baseApi, 0);
    }


    /**
     * 获取Retrofit对象
     *
     * @param connectTime
     * @param baseUrl
     * @return
     */
    public Retrofit getReTrofit(int connectTime, String baseUrl) {
        //手动创建一个OkHttpClient并设置超时时间缓存等设置
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(connectTime, TimeUnit.SECONDS);
        if (RxRetrofitApp.isDebug()) {
            builder.addInterceptor(getHttpLoggingInterceptor());
        }
        //这里是连接器缓存，暂时不用(用数据库缓存)
//        CacheInterceptor cacheInterceptor = new CacheInterceptor();
//        builder.cache(cacheInterceptor.getCachePath()).addNetworkInterceptor(cacheInterceptor);
//        builder.addInterceptor(new EnhancedCacheInterceptor());

        /*创建retrofit对象*/
        final Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
        return retrofit;
    }


    /**
     * RxRetrofit处理(无回调)
     *
     * @param flowable
     * @param baseApi
     * @param seconds  延迟时间S
     */
    public void httpDeal(Flowable flowable, BaseApi baseApi, int seconds) {
        flowable = configFlowable(flowable, baseApi, 0);
        ResourceSubscriber<String> subscriber = getResourceSubscriber(-1, null, baseApi);
        if (seconds > 0) flowable.delay(seconds, TimeUnit.SECONDS);
        flowable.subscribe(subscriber);
    }


    /**
     * RxRetrofit处理(默认)
     *
     * @param tag
     * @param listener
     * @param flowable
     * @param baseApi
     * @param seconds  延迟时间S
     */
    public void httpDeal(final int tag, final HttpOnNextListener listener, Flowable flowable, BaseApi baseApi, int seconds) {
        flowable = configFlowable(flowable, baseApi, seconds);
        ResourceSubscriber<String> subscriber = getResourceSubscriber(tag, listener, baseApi);
        flowable.subscribe(subscriber);
    }


    /**
     * RxRetrofit处理(默认带progressDialog)
     *
     * @param tag
     * @param listener
     * @param flowable
     * @param baseApi
     * @param seconds  延迟时间S
     */
    public void httpDealDialog(final int tag, final HttpOnNextListener listener, Flowable flowable, BaseApi baseApi, int seconds, Activity activity) {
        flowable = configFlowable(flowable, baseApi, seconds);
        ProgressSubscriber subscriber = getProgressSubscriber(tag, listener, activity, baseApi);
        flowable.subscribe(subscriber);
    }

    private void addQue(int tag, HttpOnNextListener callback, Disposable disposable) {
        Disposable queDisposable = null;
        if (null == callback) return;
        final String key = callback.hashCode() + "_" + tag;
        queDisposable = mRequestQue.get(key);
        if (null == queDisposable) {
            mRequestQue.put(key, disposable);
        }
    }

    private void removeQue(int tag, HttpOnNextListener callback) {
        Disposable queSubscriber = null;
        if (null == callback) return;
        final String key = callback.hashCode() + "_" + tag;
        queSubscriber = mRequestQue.get(key);
        if (null != queSubscriber) {
            mRequestQue.remove(queSubscriber);
        }
    }

    public void cancelHttpDeal(int tag, HttpOnNextListener callback) {
        if (null == callback) return;
        final String key = callback.hashCode() + "_" + tag;
        Disposable disposable = mRequestQue.get(key);
        if (null != disposable) disposable.dispose();
    }

    public Flowable configFlowable(Flowable flowable, BaseApi baseApi, int seconds) {
        /*失败后的retry配置*/
        flowable = flowable.retryWhen(new RetryWhenNetworkException(baseApi.getRetryCount(),
                baseApi.getRetryDelay(), baseApi.getRetryIncreaseDelay()))
                .delay(seconds, TimeUnit.SECONDS)
                /*异常处理*/
                .onErrorResumeNext(new ExceptionFunc())
                /*生命周期管理*/
//                .compose(appCompatActivity.bindToLifecycle())
                //Note:手动设置在activity onDestroy的时候取消订阅
//                .compose(appCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                /*返回数据统一判断*/
                .map(new ResulteFunc())
                /*http请求线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread());
        return flowable;
    }

    /**
     * 获取订阅者(默认)
     *
     * @param tag
     * @param listener
     * @return
     */
    public ResourceSubscriber<String> getResourceSubscriber(final int tag, final HttpOnNextListener listener, BaseApi baseApi) {
        return new NormalSubscriber(tag, listener, baseApi);
    }

    /**
     * 获取订阅者(默认带progressDialog)
     *
     * @param tag
     * @param listener
     * @param activity
     * @return
     */
    public ProgressSubscriber getProgressSubscriber(int tag, HttpOnNextListener listener, Activity activity, BaseApi baseApi) {
        ProgressSubscriber subscriber = new ProgressSubscriber(tag, listener, activity, baseApi);
        return subscriber;
    }

    /**
     * 日志输出
     * 自行判定是否添加
     *
     * @return
     */
    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.d("RxRetrofit", "Retrofit---->Message:" + message);
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }

}
