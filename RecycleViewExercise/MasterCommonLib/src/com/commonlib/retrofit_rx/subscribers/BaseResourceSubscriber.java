package com.commonlib.retrofit_rx.subscribers;

import com.commonlib.retrofit_rx.api.BaseApi;
import com.commonlib.retrofit_rx.exception.ApiException;
import com.commonlib.retrofit_rx.exception.CodeException;
import com.commonlib.retrofit_rx.exception.HttpTimeException;
import com.commonlib.retrofit_rx.http.cache.CookieResulte;
import com.commonlib.retrofit_rx.listener.HttpOnNextListener;
import com.commonlib.retrofit_rx.utils.CookieDbUtil;
import com.iwangfan.foundationlibary.utils.LogUtils;
import com.iwangfan.foundationlibary.utils.NetworkUtils;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * @类描述：ResourceSubscriber基类
 * @创建人：Wangxiaopan
 * @创建时间：2017/6/9 0009 15:05
 * @修改人：
 * @修改时间：2017/6/9 0009 15:05
 * @修改备注：
 */

public abstract class BaseResourceSubscriber<T> extends ResourceSubscriber<T> {
    private BaseApi baseApi;
    private HttpOnNextListener listener;
    private int tag;
    private String url;

    public BaseResourceSubscriber(int tag, HttpOnNextListener listener, BaseApi baseApi) {
        this.tag = tag;
        this.listener = listener;
        this.baseApi = baseApi;
        this.url = baseApi.getUrl();
        baseApi.addQue(tag, listener, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d("Subscriber", "onStart");
        //有网络，缓存时间内 读取缓存
        readCache();
    }

    @Override
    public void onNext(T t) {
        LogUtils.d("Subscriber", "onNext.result = " + t.toString());
        saveCache(t.toString());
        baseApi.removeQue(tag, listener);
    }

    @Override
    public void onError(Throwable t) {
        LogUtils.d("Subscriber", "onError = " + t);
        if (baseApi.isCache()) {
            getCache();
        } else {
            errorDo(t);
        }
        baseApi.removeQue(tag, listener);
    }

    @Override
    public void onComplete() {
        LogUtils.d("Subscriber", "onComplete");
        baseApi.removeQue(tag, listener);
    }

    private void errorDo(Throwable e) {
        if (listener == null) return;
        if (e instanceof ApiException) {
            listener.onError((ApiException) e);
        } else if (e instanceof HttpTimeException) {
            HttpTimeException exception = (HttpTimeException) e;
            listener.onError(new ApiException(exception, CodeException.RUNTIME_ERROR, exception.getMessage()));
        } else {
            listener.onError(new ApiException(e, CodeException.UNKNOWN_ERROR, e.getMessage()));
        }
    }

    private void readCache() {
        /*缓存并且有网*/
        if (baseApi.isCache() && NetworkUtils.isConnected()) {
             /*获取缓存数据*/
            CookieResulte cookieResulte = CookieDbUtil.getInstance().queryCookieBy(url);
            if (cookieResulte != null) {
                long time = (System.currentTimeMillis() - cookieResulte.getTime()) / 1000;
                if (time < baseApi.getCookieNetWorkTime()) {//规定缓存时间
                    if (listener != null) {
                        listener.onNext(tag, cookieResulte.getResulte());
                    }
                    onComplete();
                    dispose();
                }
            }
        }
    }

    /**
     * 获取cache数据
     */
    private void getCache() {
        Flowable.just(url).subscribe(new ResourceSubscriber<String>() {
            @Override
            public void onNext(String s) {
                /*获取缓存数据*/
                CookieResulte cookieResulte = CookieDbUtil.getInstance().queryCookieBy(s);
                if (cookieResulte == null) {
                    errorDo(new HttpTimeException(HttpTimeException.NO_CHACHE_ERROR));
                    return;
                }
                long time = (System.currentTimeMillis() - cookieResulte.getTime()) / 1000;
                if (time < baseApi.getCookieNoNetWorkTime()) {
                    if (listener != null) {
                        listener.onNext(tag, cookieResulte.getResulte());
                    }
                } else {
                    CookieDbUtil.getInstance().deleteCookie(cookieResulte);
                    errorDo(new HttpTimeException(HttpTimeException.CHACHE_TIMEOUT_ERROR));
                }
            }

            @Override
            public void onError(Throwable t) {
                errorDo(t);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 更新并保存数据
     *
     * @param result
     */
    private void saveCache(String result) {
        /*缓存处理*/
        if (baseApi.isCache()) {
            CookieResulte resulte = CookieDbUtil.getInstance().queryCookieBy(url);
            long time = System.currentTimeMillis();
            /*保存和更新本地数据*/
            if (resulte == null) {
                resulte = new CookieResulte(url, result, time);
                CookieDbUtil.getInstance().saveCookie(resulte);
            } else {
                resulte.setResulte(result);
                resulte.setTime(time);
                CookieDbUtil.getInstance().updateCookie(resulte);
            }
        }
        if (listener != null) {
            listener.onNext(tag, result);
        }
    }
}
