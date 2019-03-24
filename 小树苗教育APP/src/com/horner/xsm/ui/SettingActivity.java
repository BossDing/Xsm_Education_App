package com.horner.xsm.ui;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.horner.nread.framework.MyApplication;
import com.horner.xsm.R;
import com.horner.xsm.alipay.PayResult;
import com.horner.xsm.base.BaseActivity;
import com.horner.xsm.bean.Book;
import com.horner.xsm.bean.PriceBean;
import com.horner.xsm.bean.WePay;
import com.horner.xsm.constants.Constants;
import com.horner.xsm.data.ThirdVipUserCache;
import com.horner.xsm.data.UserCache;
import com.horner.xsm.net.AsyncHttpClient;
import com.horner.xsm.net.AsyncHttpResponseHandler;
import com.horner.xsm.net.RequestParams;
import com.horner.xsm.utils.AliPayUtils;
import com.horner.xsm.utils.DataCleanManager;
import com.horner.xsm.utils.LoadingDialog;
import com.horner.xsm.utils.SysEnvUtils;
import com.horner.xsm.utils.ToastUtil;
import com.horner.xsm.utils.WePayUtils;
import com.mouee.common.HttpManager;
import com.mouee.common.StringUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @author 作者 : sun
 * @date 创建时间：2016-1-19 上午9:38:58
 * @return
 * @description 设置界面的Activity
 */

public class SettingActivity extends BaseActivity implements OnClickListener,OnCheckedChangeListener {

	private View settingTitleBar;
	private TextView tvTitle, cachesize, tvVersion;
	private Context mContext;
	private TextView exitLogin;
	private UserCache userCache;
	private View bindLayout;
	private RelativeLayout thirdLayout, supplyInfo, clearlayout;
	private ImageButton bindBtn, weiBos, weiXins;
	// 缓存的路径
	// private String path = "/mnt/sdcard/android/data/com.horner.xsm";
	// private String path = Environment.get.getAbsolutePath();
	// private String path = "/data/data/com.horner.xsm/cache";
	// private String path = "/storage/emulated/0/xsm/book";
	private String path = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/xsm/book";

	// private String path = context.getFilesDir().getPath() ;
	public List<RelativeLayout> serBtnList = new ArrayList<RelativeLayout>();

