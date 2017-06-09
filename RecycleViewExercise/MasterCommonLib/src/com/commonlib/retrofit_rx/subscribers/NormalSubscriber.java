package com.commonlib.retrofit_rx.subscribers;

import com.commonlib.retrofit_rx.api.BaseApi;
import com.commonlib.retrofit_rx.listener.HttpOnNextListener;

/**
 * @类描述：默认Subscriber
 * @创建人：Wangxiaopan
 * @创建时间：2017/6/7 0007 16:34
 * @修改人：
 * @修改时间：2017/6/7 0007 16:34
 * @修改备注：
 */

public class NormalSubscriber extends BaseResourceSubscriber<String> {

    public NormalSubscriber(int tag, HttpOnNextListener callback, BaseApi baseApi) {
        super(tag, callback, baseApi);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onNext(String result) {
        super.onNext(result);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
    }

    @Override
    public void onComplete() {
        super.onComplete();
    }
}
