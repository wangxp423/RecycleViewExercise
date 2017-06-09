package com.commonlib.retrofit_rx.http.cache;

import com.iwangfan.foundationlibary.utils.LogUtils;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @类描述：post缓存处理(一个思路)
 * @创建人：Wangxiaopan
 * @创建时间：2017/6/8 0008 16:43
 * @修改人：
 * @修改时间：2017/6/8 0008 16:43
 * @修改备注：
 */

public class EnhancedCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        String url = request.url().toString();
        RequestBody requestBody = request.body();
        Charset charset = Charset.forName("UTF-8");
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        LogUtils.d("Test", "request.Method = " + request.method());
        if (request.method().equals("POST")) {
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(Charset.forName("UTF-8"));
            }
            Buffer buffer = new Buffer();
            try {
                requestBody.writeTo(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb.append(buffer.readString(charset));
            buffer.close();
        }
        LogUtils.d("Test", "url = " + sb.toString());
        ResponseBody responseBody = response.body();
        MediaType contentType = responseBody.contentType();

        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();

        if (contentType != null) {
            charset = contentType.charset(Charset.forName("UTF-8"));
        }
        String key = sb.toString();
        //服务器返回的json原始数据
        String json = buffer.clone().readString(charset);
        LogUtils.d("Test", "put cache-> key:" + key + "-> json:" + json);
        return chain.proceed(request);
    }
}
