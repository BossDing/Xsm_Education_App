package com.horner.xsm.ui;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.horner.nread.framework.MyApplication;
import com.horner.xsm.R;
import com.horner.xsm.base.BaseActivity;
import com.horner.xsm.constants.Constants;
import com.horner.xsm.data.ThirdVipUserCache;
import com.horner.xsm.data.UserCache;
import com.horner.xsm.utils.EmailUtils;
import com.horner.xsm.utils.LoadingDialog;
import com.horner.xsm.utils.ToastUtil;
import com.horner.xsm.utils.WifiUtils;
import com.mouee.common.HttpManager;
import com.mouee.common.StringUtils;

/**
 * @author 作者 : sun
 * @date 创建时间：2015-12-30 下午4:33:34
 * @version 1.0
 * @parameter
 * @return
 */

public class ThirdLoginActivity extends BaseActivity implements OnClickListener {

	private View titleView;
	private RelativeLayout leftButton;
	private TextView tvTitle;

	private static final String TAG = "ThirdLoginActivity";

	private Context mContext;

	private String multipartFile;
	private String nickName;
	private String thirdId;
	private String thirdType;
	private String userAddress;
	private String userSex;

	private TextView submit;
	private TextView tvGetCode;

	private EditText phoneNO;
	private EditText regisCode;
	private EditText passwordNO;

	@Override
	public void findView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏显示
		setContentView(R.layout.activity_register);
		titleView = findViewById(R.id.title_layout);
		leftButton = (RelativeLayout) titleView.findViewById(R.id.left_btn);
		tvTitle = (TextView) titleView.findViewById(R.id.tv_title);

		submit = (TextView) findViewById(R.id.register_submit);
		submit.setText("绑定");
		phoneNO = (EditText) findViewById(R.id.regis_phoneNO);
		regisCode = (EditText) findViewById(R.id.regis_code);
		passwordNO = (EditText) findViewById(R.id.regis_password);