	@Override
	public void findView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_setting);
		settingTitleBar = findViewById(R.id.setting_titlebar);
		tvTitle = (TextView) settingTitleBar.findViewById(R.id.tv_title);
		left_btn = (RelativeLayout) settingTitleBar
				.findViewById(R.id.left_btn_layout);
		exitLogin = (TextView) findViewById(R.id.exit_login);
		bindLayout = findViewById(R.id.bangdinglayout);
		thirdLayout = (RelativeLayout) findViewById(R.id.thirdlayout);
		bindBtn = (ImageButton) findViewById(R.id.bindBtn);
		serviceBtn = (ImageButton) findViewById(R.id.serviceBtn);
		weiBos = (ImageButton) findViewById(R.id.setting_weibos);
		weiXins = (ImageButton) findViewById(R.id.setting_weixins);
		supplyInfo = (RelativeLayout) findViewById(R.id.wanshanlayout);
		clearlayout = (RelativeLayout) findViewById(R.id.clearlayout);
		vipLayout = (RelativeLayout) findViewById(R.id.vipLayout);
		cachesize = (TextView) findViewById(R.id.cachesize);
		pBar = (ProgressBar) findViewById(R.id.proid);
		tvVersion = (TextView) findViewById(R.id.tvVersion);
		vipInfos = (TextView) findViewById(R.id.vipinfo);
		vipTime = (TextView) findViewById(R.id.viptime);
		openService = (RelativeLayout) findViewById(R.id.bind_service);
		service_layout = (RelativeLayout) findViewById(R.id.service_layout);
		btnWatch = (RelativeLayout) findViewById(R.id.Intell_watch);
		btnStuCard = (RelativeLayout) findViewById(R.id.stu_card);
		btnTimeCard = (RelativeLayout) findViewById(R.id.time_card);
		btnNewFeel = (RelativeLayout) findViewById(R.id.newfeel);
		serLayout = (LinearLayout) findViewById(R.id.service_count);
		serBtnList.add(btnWatch);
		serBtnList.add(btnStuCard);
		serBtnList.add(btnTimeCard);
		serBtnList.add(btnNewFeel);
		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo;
		String version = "0";
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (!StringUtils.isEmpty(version)) {
			tvVersion.setText("V " + version);
		}
	}

	private IWXAPI api;

	@Override
	public void initData() {
		MyApplication.addActivity(this);
		mContext = this;
		umShareAPI = UMShareAPI.get(this);
		userCache = new UserCache(mContext);
		userId = userCache.getUserId();
		tvTitle.setTextColor(getResources().getColor(android.R.color.white));
		tvTitle.setText("设置");
		getCachesSize(path);
		getVipInfos(userCache);
		initAlertDialog();
		// 向微信注册小树苗的appId
		api = WXAPIFactory.createWXAPI(this, null);
		api.registerApp(MyApplication.WEIXIN_APPID);
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter("com.horner.settingcast");
		filter.addAction("com.horner.settingshow");
		//注册广播接收器
		registerReceiver(receiver,filter);
		getScreenInfo();
	}

	
	
	class MyReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			//intent.getAction();
			String msg = intent.getStringExtra("msg");
			Boolean flag = intent.getBooleanExtra("isSuccess",false);
			String vipInfos = intent.getStringExtra("vip");
			Log.i("info","msg为:"+msg+"flag为:"+flag);
			if(null != msg && !msg.equals("")){
				showBuySuccess(msg,SettingActivity.this);
			}
			if(null != vipInfos && !vipInfos.equals("")){
				getVipInfos(userCache);
			}
			if (flag) {
				if (null != dialog) {
					dialog.dismiss();
				}
			}
			
		}
	}
	String vipType;
	public void getVipInfos(UserCache cache) {
		//vipInfos = (TextView) findViewById(R.id.vipinfo);
		//vipTime = (TextView) findViewById(R.id.viptime);
		vipType = cache.getVipType();
		String startTime = cache.getVipTypeStartTime();
		String endTime = cache.getVipTypeEndTime();
		if (!TextUtils.isEmpty(vipType)) {
			if (vipType.equals("0")) {
				vipInfos.setText("普通用户");
			} else if (vipType.equals("1")) {
				vipInfos.setText("VIP用户");
				vipTime.setText("起止时间(" + startTime + ","+ endTime + ")");
			} else if (vipType.equals("2")) {
				vipInfos.setText("SVIP用户");
				vipTime.setText("起止时间(" + startTime + ","+ endTime + ")");
			}
		}
	}

	AlertDialog.Builder builder;

	private void initAlertDialog() {
		builder = new AlertDialog.Builder(mContext);
		builder.setMessage("您是否要删除已下载！")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						clearCache();
					}
				})
				.setNegativeButton("暂不清除",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
	}
	 
	// 首先得到 程序的缓存大小
	private void getCachesSize(final String path) {
		new Thread() {
			public void run() {
				File file = new File(path);
				try {
					Message msg = Message.obtain();
					msg.obj = DataCleanManager.getCacheSize(file);
					msg.what = 0;
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
		}.start();

	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String size = (String) msg.obj;
				if (size != null) {
					cachesize.setText(size);
				}
				break;
			case 1:
				pBar.setVisibility(View.GONE);
				ToastUtil.showLongToast(mContext, "缓存清除成功");
				cachesize.setText(zero);
				MyApplication.downList.clear();
				MyApplication.downQueue.clear();

				for (int m = 217; m < 222; m++) {
					List<Book> datas1Tmp = readListToLocal(m + "1");
					if (datas1Tmp != null) {
						for (Book bk1 : datas1Tmp) {
							bk1.mState = 0;
							bk1.mDownloadCount = 0;
							bk1.mBookSize = 0;
						}
					}

					List<Book> datas2Tmp = readListToLocal(m + "2");
					if (datas2Tmp != null) {
						for (Book bk2 : datas2Tmp) {
							bk2.mState = 0;
							bk2.mDownloadCount = 0;
							bk2.mBookSize = 0;
						}
					}

					saveListToLocal(m + "1", datas1Tmp);
					saveListToLocal(m + "2", datas2Tmp);
				}

				for (int m = 1; m < 6; m++) {
					List<Book> datasTmp = readListToLocal(m + "");
					if (datasTmp != null) {
						for (Book bk : datasTmp) {
							bk.mState = 0;
							bk.mDownloadCount = 0;
							bk.mBookSize = 0;
						}
					}

					saveListToLocal(m + "", datasTmp);
				}

				break;
			case 1000:
				getVipInfos(userCache);
				break;
			default:
				break;
			}
		}
	};

	Handler payHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AliPayUtils.SDK_PAY_FLAG: // 支付宝支付
				PayResult payResult = new PayResult((String) msg.obj);
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(SettingActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					// 支付成功之后调python的服务器
						if (null != dialog) {
							dialog.dismiss();
						}
					pushPayInfosToServer();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(SettingActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(SettingActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case 0:// 微信支付
				LoadingDialog.finishLoading();
				WePay wePay = (WePay) msg.obj;
				Log.i("info", "Wepay的字段为" + wePay.toString());
				PayReq req = new PayReq();
				req.packageValue = "Sign=WXPay";
				req.sign = wePay.getSign();
				req.appId = wePay.getAppid();
				req.prepayId = wePay.getPrepayid();
				req.partnerId = wePay.getPartnerid();
				req.timeStamp = wePay.getTimestamp();
				req.nonceStr = wePay.getNoncestr();
				Constants.nonceStr = wePay.getNoncestr();
				api.registerApp(wePay.getAppid());
				api.sendReq(req);
				break;

			default:
				break;
			}
		};
	};
	private MessageDigest md5 = null;

	public String getMd5(String str) {
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
	public String getPayCode() {
		String code = new UserCache(this).getUserPhone() + "tpay_order_push"
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

	public void pushPayInfosToServer() {
		Log.e("info", "手机ip地址为;" + SysEnvUtils.getPhoneIp());
		AsyncHttpClient client = new AsyncHttpClient(SettingActivity.this);
		RequestParams param = new RequestParams();
		param.put("pay_user_phone", new UserCache(this).getUserPhone());
		param.put("pay_order_number", AliPayUtils.getOutTradeNo());
		param.put("pay_order_type", Constants.pay_order_type);
		param.put("pay_type", "0"); 
		param.put("pay_order_desc",Constants.extraInfo);
		param.put("pay_order_fee", aliPayMoney);
		param.put("pay_status", "0");
		param.put("pay_platform_type", "1");
		param.put("pay_order_time",new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()));
		param.put("pay_api_code",getPayCode());
		Log.i("info", "param的数值为:" + param);
		client.post("http://pdapi.xbsep.com/v1/third_pay/order_push/", param,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String result) {
						Log.i("info","调后天返回数据为:"+result);
						try {
							JSONObject object = new JSONObject(result);
							if (object.optBoolean("flag")) {
								String order_type;
								ToastUtil.showShortToast(SettingActivity.this,"提交后台成功");
								if (Constants.pay_order_type.equals("1")) {
									order_type = "亲情通";
								} else if (Constants.pay_order_type.equals("2")) {
									order_type = "智能考勤押金";
								} else if (Constants.pay_order_type.equals("3")) {
									order_type = "智能学生证";
								} else if (Constants.pay_order_type.equals("4")) {
									order_type = "智能手表";
								} else {
									order_type = "其他";
								}
								String msg = new UserCache(SettingActivity.this).getUserPhone()+"用户,您已成功购买"+order_type;
								showBuySuccess(msg,SettingActivity.this);
							}else {
								ToastUtil.showShortToast(SettingActivity.this,"提交后台失败");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						boolean internet = HttpManager
								.isConnectingToInternet(SettingActivity.this);
						if (internet) {
							Toast.makeText(SettingActivity.this,
									"提交后台失败,请重试。", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(SettingActivity.this, "请检查网络是否可用。",
									Toast.LENGTH_SHORT).show();
						}
					}

				});
	}

	
	
	private   void showBuySuccess(String msg,Context context) {
		final Dialog successDialog = new Dialog(context, R.style.translucent_Dialog);
		Window window = successDialog.getWindow();
		android.view.WindowManager.LayoutParams params = window.getAttributes();
		params.width = Constants.width / 7 * 5;
		params.height = Constants.height / 10 * 6;
		window.setAttributes(params);
		View successView = LayoutInflater.from(context).inflate(R.layout.buy_success,null);
		TextView tvMsg = (TextView) successView.findViewById(R.id.msg);
		tvMsg.setText(msg);
		RelativeLayout sure = (RelativeLayout) successView.findViewById(R.id.sure_layout);
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

	private void getScreenInfo() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Constants.width = metrics.widthPixels;
		Constants.height = metrics.heightPixels;
	}

	
	
	
	@Override
	public void addClickListener() {
		exitLogin.setOnClickListener(this);
		// bindBtn.setOnClickListener(this);
		thirdLayout.setOnClickListener(this);
		left_btn.setOnClickListener(this);
		bindLayout.setOnClickListener(this);
		clearlayout.setOnClickListener(this);
		vipLayout.setOnClickListener(this);
		supplyInfo.setOnClickListener(this);
		weiBos.setOnClickListener(this);
		weiXins.setOnClickListener(this);
		openService.setOnClickListener(this);
		btnWatch.setOnClickListener(this);
		btnNewFeel.setOnClickListener(this);
		btnTimeCard.setOnClickListener(this);
		btnStuCard.setOnClickListener(this);
	}

	Intent intent;
	SHARE_MEDIA platform = null;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.exit_login:
			showExitDiaog();
			
			break;
		case R.id.bangdinglayout:
			showThirdState();
			break;
		case R.id.left_btn_layout:
			finish();
			break;
		case R.id.wanshanlayout:
			intent = new Intent(mContext, MoreInfoActivity.class);
			intent.putExtra("id", userId);
			intent.putExtra("from", "setting");
			startActivity(intent);
			break;
		case R.id.clearlayout:
			builder.show();
			break;
		case R.id.setting_weibos:
			platform = SHARE_MEDIA.SINA;
			login(platform);
			break;
		case R.id.setting_weixins:
			platform = SHARE_MEDIA.WEIXIN;
			login(platform);
			break;
		case R.id.bind_service:
			isOpenService();
			break;
		case R.id.Intell_watch:
			setServiceBtnState(R.id.Intell_watch);
			break;
		case R.id.stu_card:
			setServiceBtnState(R.id.stu_card);
			break;
		case R.id.time_card:
			setServiceBtnState(R.id.time_card);
			break;
		case R.id.newfeel:
			setServiceBtnState(R.id.newfeel);
			break;
		case R.id.vipLayout:
			String vipType = new UserCache(this).getVipType();
			if (null != vipType&& !vipType.equals("0")) {
				ToastUtil.showShortToast(this,"您已经是vip用户无需购买");
			}else {
				showVipDialog();
			}
			break;
		default:
			break;
		}
	}
	 AlertDialog.Builder exitBuilder;
	private void showExitDiaog() {
		exitBuilder = new AlertDialog.Builder(this);
		exitBuilder.setMessage("是否确定退出").setPositiveButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		}).setNegativeButton("确定",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				exitLogin();
			}
		}).show();
		
	}

	private String cata;
	private CheckBox one,two,three,four,checkBaby;
	private TextView tvOne, tVthree,tvTwo,tVfour,tVBaby;
	View vipView;
	Dialog vipDialog;
	ImageButton aliPay,wePay;
	String desc;
	private void showVipDialog(){
		vipDialog = new Dialog(this, R.style.translucent_Dialog);
		Window window = vipDialog.getWindow();
		android.view.WindowManager.LayoutParams params = window.getAttributes();
		params.width = MyApplication.width / 7 * 4;
		params.height = MyApplication.height / 10 * 8;
		window.setAttributes(params);
		vipView = LayoutInflater.from(this).inflate(R.layout.buyvip,null);
		aliPay = (ImageButton) vipView.findViewById(R.id.sure_buy);
		wePay = (ImageButton) vipView.findViewById(R.id.sure_buy_wePay);
		
		one = (CheckBox) vipView.findViewById(R.id.check1);
		two = (CheckBox) vipView.findViewById(R.id.check2);
		three = (CheckBox) vipView.findViewById(R.id.check3);
		four = (CheckBox) vipView.findViewById(R.id.check4);
		checkBaby = (CheckBox) vipView.findViewById(R.id.check_baby);
		
		tvOne = (TextView) vipView.findViewById(R.id.one);
		tvTwo = (TextView) vipView.findViewById(R.id.two);
		tVthree = (TextView) vipView.findViewById(R.id.three);
		tVfour = (TextView) vipView.findViewById(R.id.four);
		tVBaby = (TextView) vipView.findViewById(R.id.tvbaby);
		
		one.setOnCheckedChangeListener(this);
		two.setOnCheckedChangeListener(this);
		three.setOnCheckedChangeListener(this);
		four.setOnCheckedChangeListener(this);
		checkBaby.setOnCheckedChangeListener(this);
		
		getPrice(tvOne,tvTwo,tVthree,tVfour,tVBaby);
		vipDialog.setContentView(vipView);
		vipDialog.show();
		aliPay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!one.isChecked() && !two.isChecked() && !three.isChecked()
						&& !four.isChecked()&&!checkBaby.isChecked()) {
					ToastUtil.showShortToast(SettingActivity.this,
							"您当前未勾选,请勾选!");
					return;
				}
				if (one.isChecked() && !two.isChecked() && !three.isChecked()
						&& !four.isChecked()&& !checkBaby.isChecked()) {
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++){
							PriceBean priceBean = Constants.priceBeans.get(i);
							if(priceBean.getCycle().equals("1")){
								moneyNum = priceBean.getPrice();
								//moneyNum = "0.01";
								purchaseTime = priceBean.getCycle();
								desc = priceBean.getCycle()+"个月会员";
								break;
							}
						}
					}else {
						purchaseTime = "1";
						moneyNum = "25";
					}
					cata = null;
				}
				if (!one.isChecked() && two.isChecked() && !three.isChecked()
						&& !four.isChecked()&& !checkBaby.isChecked()) {
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++){
							PriceBean priceBean = Constants.priceBeans.get(i);
							if(priceBean.getCycle().equals("3")){
								moneyNum = priceBean.getPrice();
								purchaseTime = priceBean.getCycle();
								desc = priceBean.getCycle()+"个月会员";
								break;
							}
						}
					}else {
						purchaseTime = "3";
						moneyNum = "60";
					}
					cata = null;
				}
				if (!one.isChecked() && !two.isChecked() && three.isChecked()
						&& !four.isChecked()&& !checkBaby.isChecked()) {
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++){
							PriceBean priceBean = Constants.priceBeans.get(i);
							if(priceBean.getCycle().equals("6")){
								moneyNum = priceBean.getPrice();
								purchaseTime = priceBean.getCycle();
								desc = priceBean.getCycle()+"个月会员";
								break;
							}
						}
					}else {
						purchaseTime = "6";
						moneyNum = "105";
					}
					cata = null;
				}
				if (!one.isChecked() && !two.isChecked() && !three.isChecked()
						&& four.isChecked()&& !checkBaby.isChecked()) {
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++){
							PriceBean priceBean = Constants.priceBeans.get(i);
							if(priceBean.getCycle().equals("12")){
								moneyNum = priceBean.getPrice();
								purchaseTime = priceBean.getCycle();
								desc = priceBean.getCycle()+"个月会员";
								break;
							}
						}
					}else {
						purchaseTime = "12";
						moneyNum = "168";
					}
					cata = null;
				}
				
				if (!one.isChecked() && !two.isChecked() && !three.isChecked()
						&& !four.isChecked()&& checkBaby.isChecked()) {
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++){
							PriceBean priceBean = Constants.priceBeans.get(i);
							if(priceBean.getType().equals("mod")){
								moneyNum = priceBean.getPrice();
								purchaseTime = "幼小衔接";
								desc = "幼小衔接";
								break;
							}
						}
					}else {
						purchaseTime = "幼小衔接";
						moneyNum = "25";
					}
					cata = "221";
					
				}
				AliPayUtils.pay(moneyNum, desc, vipHandler, SettingActivity.this);
			}
		});
		wePay.setOnClickListener(new OnClickListener() {
		

			@Override
			public void onClick(View v) {
				if (!one.isChecked() && !two.isChecked() && !three.isChecked()
						&& !four.isChecked()&&!checkBaby.isChecked()) {
					ToastUtil.showShortToast(SettingActivity.this,
							"您当前未勾选,请勾选!");
					return;
				}
				if (one.isChecked() && !two.isChecked() && !three.isChecked()
						&& !four.isChecked()&& !checkBaby.isChecked()) {
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++){
							PriceBean priceBean = Constants.priceBeans.get(i);
							if(priceBean.getCycle().equals("1")){
								moneyNum = priceBean.getPrice();
								//moneyNum = "0.01";
								purchaseTime = priceBean.getCycle();
								break;
							}
						}
					}else {
						purchaseTime = "1";
						moneyNum = "25";
					}
					Constants.isVipOrMod = "vip";
				}
				if (!one.isChecked() && two.isChecked() && !three.isChecked()
						&& !four.isChecked()&& !checkBaby.isChecked()) {
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++){
							PriceBean priceBean = Constants.priceBeans.get(i);
							if(priceBean.getCycle().equals("3")){
								moneyNum = priceBean.getPrice();
								purchaseTime = priceBean.getCycle();
								break;
							}
						}
					}else {
						purchaseTime = "3";
						moneyNum = "60";
					}
					Constants.isVipOrMod = "vip";
				}
				if (!one.isChecked() && !two.isChecked() && three.isChecked()
						&& !four.isChecked()&& !checkBaby.isChecked()) {
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++){
							PriceBean priceBean = Constants.priceBeans.get(i);
							if(priceBean.getCycle().equals("6")){
								moneyNum = priceBean.getPrice();
								purchaseTime = priceBean.getCycle();
								break;
							}
						}
					}else {
						purchaseTime = "6";
						moneyNum = "105";
					}
					Constants.isVipOrMod = "vip";
				}
				if (!one.isChecked() && !two.isChecked() && !three.isChecked()
						&& four.isChecked()&& !checkBaby.isChecked()) {
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++){
							PriceBean priceBean = Constants.priceBeans.get(i);
							if(priceBean.getCycle().equals("12")){
								moneyNum = priceBean.getPrice();
								purchaseTime = priceBean.getCycle();
								break;
							}
						}
					}else {
						purchaseTime = "12";
						moneyNum = "168";
					}
					Constants.isVipOrMod = "vip";
				}
				
				if (!one.isChecked() && !two.isChecked() && !three.isChecked()
						&& !four.isChecked()&& checkBaby.isChecked()) {
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++){
							PriceBean priceBean = Constants.priceBeans.get(i);
							if(priceBean.getType().equals("mod")){
								moneyNum = priceBean.getPrice();
								//moneyNum = "0.01";
								purchaseTime = "幼小衔接";
								break;
							}
						}
					}else {
						purchaseTime = "幼小衔接";
						moneyNum = "25";
					}
					Constants.isVipOrMod = "mod";
					
				}
				
				WePayUtils.getPayInfo(vipHandler, moneyNum, SettingActivity.this, WePay.class, purchaseTime);
			}
		});
		
		
	}
	
	
	Handler vipHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AliPayUtils.SDK_PAY_FLAG: // 支付宝支付
				PayResult payResult = new PayResult((String) msg.obj);
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(SettingActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
						if (null != vipDialog) {
							vipDialog.dismiss();
						}
						if (cata != null && cata.equals("221")) {
							AliPayUtils.BuyCategory("221", moneyNum, SettingActivity.this);
						}else{
							AliPayUtils.toBase_ZHU(SettingActivity.this, moneyNum, purchaseTime,mHandler);
						}
						
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(SettingActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(SettingActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case 0:// 微信支付
				LoadingDialog.finishLoading();
				WePay wePay = (WePay) msg.obj;
				Log.i("info", "Wepay的字段为" + wePay.toString());
				PayReq req = new PayReq();
				req.packageValue = "Sign=WXPay";
				req.sign = wePay.getSign();
				req.appId = wePay.getAppid();
				req.prepayId = wePay.getPrepayid();
				req.partnerId = wePay.getPartnerid();
				req.timeStamp = wePay.getTimestamp();
				req.nonceStr = wePay.getNoncestr();
				Constants.nonceStr = wePay.getNoncestr();
				api.registerApp(wePay.getAppid());
				api.sendReq(req);
				break;

			default:
				break;
			}
		};
	};
	String purchaseTime = null;
	String moneyNum = null;
	private void getPrice(TextView one,TextView two,TextView three,TextView four,TextView tVBaby){
		if(null != Constants.priceBeans){
			for (int i = 0; i < Constants.priceBeans.size(); i++) {
				PriceBean priceBean = Constants.priceBeans.get(i);
				if(priceBean.getCycle().equals("1")){
					moneyNum = priceBean.getPrice();
					purchaseTime = priceBean.getCycle();
					String content = purchaseTime+"个月会员:"+moneyNum+"人民币";
					one.setText(content);
				}else if(priceBean.getCycle().equals("3")) {
					moneyNum = priceBean.getPrice();
					purchaseTime = priceBean.getCycle();
					String content = purchaseTime+"个月会员:"+moneyNum+"人民币";
					two.setText(content);
				}else if(priceBean.getCycle().equals("6")) {
					moneyNum = priceBean.getPrice();
					purchaseTime = priceBean.getCycle();
					String content = "半年会员:"+moneyNum+"人民币";
					three.setText(content);
				}else if(priceBean.getCycle().equals("12")) {
					moneyNum = priceBean.getPrice();
					purchaseTime = priceBean.getCycle();
					String content =  "年费会员:"+moneyNum+"人民币";
					four.setText(content);
				}else if(priceBean.getType().equals("mod")) {
					moneyNum = priceBean.getPrice();
					purchaseTime = priceBean.getCycle();
					String content =  "幼小衔接 :"+moneyNum+"人民币";
					tVBaby.setText(content);
				}
				
			}
		} 
		
	}
	public void setServiceBtnState(int id) {
		for (int i = 0; i < serLayout.getChildCount(); i++) {
			RelativeLayout btn = (RelativeLayout) serLayout.getChildAt(i);
			switch (i) {
			case 0:
				TextView tv0 = null;
				tv0 = (TextView) btn.getChildAt(1);
				if (btn.getId() == id) {
					//btn.setBackgroundResource(R.drawable.setting_corner_uncheck);
					btn.getChildAt(0)
							.setBackgroundResource(R.drawable.nf_white);
					tv0.setTextColor(Color.parseColor("#ffffff"));
					showBuyServiceDialog("亲情通");
				} else {
					//btn.setBackgroundResource(R.drawable.setting_cornerbtn);
					//tv0.setTextColor(Color.parseColor("#dedede"));
					//btn.getChildAt(0).setBackgroundResource(R.drawable.nf_2);
				}
				break;
			case 1:
				TextView tv1 = null;
				tv1 = (TextView) btn.getChildAt(1);
				if (btn.getId() == id) {
					//btn.setBackgroundResource(R.drawable.setting_corner_uncheck);
					btn.getChildAt(0)
							.setBackgroundResource(R.drawable.tc_white);
					tv1.setTextColor(Color.parseColor("#ffffff"));
					showBuyServiceDialog("智能考勤卡押金");
				} else {
					//btn.setBackgroundResource(R.drawable.setting_cornerbtn);
					//tv1.setTextColor(Color.parseColor("#dedede"));
					//btn.getChildAt(0).setBackgroundResource(R.drawable.tc);
				}
				break;
			case 2:
				TextView tv2 = null;
				tv2 = (TextView) btn.getChildAt(1);
				if (btn.getId() == id) {
					//btn.setBackgroundResource(R.drawable.setting_corner_uncheck);
					btn.getChildAt(0)
							.setBackgroundResource(R.drawable.sc_white);
					tv2 = (TextView) btn.getChildAt(1);
					tv2.setTextColor(Color.parseColor("#ffffff"));
					showBuyServiceDialog("智能学生证");
				} else {
					//btn.setBackgroundResource(R.drawable.setting_cornerbtn);
					//tv2.setTextColor(Color.parseColor("#dedede"));
					//btn.getChildAt(0).setBackgroundResource(R.drawable.sc);
				}
				break;
			case 3:
				TextView tv3 = null;
				tv3 = (TextView) btn.getChildAt(1);
				if (btn.getId() == id) {
					//btn.setBackgroundResource(R.drawable.setting_corner_uncheck);
					btn.getChildAt(0)
							.setBackgroundResource(R.drawable.iw_white);
					tv3 = (TextView) btn.getChildAt(1);
					tv3.setTextColor(Color.parseColor("#ffffff"));
					showBuyServiceDialog("智能手表");
				} else {
					//btn.setBackgroundResource(R.drawable.setting_cornerbtn);
					//tv3.setTextColor(Color.parseColor("#dedede"));
					//btn.getChildAt(0).setBackgroundResource(R.drawable.iw);
				}
				break;

			default:
				break;
			}
		}
	}

	private View payView;
	RelativeLayout relative;
	// 金额
	private EditText etMoney;
	// 备注
	private EditText etRemarks;
	//String pay_order_type = null;
	String aliPayMoney;
	
	//View payView;
	Dialog dialog;
	String extraInfo;
	private void showBuyServiceDialog(final String desc) {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		dialog = new Dialog(this, R.style.translucent_Dialog);
		Window window = dialog.getWindow();
		android.view.WindowManager.LayoutParams params = window.getAttributes();
		params.width = width / 7 * 4;
		params.height = height / 10 * 8;
		window.setAttributes(params);
		payView = LayoutInflater.from(this).inflate(R.layout.buy_service,
				null);
		TextView descrption = (TextView) payView.findViewById(R.id.desc);
		ImageButton aliPay = (ImageButton) payView.findViewById(R.id.sure_buy);
		ImageButton wePay = (ImageButton) payView
				.findViewById(R.id.sure_buy_wePay);
		etMoney = (EditText) payView.findViewById(R.id.etmoney);
		etRemarks = (EditText) payView.findViewById(R.id.etremarks);
		descrption.setText(desc);
		if (desc.equals("亲情通")) {
			Constants.pay_order_type = "1";
		} else if (desc.equals("智能考勤卡押金")) {
			Constants.pay_order_type = "2";
		} else if (desc.equals("智能学生证")) {
			Constants.pay_order_type = "3";
		} else if (desc.equals("智能手表")) {
			Constants.pay_order_type = "4";
		} else {
			Constants.pay_order_type = "5";
		}
		Log.i("info","Consta"+Constants.pay_order_type);
		dialog.setContentView(payView);
		dialog.show();
		dialog.setCanceledOnTouchOutside(true);
		// 支付宝支付
		aliPay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				aliPayMoney = etMoney.getText().toString().trim(); 
				if (!TextUtils.isEmpty(aliPayMoney)&& aliPayMoney.length() > 5) {
					ToastUtil.showShortToast(SettingActivity.this,"输入金额位数必须小于或者等于五位");
					return;
				}
				if (aliPayMoney.equals(".")) {
					ToastUtil.showShortToast(SettingActivity.this,"输入金额必须为数字");
					return;
				}
				
				 Constants.extraInfo = etRemarks.getText().toString().trim(); 
				if (TextUtils.isEmpty(aliPayMoney) || aliPayMoney.equals(" ")) {
					ToastUtil.showShortToast(SettingActivity.this,"输入金额不能为空或者为空格");
					return;
				}
				float parseFloat = Float.parseFloat(aliPayMoney);
				float strand = (float) 0.01;
				Log.i("info","pase的数值为;"+parseFloat);
				if (parseFloat < strand ) {
					ToastUtil.showShortToast(SettingActivity.this, "输入金额不能小于一分钱");
				} else {
					ToastUtil.showShortToast(SettingActivity.this, "开始调起支付宝支付");
					AliPayUtils.pay(aliPayMoney, desc, payHandler,SettingActivity.this);
				}

			}
		});
		// 微信支付
		wePay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String wePayMoney = etMoney.getText().toString().trim();
				
				if (!TextUtils.isEmpty(aliPayMoney)&& aliPayMoney.length() > 5) {
					ToastUtil.showShortToast(SettingActivity.this,"输入金额位数必须小于或者等于五位");
					return;
				}
				if (wePayMoney.equals(".")) {
					ToastUtil.showShortToast(SettingActivity.this,"输入金额必须为数字");
					return;
				}
				Constants.extraInfo = etRemarks.getText().toString().trim(); 
				if (TextUtils.isEmpty(wePayMoney) || wePayMoney.equals(" ")) {
					ToastUtil.showShortToast(SettingActivity.this,"输入金额不能为空或者为空格");
					return;
				}
				float parseFloat = Float.parseFloat(wePayMoney);
				if ((parseFloat*100) < 1) {
					ToastUtil.showShortToast(SettingActivity.this, "输入金额小于1分钱,请重新输入");
					return;
				}
				if (parseFloat > 0) {
					//ToastUtil.showShortToast(SettingActivity.this, "开始调起微信支付");
					//moneyNum = String.valueOf(parseFloat * 100);
					
					WePayUtils.getPayInfo(payHandler, wePayMoney,SettingActivity.this, WePay.class, desc);
					Constants.isVipOrMod = "python";
				} else {
					ToastUtil.showShortToast(SettingActivity.this, "输入金额不能小于或者等于零");
				}
			}
		});
	}

	/**
	 * @author 作者 : sun
	 * @date 创建时间：2016-3-28 下午1:32:58
	 * @return
	 * @description 点击开通服务之后打开里边详情内容
	 */
	Boolean isServiceOpen = false;

	private void isOpenService() {
		if (!isServiceOpen) {
			service_layout.setVisibility(View.VISIBLE);
			serviceBtn.setBackgroundResource(R.drawable.pulldown);
			isServiceOpen = true;
		} else {
			service_layout.setVisibility(View.GONE);
			serviceBtn.setBackgroundResource(R.drawable.right);
//			for (int i = 0; i < serBtnList.size(); i++) {
//				RelativeLayout btn = serBtnList.get(i);
//				btn.setBackgroundResource(R.drawable.setting_cornerbtn);
//			}
			isServiceOpen = false;
		}
	}

	/**
	 * 
	 * @author 作者 : sun
	 * @date 创建时间：2015-12-29 下午5:03:45
	 * @return
	 * @description 用于平台授权,再一次登录之前需要先解除授权
	 */
	public void login(final SHARE_MEDIA platform) {

		// 获取授权
		umShareAPI.doOauthVerify(SettingActivity.this, platform,
				new UMAuthListener() {
					@Override
					public void onError(SHARE_MEDIA arg0, int arg1,
							Throwable arg2) {

						Log.i("info", "进行授权");
					}

					@Override
					public void onComplete(SHARE_MEDIA arg0, int arg1,
							Map<String, String> arg2) {
						// 获取uid
						Log.i("info", "授权成功" + arg2.toString());
						// ToastUtil.showShortToast(getApplicationContext(),
						// "授权成功");
						// uid不为空，获取用户信息
						getUserInfo(platform);

					}

					@Override
					public void onCancel(SHARE_MEDIA arg0, int arg1) {
						Log.i("info", "取消授权");
					}
				});
	}

	private void getUserInfo(final SHARE_MEDIA platform) {
		umShareAPI.getPlatformInfo(SettingActivity.this, platform,
				new UMAuthListener() {

					@Override
					public void onError(SHARE_MEDIA arg0, int arg1,
							Throwable arg2) {
						ToastUtil.showShortToast(getApplicationContext(),
								"读取数据失败");
					}

					// 获取三方平台的用户信息
					@Override
					public void onComplete(SHARE_MEDIA arg0, int arg1,
							Map<String, String> arg2) {
						if (arg2 != null) {
							Log.i("info", arg2.toString() + "用户的信息为:");
							saveThiredData(arg2, platform);

						}
					}

					@Override
					public void onCancel(SHARE_MEDIA arg0, int arg1) {
						ToastUtil.showShortToast(getApplicationContext(),
								"读取数据失败");
					}
				});
	}

	/**
	 * 
	 * @author 作者 : sun
	 * @date 创建时间：2015-12-30 下午6:30:21
	 * @return
	 * @description 将三方数据保存起来，然后跳到绑定手机号的页面
	 */
	public void saveThiredData(Map<String, String> info, SHARE_MEDIA thirdType) {
		Log.i("info", "thirdId");

		ThirdVipUserCache userCacheThird = new ThirdVipUserCache(mContext);
		String thirdId;
		String address;
		String sex;
		String imageUrl;
		String nickName;
		String wechat = "wechat";
		String weibo = "weibo";
		// Log.i("info", "thirdId" + thirdId);

		if (thirdType.equals(SHARE_MEDIA.SINA)) {
			thirdId = info.get("uid");
			// address = info.get("location");
			// sex = info.get("gender");
			imageUrl = info.get("profile_image_url");
			nickName = info.get("screen_name");

			// userCacheThird.setThirdId(thirdId);
			// // userCache.setUserAddress(address);
			// // userCache.setUserSex(sex);
			// userCacheThird.setMultipartFile(imageUrl);
			// userCacheThird.setNickName(nickName);
			// userCacheThird.setThirdType("weibo");

			submitData(weibo);
		} else {
			thirdId = info.get("unionid");
			// address = info.get("city");
			// sex = info.get("sex");
			imageUrl = info.get("headimgurl");
			nickName = info.get("nickname");

			// userCacheThird.setThirdId(thirdId);
			// // userCache.setUserAddress(address);
			// // userCache.setUserSex(sex);
			// userCacheThird.setMultipartFile(imageUrl);
			// userCacheThird.setNickName(nickName);
			// userCacheThird.setThirdType("wechat");

			submitData(wechat);
		}
	}

	private void submitData(final String thirdType) {
		AsyncTask<String, String, String> loadDataTask = new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				ThirdVipUserCache userCache = new ThirdVipUserCache(
						SettingActivity.this);
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("userId", userId);
				param.put("thirdId", userCache.getThirdId().toString());
				param.put("thirdType", thirdType);
				Log.i("info", "param:" + param.toString());
				String result = HttpManager.postDataToUrl(Constants.BASE_URL
						+ "user/bind", param);
				// String result =
				// HttpManager.postDataToUrl("http://192.168.1.102:8080/p/front/"
				// + "login/product", param);
				Log.i("info", "result的数值为:" + result);
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				LoadingDialog.finishLoading();
				if (!StringUtils.isEmpty(result)) {
					try {
						Log.i("info", "登录成功之后返回的数值为:" + result);
						JSONObject object = new JSONObject(result);
						String code = object.optString("code");
						if (code != null) {
							Message msg = Message.obtain();
							if (code.equals("1000")) {
								if (thirdType.equals("weibo")) {
									ToastUtil
											.showLongToast(mContext, "绑定微博成功!");
									thirdLayout.setVisibility(View.GONE);
									isOpen = false;
									userCache.setWeiBoState("1");
								} else {
									ToastUtil
											.showLongToast(mContext, "绑定微信成功!");
									thirdLayout.setVisibility(View.GONE);
									isOpen = false;
									userCache.setWeiChatState("1");
								}
							} else if (code.equals("1004")) {
								ToastUtil.showLongToast(mContext, "绑定异常!");
								return;
							} else {
								ThirdVipUserCache userCache = new ThirdVipUserCache(
										mContext);
								ToastUtil.showLongToast(mContext,
										"该三方账号已经被绑定过了!");
								Log.i("info","99999999"+ new UserCache(SettingActivity.this).getNickName());
								userCache.setMultipartFile(new UserCache(SettingActivity.this).getMultipartFile());
								userCache.setNickName(new UserCache(SettingActivity.this).getNickName());
								userCache.setUserAddress(new UserCache(SettingActivity.this).getUserAddress());
								userCache.setUserSex(new UserCache(SettingActivity.this).getUserSex());
								return;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					boolean internet = HttpManager
							.isConnectingToInternet(SettingActivity.this);
					if (internet) {
						Toast.makeText(SettingActivity.this, "绑定失败,请重试!",Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(SettingActivity.this, "绑定失败,请检查网络是否可用!",Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
		loadDataTask.execute("");

	}

	/**
	 * @author 作者 : sun
	 * @date 创建时间：2016-1-19 下午3:58:26
	 * @return
	 * @description 清除图片缓存,
	 */
	String zero = "0.0Byte";

	private void clearCache() {
		if (cachesize.getText().equals(zero)) {
			ToastUtil.showLongToast(mContext, "暂无缓存可清除");
			return;
		}
		ToastUtil.showLongToast(mContext, "正在清除缓存");
		pBar.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				// ToastUtil.showShortToast(mContext, "正在清除缓存！");
				DataCleanManager.deleteFolderFile(path, true);
				Message msg = Message.obtain();
				msg.what = 1;
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * @author 作者 : sun
	 * @date 创建时间：2016-1-19 下午1:56:26
	 * @return
	 * @description 点击之后查看用户是否已经绑定微博或者微信。若没有，则将图标置为灰色的
	 */
	private Boolean isOpen = false;
	private RelativeLayout left_btn;
	private String weiBoState, weChatState;
	private UMShareAPI umShareAPI;
	private String userId;
	private ProgressBar pBar;
	private RelativeLayout openService;
	private RelativeLayout service_layout;
	private ImageButton serviceBtn;
	private TextView vipInfos;
	private RelativeLayout btnWatch;
	private RelativeLayout btnStuCard;
	private RelativeLayout btnTimeCard;
	private RelativeLayout btnNewFeel;
	private LinearLayout serLayout;
	private MyReceiver receiver;
	private TextView vipTime;
	private RelativeLayout vipLayout;
	private ImageButton sure_buy;
	private ImageButton wxPay;

	private void showThirdState() {

		weiBoState = userCache.getWeiBoState();
		weChatState = userCache.getWeiChatState();
		if (!isOpen) {
			thirdLayout.setVisibility(View.VISIBLE);
			bindBtn.setBackgroundResource(R.drawable.pulldown);
			if (weiBoState.equals("1")) {
				weiBos.setBackgroundResource(R.drawable.weibo_gray);
				weiBos.setEnabled(false);
			} else {
				weiBos.setEnabled(true);
			}
			if (weChatState.equals("1")) {
				weiXins.setBackgroundResource(R.drawable.weixin_gray);
				weiXins.setEnabled(false);
			} else {
				weiXins.setEnabled(true);
			}
			isOpen = true;
		} else {
			thirdLayout.setVisibility(View.GONE);
			bindBtn.setBackgroundResource(R.drawable.right);
			isOpen = false;
		}

	}

	/**
	 * @author 作者 : sun
	 * @date 创建时间：2016-1-19 下午1:49:43
	 * @return
	 * @description 退出登录
	 */
	private void exitLogin() {
		Intent intent = null;
		if (userId != null && !userId.equals("")) { // 用户已经登录,然后将登录状态置为空
			userCache.setIsLogin(false);
			userCache.setPassword("");
			userCache.setPhone("");
			userCache.setUserId("");
			userCache.setVipType("");
			MyApplication.clearActivities();
			// 清除软引用中缓存的图片
			// MoreInfoActivity.imgCaches.clear();
			intent = new Intent(mContext, LoginActivity.class);
			startActivity(intent);
		} 
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		//避免内存泄露,删除所有的callbacks和Messages
		//mHandler.removeCallbacksAndMessages(null);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		one.setChecked(false);
		two.setChecked(false);
		three.setChecked(false);
		four.setChecked(false);
		checkBaby.setChecked(false);
		switch (buttonView.getId()) {
		case R.id.check1:
			one.setChecked(isChecked);
			tvOne.setTextColor(Color.parseColor("#ef6944"));
			tvTwo.setTextColor(Color.parseColor("#000000"));
			tVthree.setTextColor(Color.parseColor("#000000"));
			tVfour.setTextColor(Color.parseColor("#000000"));
			tVBaby.setTextColor(Color.parseColor("#000000"));
			break;
		case R.id.check2:
			two.setChecked(isChecked);
			tvOne.setTextColor(Color.parseColor("#000000"));
			tvTwo.setTextColor(Color.parseColor("#ef6944"));
			tVthree.setTextColor(Color.parseColor("#000000"));
			tVfour.setTextColor(Color.parseColor("#000000"));
			tVBaby.setTextColor(Color.parseColor("#000000"));
			break;
		case R.id.check3:
			three.setChecked(isChecked);
			tvOne.setTextColor(Color.parseColor("#000000"));
			tvTwo.setTextColor(Color.parseColor("#000000"));
			tVthree.setTextColor(Color.parseColor("#ef6944"));
			tVfour.setTextColor(Color.parseColor("#000000"));
			tVBaby.setTextColor(Color.parseColor("#000000"));
			break;
		case R.id.check4:
			four.setChecked(isChecked);
			tvOne.setTextColor(Color.parseColor("#000000"));
			tvTwo.setTextColor(Color.parseColor("#000000"));
			tVthree.setTextColor(Color.parseColor("#000000"));
			tVfour.setTextColor(Color.parseColor("#ef6944"));
			tVBaby.setTextColor(Color.parseColor("#000000"));
			break;
		case R.id.check_baby:
			checkBaby.setChecked(isChecked);
			tvOne.setTextColor(Color.parseColor("#000000"));
			tvTwo.setTextColor(Color.parseColor("#000000"));
			tVthree.setTextColor(Color.parseColor("#000000"));
			tVfour.setTextColor(Color.parseColor("#000000"));
			tVBaby.setTextColor(Color.parseColor("#ef6944"));
			break;
		default:
			break;
		}
	}

}
