package com.horner.xsm.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.util.Base64;

import com.horner.nread.framework.MyApplication;
import com.horner.xsm.bean.Book;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.Toast;

public class ExitUtil {
	private static boolean isExit;
	private static SharedPreferences spLocal;
	private static Context mContext;

	public static void Exit(Context context) {
		mContext = context;
		if (!isExit) {
			isExit = true;
			Toast.makeText(context, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			android.os.Process.killProcess(android.os.Process.myPid());
			for(int m = 217; m < 222; m++) {
				List<Book> datas1Tmp = readListToLocal(m + "1");
				if (datas1Tmp != null) {
					for (Book bk1 : datas1Tmp) {
						if(bk1.mState == 1 || bk1.mState == 4)
							bk1.mState = 3;
					}
				}
				
				List<Book> datas2Tmp = readListToLocal(m + "2");
				if (datas2Tmp != null) {
					for (Book bk2 : datas2Tmp) {
						if(bk2.mState == 1 || bk2.mState == 4)
							bk2.mState = 3;
					}
				}
				
				saveListToLocal(m + "1", datas1Tmp);
				saveListToLocal(m + "2", datas2Tmp);
			}
			
			for(int m = 1; m < 6; m++) {
				List<Book> datasTmp = readListToLocal(m + "");
				if (datasTmp != null) {
					for (Book bk : datasTmp) {
						if(bk.mState == 1 || bk.mState == 4)
							bk.mState = 3;
					}
				}
				
				saveListToLocal(m + "", datasTmp);
			}
			System.exit(0);
		}
	}

	private static Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				isExit = false;
				break;

			default:
				break;
				
				
			}
		};
	};
	
	protected static void saveListToLocal(String indexStr, List list) {
		spLocal = mContext.getSharedPreferences(indexStr, Activity.MODE_WORLD_READABLE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream(3000);
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String newWord = new String(Base64.encodeBase64(baos.toByteArray()));
		SharedPreferences.Editor editor = spLocal.edit();
		editor.putString(indexStr, newWord);
		editor.commit();
	}

	public static List readListToLocal(String indexStr) {
		spLocal = mContext.getSharedPreferences(indexStr, Activity.MODE_WORLD_READABLE);
		String wordBase64 = spLocal.getString(indexStr, "");
		if (wordBase64 != null && !wordBase64.equals("")) {
			byte[] base64Bytes = Base64.decodeBase64(wordBase64.getBytes());
			ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
			ObjectInputStream ois;
			try {
				ois = new ObjectInputStream(bais);
				List<Object> result = (List<Object>) ois.readObject();
				System.out.println("result = " + result);
				return result;
			} catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return new ArrayList();
		}
		return new ArrayList();
	}
}
