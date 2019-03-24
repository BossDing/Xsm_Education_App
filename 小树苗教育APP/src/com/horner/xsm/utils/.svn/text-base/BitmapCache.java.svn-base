package com.horner.xsm.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapCache implements ImageCache {
	private LruCache<String, Bitmap> lruCache = null;
	private static BitmapCache bitmapCache = null;
	private Map<String, SoftReference<Bitmap>> softMap = new HashMap<String, SoftReference<Bitmap>>();

	private BitmapCache() {
		int momorySize = (int) (Runtime.getRuntime().maxMemory() / 8);
		lruCache = new LruCache<String, Bitmap>(momorySize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}

			//若放到强引用的Bitmap的大小大于剩余空间,
			//则拿出来之前的旧的Bitmap的并将它放在软引用里边 ,从而新的才能放进去
			@Override
			protected void entryRemoved(boolean evicted, String key,
					Bitmap oldValue, Bitmap newValue) {
				super.entryRemoved(evicted, key, oldValue, newValue);
				if (evicted) {
					SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(
							oldValue);
					softMap.put(key, softReference);
				}
			}
		};
	}

	
	//Bitmap的单例的实现
	public static BitmapCache getInstance() {
		if (bitmapCache == null) {
			bitmapCache =  new BitmapCache();
		}
		return bitmapCache;
	}

	
	//若强引用中有直接拿出来然后返回,否则从软引用中拿,若有,加到强引用,并从软引用中移除,然后返回
	@Override
	public Bitmap getBitmap(String url) {
		Bitmap bm = null;
		bm = lruCache.get(url);
		if (bm != null) {
			return bm;
		} else {
			SoftReference<Bitmap> mySoftReference = softMap.get(url);
			if (mySoftReference != null) {
				bm = mySoftReference.get();
				if (bm != null) {
					Bitmap myBitmap = lruCache.put(url, bm);
					if (myBitmap != null) {
						softMap.remove(url);
					}
					return bm;
				}
			}
		}
		return null;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		lruCache.put(url, bitmap);
	}

}
