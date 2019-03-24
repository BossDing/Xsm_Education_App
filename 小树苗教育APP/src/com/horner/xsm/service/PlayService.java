package com.horner.xsm.service;

import java.io.IOException;

import com.horner.nread.framework.MyApplication;
import com.horner.xsm.constants.Constants;
import com.horner.xsm.service.Config.ConfigService;
import com.horner.xsm.utils.AESUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.util.Log;

/**
 * @author  作者 : sun
 * @date 创建时间：2016-1-20 上午10:54:30    
 * @return 
 * @description 
 */

public class PlayService extends Service {

	private MediaPlayer mPlayer;
	SeekReceiver seekReceiver;
	private String filePath;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	
	@Override
	public void onCreate() {
		super.onCreate();
		// 创建mp3播放的对象
		mPlayer = new MediaPlayer();

		mPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				Log.i("info", "播放结束。。。。");
				 //然后在加密,一方通过别的软件打开可以访问
				AESUtils.encryptVedios(filePath);
				Constants.isEncryptVedios = true;
				Log.i("info","88888servive"+Constants.isEncryptVedios);
				Intent intent = new Intent(ConfigService.TIMER_CANCEL);
				sendBroadcast(intent);
			}
		});
		//对seekBar发送过来的广播
		seekReceiver=new SeekReceiver();
		registerReceiver(seekReceiver, new IntentFilter(ConfigService.ACTION_PLAY_SEEK));
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent.getBooleanExtra("bool",true)){
			mPlayer.reset();//重置播放器,进入Idle状态
			try {
				filePath = intent.getStringExtra("path");
				Log.i("info","音频的路径为"+intent.getStringExtra("path"));
				mPlayer.setDataSource(intent.getStringExtra("path"));
				//mPlayer.setLooping(true);
				mPlayer.prepare();
				mPlayer.start();
				new ProgressThread().start();
				//new ProgressThread().start();
			}  catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			if(mPlayer.isPlaying()){
				mPlayer.pause();
			}else {
				//播放音频文件
				mPlayer.start();  
				new ProgressThread().start();
			}
			
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	/**
	 * @author  作者 : sun
	 * @date 创建时间：2016-1-20 上午10:54:30    
	 * @return 
	 * @description  获取当前MediaPlayer播放的进度，同时发送进度广播
	 */
	class ProgressThread extends Thread {
		@Override
		public void run() {
			try {

				while (mPlayer != null && mPlayer.isPlaying()) {

					int curPosition = mPlayer.getCurrentPosition();// 获取当前播放的位置
					int maxLen = mPlayer.getDuration(); // 播放的总时长

					// 通过广播将当前播放的进度和总时长发送给Activity
					Intent intent = new Intent(ConfigService.ACTION_PLAY_PRG);
					intent.putExtra(ConfigService.EXTRA_MAX, maxLen);
					intent.putExtra(ConfigService.EXTRA_CURRENT, curPosition);

					sendBroadcast(intent);   //发送广播
					sleep(1000);
					
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(seekReceiver);
		mPlayer.stop();
		mPlayer.release(); //资源的回收
	}
	
	/**
	 * 拖动SeekBar的时候音频的位置也跟着改变
	 * @author sun
	 */
	class SeekReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO 接收Activity发送的定位播放位置的广播
			int seekPosition=intent.getIntExtra(ConfigService.EXTRA_CURRENT,0);
			try{
				if(mPlayer!=null){
					mPlayer.seekTo(seekPosition); //指定播放的位置
				}
			}catch(Exception e){
				
			}
		}
	}
}
