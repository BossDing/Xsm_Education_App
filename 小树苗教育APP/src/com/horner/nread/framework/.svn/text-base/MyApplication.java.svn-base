package com.horner.nread.framework;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.horner.xsm.bean.Book;
import com.horner.xsm.utils.BitmapCache;
import com.horner.xsm.utils.ToastUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.socialize.PlatformConfig;

/**
 * @author 作者 : sun
 * @date 创建时间：2015-12-29 下午4:01:26
 * @version 1.0
 * @parameter
 * @return
 */

public class MyApplication extends Application {

	/**
	 * 检查是否存在支付宝终端
	 */
	public static Boolean isExistAccount = true;

	/**
	 * xml解析的内容
	 */
	public static String content = "";

	/**
	 * 检查是否购买vip
	 */
	public static Boolean hasBuy = false;
	/**
	 * 检查是否购买vip
	 */
	public static Boolean hasBuyMag = false;
	/**
	 * 微信的AppID
	 */
	public static final String WEIXIN_APPID = "wx1ea001d9326f8dbb";
	/**
	 * 微信的AppSecret
	 */
	public static final String WEIXIN_APPSECRET = "4e4cac45234704f4b5dc30744e0ec36d";

	/**
	 * 微博的AppID
	 */
	public static final String WEIBO_APPID = "1351050148";
	/**
	 * 微博的AppSecret
	 */
	public static final String WEIBO_APPSECRET = "3fbe6760a353295bc71c24a62d0e5669";

	// public DisplayImageOptions options;
	// public DisplayImageOptions rouned_options;

	// public static RequestQueue requestQueue = null;;

	// public static ImageLoader imageLoader;
	/**
	 * 微博的AppID
	 */
	public static String bookId = "";

	public static List<Book> downList = new CopyOnWriteArrayList<Book>();
	public static Queue<Book> downQueue = new LinkedList<Book>();

	private static MyApplication instance;

	// 获取屏幕的高度和宽度
	public static int width;
	public static int height;
	public static double diagonal;

	@Override
	public void onCreate() {
		super.onCreate();
		configThird();
		// initOptions();
		initImageLoader(getApplicationContext());
		Point point = new Point();
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager mManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		mManager.getDefaultDisplay().getMetrics(metrics);
		mManager.getDefaultDisplay().getRealSize(point);  
		width = metrics.widthPixels;
		height = metrics.heightPixels;
		int densityDpi = metrics.densityDpi;
		double x = Math.pow(point.x / metrics.xdpi, 2);
		double y = Math.pow(point.y / metrics.ydpi, 2);
		diagonal = Math.sqrt(x + y);
		//ToastUtil.showLongToast(getApplicationContext(), "屏幕的宽度：" + width
				//+ "屏幕的高度:" + height + "屏幕的尺寸：" + diagonal + "dpi为："
				//+ densityDpi);
		Log.i("swt", diagonal + "----" + Math.sqrt(x + y));
	}

	public MyApplication() {

	}

	private static List<Activity> list = new ArrayList<Activity>();

	public static void addActivity(Activity activity) {
		list.add(activity);
	}

	public static void clearActivities() {
		for (Activity activity : list) {
			activity.finish();
		}

		list.clear();
	}

	public static MyApplication getInstance() {
		if (instance == null) {
			instance = new MyApplication();
		}

		return instance;
	}

	public void addDownList(Book book) {
		if (!downList.contains(book) && !downQueue.contains(book)) {
			if (downList.size() < 2) {
				downList.add(book);
			} else {
				boolean inList = false;
				for (Book bk2 : downList) {
					if (bk2.mBookID.equals(book.mBookID)) {
						inList = true;
					}
				}

				if (!inList) {
					book.mState = 4;
					downQueue.add(book);
				}

			}
		}

		Log.e("czb", "addDownList downList");
		for (Book book2 : downList) {
			Log.e("czb", book2.mName);
		}

		Log.e("czb", "addDownList downQueue");
		for (Book book2 : downQueue) {
			Log.e("czb", book2.mName);
		}
	}

	public Book overDownList(Book book) {
		Book newDownBook = null;
		for (Book b : downList) {
			if (b.mBookID.equals(book.mBookID)) {
				downList.remove(book);
				if (downQueue.size() > 0) {
					newDownBook = downQueue.poll();
					downList.add(newDownBook);
				}
			}
		}

		Log.e("czb", "overDownList downList");
		for (Book book2 : downList) {
			Log.e("czb", book2.mName);
		}

		Log.e("czb", "overDownList downQueue");
		for (Book book2 : downQueue) {
			Log.e("czb", book2.mName);
		}

		return newDownBook;
	}

	public Book overDownListByBookId(String bookId) {
		Book newDownBook = null;
		for (Book b : downList) {
			if (b.mBookID.equals(bookId)) {
				downList.remove(b);
				if (downQueue.size() > 0) {
					newDownBook = downQueue.poll();
					downList.add(newDownBook);
				}

				break;
			}
		}

		Log.e("czb", "overDownListByBookId downList");
		for (Book book2 : downList) {
			Log.e("czb", book2.mName);
		}

		Log.e("czb", "overDownListByBookId downQueue");
		for (Book book2 : downQueue) {
			Log.e("czb", book2.mName);
		}

		return newDownBook;
	}

	/**
	 * @author 作者 : sun
	 * @date 创建时间：2015-12-29 下午4:08:29
	 * @return
	 * @description 微博和微信的一些初始配置
	 */
	private void configThird() {
		// 微信 appid appsecret
		PlatformConfig.setWeixin(WEIXIN_APPID, WEIXIN_APPSECRET);

		// 新浪微博 appkey appsecret
		PlatformConfig.setSinaWeibo(WEIBO_APPID, WEIBO_APPSECRET);
	}

	public static void initImageLoader(Context context) {
		// ImageLoaderConfiguration.createDefault(context);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();

		File cacheDir = context.getExternalCacheDir();
		ImageLoaderConfiguration config2 = new ImageLoaderConfiguration.Builder(
				context)
				.memoryCacheExtraOptions(480, 800)
				// max width, max height，即保存的每个缓存文件的最大长宽
				// .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75,
				// null)
				// 设置缓存的详细信息，最好不要设置这个
				.threadPoolSize(5)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(4 * 1024 * 1024))
				// 你可以通过自己的内存缓存实现
				.memoryCacheSize(4 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(100)
				// 缓存的文件数量
				.discCache(new UnlimitedDiscCache(cacheDir))
				// 自定义缓存路径
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
				.writeDebugLogs() // Remove for release app
				.build();// 开始构建
		ImageLoader.getInstance().init(config2);
	}

}
