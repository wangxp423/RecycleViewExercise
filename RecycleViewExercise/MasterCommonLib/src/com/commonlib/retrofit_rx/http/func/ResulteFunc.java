package com.commonlib.retrofit_rx.http.func;


import com.commonlib.retrofit_rx.exception.HttpTimeException;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @类描述：服务器返回数据判断
 * @创建人：Wangxiaopan
 * @创建时间：2017/6/5 09:51
 * @修改人：
 * @修改时间：2017/6/5 09:51
 * @修改备注：
 */
public class ResulteFunc implements Function<Object, Object> {

    @Override
    public Object apply(@NonNull Object o) throws Exception {
        if (o == null || "".equals(o.toString())) {
            throw new HttpTimeException("数据错误");
        }
        return o;
    }
}
