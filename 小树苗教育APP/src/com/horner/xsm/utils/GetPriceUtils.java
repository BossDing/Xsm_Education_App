package com.horner.xsm.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.StaticLayout;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hor.common.HttpManager;
import com.hor.common.StringUtils;
import com.horner.xsm.constants.Constants;
import com.horner.xsm.net.AsyncHttpClient;
import com.horner.xsm.net.AsyncHttpResponseHandler;
import com.horner.xsm.net.RequestParams;

/**
 * @author 作者 : sun
 * @date 创建时间：2016-3-7 上午10:08:55
 * @return
 * @description
 */

public class  GetPriceUtils<T> {
	public static <T> void getPrice(final Context context, final Class<T> cls,
			final Handler handler) {
		AsyncHttpClient client = new AsyncHttpClient(context);
		RequestParams param = new RequestParams();
		param.put("officeId", "40");
		client.post(Constants.GET_PRICE, param, new AsyncHttpResponseHandler() {
			private Message msg;

			@Override
			public void onSuccess(String result) {
				super.onSuccess(result);
				Log.i("info", "返回的价格为;" + result);
				if (!StringUtils.isEmpty(result)) {
					try {
						msg = Message.obtain();
						msg.what = 0;
						JSONArray array = new JSONArray(result);
						List<T> priceList = JSON.parseArray(array.toString(),
								cls);
						msg.obj = priceList;
					} catch (JSONException e) {
						msg.what = 1;
					}
					handler.sendMessage(msg);
				} else {
					Log.i("info", "解析下来的数据为空");
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				msg = Message.obtain();
				boolean internet = HttpManager
						.isConnectingToInternet((Activity) context);
				if (internet) {
					msg.what = 2;

					Toast.makeText(context, "登录失败,请重试。", Toast.LENGTH_SHORT)
							.show();
				} else {
					msg.what = 3;
					Toast.makeText(context, "登录失败,请检查网络是否可用。",
							Toast.LENGTH_SHORT).show();
				}
				handler.sendMessage(msg);
			}

		});
	}
}
