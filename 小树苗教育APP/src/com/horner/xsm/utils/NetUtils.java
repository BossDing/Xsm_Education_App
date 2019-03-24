package com.horner.xsm.utils;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;


import com.horner.xsm.constants.Constants;
import com.horner.xsm.data.ThirdVipUserCache;
import com.horner.xsm.data.UserCache;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NetUtils {
	private static HttpHandler<String> xHandler = null;
	private static HttpUtils httpUtils = null;
	private static RequestParams params;

	public static void savePersonInfo(String id, String nickName,
			String userSex, String babyBirth,String userAddress, final Context context, final Handler mHandler) {
		getSingleInstance();
		params = new RequestParams();
		//File imagePath = new File(uploadFilepath);
		//params.addBodyParameter("multipartFile", imagePath);
		params.addBodyParameter("id", id);
		params.addBodyParameter("nickName", nickName);
		params.addBodyParameter("userSex", userSex);
		params.addBodyParameter("babyBirth", babyBirth);
		params.addBodyParameter("userAddress", userAddress);
		Log.i("info", "params的d :" + nickName);
		xHandler = httpUtils.send(HttpMethod.POST,"http://szcblm.horner.cn:8080/alliance/front/reg/infor",
				params, new RequestCallBack<String>() {

					private ProgressDialog pDialog;

					@Override
					public void onStart() {
						//httpUtils.configSoTimeout(5000);
						super.onStart();
						pDialog = new ProgressDialog(context);
						pDialog.setTitle("信息保存");
						pDialog.setCancelable(true);
						pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
						pDialog.show();
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						int currentProgress = (int) ((current / (float) total) * 100);
						pDialog.setProgress(currentProgress);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Log.i("info", arg0 + arg1 + " ");
						ToastUtil.showShortToast(context, "保存信息失败,请检查网络是否可用!");
						pDialog.dismiss();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						Log.i("info", "信息保存成功之后数据为" + arg0.result);
						pDialog.dismiss();
						Message msg = Message.obtain();
						msg.what = 0;
						msg.obj = arg0.result; 
						mHandler.sendMessage(msg);
						return;
					}

				});
	}

	
	
	
	
	
	public static void uploadImage(String id,final Context context, final Handler mHandler,String imagePath,final ProgressBar progressBar) {
		getSingleInstance();
		params = new RequestParams();
		// 参数1：服务器端接收的表单控件的name值
		// 参数2：客户端要上传的file对象
		File file = new File(imagePath);
		if(null != file){
			params.addBodyParameter("multipartFile",new File(imagePath));
		}
		progressBar.setVisibility(View.VISIBLE);
		params.addBodyParameter("userId", id);
		Log.i("info","头像上传的参数:"+"id:"+id+"imagePath"+imagePath);
		xHandler = httpUtils.send(HttpMethod.POST,"http://szcblm.horner.cn:8080/alliance/front/reg/uploadPicture",
				params, new RequestCallBack<String>() {

					private ProgressBar pBar;

					@Override
					public void onStart() {
						//httpUtils.configSoTimeout(5000);
						super.onStart();
						pBar = new ProgressBar(context);
						pBar.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Log.i("info", arg0 + arg1 + " ");
						ToastUtil.showShortToast(context, "头像上传失败,请检查网络是否可用!");
						progressBar.setVisibility(View.GONE);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						Log.i("info", "头像上传之后数据为" + arg0.result);
						String info = arg0.result;
						try {
							JSONObject object = new JSONObject(info);
							String code = object.optString("code");
							if(null != code && !code.equals("")){
								if(code.equals("1000")){
									UserCache user = new UserCache(context);
									progressBar.setVisibility(View.GONE);
									ToastUtil.showShortToast(context,"头像上传成功");
									String userIcon = object.optString("userIcon");
									String userId = object.optString("userId");
									user.setMultipartFile(Constants.BASE_PIC + userIcon);
									new ThirdVipUserCache(context).setMultipartFile(Constants.BASE_PIC + userIcon);
									user.setUserId(userId);
								}else if(code.equals("1001")){
									ToastUtil.showShortToast(context,"用户ID为空");
								}else if(code.equals("1002")){
									ToastUtil.showShortToast(context,"头像上传失败");
									progressBar.setVisibility(View.GONE);
								}else if(code.equals("1003")){
									ToastUtil.showShortToast(context,"头像上传异常");
									progressBar.setVisibility(View.GONE);
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
	
	
	
	public static void getSingleInstance() {
		if (httpUtils == null) {
			httpUtils = new HttpUtils();
		}
		
	}

}