		tvGetCode = (TextView) findViewById(R.id.getcode);
	}

	@Override
	public void initData() {
		MyApplication.addActivity(this);
		tvTitle.setText("绑定手机号");
		mContext = this;
		userCache = new ThirdVipUserCache(mContext);
		userCache.setPhone("");
		getDataFromSp();
	}

	@Override
	protected void onStart() {
		super.onStart();
		userCache.setPhone("");
	}

	/**
	 * 
	 * @author 作者 : sun
	 * @date 创建时间：2015-12-30 下午7:00:46
	 * @return
	 * @description 从sp里边拿到保存的一些三方用户的数据
	 */
	private void getDataFromSp() {
		multipartFile = userCache.getMultipartFile();
		nickName = userCache.getNickName();
		thirdId = userCache.getThirdId();
		thirdType = userCache.getThirdType();
		userAddress = userCache.getUserAddress();
		userSex = userCache.getUserSex();
		Log.i("info", nickName + thirdId + thirdType + "===========    ThirdLoginActivity "
				+ multipartFile + userAddress);
	}

	@Override
	public void addClickListener() {
		leftButton.setOnClickListener(this);
		submit.setOnClickListener(this);
		tvGetCode.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_btn:
			finish();
			break;
		case R.id.register_submit: // 将三方信息连同个人手机号一同提交
			bindPhone(dateCreated, code);
			break;
		case R.id.getcode: // 将三方信息连同个人手机号一同提交
			getCode();
			break;
		default:
			break;
		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				tvGetCode.setEnabled(false);
			} else if (msg.what == 2) {
				tvGetCode.setText(msg.arg1 + "s");
			} else if (msg.what == 3) {
				tvGetCode.setEnabled(true);
				tvGetCode.setText("获取验证码");
			}
		}
	};

	private void getCode() {
		if(!WifiUtils.isConnectingToInternet(ThirdLoginActivity.this)){
			ToastUtil.showShortToast(ThirdLoginActivity.this,"当前网络未连接,请连接后再试");
			return;
		}
		
		if(!WifiUtils.isAvailable(ThirdLoginActivity.this)){
			ToastUtil.showShortToast(ThirdLoginActivity.this,"当前网络不可用,请重新连接再试");
			return;
		}
		String phone = phoneNO.getText() + "";
		Log.i("info", "phone:" + phone);
		if (StringUtils.isEmpty(phone) || !EmailUtils.isMobileNO(phone)) {
			ToastUtil.showLongToast(mContext, "请输入正确的手机号");
			return;
		}
		if (!TextUtils.isEmpty(phone)) {
			ToastUtil.showShortToast(mContext, "正在获取手机验证码");
			userCache.setPhone(phone);
			submitphone(phone);

			new Thread() {
				@Override
				public void run() {
					super.run();

					int recordSed = 60;
					Message msg = new Message();
					msg.what = 1;
					mHandler.sendMessage(msg);

					for (;;) {
						if (--recordSed < 0)
							break;

						Message msg2 = new Message();
						msg2.what = 2;
						msg2.arg1 = recordSed;
						mHandler.sendMessage(msg2);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					Message msg3 = new Message();
					msg3.what = 3;
					mHandler.sendMessage(msg3);
				}

			}.start();
		} else {
			ToastUtil.showShortToast(mContext, "请输入正确的手机号");
			return;
		}
	}

	private String dateCreated;
	private String code;
	boolean isClick = false;
	// 提交手机号获取验证码的方法
	private void submitphone(final String phone) {
		AsyncTask<String, String, String> getloadTask = new AsyncTask<String, String, String>() {
			@Override
			protected String doInBackground(String... params) {
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("phone", phone);
				Log.i("info", "电话号码为:" + phone);
				String result = HttpManager.postDataToUrl(Constants.GET_CODE,
						param);
				Log.i("info", "电话号码为:" + result);
				isClick = true;
				try {
					JSONObject object = new JSONObject(result);
					code = object.getString("code");
					JSONObject object2 = object.getJSONObject("data");
					dateCreated = object2.getString("dateCreated");
					Log.i("infos", code + "" + dateCreated);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return code;
			}
		};
		getloadTask.execute();
	}

	/**
	 * 
	 * @author 作者 : sun
	 * @date 创建时间：2016-1-14 下午3:03:24
	 * @return
	 * @description 绑定手机号时一些相关信息的合法性检查
	 */
	private Boolean checkAllInfo(String phone, String password,
			String dateCreated, String code, String regisCodeReg) {

		if (StringUtils.isEmpty(phone) || !EmailUtils.isMobileNO(phone)) {
			ToastUtil.showLongToast(mContext, "请输入正确的手机号");
			return true;
		}
		
		if (!userCache.getPhone().equals("")) {
			if (phone != null && !phone.equals(userCache.getPhone())) {
				ToastUtil.showLongToast(mContext, "请输入正确的手机号");
				return true;
			}
		}
		if (StringUtils.isEmpty(password)) {
			ToastUtil.showLongToast(mContext, "密码不能为空");
		}
		if (StringUtils.isEmpty(password) || password.length() < 6
				|| password.length() > 18 || !EmailUtils.isPassword(password)) {
			ToastUtil.showLongToast(mContext, "请输入6-18位英文和数字的组合");
			return true;
		}
		if (TextUtils.isEmpty(regisCodeReg)) {
			Log.i("info", "验证码问题问题");
			ToastUtil.showLongToast(mContext, "验证码不能为空");
			return true;
		}
		if (!regisCodeReg.equals(code)) {
			ToastUtil.showLongToast(mContext, "验证码不正确,请重新输入");
			return true;
		}
		return false;

	}

	private void bindPhone(final String dateCreated, String code) {

		final String phone = phoneNO.getText() + "";
		final String password = passwordNO.getText() + "";
		String regisCodeReg = regisCode.getText() + "";

		if (checkAllInfo(phone, password, dateCreated, code, regisCodeReg)) {
			return;
		}
		if(!WifiUtils.isConnectingToInternet(ThirdLoginActivity.this)){
			ToastUtil.showShortToast(ThirdLoginActivity.this,"当前网络未连接,请连接后再试");
			return;
		}
		
		if(!WifiUtils.isAvailable(ThirdLoginActivity.this)){
			ToastUtil.showShortToast(ThirdLoginActivity.this,"当前网络不可用,请重新连接再试");
			return;
		}
		LoadingDialog.isLoading(ThirdLoginActivity.this);

		AsyncTask<String, String, String> loadDataTask = new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("phone", phone);
				param.put("comefrom", "AndroidPad");
				param.put("dateGreated", dateCreated);
				param.put("password", password);
				Log.i("info", "bindPhone" + phone + dateCreated);

				String result = HttpManager.postDataToUrl(Constants.BASE_URL
						+ "reg/user", param);
				Log.i("info", "submitphone" + result);
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
						String userId = object.optString("userId");
						if (code != null && code.equals("1000")) {
							ToastUtil.showLongToast(mContext, "手机号绑定成功");
							Message msg = Message.obtain();
							UserCache cache = new UserCache(mContext);
							cache.setUserId(new ThirdVipUserCache(mContext).getThirdId());
							cache.setIsLogin(true);
							msg.what = 0;
							msg.obj = phone;
							thirdHandler.sendMessage(msg);
						} else {
							if (code.equals("1001")) {
								ToastUtil.showLongToast(mContext, "验证码失效!");
								return;
							} else if (code.equals("1002")) {
								ToastUtil.showLongToast(mContext, "手机号为空!");
								return;
							} else if (code.equals("1003")) {
								 //ToastUtil.showLongToast(mContext,"该手机号已经注册过!");
								Message msg = Message.obtain();
								UserCache cache = new UserCache(mContext);
								cache.setUserId(new ThirdVipUserCache(mContext).getThirdId());
								cache.setIsLogin(true);
								msg.what = 1;
								msg.obj = phone;
								thirdHandler.sendMessage(msg);
								return;
							} else if (code.equals("1004")) {
								ToastUtil.showLongToast(mContext, "密码为空!");
								return;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					boolean internet = HttpManager
							.isConnectingToInternet(ThirdLoginActivity.this);
					if (internet) {
						Toast.makeText(ThirdLoginActivity.this, "注册失败,请重试!",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(ThirdLoginActivity.this,
								"注册失败,请检查网络是否可用!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
		loadDataTask.execute("");

	}

	Handler thirdHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				submitData((String) msg.obj);
				break;
			case 1:
				submitData((String) msg.obj);
				break;

			default:
				break;
			}
		}
	};
	private ThirdVipUserCache userCache;

	/**
	 * 
	 * @author 作者 : sun
	 * @date 创建时间：2015-12-30 下午5:32:42
	 * @return
	 * @description 将三方信息连同个人手机号一同提交
	 */
	private void submitData(final String phone) {
		if(!WifiUtils.isConnectingToInternet(ThirdLoginActivity.this)){
			ToastUtil.showShortToast(ThirdLoginActivity.this,"当前网络未连接,请连接后再试");
			return;
		}
		
		if(!WifiUtils.isAvailable(ThirdLoginActivity.this)){
			ToastUtil.showShortToast(ThirdLoginActivity.this,"当前网络不可用,请重新连接再试");
			return;
		}
		AsyncTask<String, String, String> loadDataTask = new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("phone", phone);
				param.put("userSex", userSex);
				param.put("userIcon", multipartFile);
				param.put("userAddress", userAddress);
				param.put("nickName", nickName);
				param.put("thirdId", thirdId);
				param.put("thirdType", thirdType);
				param.put("officeId", "40");

				Log.i("TAG", " " + param);
				String result = HttpManager.postDataToUrl(Constants.BASE_URL
						+ "reg/third", param);
				
//				 String result = HttpManager.postDataToUrl(
//				 "http://192.168.1.118:8080/p/front/reg/third", param);
				Log.i("info", "ThirdLoginActivity:" + result + " " + phone + " ");
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				LoadingDialog.finishLoading();
				if (!StringUtils.isEmpty(result)) {
					try {
						Log.i("info", "登录成功之后返回的数值为ThirdLoginActivity------:" + result);
						JSONObject object = new JSONObject(result);
						String code = object.getString("code");
						if (code != null && code.equals("1000")) {
							ToastUtil.showLongToast(mContext, "三方登录成功!");
							// 跳转到Home页面
							LoadingDialog.finishLoading();
//							// 表明微博账号或者微信账号被绑定
//							UserCache cache = new UserCache(mContext);
//							cache.setWeiChatState("1");
//							cache.setWeiBoState("1");
							String userIcon = object.optString("userIcon");
							String nickName = object.optString("nickName");
							String sex = object.optString("sex");
							String userAddress = object.optString("userAddress");
							UserCache cache = new UserCache(ThirdLoginActivity.this);
							
							cache.setVipType(object.optString("vipType"));
							cache.setVipTypeStartTime(object.optString("startTime"));
							cache.setVipTypeEndTime(object.optString("endTime"));
							cache.setUserPhone(object.optString("phone"));
							ThirdVipUserCache userCache = new ThirdVipUserCache(ThirdLoginActivity.this);
							if (!userIcon.equals("")) {
								userCache.setMultipartFile(userIcon);
							}
							if (!nickName.equals("")) {
								userCache.setNickName(nickName);
							}
							if (!sex.equals("")) {
								userCache.setUserSex(sex);
							}
							if (!userAddress.equals("")) {
								userCache.setUserAddress(userAddress);
							}
							
							Intent intent = new Intent(ThirdLoginActivity.this,
									HomeActivity.class);
							startActivity(intent);
						} else {
							if (code.equals("1002")) {
								ToastUtil.showLongToast(mContext, "三方用户已经登录!");
								return;
							} else if (code.equals("1001")) {
								ToastUtil.showLongToast(mContext, "手机号已被绑定!");
								return;
							} else if (code.equals("1003")) {
								ToastUtil.showLongToast(mContext, "三方登录失败!");
								return;
							} 
							else if (code.equals("1004")) {
								ToastUtil.showLongToast(mContext, "三方登录失败,请重试!");
								return;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					boolean internet = HttpManager
							.isConnectingToInternet(ThirdLoginActivity.this);
					if (internet) {
						Toast.makeText(ThirdLoginActivity.this, "登录失败,请重试!",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(ThirdLoginActivity.this,
								"登录失败,请检查网络是否可用!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
		loadDataTask.execute("");

	}
}
