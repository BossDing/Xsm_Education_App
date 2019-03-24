package com.horner.xsm.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.json.JSONObject;

import com.alipay.sdk.app.PayTask;
import com.horner.nread.framework.MyApplication;
import com.horner.xsm.alipay.SignUtils;
import com.horner.xsm.constants.Constants;
import com.horner.xsm.data.UserCache;
import com.horner.xsm.ui.BookshelfActivity;
import com.horner.xsm.ui.HomeActivity;
import com.horner.xsm.ui.SettingActivity;
import com.mouee.common.HttpManager;
import com.mouee.common.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * @author 作者 : sun
 * @date 创建时间：2015-12-31 下午8:14:01
 * @version 1.0
 * @parameter
 * @return
 */

public class AliPayUtils {
	// 商户PID
	public static final String PARTNER = "2088611202292214";
	// 商户收款账号
	public static final String SELLER = "2088611202292214";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALi6hD+xaIjysfZq"
			+ "D1clXr6eFLv6Aa84IhAubqgQ1sS4a2USOd25yArD7Xlab0gs5QbeVdKyhJqtn4b+"
			+ "hli9AonKlgEuiZnQoBcAP/69ktSRazI0TGW5TpaAXDOWnc/LLOt6gGh4c4b77BVV"
			+ "+sDIZhq3uFhgLjhCIavDI+UPqbq9AgMBAAECgYBK4OfcfYgXAvFaJfGj7HT0JVXY"
			+ "QGXQZZuBHaEjJPg5c1us829bTgQlQPgQmbTD99a6KwN0zYHsxPYGHUPN0oelLEar"
			+ "04VZsc6kBKeIxjep9n7BdQReHph+0xEzdwZ9Vhke4RyaZDBVWrXRSIbczD0SrNbY"
			+ "EiFPaNaYSNv4GjSlYQJBAOcSii1YowQ900IXUmO6HELTvS9VjlujUwyEXFOJ2lHf"
			+ "2hKK0IYGeHc6rXNHbxEwjs14ocw3Bl31IxksK8f1GyUCQQDMqBzGJS+WDu2CqnxS"
			+ "52UcioZqeD36bv3Diem+PGaafsZGnJCCaUOlL7lot5q7ddCWWK7Pn3xX5FMWw5bg"
			+ "EJm5AkEAq1hHGCsS7rE9t1N95695B2Dld3UU1AT/L1fy8otVVcMNfRRsXgXsTFU5"
			+ "izMcO/3q1pguOjVPRlrhhOgJKwQo9QJADfVxcPpcUonWHVFvhWAO/3FbVau53njv"
			+ "igTAEp+gB+2ZrHtFR//SA1RL+x56yUpx3a9SzQp9uqjyGd1hTwsS0QJBAJz3g3Xl"
			+ "9LJ4DJlLicUV5vqXjYuZRwpA/plhLCpKHKvnbJ9YL7dBeQao7W9ZtiHKofTr/oWz"
			+ "MCupWjNcgRDfRg0=";
	
	public static boolean isAvilible( Context context, String packageName ){
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        
        for ( int i = 0; i < pinfo.size(); i++ )
        {
        	Log.i("info","pacekage"+packageName);
        	//Log.i("info","pinfo"+pinfo.get(i).packageName);
            if(pinfo.get(i).packageName.equalsIgnoreCase(packageName)){
            	Log.i("info","------");
            	 return true;
            }
               
        }
        Log.i("info","000000");
        return false;
    }
	
