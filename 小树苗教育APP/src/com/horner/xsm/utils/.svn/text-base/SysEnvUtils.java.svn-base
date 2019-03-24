package com.horner.xsm.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

import com.horner.xsm.R;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SysEnvUtils {

    /**
     * 获取应用程序版本（versionName）
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            Log.e("info", "获取应用程序版本失败，原因：" + e.getMessage());
            return "";
        }

        return info.versionName;
    }
    
    //获取手机ip地址
    public static String getPhoneIp() { 
        try { 
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) { 
                NetworkInterface intf = en.nextElement(); 
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) { 
                    InetAddress inetAddress = enumIpAddr.nextElement(); 
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) { 
                        return inetAddress.getHostAddress().toString(); 
                    } 
                } 
            } 
        } catch (Exception e) { 
        } 
        return "127.0.0.1"; 
    }
    
    
    //获取终端设备id号
    public static String getImieStatus(Context context) {
		String android_id = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		return android_id;
	}
    //获取设备物理内存(RAM)的大小
    public static  long getTotalMemory(Context context) {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i("info", num + "\t");
			}
			Log.i("info","孙的大小为1："+arrayOfString[1]);
			initial_memory = Long.parseLong(arrayOfString[1]) * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			Log.i("info","孙的大小为1："+initial_memory);
			localBufferedReader.close();

		} catch (IOException e) {
		}
		return initial_memory / (1024 * 1024);
	}

    //检测是否安装某个app
    public static boolean isAvilible( Context context, String packageName ){
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for ( int i = 0; i < pinfo.size(); i++ ){
            if(pinfo.get(i).packageName.equalsIgnoreCase(packageName)){
            	 return true;
            }
        }
        return false;
    }
    
    
    private static void showBuySuccess(String msg,Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		//getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		final Dialog successDialog = new Dialog(context, R.style.translucent_Dialog);
		Window window = successDialog.getWindow();
		android.view.WindowManager.LayoutParams params = window.getAttributes();
		params.width = width / 7 * 4;
		params.height = height / 10 * 6;
		window.setAttributes(params);
		View successView = LayoutInflater.from(context).inflate(R.layout.buy_success,null);
		TextView tvMsg = (TextView) successView.findViewById(R.id.msg);
		tvMsg.setText(msg);
		TextView sure = (TextView) successView.findViewById(R.id.sure);
		successDialog.setContentView(successView);
		successDialog.show();
		successDialog.setCanceledOnTouchOutside(true);
		sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != successDialog) {
					successDialog.dismiss();
				}
			}
		});
	}
}
