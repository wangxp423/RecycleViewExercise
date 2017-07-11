package com.commonlib.entity;

/**
 * @类描述：数据处理基类
 * @创建人：Wangxiaopan
 * @创建时间：2017/7/11 0011 10:47
 * @修改人：
 * @修改时间：2017/7/11 0011 10:47
 * @修改备注：
 */

public class BaseResultEntity<T> {
    private int code; //状态码
    private String msg; //状态消息
    private T data; //返回数据

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
