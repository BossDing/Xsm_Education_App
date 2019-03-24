package com.horner.xsm.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mouee.common.HttpManager;

public class WifiUtils {
	public static boolean isWifiConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetworkInfo.isConnected()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @author 作者 : sun
	 * @date 创建时间：2016-1-4 下午8:35:10
	 * @return
	 * @description 检查是否有网络
	 */
	public static Boolean isNetWorkActive(Context context, String msg) {
		boolean internet = HttpManager.isConnectingToInternet(context);
		if (internet) {
			return true;
		} else {
			ToastUtil.showShortToast(context, msg);
			return false;
		}
	}

	//检查是否有网络
	public static boolean isConnectingToInternet(Context activity) {
		ConnectivityManager connectivity = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
		}
		return false;
	}
	
	
	/**
	 * 判断网络是否可用
	 * 
	 * @return 是/否
	 */
	public static boolean isAvailable(Context mContext) {
		ConnectivityManager manager = (ConnectivityManager) mContext
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);
		if (null == manager) {
			return false;
		}
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (null == networkInfo || !networkInfo.isAvailable()) {
			return false;
		}
		return true;
	}
}
