package com.commonlib.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 
 * @author: wangxp
 * @date: 2016-08-26
 * @Desc: WIFI 工具类
 */
public class WifiUtil {
	private static String[] WIFI_SSID = {"往返免费", "WangFan", "往返WIFI", "往返免费WIFI"};
	public static final String ACTION_WANGFAN_WIFI_CHANGE = "ACTION_WANGFAN_WIFI_CHANGE";
	public static final String EXTRA_IS_WANGFAN_WIFI = "EXTRA_IS_WANGFAN_WIFI";
	
	
	/**
	 * wifi是否连接
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnection(Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifiNetInfo.isConnected();
	}

	/**
	 * 获取当前WifoInfo
	 * 
	 * @param context
	 * @return
	 */
	private static WifiInfo getWifiInfo(Context context) {
		if (!isWifiConnection(context)) return null;
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		return wifiInfo;
	}

	public static int getLinkDeviceNum(){
		int count = 0;
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("/proc/net/arp"));
			String line = reader.readLine();
			//读取第一行信息，就是IP address HW type Flags HW address Mask Device
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split("[ ]+");
				if (tokens.length < 6) {
					continue;
				}
				String ip = tokens[0]; //ip
				String mac = tokens[3];  //mac 地址
				String flag = tokens[2];//表示连接状态 "0x0"应该是未连接，mac为0  "0x2"连接mac正确
				if (!TextUtils.isEmpty(flag) && flag.equals("0x2")){
					builder.append("count = " + count++).append("   mac = " + mac).append("\n");
				}
			}
			Log.d("Test",builder.toString());
			return count;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			}
			catch (IOException e) {
			}
		}
		return count;
	}

	/**
	 * 获取mac地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getMac(Context context) {
		WifiInfo wifiInfo = getWifiInfo(context);
		return null == wifiInfo ? null : wifiInfo.getMacAddress();
	}
	
	/**
	 * 获取ssid地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getSsid(Context context) {
		WifiInfo wifiInfo = getWifiInfo(context);
		return null == wifiInfo ? null : wifiInfo.getSSID();
	}

	/**
	 * 是否是往返wifi
	 * 
	 * @param ssid
	 * @return
	 */
	private static boolean isWangfanWifi(String ssid) {
		if (ssid == null) return false;
		for (String string : WIFI_SSID) {
			if (ssid.contains(string)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否是往返wifi
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWangfanWifi(Context context) {
		WifiInfo wifiInfo = getWifiInfo(context);
		final String ssid = null == wifiInfo ? null : wifiInfo.getSSID();
		return isWangfanWifi(ssid);
	}

	/**
	 * 周边是否有往返wifi 不为null or "" 即为存在
	 * 
	 * @param context
	 * @return
	 */
	public static String findWangfanWifi(Context context) {
//	    if (!isWifiConnection(context)) return null;
		ArrayList<ScanResult> wifiList = getWangFanWifiList(context);
		if (null != wifiList && wifiList.size()>0) {
			return wifiList.get(0).SSID;
		}
		return null;
	}
	
	/**
	 * 获取附近的往返wifi
	 * @param context
	 * @return
	 */
	private static ArrayList<ScanResult> getWangFanWifiList(Context context){
		ArrayList<ScanResult> wifiList = new ArrayList<ScanResult>();
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		List<ScanResult> scanResults = wifiManager.getScanResults();
		if(null == scanResults || scanResults.size() == 0){
			return null;
		}
		for (ScanResult scanResult : scanResults) {
			final String ssid = scanResult.SSID;
			if (isWangfanWifi(ssid)) {
//				Log.d("Test","ssid = " + ssid + "   level = " + scanResult.level);
				wifiList.add(scanResult);
			}
		}
		Collections.sort(wifiList, new ScanComparator());
		return wifiList;
	}
	
	private static class ScanComparator implements Comparator<ScanResult>{

		@Override
		public int compare(ScanResult lhs, ScanResult rhs) {
			return rhs.level - lhs.level;
		}
		
	}

	/**
	 * 链接指定wifi
	 */
	public static void linkWangfanWifi(Context context, String ssid) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + ssid + "\"";
		// config.SSID = "\"" + "@往返免费Wi-Fi" + "\""; // 这里暂时写死一个
		// 没有密码情况
		config.wepKeys[0] = "\"" + "\"";
		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		config.wepTxKeyIndex = 0;
		int netID = wifiManager.addNetwork(config);
		wifiManager.enableNetwork(netID, true);
	}
	

}
