package com.commonlib.retrofit_rx.http.func;

import com.commonlib.retrofit_rx.exception.FactoryException;
import com.iwangfan.foundationlibary.utils.LogUtils;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @类描述：异常处理
 * @创建人：Wangxiaopan
 * @创建时间：2017/6/5 09:51
 * @修改人：
 * @修改时间：2017/6/5 09:51
 * @修改备注：
 */
public class ExceptionFunc implements Function<Throwable, Flowable> {

    @Override
    public Flowable apply(@NonNull Throwable throwable) throws Exception {
        LogUtils.e("Subscriber", "-------->" + throwable.getMessage());
        return Flowable.error(FactoryException.analysisExcetpion(throwable));
    }
}
