package com.commonlib.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author: wangxp
 * @date: 2016-08-15
 * @Desc: 正则表达式 工具类
 */
public class RegexUtil {

	/**
	 * 检测字符串是不是手机号
	 * @param phoneNum
	 * @return
	 */
	public static final boolean isMobileNumber(String phoneNum) {
		Pattern p = Pattern.compile("^0?(1[34578])[0-9]{9}$");
		Matcher m = p.matcher(phoneNum);
		return m.matches();
	}

	/**
	 * 检测字符串是不是电话号码
	 */
	public static final boolean isPhoneNumber(String phoneNum) {
		Pattern p = Pattern.compile("^(0[0-9]{2,4}[ -]?)?[2-9][0-9]{6,7}([ -]?[0-9]{2,5})?$");
		Matcher m = p.matcher(phoneNum);
		return m.matches();
	}

	/**
	 * 校验车牌号
	 */
	public static final boolean isCarplate(String carPlate) {
		String regex = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[警京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{0,1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(carPlate.toUpperCase());
		return m.matches();
	}

}
