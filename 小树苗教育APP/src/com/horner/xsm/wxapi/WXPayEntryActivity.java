package com.horner.xsm.wxapi;

import com.horner.nread.framework.MyApplication;
import com.horner.xsm.R;
import com.horner.xsm.utils.ToastUtil;
import com.horner.xsm.utils.WePayUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this,MyApplication.WEIXIN_APPID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "支付完成返回码 = " + resp.errStr+","+resp.errCode);

		switch (resp.errCode) {
		case 0:
			finish();
			ToastUtil.showShortToast(WXPayEntryActivity.this, "支付成功");
			Intent intent = new Intent("com.horner.settingcast");
			intent.putExtra("isSuccess",true);
			sendBroadcast(intent);
			WePayUtils.getPayOrderInfo(WXPayEntryActivity.this);
			break;
		case -1:
			ToastUtil.showShortToast(WXPayEntryActivity.this, "微信支付失败");
			finish();
			break;
		case -2:
			ToastUtil.showShortToast(WXPayEntryActivity.this, "用户取消支付");
			finish();
			break;
		default:
			break;
		} 
	}
}