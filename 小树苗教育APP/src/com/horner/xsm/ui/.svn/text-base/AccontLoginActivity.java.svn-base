package com.horner.xsm.ui;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.horner.nread.framework.MyApplication;
import com.horner.xsm.R;
import com.horner.xsm.base.BaseActivity;
import com.horner.xsm.constants.Constants;
import com.horner.xsm.utils.EmailUtils;
import com.horner.xsm.utils.LoadingDialog;
import com.horner.xsm.utils.ToastUtil;
import com.mouee.common.HttpManager;
import com.mouee.common.StringUtils;

/**
 * @author 作者 : sun
 * @date 创建时间：2016-1-12 上午10:13:53
 * @version 1.0
 * @parameter 用于登录已注册账号找回密码
 * @return
 */

public class AccontLoginActivity extends BaseActivity implements
		OnClickListener {

	private View titleBar;
	private TextView tv_title;
	public String titleContent = "找回密码";
	private TextView login_submit;
	private EditText userName;
	private Context mContext;

	@Override
	public void findView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_accountlogin);
		titleBar = findViewById(R.id.titleLayout);
		tv_title = (TextView) titleBar.findViewById(R.id.tv_title);
		imgButton = (RelativeLayout) titleBar.findViewById(R.id.left_btn);
		login_submit = (TextView) findViewById(R.id.accountlogin_submit);
		userName = (EditText) findViewById(R.id.inputs);
		getCode = (TextView) findViewById(R.id.getcode);
		inputCode = (EditText) findViewById(R.id.inputcode);
	}

	@Override
	public void initData() {
		MyApplication.addActivity(this);
		mContext = this;
		tv_title.setText(titleContent);
	}

	@Override
	public void addClickListener() {
		login_submit.setOnClickListener(this);
		imgButton.setOnClickListener(this);
		getCode.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.accountlogin_submit:
			toResetActivity(dateCreated, phoneNum, code);
			break;
		case R.id.left_btn:
			finish();
			break;
		case R.id.getcode:
			submitData();
			break;
		default:
			break;
		}
	}

	private void toResetActivity(String dateCreated, String phoneNum,
			String code) {
		
		String inputCods = inputCode.getText() + "";
		phoneNum = userName.getText() + "";
		if (StringUtils.isEmpty(phoneNum)) {
			ToastUtil.showLongToast(mContext, "手机号不能为空!");
			return;
		}
		
		if (!EmailUtils.isMobileNO(phoneNum)) {
			ToastUtil.showLongToast(mContext, "请输入正确的手机号");
			return;
		}
		
		if (StringUtils.isEmpty(inputCods)) {
			ToastUtil.showLongToast(mContext, "验证码不能为空!");
			return;
		}
		if (!inputCods.equals(code)) {
			ToastUtil.showLongToast(mContext, "验证码输入不正确,请重新输入!");
			return;
		}
		if (!StringUtils.isEmpty(dateCreated)) {
			LoadingDialog.isLoading(mContext);
			Intent intent = new Intent(AccontLoginActivity.this,
					ResetPwdActivity.class);
			intent.putExtra("dateCreated", dateCreated);
			intent.putExtra("phoneNum", phoneNum);
			Log.i("info", dateCreated + phoneNum);
			startActivity(intent);
			LoadingDialog.finishLoading();
		}
	}

	private String phoneNum = null;
	private RelativeLayout imgButton;
	private TextView getCode;
	private String dateCreated;
	private String code;
	private EditText inputCode;

	private void submitData() {

		ToastUtil.showLongToast(mContext, "正在获取验证码");
		phoneNum = userName.getText() + "";
		if (StringUtils.isEmpty(phoneNum) || !EmailUtils.isMobileNO(phoneNum)) {
			ToastUtil.showLongToast(mContext, "请输入正确的手机号");
			return;
		}
		getCode();
		AsyncTask<String, String, String> loadDataTask = new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("phone", phoneNum);
				String result = HttpManager.postDataToUrl(Constants.FORGET_PWD,
						param);
				Log.i("info", "result的数值为:" + result);
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if (!StringUtils.isEmpty(result)) {
					try {
						Log.i("info", "找回的短信验证码时间为:" + result);
						JSONObject object = new JSONObject(result);
						JSONObject dataObject = object.getJSONObject("data");
						code = object.getString("code");

						if (!StringUtils.isEmpty(code)) {
							dateCreated = dataObject.optString("dateCreated");
						} else {
							ToastUtil.showLongToast(mContext, "获取手机验证码失败!");
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					boolean internet = HttpManager
							.isConnectingToInternet(AccontLoginActivity.this);
					if (internet) {
						Toast.makeText(AccontLoginActivity.this, "找回密码失败,请重试!",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(AccontLoginActivity.this,
								"找回密码失败失败,请检查网络是否可用!", Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
		};
		loadDataTask.execute("");
	}

	private void getCode() {

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
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				getCode.setEnabled(false);
			} else if (msg.what == 2) {
				getCode.setText(msg.arg1 + "s");
			} else if (msg.what == 3) {
				getCode.setEnabled(true);
				getCode.setText("获取验证码");
			}
		}
	};

}
