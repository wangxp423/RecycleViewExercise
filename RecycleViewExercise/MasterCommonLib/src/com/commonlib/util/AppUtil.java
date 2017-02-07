package com.commonlib.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 
 * @author: wangxp
 * @date: 2016-08-23
 * @Desc: app工具类
 */
public class AppUtil {
	/**
	 * 获取版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		String versionName = null;
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			versionName = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return versionName;
		}
		return versionName;
	}

	/**
	 * 获取版本号
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		String packageName = context.getPackageName();
		int appVersionCode = 0;
		try {
			PackageInfo pkinfo = context.getPackageManager().getPackageInfo(packageName, 16384);
			appVersionCode = pkinfo.versionCode;
		} catch (NameNotFoundException e) {
			return appVersionCode;
		}
		return appVersionCode;
	}
}