	public static boolean isAppInstalled(Context context, String uri) {  
        PackageManager pm = context.getPackageManager();  
        boolean installed = false;  
        try {  
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);  
            installed = true;  
        } catch (PackageManager.NameNotFoundException e) {  
            installed = false;  
        }  
        return installed;  
    } 
	
	
	
	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	
	public static final int SDK_PAY_FLAG = 1;
	public static void pay(String moneyNum, String purchaseTime,final Handler handler,final Context context) {
		if (TextUtils.isEmpty(AliPayUtils.PARTNER)
				|| TextUtils.isEmpty(AliPayUtils.RSA_PRIVATE)
				|| TextUtils.isEmpty(AliPayUtils.SELLER)) {
			new AlertDialog.Builder(context)
					.setTitle("警告")
					.setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
								}
							}).show();
			return;
		}

		if (!AliPayUtils.isAvilible(context,
				"com.eg.android.AlipayGphone")) {
			Toast.makeText(context, "该设备不存在支付宝终端，请安装",
					Toast.LENGTH_SHORT).show();
			return;
		}

		// 订单 allPrice + ""
		String orderInfo = getOrderInfo(purchaseTime + "", "该测试商品的详细描述", moneyNum
				+ "");

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

			Runnable payRunnable = new Runnable() {
				@Override
				public void run() {
					// 构造PayTask 对象
					PayTask alipay = new PayTask((Activity) context);
					// 调用支付接口，获取支付结果
					String result = alipay.pay(payInfo);

					Message msg = new Message();
					msg.what = SDK_PAY_FLAG;
					msg.obj = result;
					handler.sendMessage(msg);
				}
			};
			// 必须异步调用
			Thread payThread = new Thread(payRunnable);
			payThread.start();
	}
	
	
	
	
	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public static String getOrderInfo(String subject, String body, String price) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + AliPayUtils.PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + AliPayUtils.SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"http://szcblm.horner.cn:8080/alliance/paperbook/alipay?tradId=\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		Log.e("MyorderActivity", "orderInfo ==>" + orderInfo);
		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public static String sign(String content) {
		return SignUtils.sign(content, AliPayUtils.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public static String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	
	
	
	
	
	
	
	public static void toBase_ZHU(final Context context,final String moneyNum,final String purchaseTime,final Handler handler) {
		Log.i("swt","result的数值为:"+moneyNum+"ffff"+purchaseTime);
		LoadingDialog.isLoading(context);
		AsyncTask<String, String, String> loadDataTask = new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("userId", new UserCache(context).getUserId());
				param.put("rechargePrice", moneyNum);
				param.put("ip",SysEnvUtils.getPhoneIp());
				param.put("alipayNum", getOutTradeNo());
				param.put("recordMac", SysEnvUtils.getImieStatus(context));
				param.put("memberMonth", purchaseTime);
				param.put("officeId","40");
				String result = HttpManager.postDataToUrl(Constants.BASE_URL
						+ "vip/vipRecord", param);
				Log.i("swt","result的数值为:"+result+"officei"+param);
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
							Message msg = Message.obtain();
							msg.what = 1000;
							handler.sendMessage(msg);
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
								ToastUtil
										.showLongToast(context, "支付宝交易流水号为空!");
								return;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					boolean internet = HttpManager
							.isConnectingToInternet(context);
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

	public static void BuyCategory(final String categoryId,final String price,final Context context) {

		AsyncTask<String, String, String> loadDataTask = new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("userId", new UserCache(context).getUserId());
				param.put("categoryId", categoryId);
				param.put("officeId", "40");
				param.put("transNum", getOutTradeNo());
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
							ToastUtil.showShortToast(context,"用户ID为空");
							return;
						} else if (code.equals("1002")) {
							ToastUtil.showShortToast(context,"分类ID为空");
							return;
						} else if (code.equals("1003")) {
							ToastUtil.showShortToast(context,"交易流水号为空");
							return;
						} else if (code.equals("1004")) {
							ToastUtil.showShortToast(context,"联盟成员ID为空");
							return;
						} else if (code.equals("1005")) {
							ToastUtil.showShortToast(context,"已经购买过了");
							return;
						} else if (code.equals("1006")) {
							ToastUtil.showShortToast(context,"购买价格不能为空或者零");
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					boolean internet = HttpManager
							.isConnectingToInternet(context);
					if (internet) {
						Toast.makeText(context, "出现异常,请重试!",Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(context, "请检查网络是否可用!",Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
		loadDataTask.execute("");
	}
}

	
	
	
