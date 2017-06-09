package com.commonlib.retrofit_rx.exception;

/**
 * @类描述：回调统一请求异常
 * @创建人：Wangxiaopan
 * @创建时间：2017/6/5 09:51
 * @修改人：
 * @修改时间：2017/6/5 09:51
 * @修改备注：
 */
public class ApiException extends Exception {
    /*错误码*/
    private int code;
    /*显示的信息*/
    private String displayMessage;

    public ApiException(Throwable e) {
        super(e);
    }

    public ApiException(Throwable cause, @CodeException.CodeEp int code, String showMsg) {
        super(showMsg, cause);
        setCode(code);
        setDisplayMessage(showMsg);
    }

    @CodeException.CodeEp
    public int getCode() {
        return code;
    }

    public void setCode(@CodeException.CodeEp int code) {
        this.code = code;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }
}
