package com.horner.xsm.ui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
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
import com.horner.xsm.net.AsyncHttpClient;
import com.horner.xsm.net.AsyncHttpResponseHandler;
import com.horner.xsm.utils.EmailUtils;
import com.horner.xsm.utils.LoadingDialog;
import com.horner.xsm.utils.ToastUtil;
import com.horner.xsm.utils.XMLPullUtil;
import com.mouee.common.HttpManager;
import com.mouee.common.StringUtils;

/**
 * @author 作者 : sun
 * @date 创建时间：2016-1-12 上午11:29:26
 * @version 1.0
 * @parameter
 * @return
 */

public class ResetPwdActivity extends BaseActivity implements OnClickListener {

	private EditText newPwd;
	private TextView submit;
	private Context mContext;
	private AsyncHttpClient client;

	@Override
	public void findView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_resetpwd);
		titleView = findViewById(R.id.reset_titlebar);
		leftButton = (RelativeLayout) titleView.findViewById(R.id.left_btn);
		textView = (TextView) titleView.findViewById(R.id.tv_title);
		newPwd = (EditText) findViewById(R.id.inputnewpwd);
		submit = (TextView) findViewById(R.id.getcode_submit);
	}

	private String dateCreated;
	private String phoneNum;

	@Override
	public void initData() {
		MyApplication.addActivity(this);
		textView.setText("密码重置");
		mContext = this;
		client = new AsyncHttpClient(mContext);
		dateCreated = getIntent().getStringExtra("dateCreated");
		phoneNum = getIntent().getStringExtra("phoneNum");
		Log.i("info", dateCreated + phoneNum);
	}

	@Override
	public void addClickListener() {
		submit.setOnClickListener(this);
		leftButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.getcode_submit:
			submitData();
			break;
		case R.id.left_btn:
			finish();
			break;

		default:
			break;
		}
	}

	private Boolean checkInfo(String newPwds) {
		if (StringUtils.isEmpty(newPwds)) {
			ToastUtil.showLongToast(mContext, "密码不能为空");
			return true;
		}

		if (StringUtils.isEmpty(newPwds) || newPwds.length() < 6
				|| newPwds.length() > 18 || !EmailUtils.isPassword(newPwds)) {
			ToastUtil.showLongToast(mContext, "请输入6-18位英文和数字的组合");
			return true;
		}
		return false;
	}

	private String newPwds;
	private RelativeLayout leftButton;
	private View titleView;
	private TextView textView;

	private void submitData() {

		newPwds = newPwd.getText() + "";
		if (checkInfo(newPwds)) {
			return;
		}
		LoadingDialog.isLoading(mContext);
		com.horner.xsm.net.RequestParams param = new com.horner.xsm.net.RequestParams();
		param.put("password", newPwds);
		param.put("userName", phoneNum);
		param.put("dateGreated", dateCreated);
		client.post(Constants.CHONGZHIPWD, param,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String result) {
						super.onSuccess(result);
						Log.d("info", "Reset页面的返回值为:" + result);
						LoadingDialog.finishLoading();
						if (!StringUtils.isEmpty(result)) {
							try {

								InputStream is_status = new ByteArrayInputStream(
										result.getBytes("UTF-8"));
								String status = XMLPullUtil
										.getStatus(is_status);
								if (status.trim().equals("1")) {
									Toast.makeText(ResetPwdActivity.this,
											"登录名不能为空", Toast.LENGTH_SHORT)
											.show();
								} else if (status.trim().equals("2")) {
									Toast.makeText(ResetPwdActivity.this,
											"密码不能为空", Toast.LENGTH_SHORT)
											.show();
								} else if (status.trim().equals("3")) {
									Toast.makeText(ResetPwdActivity.this,
											"登录名不存在", Toast.LENGTH_SHORT)
											.show();
								} else if (status.trim().equals("4")) {
									Toast.makeText(ResetPwdActivity.this,
											"验证码失效", Toast.LENGTH_SHORT).show();
								} else if (status.trim().equals("5")) {
									Toast.makeText(ResetPwdActivity.this,
											"密码重置成功", Toast.LENGTH_SHORT)
											.show();
									Intent intent3 = new Intent(
											ResetPwdActivity.this,
											LoginActivity.class);
									startActivity(intent3);
									finish();
								} else {
									ToastUtil.showLongToast(mContext,
											"找回失败，请重试!");
								}

							} catch (Exception e) {

							}
						} else {
							boolean internet = HttpManager
									.isConnectingToInternet(ResetPwdActivity.this);
							if (internet) {
								Toast.makeText(ResetPwdActivity.this,
										"重置密码失败,请重试。", Toast.LENGTH_SHORT)
										.show();
							} else {
								Toast.makeText(ResetPwdActivity.this,
										"重置密码失败,请检查网络是否可用。", Toast.LENGTH_SHORT)
										.show();
							}
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						// TODO Auto-generated method stub
					}

				});
	}

}
