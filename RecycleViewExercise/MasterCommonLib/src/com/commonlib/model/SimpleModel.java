package com.commonlib.model;


/**
 * 
 * @author: wangxp
 * @date: 2016-07-12
 * @Desc: 默认基类
 */
public class SimpleModel implements BaseModel{
	public static final String CODE_SUCCESS = "0";
	private String code;
	private String msg;
	private String time;

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public String getTime() {
		return time;
	}

}
