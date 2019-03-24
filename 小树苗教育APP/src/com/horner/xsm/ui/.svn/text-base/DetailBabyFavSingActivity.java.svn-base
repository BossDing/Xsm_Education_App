package com.horner.xsm.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.horner.nread.framework.MyApplication;
import com.horner.xsm.R;
import com.horner.xsm.base.BaseActivity;
import com.horner.xsm.constants.Constants;
import com.horner.xsm.service.PlayService;
import com.horner.xsm.service.Config.ConfigService;
import com.horner.xsm.utils.AESUtils;
import com.horner.xsm.utils.ToastUtil;

/**
 * @author 作者 : sun
 * @date 创建时间：2016-1-19 下午6:19:35
 * @return
 * @description
 */

public class DetailBabyFavSingActivity extends BaseActivity implements
		OnClickListener {

	private String path, bookName;
	private String firstName;
	private Intent playIntent;
	private ImageButton playBtn;
	private RelativeLayout leftBack;
	ImageView controlTime;
	RelativeLayout controlTimeLayout;
	private ProgressReceiver prgReceiver;
	private TextView babyName;
	private RelativeLayout close, fives, tens, fiftys, ends;
	private SeekBar mSeekBar;
	private TextView timeShow, curTime;
	private ImageView btn_close, btn_five, btn_ten, btn_fifty, btn_end;

	@Override
	public void findView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_detaibabyfavsing);
		popupView = getLayoutInflater().inflate(R.layout.detailbabysing, null);
		leftBack = (RelativeLayout) findViewById(R.id.baby_back);
		playBtn = (ImageButton) findViewById(R.id.baby_play);
		babyName = (TextView) findViewById(R.id.baby_name);
		mSeekBar = (SeekBar) findViewById(R.id.seekbar);
		timeShow = (TextView) findViewById(R.id.maxlength);
		curTime = (TextView) findViewById(R.id.curtime);
		controlTimeLayout = (RelativeLayout) findViewById(R.id.baby_controltime_layout);
		controlTime = (ImageView) findViewById(R.id.baby_controltime);
		
		close = (RelativeLayout) popupView.findViewById(R.id.closewindow);
		fives = (RelativeLayout) popupView.findViewById(R.id.five);
		tens = (RelativeLayout) popupView.findViewById(R.id.ten);
		fiftys = (RelativeLayout) popupView.findViewById(R.id.fifty);
		ends = (RelativeLayout) popupView.findViewById(R.id.end);
		
		btn_close = (ImageView) popupView.findViewById(R.id.right_btn_close);
		btn_five = (ImageView) popupView.findViewById(R.id.right_btn_five);
		btn_ten = (ImageView) popupView.findViewById(R.id.right_btn_ten);
		btn_fifty = (ImageView) popupView.findViewById(R.id.right_btn_fifty);
		btn_end = (ImageView) popupView.findViewById(R.id.right_btn_end);
		
		layoutBgTop = (LinearLayout) findViewById(R.id.bgtop);
		if (screenType == $1920x1080) {
			layoutBgTop.setBackgroundResource(R.drawable.baby_bgtop);
		} else {
			layoutBgTop.setBackgroundResource(R.drawable.baby_bgtop1);
		}

	}

	@Override
	public void initData() {
		MyApplication.addActivity(this);
		path = getIntent().getStringExtra("path");
		bookName = getIntent().getStringExtra("name");
		Log.d("info", "path的路径为:" + path + "书本的名字:" + bookName);
		TraversalFile();
		if (bookName != null) {
			babyName.setText(bookName);
		}
		registerBroadcast();
		seekBarEvent();
		showPopUpWindow();
	}

	private void hiddenPopRightIcon() {
		btn_close.setVisibility(View.GONE);
		btn_five.setVisibility(View.GONE);
		btn_ten.setVisibility(View.GONE);
		btn_fifty.setVisibility(View.GONE);
		btn_end.setVisibility(View.GONE);
	}

	// 对广播接收器要接收的广播进行注册
	private void registerBroadcast() {
		prgReceiver = new ProgressReceiver();
		registerReceiver(prgReceiver, new IntentFilter(
				ConfigService.ACTION_PLAY_PRG));

		cancelReceiver = new TimeCancelReceiver();
		registerReceiver(cancelReceiver, new IntentFilter(
				ConfigService.TIMER_CANCEL));
	}

	/**
	 * @author 作者 : sun
	 * @date 创建时间：2016-1-20 下午3:07:05
	 * @return
	 * @description 对 seekBar的做监听，然后发送广播更新音频文件
	 */
	private void seekBarEvent() {
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO 停止拖动时发送广播通知MediaPlayer对音频进行处理
				int seekPosition = seekBar.getProgress();
				Intent intent = new Intent(ConfigService.ACTION_PLAY_SEEK);
				intent.putExtra(ConfigService.EXTRA_CURRENT, seekPosition);
				sendBroadcast(intent);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// int seekPosition= progress;//seekBar.getProgress();
				// Intent intent=new Intent(ConfigService.ACTION_PLAY_SEEK);
				// intent.putExtra(ConfigService.EXTRA_CURRENT, seekPosition);
				// sendBroadcast(intent);
			}
		});
	}

	// 遍历文件
	Boolean isFirst = true;
	String filePath = null;

	private void TraversalFile() {

		File files = new File(path);

		long maxSize = 0;
		for (File file : files.listFiles()) {
			if (file.exists()) {
				String name = file.getName();
				Log.i("info", "named的妹子；" + name);

				if (name.endsWith(".mp3")) {
					if (file.length() > maxSize) {
						Log.i("info", file.getName() + "===" + file.length());
						maxSize = file.length();
						maxName = file.getName();
						Log.i("info", "maxNmae的价格为:" + maxName);
					} else {

					}
					filePath = path + File.separator + maxName;
					Log.i("info", "filePath的路径为;" + filePath + firstName);

					new Thread() {
						public void run() {
							if (Constants.isEncryptVedios) {
								AESUtils.decryptVedios(filePath);
								Constants.isEncryptVedios = false;
							}
							Message msg = Message.obtain();
							msg.what = 0;
							msg.obj = filePath;
							mHandlers.sendMessage(msg);
						}
					}.start();

				}
			}
		}
		// playIntent = new Intent(getApplicationContext(), PlayService.class);
		// playIntent.putExtra("path", filePath);
		// playIntent.putExtra("bool", true);
		// startService(playIntent);
		// Log.i("info", "jdsfkjsdkf");

	}

	Handler mHandlers = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				playIntent = new Intent(getApplicationContext(),
						PlayService.class);
				playIntent.putExtra("path", (String) msg.obj);
				playIntent.putExtra("bool", true);
				startService(playIntent);
				Log.i("info", "jdsfkjsdkf");
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void addClickListener() {
		leftBack.setOnClickListener(this);
		playBtn.setOnClickListener(this);
		// controlTime.setOnClickListener(this);
		controlTimeLayout.setOnClickListener(this);
		close.setOnClickListener(this);
		fives.setOnClickListener(this);
		tens.setOnClickListener(this);
		fiftys.setOnClickListener(this);
		ends.setOnClickListener(this);
	}

	Boolean isPlay = true;
	private static final int FIVE = 300;
	private static final int TEN = 600;
	private static final int FIFTY = 900;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.baby_back:
			finish();
			break;
		case R.id.baby_play:
			playIntent = new Intent(getApplicationContext(), PlayService.class);
			playIntent.putExtra("bool", false);
			startService(playIntent);
			if (isPlay) {
				playBtn.setBackgroundResource(R.drawable.baby_pause);
				isPlay = false;
			} else {
				playBtn.setBackgroundResource(R.drawable.baby_play);
				isPlay = true;
			}
			break;
		case R.id.baby_controltime_layout:
			// hiddenPopRightIcon();
			showControlView(v);
			break;
		case R.id.five:
			countDown5(FIVE);
			btn_five.setVisibility(View.VISIBLE);

			btn_close.setVisibility(View.GONE);
			btn_end.setVisibility(View.GONE);
			btn_ten.setVisibility(View.GONE);
			btn_fifty.setVisibility(View.GONE);
			controlTime.setBackgroundResource(R.drawable.baby_controltime);
			break;
		case R.id.ten:
			countDown10(TEN);
			btn_ten.setVisibility(View.VISIBLE);

			btn_close.setVisibility(View.GONE);
			btn_end.setVisibility(View.GONE);
			btn_five.setVisibility(View.GONE);
			btn_fifty.setVisibility(View.GONE);
			controlTime.setBackgroundResource(R.drawable.baby_controltime);
			break;
		case R.id.fifty:
			countDown15(FIFTY);
			btn_fifty.setVisibility(View.VISIBLE);

			btn_close.setVisibility(View.GONE);
			btn_end.setVisibility(View.GONE);
			btn_ten.setVisibility(View.GONE);
			btn_five.setVisibility(View.GONE);
			controlTime.setBackgroundResource(R.drawable.baby_controltime);
			break;
		case R.id.end:
			btn_end.setVisibility(View.VISIBLE);

			btn_close.setVisibility(View.GONE);
			btn_five.setVisibility(View.GONE);
			btn_ten.setVisibility(View.GONE);
			btn_fifty.setVisibility(View.GONE);
			controlTime.setBackgroundResource(R.drawable.baby_controltime);
			break;
		case R.id.closewindow:
			btn_close.setVisibility(View.VISIBLE);

			btn_five.setVisibility(View.GONE);
			btn_end.setVisibility(View.GONE);
			btn_ten.setVisibility(View.GONE);
			btn_fifty.setVisibility(View.GONE);

			controlTime.setBackgroundResource(R.drawable.baby_controltimewhite);

			break;
		default:
			break;
		}
	}

	private void popDismiss() {
		mPopupWindow.dismiss();
		controlTime.setBackgroundResource(R.drawable.baby_controltimewhite);
	}

	/**
	 * @author 作者 : sun
	 * @date 创建时间：2016-1-21 上午10:05:35
	 * @return
	 * @description 倒计时关闭音频
	 */
	Timer timer5;
	int countSec5;

	private void countDown5(final int seconds) {
		if (timer10 != null) {
			timer10.cancel();
		}
		if (timer15 != null) {
			timer15.cancel();
		}
		timer5 = new Timer();
		countSec5 = seconds;
		Log.i("info", "countSec5的数值为===:" + countSec5);
		timer5.schedule(new TimerTask() {
			@Override
			public void run() {
				if (--countSec5 < 0) {
					timer5.cancel();
					timer5 = null;
					mHandler.sendEmptyMessage(0);
				}
			}
		}, 0, 1000);
	}

	Timer timer10;
	int countSec10;

	private void countDown10(final int seconds) {

		if (timer5 != null) {
			timer5.cancel();
		}
		if (timer15 != null) {
			timer15.cancel();
		}

		timer10 = new Timer();
		countSec10 = seconds;
		timer10.schedule(new TimerTask() {
			@Override
			public void run() {
				if (--countSec10 < 0) {
					timer10.cancel();
					timer10 = null;
					mHandler.sendEmptyMessage(0);
				}

			}
		}, 0, 1000);
	}

	Timer timer15;
	int countSec15;

	private void countDown15(final int seconds) {

		if (timer10 != null) {
			timer10.cancel();
		}
		if (timer5 != null) {
			timer5.cancel();
		}
		timer15 = new Timer();
		countSec15 = seconds;
		timer15.schedule(new TimerTask() {
			@Override
			public void run() {
				if (--countSec15 < 0) {
					timer15.cancel();
					timer15 = null;
					mHandler.sendEmptyMessage(0);
				}
				Log.i("info", "当前的计数为:" + countSec15);

			}
		}, 0, 1000);
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				playIntent = new Intent(getApplicationContext(),
						PlayService.class);
				playIntent.putExtra("bool", false);
				startService(playIntent);
				playBtn.setBackgroundResource(R.drawable.baby_pause);
				isPlay = false;
				break;

			default:
				break;
			}
		}
	};
	// 显示时间的问题
	Boolean isOpen = false;
	Boolean isFirsts = true;

	private void showControlView(View view) {
		if (!isOpen) {
			// controlTime.setBackgroundResource(R.drawable.baby_controltime);
			if (!mPopupWindow.isShowing()) {
				// mPopupWindow.showAsDropDown(view, -100, -100);
				mPopupWindow.showAtLocation(view, Gravity.CENTER, 300, 100);
				if (isFirsts) {
					btn_fifty.setVisibility(View.GONE);

					btn_close.setVisibility(View.VISIBLE);
					btn_end.setVisibility(View.GONE);
					btn_ten.setVisibility(View.GONE);
					btn_five.setVisibility(View.GONE);
					isFirsts = false;
				}

			} else {
				mPopupWindow.dismiss();
			}

			isOpen = true;
		} else {
			mPopupWindow.dismiss();
			isOpen = false;
		}
	}

	private PopupWindow mPopupWindow;
	private View popupView;
	private String maxName;
	private LinearLayout layoutBgTop;
	private TimeCancelReceiver cancelReceiver;

	private void showPopUpWindow() {

		mPopupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, false);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);

		// mPopupWindow.setTouchable(true);
		// mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),
				(Bitmap) null));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer5 != null) {
			timer5.cancel();
		}
		if (timer10 != null) {
			timer10.cancel();
		}
		if (timer15 != null) {
			timer15.cancel();
		}
		unregisterReceiver(prgReceiver);
		unregisterReceiver(cancelReceiver);
		playIntent = new Intent(getApplicationContext(), PlayService.class);
		stopService(playIntent);
	}

	/**
	 * 自定义广播接收器，用于接收播放服务组中发送过来的当前播放进度数据
	 * @author sun
	 */
	class ProgressReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// 获取广播中包含的数据
			int maxLen = intent.getIntExtra(ConfigService.EXTRA_MAX, 0);
			int curPosition = intent
					.getIntExtra(ConfigService.EXTRA_CURRENT, 0);

			mSeekBar.setMax(maxLen);
			mSeekBar.setProgress(curPosition);

			// 总时长与当前时长的信息，格式：00:00/00:00
			StringBuilder timeInfoCur = new StringBuilder();
			StringBuilder timeInfoMax = new StringBuilder();

			int m = curPosition / 1000 / 60; // 当前时长的分钟
			int s = curPosition / 1000 % 60; // 当前时长的秒

			int _m = maxLen / 1000 / 60; // 总时长的分钟
			int _s = maxLen / 1000 % 60; // 总时长的秒

			timeInfoCur.append(m / 10).append(m % 10).append(":")
					.append(s / 10).append(s % 10);

			timeInfoMax.append(_m / 10).append(_m % 10).append(":")
					.append(_s / 10).append(_s % 10);

			timeShow.setText(timeInfoMax);
			curTime.setText(timeInfoCur);
		}
	}

	class TimeCancelReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			 finish();
		}

	}

}
