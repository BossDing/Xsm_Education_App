package com.horner.xsm.utils;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.hor.common.HttpManager;
import com.hor.common.StringUtils;
import com.horner.nread.framework.MyApplication;
import com.horner.xsm.R;
import com.horner.xsm.bean.WePay;
import com.horner.xsm.constants.Constants;
import com.horner.xsm.data.UserCache;
import com.horner.xsm.net.AsyncHttpClient;
import com.horner.xsm.net.AsyncHttpResponseHandler;
import com.horner.xsm.net.RequestParams;
import com.horner.xsm.ui.BookshelfActivity;
import com.horner.xsm.ui.HomeActivity;
import com.horner.xsm.ui.SettingActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author 作者 : sun
 * @date 创建时间：2016-3-22 下午3:00:39
 * @return
 * @description 执行网络请求,获取从后台拿到的订单信息,微信支付时
 */

public class WePayUtils {
	private static String numberString;
	private static String purchase;


	public static <T> void getPayInfo(final Handler handler, String moneyNum,
			final Context context, final Class<T> cls,String purchaseTime) {
		
		if(!AliPayUtils.isAvilible(context,"com.tencent.mm")){
			ToastUtil.showShortToast(context,"未安装微信客户端,请安装");
			return;
		}
		LoadingDialog.isLoading(context);
		float wePayPrice = Float.parseFloat(moneyNum);
		int prices = (int) (wePayPrice*100);
		numberString = moneyNum;
		purchase = purchaseTime;
		String price = String.valueOf(prices);
		ToastUtil.showShortToast(context,"开始调起微信支付");
		Log.e("info","手机ip地址为;"+SysEnvUtils.getPhoneIp());
		AsyncHttpClient client = new AsyncHttpClient(context);
		RequestParams param = new RequestParams();
		param.put("price",price);
		param.put("ip",SysEnvUtils.getPhoneIp());
		if (null != purchaseTime && purchaseTime.length() > 2) {
			param.put("body", purchaseTime);
		}else {
			param.put("body", purchaseTime+"个月会员");
		}
		param.put("detail", "描述");
		Log.i("info","param的数值为:"+param);
		client.post("http://szcblm.horner.cn:8080/alliance/wxpay/prepay",
				param, new AsyncHttpResponseHandler() {
					private Message msg;

					@Override
					public void onSuccess(String result) {
						super.onSuccess(result);
						Log.i("info", "微信返回订单信息为:;" + result);
						if (!StringUtils.isEmpty(result)) {
							try {
								msg = Message.obtain();
								msg.what = 0;
								JSONObject object = new JSONObject(result);
								T a = JSON.parseObject(object.toString(), cls);
								msg.obj = a;
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

							Toast.makeText(context, "获取微信订单信息失败,请重试。",
									Toast.LENGTH_SHORT).show();
						} else {
							msg.what = 3;
							Toast.makeText(context, "请检查网络是否可用。",
									Toast.LENGTH_SHORT).show();
						}
						// handler.sendMessage(msg);
					}

				});
	}
	
	
	
	
	
	
	public static  void getPayOrderInfo(final Context context) {
		AsyncHttpClient client = new AsyncHttpClient(context);
		RequestParams param = new RequestParams();
		param.put("nonceStr", Constants.nonceStr);
		Log.i("info","param的数值为:"+Constants.nonceStr);
		client.post("http://szcblm.horner.cn:8080/alliance/wxpay/transactionId",
				param, new AsyncHttpResponseHandler() {
					private Message msg;

					@Override
					public void onSuccess(String result) {
						super.onSuccess(result);
						Log.i("info", "微信返回订单信息为:;" + result);
						if (!StringUtils.isEmpty(result)) {
							Constants.wePayOrderNo = result;
							if (Constants.isVipOrMod != null && Constants.isVipOrMod.equals("mod")) {
								//微信支付成功之后传给自己后台服务器
								BuyCategory(numberString,context);
							}else if(Constants.isVipOrMod != null && Constants.isVipOrMod.equals("vip")) {
								//微信支付成功之后传给自己后台服务器
								toBase_ZHU(context,result);
							}else if(Constants.isVipOrMod != null && Constants.isVipOrMod.equals("python")) {
								//微信支付成功之后传给自己后台服务器
								pushPayInfosToServer(context);
							}
							
						} else {
							Log.i("info", "解析下来的数据为空");
						}
					}
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						boolean internet = HttpManager
								.isConnectingToInternet((Activity) context);
						if (internet) {
//							Toast.makeText(context, "获取微信订单信息失败,请重试。",
//									Toast.LENGTH_SHORT).show();
						} else {
//							msg.what = 3;
//							Toast.makeText(context, "请检查网络是否可用。",
//									Toast.LENGTH_SHORT).show();
						}
						// handler.sendMessage(msg);
					}

				});
	}
	
	
	/**
	 * 
	 * @author 作者 : SUN
	 * @date 创建时间：2015-12-31 下午8:17:04
	 * @return
	 * @description 支付成功之后给服务器的
	 */
	public static void toBase_ZHU(final Context context,final String result) {
		AsyncTask<String, String, String> loadDataTask = new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("userId",new UserCache(context).getUserId());
				param.put("rechargePrice", numberString);
				param.put("ip",SysEnvUtils.getPhoneIp());
				param.put("alipayNum",result);
				param.put("recordMac", SysEnvUtils.getImieStatus(context));
				param.put("memberMonth",purchase);
				param.put("officeId","40");
				String result = HttpManager.postDataToUrl(Constants.BASE_URL
						+ "vip/vipRecord", param);
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				LoadingDialog.finishLoading();
				if (!StringUtils.isEmpty(result)) {
					try {
						JSONObject object = new JSONObject(result);
						String code = object.getString("code");
						String vipType = object.getString("vipType");
						String startTime = object.getString("startTime");
						String endTime = object.getString("endTime");
						if (code != null && code.equals("1000")) {
							ToastUtil.showLongToast(context, "会员购买成功!");
							UserCache userCache = new UserCache(
									context);
							MyApplication.hasBuy = true; // 购买vip
							userCache.setVipType(vipType);
							userCache.setVipTypeStartTime(startTime);
							userCache.setVipTypeEndTime(endTime);
							Intent intent = new Intent("com.horner.settingshow");
							intent.putExtra("vip","vipInfo");
							context.sendBroadcast(intent);
						} else {
							if (code.equals("1001")) {
								ToastUtil.showLongToast(context, "用户ID为空!");
								return;
							} else if (code.equals("1002")) {
								ToastUtil.showLongToast(context, "充值金额为空!");
								return;
							} else if (code.equals("1003")) {
								ToastUtil.showLongToast(context, "用户IP为空!");
								return;
							} else if (code.equals("1004")) {
								ToastUtil.showLongToast(context, "支付宝交易流水号为空!");
								return;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					boolean internet = HttpManager.isConnectingToInternet((Activity) context);
					if (internet) {
						Toast.makeText(context, "充值失败,请重试!",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(context,
								"登录失败,请检查网络是否可用!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
		loadDataTask.execute("");
	}
	
	
	private static void BuyCategory(final String price,final Context context) {

		AsyncTask<String, String, String> loadDataTask = new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("userId", new UserCache(context).getUserId());
				param.put("categoryId", "221");
				param.put("officeId", "40");
				//if(null != Constants.wePayOrderNo)
				param.put("transNum",Constants.wePayOrderNo);
				param.put("price", price);
				Log.i("info", "param:" + param.toString());
				String result = HttpManager.postDataToUrl(Constants.BASE_URL
						+ "buy/category", param);
				Log.i("info", "result的数值为:" + result);
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				LoadingDialog.finishLoading();
				if (!StringUtils.isEmpty(result)) {
					try {
						JSONObject object = new JSONObject(result);
						// Log.i("in)
						String code = object.optString("code");
						if (code != null && code.equals("1000")) {
							ToastUtil.showShortToast(context,
									"分类购买成功");
							String isBuy = object.optString("isBuy");
							if (isBuy != null) {
								Log.i("info", "isBuy购买成功的数值为:" + isBuy);
								UserCache cache = new UserCache(
										context);
								cache.setHasBuy(true);
							}
						} else if (code.equals("1001")) {
							ToastUtil.showShortToast(context,
									"用户ID为空");
							return;
						} else if (code.equals("1002")) {
							ToastUtil.showShortToast(context,
									"分类ID为空");
							return;
						} else if (code.equals("1003")) {
							ToastUtil.showShortToast(context,
									"交易流水号为空");
							return;
						} else if (code.equals("1004")) {
							ToastUtil.showShortToast(context,
									"联盟成员ID为空");
							return;
						} else if (code.equals("1005")) {
							ToastUtil.showShortToast(context,
									"已经购买过了");
							return;
						} else if (code.equals("1006")) {
							ToastUtil.showShortToast(context,
									"购买价格不能为空或者零");
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					boolean internet = HttpManager
							.isConnectingToInternet((Activity) context);
					if (internet) {
						Toast.makeText(context, "出现异常,请重试!",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(context, "请检查网络是否可用!",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
		loadDataTask.execute("");
	}
	
	public static void pushPayInfosToServer(final Context context) {
		Log.e("info", "手机ip地址为;" + SysEnvUtils.getPhoneIp());
		AsyncHttpClient client = new AsyncHttpClient(context);
		RequestParams param = new RequestParams();
		param.put("pay_user_phone", new UserCache(context).getUserPhone());
		param.put("pay_order_number",Constants.nonceStr);
		param.put("pay_order_type", Constants.pay_order_type);
		param.put("pay_type", "0");
		param.put("pay_order_fee", numberString);
		param.put("pay_order_desc",Constants.extraInfo);
		param.put("pay_status", "0");
		param.put("pay_platform_type", "1");
		param.put("pay_order_time",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		param.put("pay_api_code",getPayCode(context,numberString));
		Log.i("info", "param的数值为:" + param);
		client.post("http://pdapi.xbsep.com/v1/third_pay/order_push/", param,
				new AsyncHttpResponseHandler() {
					private Message msg;

					@Override
					public void onSuccess(String result) {
						Log.i("info","调后天返回数据为:"+result);
						try {
							JSONObject object = new JSONObject(result);
							if (object.optBoolean("flag")) {
								String order_type;
								ToastUtil.showShortToast(context,"提交后台成功");
								if (Constants.pay_order_type.equals("1")) {
									order_type = "亲情通";
								} else if (Constants.pay_order_type.equals("2")) {
									order_type = "智能考勤卡押金";
								} else if (Constants.pay_order_type.equals("3")) {
									order_type = "智能学生证";
								} else if (Constants.pay_order_type.equals("4")) {
									order_type = "智能手表";
								} else {
									order_type = "5";
								}
								String msg = new UserCache(context).getUserPhone()+"用户,您已成功购买"+order_type;
								Intent intent = new Intent("com.horner.settingshow");
								intent.putExtra("msg",msg);
								context.sendBroadcast(intent);
								//showBuySuccess(msg,context);
							}else {
								ToastUtil.showShortToast(context,"提交后台失败");
							}
						} catch (JSONException e) {
							e.printStackTrace();
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
							Toast.makeText(context,
									"提交后台失败,请重试。", Toast.LENGTH_SHORT).show();
						} else {
							msg.what = 3;
							Toast.makeText(context, "请检查网络是否可用。",
									Toast.LENGTH_SHORT).show();
						}
					}

				});
	}
	
	private static   void showBuySuccess(String msg,Context context) {
		final Dialog successDialog = new Dialog(context, R.style.translucent_Dialog);
		Window window = successDialog.getWindow();
		android.view.WindowManager.LayoutParams params = window.getAttributes();
		params.width = Constants.width / 7 * 5;
		params.height = Constants.height / 10 * 6;
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

	private static MessageDigest md5 = null;

	public static String getMd5(String str) {
		 try {
			md5 = MessageDigest.getInstance("MD5");
			byte[] bs = md5.digest(str.getBytes());
			StringBuilder sb = new StringBuilder(40);
			for (byte x : bs) {
				if ((x & 0xff) >> 4 == 0) {
					sb.append("0").append(Integer.toHexString(x & 0xff));
				} else {
					sb.append(Integer.toHexString(x & 0xff));
				}
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
		
	}
	
	// 为python后台生成跨区攻击校验码
		public static String getPayCode(Context context,String aliPayMoney) {
			String code = new UserCache(context).getUserPhone() + "tpay_order_push"
					+ aliPayMoney;
			try {
				String utfCode = URLEncoder.encode(code, "utf-8");
				String apiCode = getMd5(utfCode).toUpperCase();
				return apiCode;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}
}
