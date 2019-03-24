package com.horner.xsm.ui;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.horner.nread.framework.MyApplication;
import com.horner.xsm.R;
import com.horner.xsm.base.BaseActivity;
import com.horner.xsm.bean.Book;
import com.horner.xsm.constants.Constants;
import com.horner.xsm.data.BookDataManager;
import com.horner.xsm.data.UserCache;
import com.horner.xsm.net.AsyncHttpClient;
import com.horner.xsm.net.RequestParams;
import com.horner.xsm.utils.LoadingDialog;
import com.horner.xsm.utils.ToastUtil;
import com.horner.xsm.widget.MyVideoView;
import com.mouee.common.HttpManager;
import com.mouee.common.StringUtils;

public class MainActivity extends BaseActivity {
	private MediaPlayer mMediaPlayer;
	private int mVideoWidth = 0;
	private int mVideoHeight = 0;
	private MyVideoView videoView;

	private boolean readyToPlay = false;

	private String VIDEO = "v1920_1080.mp4";

	public boolean storeVideoFile() {
		try {
			InputStream is = getResources().getAssets().open(VIDEO);

			// 注意,这里用 MODE_WORLD_READABLE 是因为播放Video的是MediaPlayer进程,不是本进程
			// 为了让, MediaPlayer进程能读取此文件,所以设置为: MODE_WORLD_READABLE
			// if (screenType == $1920x1080) {
			// VIDEO = "v1920_1080.mp4";
			// } else {
			// VIDEO = "v2048_1536.mp4";
			// }
			VIDEO = "v2048_1536.mp4";
			FileOutputStream os = openFileOutput(VIDEO, MODE_WORLD_READABLE);

			byte[] buffer = new byte[1024];
			while (is.read(buffer) > -1) {
				os.write(buffer);
			}
			is.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	  String userId;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏显示

		if (screenType == $1920x1080) {
			setContentView(R.layout.activity_main);
		} else {
			setContentView(R.layout.activity_main1);
		}
		getBooIdFromXinban();
		videoView = (MyVideoView) findViewById(R.id.videoView1);

		if (!storeVideoFile())
			return;

		videoView.setVideoPath(getFilesDir().getAbsolutePath() + "/" + VIDEO);

		videoView.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.setLooping(false);
			}
		});
		videoView.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				return true;
			}
		});
		
		videoView.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {
				/** flash播放结束跳转到登录界面 */
				UserCache userCache = new UserCache(MainActivity.this);
				userId = userCache.getUserId();
				Intent intent = null;
				//isBuyCategory("221",userId);
				if (userId != null && !userId.equals("")) { // 用户已经登录
					if (null != MyApplication.bookId && !"".equals(MyApplication.bookId)) { 
						new Thread(){
							public void run(){
								BookDataManager.getBookListByDetail(mHandler, MyApplication.bookId,MainActivity.this);
							}
						}.start();
					}else {
						intent = new Intent(MainActivity.this, HomeActivity.class);
						startActivity(intent);
					}
					
				} else {
					intent = new Intent(MainActivity.this, LoginActivity.class);
					startActivity(intent);
				}
				finish();
			}

		});
		
		videoView.start();
	}
	private Boolean hasBuy = false;
	private void isBuyCategory(final String categoryId,final String userId) {

		AsyncTask<String, String, String> loadDataTask = new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("userId", userId);
				param.put("categoryId", categoryId);
				param.put("officeId", "40");
				Log.i("info", "param:" + param.toString());
				String result = HttpManager.postDataToUrl(Constants.BASE_URL
						+ "isBuy/category", param);
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
						String code = object.optString("code");
						if (code != null && code.equals("1000")) {
							String isBuy = object.optString("isBuy");
							if (isBuy != null) {
								if (isBuy.equals("1")) {
									hasBuy = true;
								} else {
									hasBuy = false;
								}
								Message msg = Message.obtain();
								msg.obj = hasBuy;
								msg.what = 0;
								mHandler.sendMessage(msg);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					boolean internet = HttpManager
							.isConnectingToInternet(MainActivity.this);
					if (internet) {
						Toast.makeText(MainActivity.this, "出现异常,请重试!",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MainActivity.this, "请检查网络是否可用!",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
		loadDataTask.execute("");
	}

	
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Boolean hasBuy = (Boolean) msg.obj;
				if (hasBuy) {
					Intent intent = new Intent(MainActivity.this,BookshelfActivity.class);
					intent.putExtra("category","221");
					intent.putExtra("xin","xinban");
					startActivity(intent);
				} else {
					Intent intent = new Intent(MainActivity.this, HomeActivity.class);
					intent.putExtra("xinban","xinban");
					startActivity(intent);
				}
				break;
			case 2001:
				String categoryId = (String) msg.obj;
				//String categoryId = "221"; 
				Log.i("info","categoryid的数值为;"+categoryId);
				if (categoryId != null && categoryId.equals("221")) {
					isBuyCategory(categoryId,userId);
				}else{
					Log.i("info","到这二了");
					Intent intent = new Intent(MainActivity.this,BookshelfActivity.class);
					intent.putExtra("category",categoryId);
					intent.putExtra("xin","xinban");
					startActivity(intent);
				}
			default:
				break;
			}
		};
	};
	
	
	
	private void getBooIdFromXinban() {
		String bookId = getIntent().getStringExtra("bookId");
		System.out.println("bookId的数值为:"+bookId);
		if (null != bookId && !"".equals(bookId)) {
			MyApplication.bookId = bookId;
		}
	}

	@Override
	protected void onResume() {
		/** * 设置为横屏 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		super.onResume();
	}

	@Override
	public void findView() {

	}

	@Override
	public void initData() {
		 
	}

	@Override
	public void addClickListener() {
		// TODO Auto-generated method stub
	}
	
	
	

}
