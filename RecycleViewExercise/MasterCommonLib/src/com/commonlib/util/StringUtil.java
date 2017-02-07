package com.commonlib.util;

import android.text.TextUtils;

import java.util.Locale;
import java.util.Map;

/**
 * 
 * @author: wangxp
 * @date: 2016-08-15
 * @Desc: 字符串工具类
 */
public class StringUtil {

	/**
	 * 过滤iccid
	 * @param iccid
	 * @return
	 */
	public static String filterIccid(String iccid){
		//只允许数字
//		String regEx = "[^0-9]";
//		Pattern pattern = Pattern.compile(regEx);
//		Matcher matcher = pattern.matcher(iccid);
//		return matcher.replaceAll("").trim();
		if (iccid.startsWith("ICCID") || iccid.startsWith("iccid")) {
			String newIccid = iccid.substring(5, iccid.length());
			return filterInput(newIccid);
		}
		return filterInput(iccid);
	}
	
	/**
	 * 过滤扫码SN
	 * @param sn
	 * @return
	 */
	public static String filterSn(String sn){
		return filterInput(sn);
	}
	
	
	/**
	 * 过滤输入 只能是字母数字
	 * @param input
	 * @return
	 */
	public static String filterInput(String input){
		return input.replaceAll("[^a-z^A-Z^0-9]","").toUpperCase(Locale.US);
	}

	/**
	 * 单引号 变 双引号
	 *
	 * @param arg
	 * @return
	 */
	public static String convertOnetoTwoSingleQuote(String arg) {
		String str = "";
		if (!TextUtils.isEmpty(arg)) {
			str = arg.replace("'", "''");
		}
		return str;
	}

	/**
	 * 返回指定Map中指定key的value值
	 *
	 * @param responeMap
	 * @param key
	 * @return
	 */
	public static String get(Map<String, Object> responeMap, String key) {
		String value;
		if (responeMap.get(key) == null || "null".equals(responeMap.get(key).toString())) {
			value = "";
		} else {
			value = responeMap.get(key).toString();
		}
		return value;
	}
}
