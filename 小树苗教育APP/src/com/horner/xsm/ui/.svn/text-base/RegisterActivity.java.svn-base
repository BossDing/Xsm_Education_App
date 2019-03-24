package com.horner.xsm.ui;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
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
import com.horner.xsm.data.UserCache;
import com.horner.xsm.utils.EmailUtils;
import com.horner.xsm.utils.LoadingDialog;
import com.horner.xsm.utils.ToastUtil;
import com.horner.xsm.utils.WifiUtils;
import com.mouee.common.HttpManager;
import com.mouee.common.StringUtils;

public class RegisterActivity extends BaseActivity implements OnClickListener {

	private View titleView;
	private TextView tvTitle;
	private RelativeLayout leftButton;
	private TextView submit;
	private TextView tvGetCode;

	private EditText phoneNO;
	private EditText regisCode;
	private EditText passwordNO;

	private Context mContext;

	@Override
	public void findView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏显示
		setContentView(R.layout.activity_register);
		// 顶部标题栏的
		titleView = findViewById(R.id.title_layout);
		leftButton = (RelativeLayout) titleView.findViewById(R.id.left_btn);
		tvTitle = (TextView) titleView.findViewById(R.id.tv_title);

		submit = (TextView) findViewById(R.id.register_submit);
		submit.setText("注册");
		phoneNO = (EditText) findViewById(R.id.regis_phoneNO);
		regisCode = (EditText) findViewById(R.id.regis_code);
		passwordNO = (EditText) findViewById(R.id.regis_password);

		tvGetCode = (TextView) findViewById(R.id.getcode);
		Log.i("info", "phoneNO的数值为:" + phoneNO + "  " + passwordNO + " "
				+ regisCode + " " + tvGetCode + " " + submit);
		
		tvGetCode.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(arg1){  
					tvGetCode.setTextColor(Color.YELLOW);
                }else{  
                	tvGetCode.setTextColor(Color.rgb(0x34, 0xd3, 0xca));
                }
				
			}
			
		});
		
		submit.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(arg1){  
					submit.setTextColor(Color.YELLOW);
                }else{  
                	submit.setTextColor(Color.WHITE);
                }
				
			}
			
		});
	}

	@Override
	public void initData() {
		MyApplication.addActivity(this);
		mContext = this;
		userCache = new UserCache(mContext);
		tvTitle.setText("注册");
		userCache.setPhone("");
	}

	@Override
	protected void onStart() {
		super.onStart();
		//userCache.setPhone("");
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
		case R.id.register_submit:
			submitData(dateCreated, code);
			break;
		case R.id.getcode: // 获取手机验证码
			Log.i("info", "验证码");
			getCode();
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @author 作者 : sun
	 * @date 创建时间：2016-1-4 上午11:29:52
	 * @return
	 * @description 对手机号验证码以及密码输入的合法性检查
	 * 
	 */

	String oldDatecreated = "000";
	Boolean isFirst = true;

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
		
//		if (!isClick) {
//			ToastUtil.showLongToast(mContext, "请点击获取验证码");
//		}
//		if (isClick) {
//			isClick = false;
//		}
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

	private void submitData(final String dateCreated, String code) {

		final String phone = phoneNO.getText() + "";
		final String password = passwordNO.getText() + "";
		String regisCodeReg = regisCode.getText() + "";

		if (checkAllInfo(phone, password, dateCreated, code, regisCodeReg)) {
			return;
		}

		Log.i("info", "返回值问题");

		LoadingDialog.isLoading(RegisterActivity.this);
		AsyncTask<String, String, String> loadDataTask = new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("phone", phone);
				param.put("password", password);
				param.put("comefrom", "AndroidPad");
				param.put("dateGreated", dateCreated);
				String result = HttpManager.postDataToUrl(Constants.BASE_URL
						+ "reg/user", param);
				Log.i("info", "result的数值为:" + result);
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				LoadingDialog.finishLoading();
				if (!StringUtils.isEmpty(result)) {
					try {
						Log.i("info", "result的数值为i:" + result);
						JSONObject object = new JSONObject(result);
						String code = object.getString("code");
						String userId = object.optString("userId");
						Log.i("info", "userid的数值为:" + userId);
						if (code != null && code.equals("1000")) {
							ToastUtil.showLongToast(mContext, "注册成功!");
							UserCache cache = new UserCache(mContext);
							//cache.setUserId(userId);
							Intent intent = new Intent(RegisterActivity.this,
									MoreInfoActivity.class);
							intent.putExtra("id", userId);
							intent.putExtra("from", "Login");
							startActivity(intent);
						} else if (code.equals("1001")) {
							ToastUtil.showLongToast(mContext, "验证码失效!");
							return;
						} else if (code.equals("1002")) {
							ToastUtil.showLongToast(mContext, "手机号为空!");
							return;
						} else if (code.equals("1003")) {
							ToastUtil.showLongToast(mContext, "该手机号已经注册过!");
							return;
						} else if (code.equals("1004")) {
							ToastUtil.showLongToast(mContext, "密码为空!");
							return;
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					boolean internet = HttpManager
							.isConnectingToInternet(RegisterActivity.this);
					if (internet) {
						Toast.makeText(RegisterActivity.this, "注册失败,请重试!",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(RegisterActivity.this,
								"注册失败,请检查网络是否可用!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
		loadDataTask.execute("");

	}

	
	Boolean isGetCode = false;
	Boolean isClick = false;
	private void getCode() {
		if (!WifiUtils.isNetWorkActive(mContext, "获取验证码失败,请检查网络是否可用!")) {
			return;
		}
		String phone = phoneNO.getText() + "";
		if (StringUtils.isEmpty(phone) || !EmailUtils.isMobileNO(phone)) {
			ToastUtil.showLongToast(mContext, "请输入正确的手机号");
			return;
		}  
		Log.i("info", "phone:" + phone);
		if (!TextUtils.isEmpty(phone)) {
			ToastUtil.showShortToast(mContext, "正在获取手机验证码");
			userCache.setPhone(phone);
			submitphone(phone);
			isClick = true;
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

	private String dateCreated = "";
	private String code = "";

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
				try {
					JSONObject object = new JSONObject(result);
					code = object.getString("code");
					Message msg = Message.obtain();
					msg.what = 10;
					mHandler.sendMessage(msg);
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

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				tvGetCode.setEnabled(false);
			} else if (msg.what == 2) {
				tvGetCode.setText(msg.arg1 + "s");
			} else if (msg.what == 3) {
				tvGetCode.setEnabled(true);
				tvGetCode.setText("获取验证码");
			} else {
				//regisCode.setText(code);
			}
		}
	};
	private UserCache userCache;

}
