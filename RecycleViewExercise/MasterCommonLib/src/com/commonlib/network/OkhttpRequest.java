package com.commonlib.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkhttpRequest {
    private final int CONNECTION_TIMEOUT = 10 * 1000;
    private final int READ_TIMEOUT = 15 * 1000;

    protected Handler mHandler = new Handler(Looper.getMainLooper());
    private OkHttpClient mOkHttpClient;
    private static final HashMap<String, Call> mRequestQue = new HashMap<String, Call>(10);
    private HashMap<String, String> mHeaders;

    //post方式提交图片
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
    //post方式提交String
//    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");

    //请求回调
    public interface HttpCallback {
        void onHttpResponse(int tag, String response);

        void onHttpRespFail(int tag, String error);
    }

    public OkhttpRequest() {
        getOkHttpClient();
    }

    public OkHttpClient getOkHttpClient() {
        if (null == mOkHttpClient) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
            clientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
            mOkHttpClient = clientBuilder.build();
        }
        return mOkHttpClient;
    }

    private void addQue(int tag, HttpCallback callback, Call call) {
        Call queCall = null;
        if (null == callback) return;
        final String key = callback.hashCode() + "_" + tag;
        queCall = mRequestQue.get(key);
        if (null == queCall) {
            mRequestQue.put(key, call);
        }
    }

    private void removeQue(int tag, HttpCallback callback) {
        Call queCall = null;
        if (null == callback) return;
        final String key = callback.hashCode() + "_" + tag;
        queCall = mRequestQue.get(key);
        if (null != queCall) {
            mRequestQue.remove(key);
        }
    }

    public static void cancel(int tag, HttpCallback callback) {
        if (null == callback) return;
        final String key = callback.hashCode() + "_" + tag;
        Call call = mRequestQue.get(key);
        if (null != call) call.cancel();
    }

    private void excute(final int tag, final HttpCallback callback, Call call) {
        addQue(tag, callback, call);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final int code = response.code();
                final String responseBody = response.body().string();
                Log.d("OkhttpRequest", "request.onResponse() = " + responseBody);
                removeQue(tag, callback);
                call.cancel();
                if (200 == code) {
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            callback.onHttpResponse(tag, responseBody);
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            callback.onHttpRespFail(tag, "error_code: " + code);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("OkhttpRequest", "request.onFailure()");
                removeQue(tag, callback);
                if (call.isCanceled()) return;
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        callback.onHttpRespFail(tag, "error_msg: network_error");
                    }
                });
            }
        });
    }

    private void excuteAsync(final int tag, final HttpCallback callback, Call call) {
        addQue(tag, callback, call);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final int code = response.code();
                final String responseBody = response.body().string();
                Log.d("OkhttpRequest", "request.onResponse() = " + responseBody);
                removeQue(tag, callback);
                call.cancel();
                if (200 == code) {
                    callback.onHttpResponse(tag, responseBody);
                } else {
                    callback.onHttpRespFail(tag, "error_code: " + code);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("OkhttpRequest", "request.onFailure()");
                removeQue(tag, callback);
                if (call.isCanceled()) return;
                callback.onHttpRespFail(tag, "error_msg: network_error");

            }
        });
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.mHeaders = headers;
    }

    public Request.Builder initRequestBuilder() {
        Request.Builder builder = new Request.Builder();
        if (null != mHeaders && mHeaders.size() > 0) {
            for (Map.Entry<String, String> iterable_element : mHeaders.entrySet()) {
                builder.addHeader(iterable_element.getKey(), iterable_element.getValue());
            }
        }
        return builder;
    }

    public Request.Builder initRequestBuilder(HashMap<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        if (null != headers && headers.size() > 0) {
            for (Map.Entry<String, String> iterable_element : headers.entrySet()) {
                builder.addHeader(iterable_element.getKey(), iterable_element.getValue());
            }
        }
        return builder;
    }

    public Builder initFormBody(HashMap<String, String> params) {
        Builder body = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> iterable_element : params.entrySet()) {
                body.add(iterable_element.getKey(), iterable_element.getValue());
            }
        }
        return body;
    }

    public MultipartBody.Builder initMultipartBody(HashMap<String, String> params, HashMap<String, String> imgParams) {
        okhttp3.MultipartBody.Builder uploadBuilder = new MultipartBody.Builder();
        uploadBuilder.setType(MultipartBody.FORM);
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> iterable_element : params.entrySet()) {
                uploadBuilder.addFormDataPart(iterable_element.getKey(), null, RequestBody.create(MEDIA_TYPE_MARKDOWN, iterable_element.getValue()));
            }
        }
        if (imgParams != null && imgParams.size() > 0) {
            for (Map.Entry<String, String> img_element : imgParams.entrySet()) {
                uploadBuilder.addFormDataPart(img_element.getKey(), img_element.getValue(), RequestBody.create(MEDIA_TYPE_PNG, new File(img_element.getValue())));
            }
        }
        return uploadBuilder;
    }

    /**
     * http get 回调至主线程
     *
     * @param tag
     * @param url
     * @param callback
     */
    public void okHttpGet(int tag, String url, HttpCallback callback) {
        Log.d("OkhttpRequest", "Url.okHttpGet = " + url);
        //创建一个Request.Builder
        final Request.Builder builder = initRequestBuilder();
        builder.url(url);
        //new call
        Call call = mOkHttpClient.newCall(builder.build());
        //请求加入调度
        excute(tag, callback, call);

    }

    /**
     * http get 回调至主线程
     *
     * @param tag
     * @param url
     * @param headers
     * @param callback
     */
    public void okHttpGet(int tag, String url, HashMap<String, String> headers, HttpCallback callback) {
        Log.d("OkhttpRequest", "Url.okHttpGet = " + url);
        //创建一个Request.Builder
        final Request.Builder builder = initRequestBuilder(headers);
        builder.url(url);
        //new call
        Call call = mOkHttpClient.newCall(builder.build());
        //请求加入调度
        excute(tag, callback, call);

    }

    /**
     * http get 回调至子线程
     *
     * @param tag
     * @param url
     * @param callback
     */
    public void okHttpAsyncGet(int tag, String url, HttpCallback callback) {
        Log.d("OkhttpRequest", "Url.okHttpAsyncGet = " + url);
        //创建一个Request.Builder
        final Request.Builder builder = initRequestBuilder();
        builder.url(url);
        //new call
        Call call = mOkHttpClient.newCall(builder.build());
        //请求加入调度
        excuteAsync(tag, callback, call);

    }

    /**
     * http get 回调至子线程
     *
     * @param tag
     * @param url
     * @param headers
     * @param callback
     */
    public void okHttpAsyncGet(int tag, String url, HashMap<String, String> headers, HttpCallback callback) {
        Log.d("OkhttpRequest", "Url.okHttpAsyncGet = " + url);
        //创建一个Request.Builder
        final Request.Builder builder = initRequestBuilder(headers);
        builder.url(url);
        //new call
        Call call = mOkHttpClient.newCall(builder.build());
        //请求加入调度
        excuteAsync(tag, callback, call);

    }

    /**
     * http  post 回调至主线程
     *
     * @param tag
     * @param url
     * @param params
     * @param callback
     */
    public void okHttpPost(int tag, String url, HashMap<String, String> params, HttpCallback callback) {
        Log.d("OkhttpRequest", "Url.okHttpPost = " + url);
        //创建一个Request.Builder
        final Request.Builder builder = initRequestBuilder();
        builder.url(url).post(initFormBody(params).build());
        //new call
        Call call = mOkHttpClient.newCall(builder.build());
        //请求加入调度
        excute(tag, callback, call);

    }

    /**
     * http  post 回调至主线程
     *
     * @param tag
     * @param url
     * @param params
     * @param headers
     * @param callback
     */
    public void okHttpPost(int tag, String url, HashMap<String, String> params, HashMap<String, String> headers, HttpCallback callback) {
        Log.d("OkhttpRequest", "Url.okHttpPost = " + url);
        //创建一个Request.Builder
        final Request.Builder builder = initRequestBuilder(headers);
        builder.url(url).post(initFormBody(params).build());
        //new call
        Call call = mOkHttpClient.newCall(builder.build());
        //请求加入调度
        excute(tag, callback, call);

    }

    /**
     * http  post 回调至子线程
     *
     * @param tag
     * @param url
     * @param params
     * @param callback
     */
    public void okHttpAsyncPost(int tag, String url, HashMap<String, String> params, HttpCallback callback) {
        Log.d("OkhttpRequest", "Url.okHttpAsyncPost = " + url);
        //创建一个Request.Builder
        final Request.Builder builder = initRequestBuilder();
        builder.url(url).post(initFormBody(params).build());
        //new call
        Call call = mOkHttpClient.newCall(builder.build());
        //请求加入调度
        excuteAsync(tag, callback, call);

    }

    /**
     * http  post 回调至子线程
     *
     * @param tag
     * @param url
     * @param params
     * @param headers
     * @param callback
     */
    public void okHttpAsyncPost(int tag, String url, HashMap<String, String> params, HashMap<String, String> headers, HttpCallback callback) {
        Log.d("OkhttpRequest", "Url.okHttpAsyncPost = " + url);
        //创建一个Request.Builder
        final Request.Builder builder = initRequestBuilder(headers);
        builder.url(url).post(initFormBody(params).build());
        //new call
        Call call = mOkHttpClient.newCall(builder.build());
        //请求加入调度
        excuteAsync(tag, callback, call);

    }

    /**
     * http  post 图片上传(单图or多图)  回调至主线程
     *
     * @param tag
     * @param url
     * @param params
     * @param imgParams
     * @param callback
     */
    public void okHttpPostUploadImgs(int tag, String url, HashMap<String, String> params, HashMap<String, String> imgParams, HttpCallback callback) {
        Log.d("OkhttpRequest", "Url.okHttpPost.uploadImgs = " + url);
        //创建一个Request.Builder
        final Request.Builder builder = initRequestBuilder();
        builder.url(url).post(initMultipartBody(params, imgParams).build());
        //new call
        Call call = mOkHttpClient.newCall(builder.build());
        //请求加入调度
        excute(tag, callback, call);
    }

}
