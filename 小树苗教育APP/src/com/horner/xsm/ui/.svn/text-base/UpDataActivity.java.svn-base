package com.horner.xsm.ui;

import com.horner.nread.framework.MyApplication;
import com.horner.xsm.R;
import com.horner.xsm.base.BaseActivity;
import com.horner.xsm.utils.LoadingDialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class UpDataActivity extends BaseActivity {

	private WebView webView;
	@Override
	public void findView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_updata);
	}

	@Override
	public void initData() {
		MyApplication.addActivity(this);
		Intent intent = getIntent();
		final String myUrl = intent.getStringExtra("result");
		//String url = "http://www.pgyer.com/hs6N";
		webView = (WebView) findViewById(R.id.webView1);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		// webView.loadDataWithBaseURL(url, null, null, null, null);
		webView.loadUrl(myUrl);
//		webView.setWebViewClient(new WebViewClient() {
//			@Override
//			public void onPageStarted(WebView view, String url, Bitmap favicon) {
//				super.onPageStarted(view, url, favicon);
//				LoadingDialog.isLoading(UpDataActivity.this);
//			}
//
//			@Override
//			public void onPageFinished(WebView view, String url) {
//				super.onPageFinished(view, url);
//				LoadingDialog.finishLoading();
//			}
//
//			@Override
//			public boolean shouldOverrideUrlLoading(final WebView view,  //新的链接地址在webView中打开
//					final String url) {
//				view.loadUrl(url);
//				Log.i("info","updateActivity");
//				return false;
//			}
//		});
	}

	@Override
	public void addClickListener() {
		
	}

}
