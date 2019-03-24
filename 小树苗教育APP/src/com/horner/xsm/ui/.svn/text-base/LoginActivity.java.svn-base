package com.horner.xsm.ui;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.horner.xsm.utils.AliPayUtils;
import com.horner.xsm.utils.EmailUtils;
import com.horner.xsm.utils.LoadingDialog;
import com.horner.xsm.utils.SoftInputUtils;
import com.horner.xsm.utils.SysEnvUtils;
import com.horner.xsm.utils.ToastUtil;
import com.mouee.common.HttpManager;
import com.mouee.common.StringUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @version
 * @author sun
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private View titleView;
	private RelativeLayout leftButton;
	private TextView tvTitle;
	private Button rightButton;
	private TextView loginButton;
	private EditText phoneNO;
	private EditText passwordNO;
	private TextView forgetPwd;
	private Context mContext;
	private UMShareAPI umShareAPI;
	private ImageButton weiBoButton;
	private ImageButton weiXinButton;
	private RelativeLayout login_layout;

	// 以何种方式登录
	public static final String RAW_LOGIN = "raw_login";
	public static final String THIRD_LOGIN = "third_login";

	@Override
	public void findView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// setContentView(R.layout.activity_login);
		if (screenType == $1920x1080) {
			setContentView(R.layout.activity_login);
		} else {
			setContentView(R.layout.activity_login1);
		}
		titleView = findViewById(R.id.title_layout);
		leftButton = (RelativeLayout) titleView.findViewById(R.id.left_btn);
		rightButton = (Button) titleView.findViewById(R.id.right_btn);
		tvTitle = (TextView) titleView.findViewById(R.id.tv_title);
		loginButton = (TextView) findViewById(R.id.login_submit);
		leftButton.setVisibility(View.GONE);

		phoneNO = (EditText) findViewById(R.id.phone_no);
		passwordNO = (EditText) findViewById(R.id.password);
		forgetPwd = (TextView) findViewById(R.id.forget_password);

		// phoneNO.setText("15611664430");
		// passwordNO.setText("abc123");

		// hideInput(this,phoneNO);

		weiBoButton = (ImageButton) findViewById(R.id.weibo_login);
		weiXinButton = (ImageButton) findViewById(R.id.weixin_login);
		
		login_layout = (RelativeLayout) findViewById(R.id.login_layout);
		loginButton.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(arg1){  
					loginButton.setTextColor(Color.YELLOW);
                }else{  
                	loginButton.setTextColor(Color.WHITE);
                }
				
			}
			
		});
	}

	UserCache userCache = null;

	@Override
	public void initData() {
		MyApplication.addActivity(this);
		mContext = this;
		// TextView设置下划线
		forgetPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		userCache = new UserCache(this);
		SoftInputUtils.closeSoftInput(this);
		tvTitle.setText("登录");
		rightButton.setVisibility(View.VISIBLE);
		rightButton.setText("注册");
		rightButton.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(arg1){  
					rightButton.setTextColor(Color.YELLOW);
                }else{  
                	rightButton.setTextColor(Color.WHITE);
                }
				
			}
			
		});

		umShareAPI = UMShareAPI.get(this);
		// delAuth(SHARE_MEDIA.WEIXIN);
		// delAuth(SHARE_MEDIA.SINA);
	}

	@Override
	public void addClickListener() {
		rightButton.setOnClickListener(this);
		loginButton.setOnClickListener(this);
		leftButton.setOnClickListener(this);
		forgetPwd.setOnClickListener(this);
		weiBoButton.setOnClickListener(this);
		weiXinButton.setOnClickListener(this);
	}

	private void hideInput(Context context, View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	@Override
	public void onClick(View v) {
		SHARE_MEDIA platform = null;
		switch (v.getId()) {
		case R.id.right_btn: // 跳转到登录界面
			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.login_submit:
			userCache.setLoginMethods(RAW_LOGIN);
			submitData();
			break;
		case R.id.weibo_login:
			platform = SHARE_MEDIA.SINA;
			Log.i("info", "sjfdsfsdfsdfdf---");
			userCache.setLoginMethods(THIRD_LOGIN);
			login(platform);
			break;
		case R.id.weixin_login:
			platform = SHARE_MEDIA.WEIXIN;
			Log.i("info", "weixin");
			userCache.setLoginMethods(THIRD_LOGIN);
			if(!SysEnvUtils.isAvilible(this,"com.tencent.mm")){
				ToastUtil.showShortToast(this,"未安装微信客户端,请安装");
				return;
			}
			login(platform);
			break;
		case R.id.forget_password: // 忘记密码功能的实现
			intent = new Intent(LoginActivity.this, AccontLoginActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	private void close() {
		finish();
	}

	private Intent intent;

	private void submitData() {
		final String phone = phoneNO.getText() + "";
		final String password = passwordNO.getText() + "";
		if (checkAllInfo(phone, password)) {
			return;
		}
		LoadingDialog.isLoading(LoginActivity.this);
		AsyncTask<String, String, String> loadDataTask = new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("phone", phone);
				param.put("password", password);
				param.put("officeId", "40");
				Log.i("info", "param:" + param.toString());
				String result = HttpManager.postDataToUrl(Constants.BASE_URL
						+ "login/product", param);
				// String result =
				// HttpManager.postDataToUrl("http://192.168.1.102:8080/p/front/"
				// + "login/product", param);
				Log.i("info", "result的数值为:" + result + " " + phone + " "
						+ password);
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
						if (code != null && code.equals("1000")) {
							String userId = object.getString("userId");
							String vipType = object.getString("vipType");
							String startTime = object.optString("startTime");
							String userPhone = object.optString("phone");
							String endTime = object.optString("endTime");
							String weiboState = object.optString("weibo");
							String weixinState = object.optString("wechat");
							String userIcon = object.optString("userIcon");
							String nickName = object.optString("nickName");
							String birthday = object.optString("birthday");
							String sex = object.optString("sex");
							String userAddress = object.optString("userAddress");
							saveUserInfo(userId, vipType, startTime, endTime,
									weiboState, weixinState, userIcon,
									nickName, birthday, sex, userAddress,userPhone);

							intent = new Intent(LoginActivity.this,
									HomeActivity.class);
							startActivity(intent);
							close();
							// finish();
						} else {
							if (code.equals("1001")) {
								ToastUtil.showLongToast(mContext, "手机号为空!");
								return;
							} else if (code.equals("1002")) {
								ToastUtil.showLongToast(mContext, "密码为空!");
								return;
							} else if (code.equals("1003")) {
								ToastUtil.showLongToast(mContext, "密码不正确!");
								return;
							} else if (code.equals("1004")) {
								ToastUtil.showLongToast(mContext, "登录失败!");
								return;
							} else if (code.equals("1005")) {
								ToastUtil.showLongToast(mContext, "登录失败!");
								return;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					boolean internet = HttpManager
							.isConnectingToInternet(LoginActivity.this);
					if (internet) {
						Toast.makeText(LoginActivity.this, "登录失败,请重试!",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(LoginActivity.this, "登录失败,请检查网络是否可用!",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
		loadDataTask.execute("");
	}

	private boolean checkAllInfo(String phone, String password) {
		if (StringUtils.isEmpty(phone) || !EmailUtils.isMobileNO(phone)) {
			ToastUtil.showLongToast(mContext, "请输入正确的手机号");
			return true;
		}
		if (StringUtils.isEmpty(password)) {
			ToastUtil.showLongToast(mContext, "密码不能为空");
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @author 作者 : sun
	 * @date 创建时间：2015-12-31 下午5:54:57
	 * @return
	 * @description 用户登陆之后将信息保存(非三方用户)
	 */
	private void saveUserInfo(String userId, String vipType, String startTime,
			String endTime, String weiboState, String weixinState,
			String userIcon, String nickName, String birthday, String userSex,
			String userAddress,String phone) {

		userCache.setUserId(userId);
		userCache.setIsLogin(true);
		userCache.setVipType(vipType);
		userCache.setVipTypeStartTime(startTime);
		userCache.setVipTypeEndTime(endTime);
		userCache.setWeiBoState(weiboState);
		userCache.setWeiChatState(weixinState);
		if (userIcon != null && userIcon.contains("http")) {
			userCache.setMultipartFile( userIcon);
		}else {
			userCache.setMultipartFile(Constants.BASE_PIC + userIcon);
		}
		userCache.setNickName(nickName);
		userCache.setBirthDate(birthday);
		userCache.setUserSex(userSex);
		userCache.setUserAddress(userAddress);
		userCache.setUserPhone(phone);
	}

	/**
	 * 
	 * @author 作者 : sun
	 * @date 创建时间：2015-12-30 上午11:17:51
	 * @return
	 * @description 取消授权
	 */
	public void delAuth(final SHARE_MEDIA platform) {
		// 取消授权
		umShareAPI.deleteOauth(this, platform, new UMAuthListener() {

			@Override
			public void onError(SHARE_MEDIA arg0, int arg1, Throwable arg2) {
			}

			@Override
			public void onComplete(SHARE_MEDIA arg0, int arg1,
					Map<String, String> arg2) {
				// ToastUtil.showShortToast(getApplicationContext(), "取消授权");
			}

			@Override
			public void onCancel(SHARE_MEDIA arg0, int arg1) {
			}
		});

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
		umShareAPI.doOauthVerify(LoginActivity.this, platform,
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
		umShareAPI.getPlatformInfo(LoginActivity.this, platform,
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		umShareAPI.onActivityResult(requestCode, resultCode, data);
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

		ThirdVipUserCache userCache = new ThirdVipUserCache(mContext);
		String thirdId;
		String address;
		String sex;
		String imageUrl;
		String nickName;

		UserCache cache = new UserCache(mContext);
		// Log.i("info", "thirdId" + thirdId);

		if (thirdType.equals(SHARE_MEDIA.SINA)) {
			thirdId = info.get("uid");
			address = info.get("location");
			sex = info.get("gender");
			Log.i("LoginActivity","微博的性别为:"+sex);
			imageUrl = info.get("profile_image_url");
			nickName = info.get("screen_name");

			userCache.setThirdId(thirdId);
			userCache.setUserAddress(address);
			userCache.setUserSex(sex);
			userCache.setMultipartFile(imageUrl);
			userCache.setNickName(nickName);
			userCache.setThirdType("weibo");

			cache.setWeiBoState("1");

			// 拿到三方信息之后，确认三方用户是否已经登录
			isThirdLogin(thirdId, "weibo");
		} else {
			thirdId = info.get("unionid");
			address = info.get("city");
			sex = info.get("sex");
			Log.i("LoginActivity","微信的性别为:"+sex);
			imageUrl = info.get("headimgurl");
			nickName = info.get("nickname");

			userCache.setThirdId(thirdId);
			userCache.setUserAddress(address);
			userCache.setUserSex(sex);
			userCache.setMultipartFile(imageUrl);
			Log.i("info", "userCache.setMultipartFile(imageUrl);");
			userCache.setNickName(nickName);
			userCache.setThirdType("wechat");

			Log.i("info", "thirdId " + thirdId + address + sex + imageUrl
					+ nickName);

			cache.setWeiChatState("1");

			// 拿到三方信息之后，确认三方用户是否已经登录
			isThirdLogin(thirdId, "wechat");
		}
	}

	private void isThirdLogin(final String thirdId, final String thirdType) {

		AsyncTask<String, String, String> loadDataTask = new AsyncTask<String, String, String>() {

			private String third;

			@Override
			protected String doInBackground(String... params) {
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("thirdId", thirdId);
				param.put("thirdType", thirdType);
				param.put("officeId", "40");
				Log.i("info", "param:" + param.toString());
				String result = HttpManager.postDataToUrl(Constants.BASE_URL
						+ "login/third", param);

				// String result = HttpManager.postDataToUrl(
				// "192.168.1.118:8080/p/front/login/third", param);
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				LoadingDialog.finishLoading();
				if (!StringUtils.isEmpty(result)) {
					try {
						Log.i("info", "isThirdLogin 登录成功之后返回的数值为:" + result);
						JSONObject object = new JSONObject(result);
						String code = object.optString("code");

						Log.i("info", "登录成功之后返回的数值为:" + code);
						// if (code.equals("1000")) {
						// third = object.getString("thirdId");
						// }
						// dianzikeda
						if (code != null && code.equals("1000")) {
							Log.i("info", "Login登录成功之后返回的数值为:" + code);

							ThirdVipUserCache userCache = new ThirdVipUserCache(
									LoginActivity.this);
							userCache.setMultipartFile(object.optString("userIcon"));
							userCache.setNickName(object.optString("nickName"));
							userCache.setUserSex(object.optString("sex"));
							userCache.setBirthDate(object.optString("birthday"));
							userCache.setUserAddress(object.optString("userAddress"));
							String userId = object.optString("userId");
							UserCache cache = new UserCache(LoginActivity.this);
							cache.setIsLogin(true);
							cache.setUserId(userId);
							cache.setVipType(object.optString("vipType"));
							cache.setVipTypeStartTime(object.optString("startTime"));
							cache.setVipTypeEndTime(object.optString("endTime"));
							cache.setUserPhone(object.optString("phone"));
							// ToastUtil.showLongToast(mContext, "三方用户登录成功!");
							Intent intent = new Intent(LoginActivity.this,
									HomeActivity.class);
							startActivity(intent);
						} else {
							if (code.equals("1001")) {
								// ToastUtil.showLongToast(mContext,
								// "三方用户不存在!");
								Intent intent = new Intent(LoginActivity.this,
										ThirdLoginActivity.class);
								startActivity(intent);
							} else if (code.equals("1002")) {
								ToastUtil.showLongToast(mContext,
										"该账号已经绑定别的手机号!");
								Log.i("info", "该账号已经绑定别的手机号");
								return;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					boolean internet = HttpManager
							.isConnectingToInternet(LoginActivity.this);
					if (internet) {
						Toast.makeText(LoginActivity.this, "登录失败,请重试!",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(LoginActivity.this, "登录失败,请检查网络是否可用!",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
		loadDataTask.execute("");

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//Toast.makeText(this, keyCode + "", Toast.LENGTH_SHORT).show();
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			com.horner.xsm.utils.ExitUtil.Exit(mContext);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
