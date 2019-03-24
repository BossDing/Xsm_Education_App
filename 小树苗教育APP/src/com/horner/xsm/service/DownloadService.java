package com.horner.xsm.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.horner.nread.framework.MyApplication;
import com.horner.xsm.bean.Book;
import com.horner.xsm.constants.Constants;

public class DownloadService extends Service {
	private List<String> downIds = new ArrayList<String>();

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("czb", "onCreate()");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("czb", "onDestroy()");
	}

	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("czb", "onStartCommand()");
		if(intent != null) {
			int command = intent.getIntExtra("command", 0);
			if (command == 1) {
				Book book = (Book) intent.getSerializableExtra("serviceDownBook");
				for (int j = 0; j < MyApplication.downList.size(); j++) {
					if(MyApplication.downList.get(j).equals(book) && MyApplication.downList.get(j).isServiceDown) {
						new DownThread(book).start();
					}
				}
				
			} else if (command == 2) {
			} else if (command == 3) {
				stopSelf();
			}
		}
		
		return super.onStartCommand(intent, flags, startId);

	}
	
	class DownThread extends Thread {
		private Book mBook;
		public String localbookbasepath = Environment
				.getExternalStorageDirectory().getAbsolutePath() + "/xsm/book/";
		public DownThread(Book book) {
			mBook = book;
		}
		
		public void run() {
			downLoadResourceWithProgress();
		}
		
		public boolean downLoadResourceWithProgress() {
			String urlPath = Constants.DOWNLOAD_URL
					+ mBook.mBookurl;
			String localPath = localbookbasepath + mBook.mBookID + "/book.zip";
			HttpURLConnection.setFollowRedirects(true);
			URL url;
			File output;
			RandomAccessFile out = null;
			InputStream in = null;
			HttpURLConnection connection = null;
			try {
				url = new URL(urlPath);

				connection = (HttpURLConnection) url.openConnection();
				connection.setReadTimeout(10000);
				connection.setRequestMethod("GET");
				// 有些网站不允许没有浏览器信息的请求，故添加浏览器信息
				connection.setRequestProperty("User-Agent",
						"Mozilla/4.0(compatible;MSIE7.0;windows NT 5)");
				// // 有些网站可能返回的是一个xml文件，这时候需要指定内容类型
				connection.setRequestProperty("Content-Type", "text/html");

				output = new File(localPath).getAbsoluteFile();
				if (!output.exists()) {
					File parent = output.getParentFile();
					if (parent != null) {
						parent.mkdirs();
					}
					output.createNewFile();

					Log.i("czb", "下载数据，创建指定文件" + localPath);
				}
				out = new RandomAccessFile(output, "rw");

				connection.setRequestProperty("range",
						"bytes=" + mBook.mDownloadCount + "-");
				out.seek(mBook.mDownloadCount);
				Log.e("czb", "start mDownloadCount " + mBook.mDownloadCount);

				Log.e("czb", connection.getResponseCode() + "  fffffffff");
				// 如果网络地址上存在这个文件，直接下载，如果不存在，返回false，下载失败
				if (connection.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {
					in = connection.getInputStream();
					int contentLength = connection.getContentLength();
					Log.i("czb", "申请下载数据资源大小是2   " + contentLength);
					if (contentLength == 0) {
						return false;
					}


					byte[] data = new byte[1024];
					int read = 0;
					while ((read = in.read(data)) != -1) {
						out.write(data, 0, read);
						for (int j = 0; j < MyApplication.downList.size(); j++) {
							if(MyApplication.downList.get(j).equals(mBook)) {
								MyApplication.downList.get(j).mDownloadCount += read;
								if(!MyApplication.downList.get(j).isServiceDown) {
									return false;
								}
							}
						}
					}
					// out.flush();
					connection.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (out != null)
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				if (in != null)
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				if (connection != null)
					connection.disconnect();
				Log.i("czb", "下载结束2。");

			}
			return false;
		}
	}
}
