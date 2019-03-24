package com.horner.xsm.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.net.util.Base64;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public abstract class BaseActivity extends Activity {
	public final static int $1920x1080 = 0;
	public final static int $2048x1536 = 1;
	public static HashMap<String, String> category = new HashMap<String, String>();
	public static int screenType = 0;
	private SharedPreferences spLocal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		findView();
		initData();
		addClickListener();
		Log.e("czb", "screenType " + (float)getScreenHeight() + " " + (float)getScreenWidth());
		
		WindowManager w = getWindowManager();
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);
		// since SDK_INT = 1;
		float widthPixels = metrics.widthPixels;
		float heightPixels = metrics.heightPixels;
		if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
			try {
				widthPixels = (Integer) Display.class.getMethod("getRawWidth")
						.invoke(d);
				heightPixels = (Integer) Display.class
						.getMethod("getRawHeight").invoke(d);
			} catch (Exception ignored) {
			}
		if (Build.VERSION.SDK_INT >= 17)
			try {
				Point realSize = new Point();
				Display.class.getMethod("getRealSize", Point.class).invoke(d,
						realSize);
				widthPixels = realSize.x;
				heightPixels = realSize.y;
			} catch (Exception ignored) {
			}
		screenType = (heightPixels/widthPixels < 0.67) ? $1920x1080 : $2048x1536;
		Log.i("base","screenType的数值为:"+"高度为:"+heightPixels+"宽度为:"+widthPixels+"高宽比为:"+screenType);
		// ImageLoader imageLoader = ImageLoader.getInstance();
		// imageLoader.init(ImageLoaderConfiguration.createDefault(this));
	}

	public abstract void findView();

	public abstract void initData();

	public abstract void addClickListener();

	public int getScreenHeight() {
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenHeight = display.getHeight();
		return screenHeight;
	}
	
	public int getScreenWidth() {
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		return screenWidth;
	}

	protected void saveListToLocal(String indexStr, List list) {
		spLocal = getSharedPreferences(indexStr, Activity.MODE_WORLD_READABLE);
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

	public List readListToLocal(String indexStr) {
		spLocal = getSharedPreferences(indexStr, Activity.MODE_WORLD_READABLE);
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
