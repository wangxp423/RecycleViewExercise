package com.commonlib.retrofit_rx.subscribers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.commonlib.retrofit_rx.api.BaseApi;
import com.commonlib.retrofit_rx.listener.HttpOnNextListener;

/**
 * @类描述：带loading的Subscriber
 * @创建人：Wangxiaopan
 * @创建时间：2017/6/6 16:51
 * @修改人：
 * @修改时间：2017/6/6 16:51
 * @修改备注：
 */

public class ProgressSubscriber extends BaseResourceSubscriber<String> {
    private BaseApi baseApi;
    private ProgressDialog progressDialog;
    private HttpOnNextListener listener;
    private Activity activity;
    private int tag;
    private String url;

    public ProgressSubscriber(int tag, HttpOnNextListener callback, Activity activity, BaseApi baseApi) {
        super(tag, callback, baseApi);
        this.tag = tag;
        this.listener = callback;
        this.activity = activity;
        this.baseApi = baseApi;
        this.url = baseApi.getUrl();
        if (baseApi.isShowProgress()) {
            initProgressDialog(baseApi.isCancel());
        }
    }


    /**
     * 初始化加载框
     */
    private void initProgressDialog(boolean cancel) {
        if (progressDialog == null && activity != null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setCancelable(cancel);
            if (cancel) {
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                    }
                });
            }
        }
    }


    /**
     * 显示加载框
     */
    private void showProgressDialog() {
        if (!isShowPorgress()) return;
        if (progressDialog == null || activity == null) return;
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    /**
     * 隐藏
     */
    private void dismissProgressDialog() {
        if (!isShowPorgress()) return;
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public boolean isShowPorgress() {
        return baseApi.isShowProgress();
    }

    @Override
    protected void onStart() {
        showProgressDialog();
        super.onStart();
    }

    @Override
    public void onNext(String result) {
        super.onNext(result);
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        dismissProgressDialog();
    }

    @Override
    public void onComplete() {
        super.onComplete();
        dismissProgressDialog();
    }
}
