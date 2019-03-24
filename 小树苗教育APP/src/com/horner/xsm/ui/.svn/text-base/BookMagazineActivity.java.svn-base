package com.horner.xsm.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.Volley;
//import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.hl.android.HLReader;
import com.hor.common.ScreenAdapter;
import com.horner.nread.framework.MyApplication;
import com.horner.xsm.R;
import com.horner.xsm.alipay.PayResult;
import com.horner.xsm.alipay.SignUtils;
import com.horner.xsm.base.BaseActivity;
import com.horner.xsm.bean.Book;
import com.horner.xsm.bean.PriceBean;
import com.horner.xsm.bean.WePay;
import com.horner.xsm.constants.Constants;
import com.horner.xsm.data.BookDataManager;
//import com.horner.xsm.data.DataBase;
import com.horner.xsm.data.UserCache;
import com.horner.xsm.service.DownloadService;
import com.horner.xsm.utils.AliPayUtils;
import com.horner.xsm.utils.CalculateUtil;
import com.horner.xsm.utils.DataCleanManager;
import com.horner.xsm.utils.LoadingDialog;
import com.horner.xsm.utils.StringUtils;
import com.horner.xsm.utils.ToastUtil;
import com.horner.xsm.utils.WePayUtils;
import com.horner.xsm.utils.WifiUtils;
import com.mouee.common.HttpManager;
import com.mouee.common.ProgressCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class BookMagazineActivity extends BaseActivity implements
		OnClickListener ,OnCheckedChangeListener{

//	private RecyclerView mRecyclerView1, mRecyclerView2, mRecyclerView3,
//			mRecyclerView4, mRecyclerView5;
//	private GalleryAdapter mAdapter1, mAdapter2, mAdapter3, mAdapter4,
//			mAdapter5;
	private List<Book> mDatas1 = new ArrayList<Book>();
	private List<Book> mDatas2 = new ArrayList<Book>();
	private List<Book> mDatas3 = new ArrayList<Book>();
	private List<Book> mDatas4 = new ArrayList<Book>();
	private List<Book> mDatas5 = new ArrayList<Book>();
	// private RelativeLayout containerId;
	
	private RecyclerView mRecyclerView1;
	private GalleryAdapter mAdapter1;

	private ImageView mBackImg;
	// private ImageView mQiKanImg;
	private static Context mContext;
	private MyApplication myapp;
	private String category;
	private int progress_data = 0;
	private String cuId = "";
	private boolean isMove = false;

	public static String localbookbasepath = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/xsm/book/";
	private UserCache userCache;
	
	private RelativeLayout wqlayout1, wqlayout2, wqlayout3, wqlayout4, wqlayout5;
	private ImageView wqp1, wqp2, wqp3, wqp4, wqp5;
	private TextView wqt1, wqt2, wqt3, wqt4, wqt5;
	
	Boolean isBaby = false;
	
	private int selectItem = 1;
	private boolean needGetData = false;
	String alipayName = "com.eg.android.AlipayGphone";
    private boolean isDownOver = false;
    
    private SparseArray <String> ZipedId = new SparseArray <String> ();
    //private boolean isZiping = false;
    private SharedPreferences spLocal;
    private  SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = BookMagazineActivity.this;
		userCache = new UserCache(mContext);
		
		isDownOver = false;
		
		spLocal = getSharedPreferences("downBookName", Activity.MODE_WORLD_WRITEABLE);
		editor = spLocal.edit();
	}

	@Override
	public void findView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		if (screenType == $1920x1080) {
//			setContentView(R.layout.activity_magazine);
//		} else {
			setContentView(R.layout.activity_magazine);
//		}

			
		wqlayout1 = (RelativeLayout) findViewById(R.id.wqlayout1);
		wqlayout1.setOnClickListener(this);
		wqlayout2 = (RelativeLayout) findViewById(R.id.wqlayout2);
		wqlayout2.setOnClickListener(this);
		wqlayout3 = (RelativeLayout) findViewById(R.id.wqlayout3);
		wqlayout3.setOnClickListener(this);
		wqlayout4 = (RelativeLayout) findViewById(R.id.wqlayout4);
		wqlayout4.setOnClickListener(this);
		wqlayout5 = (RelativeLayout) findViewById(R.id.wqlayout5);
		wqlayout5.setOnClickListener(this);
		
		wqp1 = (ImageView) findViewById(R.id.wqp1);
		wqp2 = (ImageView) findViewById(R.id.wqp2);
		wqp3 = (ImageView) findViewById(R.id.wqp3);
		wqp4 = (ImageView) findViewById(R.id.wqp4);
		wqp5 = (ImageView) findViewById(R.id.wqp5);
		CalculateUtil.calculateViewSize(wqp1, 40, 40);
		CalculateUtil.calculateViewSize(wqp2, 40, 40);
		CalculateUtil.calculateViewSize(wqp3, 40, 40);
		CalculateUtil.calculateViewSize(wqp4, 40, 40);
		CalculateUtil.calculateViewSize(wqp5, 40, 40);
		
		wqt1 = (TextView) findViewById(R.id.wqt1);
		wqt2 = (TextView) findViewById(R.id.wqt2);
		wqt3 = (TextView) findViewById(R.id.wqt3);
		wqt4 = (TextView) findViewById(R.id.wqt4);
		wqt5 = (TextView) findViewById(R.id.wqt5);
		CalculateUtil.calculateTextSize(wqt1, 30);
		CalculateUtil.calculateTextSize(wqt2, 30);
		CalculateUtil.calculateTextSize(wqt3, 30);
		CalculateUtil.calculateTextSize(wqt4, 30);
		CalculateUtil.calculateTextSize(wqt5, 30);
		
		mBackImg = (ImageView) findViewById(R.id.activity_magazine_backImg);

		mRecyclerView1 = (RecyclerView) findViewById(R.id.id_recyclerview_horizontal1);
		WrapContentLinearLayoutManager linearLayoutManager = new WrapContentLinearLayoutManager(this, 5);
		linearLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
		mRecyclerView1.setLayoutManager(linearLayoutManager);
		mAdapter1 = new GalleryAdapter(this, 1);
		mRecyclerView1.setAdapter(mAdapter1);
		mRecyclerView1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					isMove = true;
					break;
				case MotionEvent.ACTION_UP:
					isMove = false;
					break;
				}
				return false;
			}

		});
		
		changeItemBg();
	}
	 private IWXAPI api;
	@Override
	public void initData() {
		MyApplication.addActivity(this);
		if (!WifiUtils.isNetWorkActive(this, "请检查网络是否可用!")) {
			Intent it = new Intent(this, LoginActivity.class);
			startActivity(it);
			finish();
		}
		UserCache userCache = new UserCache(this);
		userId = userCache.getUserId();
		category = "221";
		DisplayMetrics metric = new DisplayMetrics(); 
		//获取窗口属性
		getWindowManager().getDefaultDisplay().getMetrics(metric); 
		width = metric.widthPixels;
		height = metric.heightPixels;
		float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5） 
		int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
		
		// if(category.equals("217")) {
		// containerId.setBackgroundResource(R.drawable.bg_qzyd1);
		// } else if(category.equals("218")) {
		// containerId.setBackgroundResource(R.drawable.bg_bbax1);
		// } else if(category.equals("219")) {
		// containerId.setBackgroundResource(R.drawable.bg_bbat1);
		// } else if(category.equals("220")) {
		// containerId.setBackgroundResource(R.drawable.bg_bbak1);
		// } if(category.equals("221")) {
		// containerId.setBackgroundResource(R.drawable.bg_yxxj1);
		// }
		myapp = (MyApplication) getApplicationContext();
		clearData();
		getCata();
		getImieStatus();
		getLocalIpAddress();
		initAlertDialog();
		//向微信注册小树苗的appId
		api = WXAPIFactory.createWXAPI(this,null);
		api.registerApp(MyApplication.WEIXIN_APPID);
	}

	@Override
	public void addClickListener() {
		mBackImg.setOnClickListener(this);
		// mQiKanImg.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.activity_magazine_backImg:
			// 返回键
			finish();
			break;
		// case R.id.activity_magazine_rightImg:
		// // 跳转书架
		// finish();
		// break;
		case R.id.wqlayout1:
			clearData();
			saveState();
			selectItem = 1;
			changeItemBg();
			getCata();
			break;
		case R.id.wqlayout2:
			clearData();
			saveState();
			selectItem = 2;
			changeItemBg();
			getCata();
			break;
		case R.id.wqlayout3:
			clearData();
			saveState();
			selectItem = 3;
			changeItemBg();
			getCata();
			break;
		case R.id.wqlayout4:
			clearData();
			saveState();
			selectItem = 4;
			changeItemBg();
			getCata();
			break;
		case R.id.wqlayout5:
			clearData();
			saveState();
			selectItem = 5;
			isBaby = true;
			changeItemBg();
			if(!WifiUtils.isNetWorkActive(BookMagazineActivity.this,"网络连接超时,请重新连接")){
				return;
			}
			isBuyCategory("221");	
			
//			UserCache cache = new UserCache(BookMagazineActivity.this);
//			 Boolean hasBuy = cache.getHasBuy();
//			 if (hasBuy) {
//				 changeItemBg();
//					getCata();
//			}else {
//				builder.show();
//			}
			break;
		default:
			break;
		}
	}
	private void clearData() {
		mDatas1.clear();
		mDatas2.clear();
		mDatas3.clear();
		mDatas4.clear();
		mDatas5.clear();
	}
	private AlertDialog.Builder builder;
	String price = null;
	private void initAlertDialog() {
		price =  getModePrice();
		builder = new AlertDialog.Builder(BookMagazineActivity.this);
		builder.setTitle("购买"+"价格为:"+price+"元人民币")
				.setMessage("此模块需要购买才能进行观看，你是否要购买！ ")
				.setPositiveButton("支付宝支付", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//price = "0.01";
						if(price != null){
							pay(price,"幼小衔接");
						}
						
					}
				}).setNeutralButton("暂不购买", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.setNegativeButton("微信支付",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
								WePayUtils.getPayInfo(wePayHandler, price,BookMagazineActivity.this,WePay.class,"幼小衔接");
								Constants.isVipOrMod = "mod";
							}
						});
	}
	
	private String getModePrice(){
		if(null != Constants.priceBeans){
			for (int i = 0; i < Constants.priceBeans.size(); i++) {
				PriceBean priceBean = Constants.priceBeans.get(i);
				if ( priceBean.getType().equals("mod")) {
					return priceBean.getPrice();
				}else {
					
				}
			}
		}else {
			return "25";
		}
		return "25";
	}
	
	/*
	 * 对幼小衔接的分类进行购买，购买之前先判断是否已经购买
	 */
	
	private Boolean hasBuy = false;
	
	private void isBuyCategory(final String categoryId){
		 
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
						//Log.i("in)
						String code = object.optString("code");
						if (code != null && code.equals("1000")) {
							String isBuy = object.optString("isBuy");
							if(isBuy != null){
								if(isBuy.equals("1")){
									hasBuy = true;
								}else {
									hasBuy = false;
								}
								Message msg = Message.obtain();
								msg.obj = hasBuy;
								msg.what = 0;
								mHandlers.sendMessage(msg);
							}
						}  
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					boolean internet = HttpManager
							.isConnectingToInternet(BookMagazineActivity.this);
					if (internet) {
						Toast.makeText(BookMagazineActivity.this, "出现异常,请重试!",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(BookMagazineActivity.this, "请检查网络是否可用!",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
		loadDataTask.execute("");
	}
	
	
	
	Handler mHandlers = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Boolean hasBuy = (Boolean) msg.obj;
				if(hasBuy){
					getCata();
				}else {
					builder.show();
				}
				break;

			default:
				break;
			}
		}
	};
 
	
	private void BuyCategory(final String categoryId){
		 
		AsyncTask<String, String, String> loadDataTask = new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("userId", userId);
				param.put("categoryId", categoryId);
				param.put("officeId", "40");
				param.put("transNum",  getOutTradeNo());
				param.put("price", price);
				Log.i("info", "param:" + param.toString());
				String result = HttpManager.postDataToUrl(Constants.BASE_URL
						+ "buy/category", param);
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
						//Log.i("in)
						String code = object.optString("code");
						if (code != null && code.equals("1000")) {
							ToastUtil.showShortToast(BookMagazineActivity.this,"分类购买成功");
							String isBuy = object.optString("isBuy");
							if(isBuy != null){
								 Log.i("info","isBuy购买成功的数值为:"+isBuy);
								 Log.i("info","isBuy购买成功的数值为:"+isBuy);
								 UserCache cache = new UserCache(BookMagazineActivity.this);
								 cache.setHasBuy(true);
							}
						}else if (code.equals("1001")) {
							ToastUtil.showShortToast(BookMagazineActivity.this,"用户ID为空");
							return;
						} else if (code.equals("1002")) {
							ToastUtil.showShortToast(BookMagazineActivity.this,"分类ID为空");
							return;
						}else if (code.equals("1003")) {
							ToastUtil.showShortToast(BookMagazineActivity.this,"交易流水号为空");
							return;
						}else if (code.equals("1004")) {
							ToastUtil.showShortToast(BookMagazineActivity.this,"联盟成员ID为空");
							return;
						}else if (code.equals("1005")) {
							ToastUtil.showShortToast(BookMagazineActivity.this,"已经购买过了");
							return;
						}else if (code.equals("1006")) {
							ToastUtil.showShortToast(BookMagazineActivity.this,"购买价格不能为空或者零");
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					boolean internet = HttpManager
							.isConnectingToInternet(BookMagazineActivity.this);
					if (internet) {
						Toast.makeText(BookMagazineActivity.this, "出现异常,请重试!",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(BookMagazineActivity.this, "请检查网络是否可用!",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
		loadDataTask.execute("");
	}
	
	private void changeItemBg() {
		wqp1.setBackgroundResource(R.drawable.wqb11);
		wqp2.setBackgroundResource(R.drawable.wqb21);
		wqp3.setBackgroundResource(R.drawable.wqb31);
		wqp4.setBackgroundResource(R.drawable.wqb41);
		wqp5.setBackgroundResource(R.drawable.wqb51);
		wqt1.setTextColor(Color.argb(0xFF, 0xFF, 0xFF, 0xFF));
		wqt2.setTextColor(Color.argb(0xFF, 0xFF, 0xFF, 0xFF));
		wqt3.setTextColor(Color.argb(0xFF, 0xFF, 0xFF, 0xFF));
		wqt4.setTextColor(Color.argb(0xFF, 0xFF, 0xFF, 0xFF));
		wqt5.setTextColor(Color.argb(0xFF, 0xFF, 0xFF, 0xFF));
		wqlayout1.setBackgroundColor(Color.argb(0x00, 0x00, 0x00, 0x00));
		wqlayout2.setBackgroundColor(Color.argb(0x00, 0x00, 0x00, 0x00));
		wqlayout3.setBackgroundColor(Color.argb(0x00, 0x00, 0x00, 0x00));
		wqlayout4.setBackgroundColor(Color.argb(0x00, 0x00, 0x00, 0x00));
		wqlayout5.setBackgroundColor(Color.argb(0x00, 0x00, 0x00, 0x00));
		switch(selectItem) {
		case 1:
			wqp1.setBackgroundResource(R.drawable.wqb1);
			wqt1.setTextColor(Color.argb(0xff, 0x22, 0x22, 0x22));
			wqlayout1.setBackgroundColor(Color.argb(0xFF, 0xFF, 0xFF, 0xFF));
			break;
		case 2:
			wqp2.setBackgroundResource(R.drawable.wqb2);
			wqt2.setTextColor(Color.argb(0xff, 0x22, 0x22, 0x22));
			wqlayout2.setBackgroundColor(Color.argb(0xFF, 0xFF, 0xFF, 0xFF));
			break;
		case 3:
			wqp3.setBackgroundResource(R.drawable.wqb3);
			wqt3.setTextColor(Color.argb(0xff, 0x22, 0x22, 0x22));
			wqlayout3.setBackgroundColor(Color.argb(0xFF, 0xFF, 0xFF, 0xFF));
			break;
		case 4:
			wqp4.setBackgroundResource(R.drawable.wqb4);
			wqt4.setTextColor(Color.argb(0xff, 0x22, 0x22, 0x22));
			wqlayout4.setBackgroundColor(Color.argb(0xFF, 0xFF, 0xFF, 0xFF));
			break;
		case 5:
			wqp5.setBackgroundResource(R.drawable.wqb5);
			wqt5.setTextColor(Color.argb(0xff, 0x22, 0x22, 0x22));
			wqlayout5.setBackgroundColor(Color.argb(0xFF, 0xFF, 0xFF, 0xFF));
			break;
		}
	}

	private void getCata() {
		if (!WifiUtils.isNetWorkActive(this, "请检查网络是否可用!")) {
			Intent it = new Intent(this, LoginActivity.class);
			startActivity(it);
			finish();
		}
		LoadingDialog.isLoading(BookMagazineActivity.this);
		AsyncTask<String, String, String> loadCataTask = new AsyncTask<String, String, String>() {
			@Override
			protected String doInBackground(String... params) {
				switch(selectItem) {
				case 1:
					ArrayList<Book> bookTopList1 = BookDataManager
					.getBookListByCatagory("217");
					mDatas1.clear();
					if(bookTopList1 != null)
						mDatas1.addAll(bookTopList1);
		
					List<Book> datas1Tmp = readListToLocal("1");
					if (datas1Tmp != null && mDatas1 != null) {
						for (int i = 0; i < mDatas1.size(); i++) {
							for (Book bk : datas1Tmp) {
								if (mDatas1.get(i).mBookID.equals(bk.mBookID)) {
									mDatas1.remove(i);
									mDatas1.add(i, bk);
								}
							}
						}
					}
					break;
				case 2:
					ArrayList<Book> bookTopList2 = BookDataManager
					.getBookListByCatagory("218");
					mDatas2.clear();
					if(bookTopList2 != null)
						mDatas2.addAll(bookTopList2);
		
					List<Book> datas2Tmp = readListToLocal("2");
					if (datas2Tmp != null && mDatas2 != null) {
						for (int i = 0; i < mDatas2.size(); i++) {
							for (Book bk : datas2Tmp) {
								if (mDatas2.get(i).mBookID.equals(bk.mBookID)) {
									mDatas2.remove(i);
									mDatas2.add(i, bk);
								}
							}
						}
					}
					break;
				case 3:
					ArrayList<Book> bookTopList3 = BookDataManager
					.getBookListByCatagory("219");
					mDatas3.clear();
					if(bookTopList3 != null)
						mDatas3.addAll(bookTopList3);
					
					for (int i = 0; i < mDatas3.size(); i++) {
						Log.i("info","mDatas的大小为:"+mDatas3.get(i).mCoverurl);
					}
					Log.i("info","mDatas的大小为:"+mDatas3);
					
					List<Book> datas3Tmp = readListToLocal("3");
					if (datas3Tmp != null && mDatas3 != null) {
						for (int i = 0; i < mDatas3.size(); i++) {
							for (Book bk : datas3Tmp) {
								if (mDatas3.get(i).mBookID.equals(bk.mBookID)) {
									mDatas3.remove(i);
									mDatas3.add(i, bk);
								}
							}
						}
					}
					break;
				case 4:
					ArrayList<Book> bookTopList4 = BookDataManager
					.getBookListByCatagory("220");
					mDatas4.clear();
					if(bookTopList4 != null)
						mDatas4.addAll(bookTopList4);
		
					List<Book> datas4Tmp = readListToLocal("4");
					if (datas4Tmp != null && mDatas4 != null) {
						for (int i = 0; i < mDatas4.size(); i++) {
							for (Book bk : datas4Tmp) {
								if (mDatas4.get(i).mBookID.equals(bk.mBookID)) {
									mDatas4.remove(i);
									mDatas4.add(i, bk);
								}
							}
						}
					}
					break;
				case 5:
					ArrayList<Book> bookTopList5 = BookDataManager
					.getBookListByCatagory("221");
					mDatas5.clear();
					if(bookTopList5 != null)
						mDatas5.addAll(bookTopList5);
		
					List<Book> datas5Tmp = readListToLocal("5");
					if (datas5Tmp != null && mDatas5 != null) {
						for (int i = 0; i < mDatas5.size(); i++) {
							for (Book bk : datas5Tmp) {
								if (mDatas5.get(i).mBookID.equals(bk.mBookID)) {
									mDatas5.remove(i);
									mDatas5.add(i, bk);
								}
							}
						}
					}
					break;
				}

				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				
				switch(selectItem) {
				case 1:
					List<Book> datas1Tmp = readListToLocal("1");
					if (datas1Tmp != null && mDatas1 != null) {
						for (int i = 0; i < mDatas1.size(); i++) {
							for (Book bk : datas1Tmp) {
								if (mDatas1.get(i).mBookID.equals(bk.mBookID)) {
									mDatas1.remove(i);
									mDatas1.add(i, bk);
								}
							}
						}
					}
					break;
				case 2:
					List<Book> datas2Tmp = readListToLocal("2");
					if (datas2Tmp != null && mDatas2 != null) {
						for (int i = 0; i < mDatas2.size(); i++) {
							for (Book bk : datas2Tmp) {
								if (mDatas2.get(i).mBookID.equals(bk.mBookID)) {
									mDatas2.remove(i);
									mDatas2.add(i, bk);
								}
							}
						}
					}
					break;
				case 3:
					List<Book> datas3Tmp = readListToLocal("3");
					if (datas3Tmp != null && mDatas3 != null) {
						for (int i = 0; i < mDatas3.size(); i++) {
							for (Book bk : datas3Tmp) {
								if (mDatas3.get(i).mBookID.equals(bk.mBookID)) {
									mDatas3.remove(i);
									mDatas3.add(i, bk);
								}
							}
						}
					}
					break;
				case 4:
					List<Book> datas4Tmp = readListToLocal("4");
					if (datas4Tmp != null && mDatas4 != null) {
						for (int i = 0; i < mDatas4.size(); i++) {
							for (Book bk : datas4Tmp) {
								if (mDatas4.get(i).mBookID.equals(bk.mBookID)) {
									mDatas4.remove(i);
									mDatas4.add(i, bk);
								}
							}
						}
					}
					break;
				case 5:
					List<Book> datas5Tmp = readListToLocal("5");
					if (datas5Tmp != null && mDatas5 != null) {
						for (int i = 0; i < mDatas5.size(); i++) {
							for (Book bk : datas5Tmp) {
								if (mDatas5.get(i).mBookID.equals(bk.mBookID)) {
									mDatas5.remove(i);
									mDatas5.add(i, bk);
								}
							}
						}
					}
					break;
				}
				
				checkDownState();
				mAdapter1.notifyDataSetChanged();
				LoadingDialog.finishLoading();
			}
		};
		loadCataTask.execute("");
	}
	
	@Override
	protected void onResume() {
		mAdapter1.notifyDataSetChanged();
		if(needGetData) {
			getCata();
			needGetData = false;
		}
		
		isDownOver = false;
		
		List<Book> datas1Tmp = readListToLocal("1");
		if (datas1Tmp != null && mDatas1 != null) {
			for (int i = 0; i < mDatas1.size(); i++) {
				for (Book bk : datas1Tmp) {
					if (mDatas1.get(i).mBookID.equals(bk.mBookID)) {
						mDatas1.remove(i);
						mDatas1.add(i, bk);
					}
				}
			}
		}
		
		List<Book> datas2Tmp = readListToLocal("2");
		if (datas2Tmp != null && mDatas2 != null) {
			for (int i = 0; i < mDatas2.size(); i++) {
				for (Book bk : datas2Tmp) {
					if (mDatas2.get(i).mBookID.equals(bk.mBookID)) {
						mDatas2.remove(i);
						mDatas2.add(i, bk);
					}
				}
				
			}
		}
		
		List<Book> datas3Tmp = readListToLocal("3");
		if (datas3Tmp != null && mDatas3 != null) {
			for (int i = 0; i < mDatas3.size(); i++) {
				for (Book bk : datas3Tmp) {
					if (mDatas3.get(i).mBookID.equals(bk.mBookID)) {
						mDatas3.remove(i);
						mDatas3.add(i, bk);
					}
				}
				
			}
		}
		
		List<Book> datas4Tmp = readListToLocal("4");
		if (datas4Tmp != null && mDatas4 != null) {
			for (int i = 0; i < mDatas4.size(); i++) {
				for (Book bk : datas4Tmp) {
					if (mDatas4.get(i).mBookID.equals(bk.mBookID)) {
						mDatas4.remove(i);
						mDatas4.add(i, bk);
					}
				}
				
			}
		}
		
		List<Book> datas5Tmp = readListToLocal("5");
		if (datas5Tmp != null && mDatas5 != null) {
			for (int i = 0; i < mDatas5.size(); i++) {
				for (Book bk : datas5Tmp) {
					if (mDatas5.get(i).mBookID.equals(bk.mBookID)) {
						mDatas5.remove(i);
						mDatas5.add(i, bk);
					}
				}
				
			}
		}
		
		checkDownState();
		mAdapter1.notifyDataSetChanged();
		super.onResume();
	}
	private DisplayImageOptions options = null;
	int size = 0;
	class GalleryAdapter extends
			RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

		private LayoutInflater mInflater;
		private int adapterId;

		public GalleryAdapter(Context context, int id) {
			adapterId = id;
			mInflater = LayoutInflater.from(context);

			// Log.i("info", "imageLoader的数值为:" + imageLoader);
			options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.moreinfo_photo)
			.showImageForEmptyUri(R.drawable.moreinfo_photo)
			.showImageOnFail(R.drawable.moreinfo_photo).cacheInMemory(true)
			.cacheOnDisc(true).considerExifParams(true)
			.displayer(new RoundedBitmapDisplayer(20)).build();
		}

		class ViewHolder extends RecyclerView.ViewHolder {
			public ViewHolder(View arg0) {
				super(arg0);
			}

			ImageView mBigImg, mFlagImg, mDownImg, mRestartImg, mPauseImg, mQueueImg;
			ProgressBar mLoadBar;
			RelativeLayout layout_item;
			FrameLayout layout_item_all;
			// TextView mTxt;
		}

		@Override
		public int getItemCount() {
			switch (selectItem) {
			case 1:
				return mDatas1.size();
			case 2:
				return mDatas2.size();
			case 3:
				return mDatas3.size();
			case 4:
				return mDatas4.size();
			case 5:
				return mDatas5.size();
			default:
				return 0;
			}

		}

		/**
		 * 创建ViewHolder
		 */
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
			View view;
//			if (screenType == $1920x1080) {
				view = mInflater.inflate(R.layout.activity_bookmagazine_item,
						viewGroup, false);
//			} else {
//				view = mInflater.inflate(R.layout.activity_bookmagazine_item,
//						viewGroup, false);
//			}
			ViewHolder viewHolder = new ViewHolder(view);

			viewHolder.layout_item_all =  (FrameLayout) view
					.findViewById(R.id.layout_item_all);
			viewHolder.layout_item = (RelativeLayout) view
					.findViewById(R.id.layout_item);
			viewHolder.mBigImg = (ImageView) view
					.findViewById(R.id.id_index_gallery_item_image);
			viewHolder.mFlagImg = (ImageView) view
					.findViewById(R.id.id_index_gallery_item_image_right);
			viewHolder.mDownImg = (ImageView) view
					.findViewById(R.id.id_index_gallery_item_image_down);
			viewHolder.mRestartImg = (ImageView) view
					.findViewById(R.id.id_restart_image);
			viewHolder.mLoadBar = new ProgressBar(BookMagazineActivity.this,
					null, android.R.attr.progressBarStyleHorizontal);
			viewHolder.mPauseImg = (ImageView) view
					.findViewById(R.id.id_pause_image);
			viewHolder.mQueueImg = (ImageView) view
					.findViewById(R.id.id_queue_image);

			if (screenType == $1920x1080) {
				size = viewGroup.getHeight() * 5 / 18;
			} else {
				size = viewGroup.getHeight() / 7;
			}
			
			LayoutParams restartParams = new LayoutParams(size, size);
			//restartParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
			restartParams.gravity = Gravity.CENTER;
			viewHolder.mBigImg.setLayoutParams(restartParams);
			viewHolder.mRestartImg.setLayoutParams(restartParams);
			viewHolder.mPauseImg.setLayoutParams(restartParams);
			viewHolder.mQueueImg.setLayoutParams(restartParams);
			viewHolder.mLoadBar = new ProgressBar(BookMagazineActivity.this, null,
					android.R.attr.progressBarStyleHorizontal);
			RelativeLayout.LayoutParams loadParams = new RelativeLayout.LayoutParams(
					size-15,
					ScreenAdapter.calcWidth(15));
			loadParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			viewHolder.mLoadBar.setLayoutParams(loadParams);
			viewHolder.mLoadBar.setVisibility(View.GONE);
			viewHolder.mLoadBar.setId(102358);
			viewHolder.mLoadBar.setIndeterminate(false);
			viewHolder.mLoadBar.setProgressDrawable(mContext.getResources()
					.getDrawable(R.drawable.progress_small));
			viewHolder.layout_item.addView(viewHolder.mLoadBar);

			return viewHolder;
		}

		/**
		 * 设置值ֵ
		 */
		Book detailBook = null;
		float price = 0;
		//public   RequestQueue requestQueue = null;;

		//public   ImageLoader imageLoader;
		
		
		ImageLoader imageLoader; 
		@Override
		public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
			
//			requestQueue = Volley.newRequestQueue(BookMagazineActivity.this);
//			imageLoader = new ImageLoader(requestQueue, BitmapCache.getInstance());
			imageLoader = ImageLoader.getInstance();
			Book book;
			switch (selectItem) {
			case 1:
				book = mDatas1.get(i);
				break;
			case 2:
				book = mDatas2.get(i);
				break;
			case 3:
				book = mDatas3.get(i);
				break;
			case 4:
				book = mDatas4.get(i);
				break;
			case 5:
				book = mDatas5.get(i);
				break;
			default:
				book = mDatas1.get(i);
			}

			if (book != null) {
				viewHolder.layout_item_all.setBackgroundColor(Color.TRANSPARENT);
				if(clickRunBehaviorItem2 == i && isRight) {
					viewHolder.layout_item_all.setBackgroundColor(Color.YELLOW);
				}
					
				price = Float.parseFloat(book.mPrice);
				if (price > 0.0) {
					//viewHolder.mFlagImg.setBackgroundResource();
					viewHolder.mFlagImg.setBackgroundResource(R.drawable.vip);
				}else {
					viewHolder.mFlagImg.setBackgroundResource(R.drawable.free);
				}

				String coverurl = book.mCoverurl;
				if (!StringUtils.isEmpty(coverurl)) {

//					ImageListener listener = ImageLoader.getImageListener(
//							viewHolder.mBigImg, R.drawable.moreinfo_photo,
//							R.drawable.moreinfo_photo);
//
//
//					imageLoader.get(Constants.DOWNURL + coverurl,
//							listener, 250, 250);
					imageLoader.displayImage(Constants.DOWNLOAD_URL + coverurl,
							viewHolder.mBigImg, options,
							new SimpleImageLoadingListener() {
								@Override
								public void onLoadingStarted(String imageUri, View view) {
									 
								}

							}, new ImageLoadingProgressListener() {
								@Override
								public void onProgressUpdate(String imageUri, View view,
										int current, int total) {
									 
								}
							});
					
					
				} else {
					viewHolder.mBigImg
							.setImageResource(R.drawable.moreinfo_photo);
				}

//				final String localPath = DataBase.getLocalPath(
//						BookMagazineActivity.this, book.mBookID);
				final String localPath = localbookbasepath + book.mBookID + "/book";
//				File file = null;
//				if(localPath != null) {
//					file = new File(localPath);
//				}

				File f1 = new File(localPath + "/book.dat");
				File f2 = new File(localPath + "/book.xml");
				File f3 = new File(localPath + "/hash.dat");
				
				if (localPath != null && f1.exists() && f2.exists() && f3.exists()) {
					viewHolder.mLoadBar.setVisibility(View.GONE);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mPauseImg.setVisibility(View.GONE);
					viewHolder.mRestartImg.setVisibility(View.GONE);
					viewHolder.mLoadBar.setVisibility(View.GONE);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mQueueImg.setVisibility(View.GONE);
				} else if (book.mState == 1) {
					viewHolder.mRestartImg.setVisibility(View.GONE);
					viewHolder.mPauseImg.setVisibility(View.VISIBLE);
					viewHolder.mLoadBar.setVisibility(View.VISIBLE);
					viewHolder.mLoadBar.setProgress(book.mDownloadProgress);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mQueueImg.setVisibility(View.GONE);
					// }
				} else if (book.mState == 3) {
					viewHolder.mRestartImg.setVisibility(View.VISIBLE);
					viewHolder.mPauseImg.setVisibility(View.GONE);
					viewHolder.mLoadBar.setVisibility(View.VISIBLE);
					viewHolder.mLoadBar.setProgress(book.mDownloadProgress);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mQueueImg.setVisibility(View.GONE);
					// }
				} else if (book.mState == 2) {
					viewHolder.mLoadBar.setVisibility(View.GONE);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mPauseImg.setVisibility(View.GONE);
					viewHolder.mRestartImg.setVisibility(View.GONE);
					viewHolder.mLoadBar.setVisibility(View.GONE);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mQueueImg.setVisibility(View.GONE);
					// }
				} else if (book.mState == 5) {
					viewHolder.mLoadBar.setVisibility(View.GONE);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mPauseImg.setVisibility(View.GONE);
					viewHolder.mRestartImg.setVisibility(View.GONE);
					viewHolder.mLoadBar.setVisibility(View.GONE);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mQueueImg.setVisibility(View.GONE);
					// }
				}  else if (book.mState == 6) {
					viewHolder.mLoadBar.setVisibility(View.GONE);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mPauseImg.setVisibility(View.GONE);
					viewHolder.mRestartImg.setVisibility(View.GONE);
					viewHolder.mLoadBar.setVisibility(View.GONE);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mQueueImg.setVisibility(View.GONE);
					// }
				}else if (book.mState == 4) {
					viewHolder.mRestartImg.setVisibility(View.GONE);
					viewHolder.mPauseImg.setVisibility(View.GONE);
					viewHolder.mLoadBar.setVisibility(View.GONE);
					//viewHolder.mLoadBar.setProgress(book.mDownloadProgress);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mQueueImg.setVisibility(View.VISIBLE);
					// }
				} else {
					viewHolder.mLoadBar.setVisibility(View.GONE);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mPauseImg.setVisibility(View.GONE);
					viewHolder.mRestartImg.setVisibility(View.GONE);
					viewHolder.mLoadBar.setVisibility(View.GONE);
					viewHolder.mDownImg.setVisibility(View.VISIBLE);
					viewHolder.mQueueImg.setVisibility(View.GONE);
				}
				final Book bookItem = book;
				// 下载图书
				viewHolder.mBigImg.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

//						String localPath = DataBase.getLocalPath(
//								BookMagazineActivity.this, bookItem.mBookID);

						if (Float.parseFloat(bookItem.mPrice) == 0.0) {
							Log.e("czb", (localPath == null) +"  "+ bookItem.mState);
							File f1 = new File(localPath + "/book.dat");
							File f2 = new File(localPath + "/book.xml");
							File f3 = new File(localPath + "/hash.dat");
							
							if (localPath != null && f1.exists() && f2.exists() && f3.exists()) {
								File f = new File(localPath);
								
									if (f.exists()) {
										if (selectItem == 3) {
											 Intent intent = new Intent(BookMagazineActivity.this,DetailBabyFavSingActivity.class);
											 intent.putExtra("path",localPath);
											 intent.putExtra("name", bookItem.mName);
											 startActivity(intent);
										} else if(selectItem == 1) {
											needGetData = true;
											HLReader.show(BookMagazineActivity.this,
													 localPath, true);
										} else {
											needGetData = true;
											HLReader.show(BookMagazineActivity.this,
													 localPath, false);
										}
										
										bookItem.mState = 2;
										return;
									} else {
//										DataBase.deleteSearchRecord(
//										BookshelfActivity.this,
//										bookItem.mBookID);
								editor.putString(bookItem.mBookID, "");
								editor.commit();
									}

							}

							Log.e("czb", "MyApplication.getInstance().getDownList().size() " + MyApplication.downList.size());
							if(MyApplication.downList.size() >= 2) {
								
								Toast.makeText(mContext, "已加入下载队列",
										Toast.LENGTH_SHORT).show();
								
								for(int i = 0; i < ZipedId.size(); i++) {
									if(ZipedId.get(i).equals(bookItem.mBookID)) {
										ZipedId.remove(i);
									}
								}
								switch (selectItem) {
								case 1:
									mDatas1.get(i).mState = 4;
									MyApplication.getInstance().addDownList(mDatas1.get(i));
									notifyItemChanged(i);
									break;
								case 2:
									mDatas2.get(i).mState = 4;
									MyApplication.getInstance().addDownList(mDatas2.get(i));
									notifyItemChanged(i);
									break;
								case 3:
									mDatas3.get(i).mState = 4;
									MyApplication.getInstance().addDownList(mDatas3.get(i));
									notifyItemChanged(i);
									break;
								case 4:
									mDatas4.get(i).mState = 4;
									MyApplication.getInstance().addDownList(mDatas4.get(i));
									notifyItemChanged(i);
									break;
								case 5:
									mDatas5.get(i).mState = 4;
									MyApplication.getInstance().addDownList(mDatas5.get(i));
									notifyItemChanged(i);
									break;
								}
								
							} else if (bookItem.mState != 1) {
								Toast.makeText(mContext, "正在下载...",
										Toast.LENGTH_SHORT).show();
								String downLoadPath = "";
								for(int i = 0; i < ZipedId.size(); i++) {
									if(ZipedId.get(i).equals(bookItem.mBookID)) {
										ZipedId.remove(i);
									}
								}
								// String isPay = DataUtils.getPreference(
								// BookMagazineActivity.this, "userType", "");
								downLoadPath = Constants.DOWNLOAD_URL
										+ bookItem.mBookurl;
								switch (selectItem) {
								case 1:
									mDatas1.get(i).mDownloadCount = 0;
									MyApplication.getInstance().addDownList(mDatas1.get(i));
									startDownBook(bookItem.mBookID,
											downLoadPath, mHandler,
											BookMagazineActivity.this, 1, i);
									break;
								case 2:
									mDatas2.get(i).mDownloadCount = 0;
									MyApplication.getInstance().addDownList(mDatas2.get(i));
									startDownBook(bookItem.mBookID,
											downLoadPath, mHandler,
											BookMagazineActivity.this, 2, i);
									break;
								case 3:
									mDatas3.get(i).mDownloadCount = 0;
									MyApplication.getInstance().addDownList(mDatas3.get(i));
									startDownBook(bookItem.mBookID,
											downLoadPath, mHandler,
											BookMagazineActivity.this, 3, i);
									break;
								case 4:
									mDatas4.get(i).mDownloadCount = 0;
									MyApplication.getInstance().addDownList(mDatas4.get(i));
									startDownBook(bookItem.mBookID,
											downLoadPath, mHandler,
											BookMagazineActivity.this, 4, i);
									break;
								case 5:
									mDatas5.get(i).mDownloadCount = 0;
									MyApplication.getInstance().addDownList(mDatas5.get(i));
									startDownBook(bookItem.mBookID,
											downLoadPath, mHandler,
											BookMagazineActivity.this, 5, i);
									break;
								}

//								if (!DataBase.checkIsExist(
//								BookshelfActivity.this,
//								bookItem.mBookID)) {
//							DataBase.insertSearchRecord(
//									BookshelfActivity.this, bookItem,
//									localbookbasepath + bookItem.mBookID + "/book");
//						}
						editor.putString(bookItem.mBookID, localbookbasepath
								+ bookItem.mBookID
								+ "/book");
						editor.commit();
							} else if (bookItem.mState == 1) {
								viewHolder.mRestartImg
										.setVisibility(View.VISIBLE);
								switch (selectItem) {
								case 1:
									mDatas1.get(i).mState = 3;
									break;
								case 2:
									mDatas2.get(i).mState = 3;
									break;
								case 3:
									mDatas3.get(i).mState = 3;
									break;
								case 4:
									mDatas4.get(i).mState = 3;
									break;
								case 5:
									mDatas5.get(i).mState = 3;
									break;
								}
							}
						} else {
							if (userCache.getVipType().equals("0")) { // 非会员，则购买,否则直接下载查看
								buyBook(); // 购买之后，才能进行查看
							}
							if (!userCache.getVipType().equals("0")) {
								File f1 = new File(localPath + "/book.dat");
								File f2 = new File(localPath + "/book.xml");
								File f3 = new File(localPath + "/hash.dat");
								if (localPath != null && f1.exists() && f2.exists() && f3.exists()) {
									File f = new File(localPath);
									
									if (f.exists()) {
										if (selectItem == 3) {
											 Intent intent = new Intent(BookMagazineActivity.this,DetailBabyFavSingActivity.class);
											 intent.putExtra("path",localPath);
											 intent.putExtra("name", bookItem.mName);
											 startActivity(intent);
										} else if(selectItem == 1) {
											needGetData = true;
											HLReader.show(BookMagazineActivity.this,
													 localPath, true);
										} else {
											needGetData = true;
											HLReader.show(BookMagazineActivity.this,
													 localPath, false);
										}
										bookItem.mState = 2;
										return;
									} else {
//										DataBase.deleteSearchRecord(
//										BookshelfActivity.this,
//										bookItem.mBookID);
								editor.putString(bookItem.mBookID, "");
								editor.commit();
									}
								}

								if(MyApplication.downList.size() >= 2) {
									
									Toast.makeText(mContext, "已加入下载队列",
											Toast.LENGTH_SHORT).show();
									for(int i = 0; i < ZipedId.size(); i++) {
										if(ZipedId.get(i).equals(bookItem.mBookID)) {
											ZipedId.remove(i);
										}
									}
									switch (selectItem) {
									case 1:
										mDatas1.get(i).mState = 4;
										MyApplication.getInstance().addDownList(mDatas1.get(i));
										notifyItemChanged(i);
										break;
									case 2:
										mDatas2.get(i).mState = 4;
										MyApplication.getInstance().addDownList(mDatas2.get(i));
										notifyItemChanged(i);
										break;
									case 3:
										mDatas3.get(i).mState = 4;
										MyApplication.getInstance().addDownList(mDatas3.get(i));
										notifyItemChanged(i);
										break;
									case 4:
										mDatas4.get(i).mState = 4;
										MyApplication.getInstance().addDownList(mDatas4.get(i));
										notifyItemChanged(i);
										break;
									case 5:
										mDatas5.get(i).mState = 4;
										MyApplication.getInstance().addDownList(mDatas5.get(i));
										notifyItemChanged(i);
										break;
									}
									
								} else if (bookItem.mState != 1) {
									Toast.makeText(mContext, "正在下载...",
											Toast.LENGTH_SHORT).show();
									for(int i = 0; i < ZipedId.size(); i++) {
										if(ZipedId.get(i).equals(bookItem.mBookID)) {
											ZipedId.remove(i);
										}
									}
									String downLoadPath = "";
									// String isPay = DataUtils.getPreference(
									// BookMagazineActivity.this, "userType",
									// "");
									downLoadPath = Constants.DOWNLOAD_URL
											+ bookItem.mBookurl;
									switch (selectItem) {
									case 1:
										MyApplication.getInstance().addDownList(mDatas1.get(i));
										startDownBook(bookItem.mBookID,
												downLoadPath, mHandler,
												BookMagazineActivity.this, 1, i);
										break;
									case 2:
										MyApplication.getInstance().addDownList(mDatas2.get(i));
										startDownBook(bookItem.mBookID,
												downLoadPath, mHandler,
												BookMagazineActivity.this, 2, i);
										break;
									case 3:
										MyApplication.getInstance().addDownList(mDatas3.get(i));
										startDownBook(bookItem.mBookID,
												downLoadPath, mHandler,
												BookMagazineActivity.this, 3, i);
										break;
									case 4:
										MyApplication.getInstance().addDownList(mDatas4.get(i));
										startDownBook(bookItem.mBookID,
												downLoadPath, mHandler,
												BookMagazineActivity.this, 4, i);
										break;
									case 5:
										MyApplication.getInstance().addDownList(mDatas5.get(i));
										startDownBook(bookItem.mBookID,
												downLoadPath, mHandler,
												BookMagazineActivity.this, 5, i);
										break;
									}

//									if (!DataBase.checkIsExist(
//									BookshelfActivity.this,
//									bookItem.mBookID)) {
//								DataBase.insertSearchRecord(
//										BookshelfActivity.this, bookItem,
//										localbookbasepath + bookItem.mBookID + "/book");
//							}
							editor.putString(bookItem.mBookID, localbookbasepath
									+ bookItem.mBookID
									+ "/book");
							editor.commit();
								} else if (bookItem.mState == 1) {
									viewHolder.mRestartImg
											.setVisibility(View.VISIBLE);
									switch (selectItem) {
									case 1:
										mDatas1.get(i).mState = 3;
										break;
									case 2:
										mDatas2.get(i).mState = 3;
										break;
									case 3:
										mDatas3.get(i).mState = 3;
										break;
									case 4:
										mDatas4.get(i).mState = 3;
										break;
									case 5:
										mDatas5.get(i).mState = 3;
										break;
									}
								}
							}
						}

					}
				});

				viewHolder.mRestartImg
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								if(MyApplication.downList.size() >= 2) {
									
									Toast.makeText(mContext, "已加入下载队列",
											Toast.LENGTH_SHORT).show();
									switch (selectItem) {
									case 1:
										mDatas1.get(i).mState = 4;
										MyApplication.getInstance().addDownList(mDatas1.get(i));
										notifyItemChanged(i);
										break;
									case 2:
										mDatas2.get(i).mState = 4;
										MyApplication.getInstance().addDownList(mDatas2.get(i));
										notifyItemChanged(i);
										break;
									case 3:
										mDatas3.get(i).mState = 4;
										MyApplication.getInstance().addDownList(mDatas3.get(i));
										notifyItemChanged(i);
										break;
									case 4:
										mDatas4.get(i).mState = 4;
										MyApplication.getInstance().addDownList(mDatas4.get(i));
										notifyItemChanged(i);
										break;
									case 5:
										mDatas5.get(i).mState = 4;
										MyApplication.getInstance().addDownList(mDatas5.get(i));
										notifyItemChanged(i);
										break;
									}
									
								} else {
									viewHolder.mRestartImg.setVisibility(View.GONE);
									viewHolder.mPauseImg.setVisibility(View.VISIBLE);
									Toast.makeText(mContext, "正在下载...",
											Toast.LENGTH_SHORT).show();
									String downLoadPath = "";
									downLoadPath = Constants.DOWNLOAD_URL
											+ bookItem.mBookurl;
									switch (selectItem) {
									case 1:
										MyApplication.getInstance().addDownList(mDatas1.get(i));
										startDownBook(bookItem.mBookID,
												downLoadPath, mHandler,
												BookMagazineActivity.this, 1, i);
										break;
									case 2:
										MyApplication.getInstance().addDownList(mDatas2.get(i));
										startDownBook(bookItem.mBookID,
												downLoadPath, mHandler,
												BookMagazineActivity.this, 2, i);
										break;
									case 3:
										MyApplication.getInstance().addDownList(mDatas3.get(i));
										startDownBook(bookItem.mBookID,
												downLoadPath, mHandler,
												BookMagazineActivity.this, 3, i);
										break;
									case 4:
										MyApplication.getInstance().addDownList(mDatas4.get(i));
										startDownBook(bookItem.mBookID,
												downLoadPath, mHandler,
												BookMagazineActivity.this, 4, i);
										break;
									case 5:
										MyApplication.getInstance().addDownList(mDatas5.get(i));
										startDownBook(bookItem.mBookID,
												downLoadPath, mHandler,
												BookMagazineActivity.this, 5, i);
										break;
									}

//									if (!DataBase.checkIsExist(
//									BookshelfActivity.this,
//									bookItem.mBookID)) {
//								DataBase.insertSearchRecord(
//										BookshelfActivity.this, bookItem,
//										localbookbasepath + bookItem.mBookID + "/book");
//							}
							editor.putString(bookItem.mBookID, localbookbasepath
									+ bookItem.mBookID
									+ "/book");
							editor.commit();
								}
								
								// DataBase.reSetState(bookItem.mBookID, "0",
								// BookMagazineActivity.this);
							}

						});
				
				viewHolder.mPauseImg
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
							viewHolder.mRestartImg.setVisibility(View.VISIBLE);
							viewHolder.mPauseImg.setVisibility(View.GONE);
							switch (selectItem) {
							case 1:
								MyApplication.getInstance().overDownListByBookId(mDatas1.get(i).mBookID);
								mDatas1.get(i).mState = 3;
								break;
							case 2:
								MyApplication.getInstance().overDownListByBookId(mDatas2.get(i).mBookID);
								mDatas2.get(i).mState = 3;
								break;
							case 3:
								MyApplication.getInstance().overDownListByBookId(mDatas3.get(i).mBookID);
								mDatas3.get(i).mState = 3;
								break;
							case 4:
								MyApplication.getInstance().overDownListByBookId(mDatas4.get(i).mBookID);
								mDatas4.get(i).mState = 3;
								break;
							case 5:
								MyApplication.getInstance().overDownListByBookId(mDatas5.get(i).mBookID);
								mDatas5.get(i).mState = 3;
								break;
							}
						
							notifyDataSetChanged();
					}

				});
			}
		}
	}

	public Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int adapterP = msg.getData().getInt("adapterP");
			int adapterItem = msg.getData().getInt("adapterItem");

			switch (msg.what) {
			case Constants.SRART_DOWN:
				// mAdapter1.notifyDataSetChanged();
				// mAdapter2.notifyDataSetChanged();
				// mAdapter3.notifyDataSetChanged();
				// mAdapter4.notifyDataSetChanged();
				// mAdapter5.notifyDataSetChanged();
				break;
			case Constants.OVER_DOWN:
				String oid = (String) msg.obj;
				switch (msg.arg1) {
				case 1:
					for (int i = 0; i < mDatas1.size(); i++) {
						String lid = mDatas1.get(i).mBookID;
						if (lid.equals(oid)) {
							mDatas1.get(i).mState = 2;
							mDatas1.get(i).LOCALPATH = localbookbasepath + oid
									+ "/book";
							MyApplication.getInstance().overDownList(mDatas1.get(i));
							checkDownState();
						}
					}
					mAdapter1.notifyDataSetChanged();
					break;
				case 2:
					for (int i = 0; i < mDatas2.size(); i++) {
						String lid = mDatas2.get(i).mBookID;
						if (lid.equals(oid)) {
							mDatas2.get(i).mState = 2;
							mDatas2.get(i).LOCALPATH = localbookbasepath + oid
									+ "/book";
							MyApplication.getInstance().overDownList(mDatas2.get(i));
							checkDownState();
						}
					}
					mAdapter1.notifyDataSetChanged();
					break;
				case 3:
					for (int i = 0; i < mDatas3.size(); i++) {
						String lid = mDatas3.get(i).mBookID;
						if (lid.equals(oid)) {
							mDatas3.get(i).mState = 2;
							mDatas3.get(i).LOCALPATH = localbookbasepath + oid
									+ "/book";
							MyApplication.getInstance().overDownList(mDatas3.get(i));
							checkDownState();
						}
					}
					mAdapter1.notifyDataSetChanged();
					break;
				case 4:
					for (int i = 0; i < mDatas4.size(); i++) {
						String lid = mDatas4.get(i).mBookID;
						if (lid.equals(oid)) {
							mDatas4.get(i).mState = 2;
							mDatas4.get(i).LOCALPATH = localbookbasepath + oid
									+ "/book";
							MyApplication.getInstance().overDownList(mDatas4.get(i));
							checkDownState();
						}
					}
					mAdapter1.notifyDataSetChanged();
					break;
				case 5:
					for (int i = 0; i < mDatas5.size(); i++) {
						String lid = mDatas5.get(i).mBookID;
						if (lid.equals(oid)) {
							mDatas5.get(i).mState = 2;
							mDatas5.get(i).LOCALPATH = localbookbasepath + oid
									+ "/book";
							MyApplication.getInstance().overDownList(mDatas5.get(i));
							checkDownState();
						}
					}
					mAdapter1.notifyDataSetChanged();
					break;
				}

				break;
			case Constants.LOAD_DOWN:
				int pro = msg.arg1;
				String idd = msg.obj.toString().trim();
				setProgressData(adapterP, adapterItem, pro, idd);
				break;
			case Constants.DOWN_ERROR:
				Object[] ob = (Object[]) msg.obj;
				String bij = ob[0].toString().trim();
				Activity ac = (Activity) ob[1];
				//DataBase.deleteSearchRecord(ac, bij);
				// freshenData();
				break;
			case 250250250:
				for (Book bk1 : mDatas1) {
					if (bk1.mState == 1) {
						MyApplication.getInstance().addDownList(bk1);
						for (int j = 0; j < MyApplication.downList.size(); j++) {
							if (bk1.equals(MyApplication.downList.get(j)))  {
								MyApplication.downList.get(j).mDownloadCount = bk1.mDownloadCount;
								MyApplication.downList.get(j).isServiceDown = true;
								Intent intent = new Intent(BookMagazineActivity.this, DownloadService.class);
								intent.putExtra("serviceDownBook", bk1);
								intent.putExtra("command", 1);
								startService(intent);
								bk1.mState = 3;
							}
						}
						
					}
				}

				for (Book bk2 : mDatas2) {
					if (bk2.mState == 1) {
						MyApplication.getInstance().addDownList(bk2);
						for (int j = 0; j < MyApplication.downList.size(); j++) {
							if (bk2.equals(MyApplication.downList.get(j)))  {
								MyApplication.downList.get(j).mDownloadCount = bk2.mDownloadCount;
								MyApplication.downList.get(j).isServiceDown = true;
								Intent intent = new Intent(BookMagazineActivity.this, DownloadService.class);
								intent.putExtra("serviceDownBook", bk2);
								intent.putExtra("command", 1);
								startService(intent);
								bk2.mState = 3;
							}
						}
						
					}
				}
				
				for (Book bk : mDatas3) {
					if (bk.mState == 1) {
						MyApplication.getInstance().addDownList(bk);
						for (int j = 0; j < MyApplication.downList.size(); j++) {
							if (bk.equals(MyApplication.downList.get(j)))  {
								MyApplication.downList.get(j).mDownloadCount = bk.mDownloadCount;
								MyApplication.downList.get(j).isServiceDown = true;
								Intent intent = new Intent(BookMagazineActivity.this, DownloadService.class);
								intent.putExtra("serviceDownBook", bk);
								intent.putExtra("command", 1);
								startService(intent);
								bk.mState = 3;
							}
						}
						
					}
				}
				
				for (Book bk : mDatas4) {
					if (bk.mState == 1) {
						MyApplication.getInstance().addDownList(bk);
						for (int j = 0; j < MyApplication.downList.size(); j++) {
							if (bk.equals(MyApplication.downList.get(j)))  {
								MyApplication.downList.get(j).mDownloadCount = bk.mDownloadCount;
								MyApplication.downList.get(j).isServiceDown = true;
								Intent intent = new Intent(BookMagazineActivity.this, DownloadService.class);
								intent.putExtra("serviceDownBook", bk);
								intent.putExtra("command", 1);
								startService(intent);
								bk.mState = 3;
							}
						}
						
					}
				}
				
				for (Book bk : mDatas5) {
					if (bk.mState == 1) {
						MyApplication.getInstance().addDownList(bk);
						for (int j = 0; j < MyApplication.downList.size(); j++) {
							if (bk.equals(MyApplication.downList.get(j)))  {
								MyApplication.downList.get(j).mDownloadCount = bk.mDownloadCount;
								MyApplication.downList.get(j).isServiceDown = true;
								Intent intent = new Intent(BookMagazineActivity.this, DownloadService.class);
								intent.putExtra("serviceDownBook", bk);
								intent.putExtra("command", 1);
								startService(intent);
								bk.mState = 3;
							}
						}
						
					}
				}
				
				for(Book bk : mDatas1) {
					if(bk.mState == 1) {
						bk.mState = 3;
					}
				}
				for(Book bk : mDatas2) {
					if(bk.mState == 1) {
						bk.mState = 3;
					}
				}
				for(Book bk : mDatas3) {
					if(bk.mState == 1) {
						bk.mState = 3;
					}
				}
				for(Book bk : mDatas4) {
					if(bk.mState == 1) {
						bk.mState = 3;
					}
				}
				for(Book bk : mDatas5) {
					if(bk.mState == 1) {
						bk.mState = 3;
					}
				}

				saveListToLocal("1", mDatas1);
				saveListToLocal("2", mDatas2);
				saveListToLocal("3", mDatas3);
				saveListToLocal("4", mDatas4);
				saveListToLocal("5", mDatas5);
				break;
			case 180180180:
				Bundle bundle = msg.getData();
		        String str = (String) bundle.get("bookName");
		        showErrorDialog(str);
				break;
			default:
				break;
			}
		};
	};
	
	private AlertDialog errorDialog;
	private void showErrorDialog(String str) {
		if(errorDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this); 
			builder.setMessage(str +"，下载出错，请重新下载！") 
			       .setCancelable(false) 
			       .setPositiveButton("确定", new DialogInterface.OnClickListener() { 
			           public void onClick(DialogInterface dialog, int id) { 
			           } 
			       }) ; 
			errorDialog = builder.create(); 
		}
			
		
		if(!errorDialog.isShowing()) {
			errorDialog.show();
		}
	}

	/**
	 * 开始下载
	 * 
	 * @param bookId
	 * @param downUrl
	 * @param handler
	 * @param ac
	 */

	public void startDownBook(final String bookId, final String downUrl,
			final Handler handler, final Activity ac, final int adapterP,
			final int adapterItem) {
		
		switch (adapterP) {
		case 1:
			if(mDatas1.get(adapterItem).mState == 1) {
				return;
			} else {
				mDatas1.get(adapterItem).mState = 1;
			}
			break;
		case 2:
			if(mDatas2.get(adapterItem).mState == 1) {
				return;
			} else {
				mDatas2.get(adapterItem).mState = 1;
			}
			break;
		case 3:
			if(mDatas3.get(adapterItem).mState == 1) {
				return;
			} else {
				mDatas3.get(adapterItem).mState = 1;
			}
			break;
		case 4:
			if(mDatas4.get(adapterItem).mState == 1) {
				return;
			} else {
				mDatas4.get(adapterItem).mState = 1;
			}
			break;
		case 5:
			if(mDatas5.get(adapterItem).mState == 1) {
				return;
			} else {
				mDatas5.get(adapterItem).mState = 1;
			}
			break;
		}
		String tempPath = "";
		Log.d("czb", "____" + downUrl);
		tempPath = localbookbasepath + bookId + "/book.zip";

		final String zipPath = tempPath;
		new Thread() {
			public void run() {
				downLoadResourceWithProgress(ac, bookId, downUrl, zipPath,
						adapterP, adapterItem, new ProgressCallBack() {
							@Override
							public boolean startDown(int arg0) {
								// TODO Auto-generated method stub
								Message msg = Message.obtain();
								msg.obj = bookId;
								msg.what = Constants.SRART_DOWN;
								mHandler.sendMessage(msg);
								return false;
							}

							@Override
							public boolean downOver(int arg0) {
								Message msg = Message.obtain();
								msg.obj = bookId;
								msg.arg1 = adapterP;
								msg.what = Constants.OVER_DOWN;
								try {
									
//									switch (selectItem) {
//									case 1:
//										mDatas1.get(adapterItem).mState = 1;
//										break;
//									case 2:
//										mDatas2.get(adapterItem).mState = 1;
//										break;
//									case 3:
//										mDatas3.get(adapterItem).mState = 1;
//										break;
//									case 4:
//										mDatas4.get(adapterItem).mState = 1;
//										break;
//									case 5:
//										mDatas5.get(adapterItem).mState = 1;
//										break;
//									}
									if(ZipedId.indexOfValue(bookId) == -1) {
//										while(true) {
//											if(!isZiping) {
//												isZiping = true;
//												break;
//											}
//											Thread.sleep(2000);
//										}
										
										if(ZipedId.indexOfValue(bookId) == -1) {
											Log.e("czb", "11111");
											UnZipFolder(zipPath,
														localbookbasepath + bookId);
											Log.e("czb", "22222");
										}
										
										ZipedId.append(ZipedId.size(), bookId);
//										isZiping = false;
										
										switch (selectItem) {
										case 1:
											mDatas1.get(adapterItem).mState = 2;
											break;
										case 2:
											mDatas2.get(adapterItem).mState = 2;
											break;
										case 3:
											mDatas3.get(adapterItem).mState = 2;
											break;
										case 4:
											mDatas4.get(adapterItem).mState = 2;
											break;
										case 5:
											mDatas5.get(adapterItem).mState = 2;
											break;
										}
										mHandler.sendMessage(msg);
									}
									
									
								} catch (Exception e) {
									//isZiping = false;
									String name = "";
									switch (adapterP) {
									case 1:
										name = mDatas1.get(adapterItem).mName;
										mDatas1.get(adapterItem).mState = 0;
										break;
									case 2:
										name = mDatas2.get(adapterItem).mName;
										mDatas2.get(adapterItem).mState = 0;
										break;
									case 3:
										name = mDatas3.get(adapterItem).mName;
										mDatas3.get(adapterItem).mState = 0;
										break;
									case 4:
										name = mDatas4.get(adapterItem).mName;
										mDatas4.get(adapterItem).mState = 0;
										break;
									case 5:
										name = mDatas5.get(adapterItem).mName;
										mDatas5.get(adapterItem).mState = 0;
										break;
									}
									Bundle bundle= new Bundle();
									bundle.putString("bookName",name);
									Message msg2 = Message.obtain();
									msg2.what = 180180180;
									msg2.setData(bundle);
									mHandler.sendMessage(msg2);
									//Toast.makeText(BookshelfActivity.this, "下载出错，请重新下载！", Toast.LENGTH_SHORT).show();
									File f = new File(localbookbasepath
											+ bookId);
									DataCleanManager.deleteFolderFile(f.getAbsolutePath(),true);
									Log.e("czb", "This is ZipUtil Exception : " + f.getAbsolutePath() + " been delete", e);
								} finally {
									//isZiping = false;
								}
								return true;
							}

							@Override
							public boolean doProgressAction(int total, int pro) {
								// TODO Auto-generated method stub
								Message msg = Message.obtain();
								int proint = (int) ((float) pro / (float) total * 100);
								if (proint % 2 == 0) {
									msg.arg1 = proint;
									msg.obj = bookId;
									Bundle bundleData = new Bundle();
									bundleData.putInt("adapterP", adapterP);
									bundleData.putInt("adapterItem",
											adapterItem);
									msg.setData(bundleData);
									msg.what = Constants.LOAD_DOWN;
									mHandler.sendMessage(msg);
								}
								return true;
							}
						});
			};
		}.start();
	}

	public boolean downLoadResourceWithProgress(Activity ac, String bookid,
			String urlPath, String localPath, int adapterP, int adapterItem,
			ProgressCallBack progressCall) {

		// 是不是要自动重定向，一定要，因为我的图片可能不存在apache服务器上，不转发怎么行
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

			// out = new FileOutputStream(output, false);

			switch (adapterP) {
			case 1:
				connection.setRequestProperty("range",
						"bytes=" + mDatas1.get(adapterItem).mDownloadCount
								+ "-");
				out.seek(mDatas1.get(adapterItem).mDownloadCount);

				break;
			case 2:
				connection.setRequestProperty("range",
						"bytes=" + mDatas2.get(adapterItem).mDownloadCount
								+ "-");
				out.seek(mDatas2.get(adapterItem).mDownloadCount);
				break;
			case 3:
				connection.setRequestProperty("range",
						"bytes=" + mDatas3.get(adapterItem).mDownloadCount
								+ "-");
				out.seek(mDatas3.get(adapterItem).mDownloadCount);
				break;
			case 4:
				connection.setRequestProperty("range",
						"bytes=" + mDatas4.get(adapterItem).mDownloadCount
								+ "-");
				out.seek(mDatas4.get(adapterItem).mDownloadCount);
				break;
			case 5:
				connection.setRequestProperty("range",
						"bytes=" + mDatas5.get(adapterItem).mDownloadCount
								+ "-");
				out.seek(mDatas5.get(adapterItem).mDownloadCount);
				break;
			}

			Log.e("czb", connection.getResponseCode() + "  bff");
			if(connection.getResponseCode() == 416) {
				return progressCall.downOver(0);
			}
			// 如果网络地址上存在这个文件，直接下载，如果不存在，返回false，下载失败
			if (connection.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {
				in = connection.getInputStream();
				int contentLength = connection.getContentLength();
				Log.i("czb", "2申请下载数据资源大小是" + contentLength);
				progressCall.startDown(contentLength);
				if (contentLength == 0) {
					return false;
				}
				// int curFileSize = 0;

				switch (adapterP) {
				case 1:
					if (mDatas1.get(adapterItem).mBookSize == 0) {
						mDatas1.get(adapterItem).mBookSize = contentLength;
					}
					break;
				case 2:
					if (mDatas2.get(adapterItem).mBookSize == 0) {
						mDatas2.get(adapterItem).mBookSize = contentLength;
					}
					break;
				case 3:
					if (mDatas3.get(adapterItem).mBookSize == 0) {
						mDatas3.get(adapterItem).mBookSize = contentLength;
					}
					break;
				case 4:
					if (mDatas4.get(adapterItem).mBookSize == 0) {
						mDatas4.get(adapterItem).mBookSize = contentLength;
					}
					break;
				case 5:
					if (mDatas5.get(adapterItem).mBookSize == 0) {
						mDatas5.get(adapterItem).mBookSize = contentLength;
					}
					break;
				}

				Log.e("czb", "contentLength " + contentLength);
				byte[] data = new byte[1024];
				int read = 0;
				while ((read = in.read(data)) != -1) {
					out.write(data, 0, read);
					switch (adapterP) {
					case 1:
						if (mDatas1.get(adapterItem).mState == 3 || isDownOver) {
							return false;
						} else {
							mDatas1.get(adapterItem).mDownloadCount += read;
							boolean b1 = progressCall.doProgressAction(
									mDatas1.get(adapterItem).mBookSize,
									mDatas1.get(adapterItem).mDownloadCount);
						}

						break;
					case 2:
						if (mDatas2.get(adapterItem).mState == 3 || isDownOver) {
							return false;
						} else {
							mDatas2.get(adapterItem).mDownloadCount += read;
							boolean b2 = progressCall.doProgressAction(
									mDatas2.get(adapterItem).mBookSize,
									mDatas2.get(adapterItem).mDownloadCount);
						}
						break;
					case 3:
						
						if (mDatas3.get(adapterItem).mState == 3 || isDownOver) {
							return false;
						} else {
							mDatas3.get(adapterItem).mDownloadCount += read;
							boolean b3 = progressCall.doProgressAction(
									mDatas3.get(adapterItem).mBookSize,
									mDatas3.get(adapterItem).mDownloadCount);
						}
						break;
					case 4:
						
						if (mDatas4.get(adapterItem).mState == 3 || isDownOver) {
							return false;
						} else {
							mDatas4.get(adapterItem).mDownloadCount += read;
							boolean b4 = progressCall.doProgressAction(
									mDatas4.get(adapterItem).mBookSize,
									mDatas4.get(adapterItem).mDownloadCount);
						}
						break;
					case 5:
						
						if (mDatas5.get(adapterItem).mState == 3 || isDownOver) {
							return false;
						} else {
							mDatas5.get(adapterItem).mDownloadCount += read;
							boolean b5 = progressCall.doProgressAction(
									mDatas5.get(adapterItem).mBookSize,
									mDatas5.get(adapterItem).mDownloadCount);
						}
						break;
					}
					// if (!b) {
					// Log.i("mouee", "下载被终止");
					// return false;
					// }

					// Log.e("czb", "read " + read);
				}
				// out.flush();
				connection.disconnect();
				return progressCall.downOver(contentLength);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Object[] ob = { bookid, ac };
			Message message = Message.obtain();
			message.obj = ob;
			message.what = Constants.DOWN_ERROR;
			mHandler.sendMessage(message);
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
			Log.i("czb", "下载结束。");

		}
		return false;
	}

	public void setProgressData(int adapterP, int adapterItem, int i, String id) {
		// TODO Auto-generated method stub
		progress_data = i;
		cuId = id;

		switch (adapterP) {
		case 1:
			if(adapterItem < mDatas1.size())
				mDatas1.get(adapterItem).mDownloadProgress = i;
			if (mAdapter1 != null)
				mAdapter1.notifyItemChanged(adapterItem);
			break;
		case 2:
			if(adapterItem < mDatas2.size())
				mDatas2.get(adapterItem).mDownloadProgress = i;
			if (mAdapter1 != null)
				mAdapter1.notifyItemChanged(adapterItem);
			break;
		case 3:
			if(adapterItem < mDatas3.size())
				mDatas3.get(adapterItem).mDownloadProgress = i;
			if (mAdapter1 != null)
				mAdapter1.notifyItemChanged(adapterItem);
			break;
		case 4:
			if(adapterItem < mDatas4.size())
				mDatas4.get(adapterItem).mDownloadProgress = i;
			if (mAdapter1 != null)
				mAdapter1.notifyItemChanged(adapterItem);
			break;
		case 5:
			if(adapterItem < mDatas5.size())
				mDatas5.get(adapterItem).mDownloadProgress = i;
			if (mAdapter1 != null)
				mAdapter1.notifyItemChanged(adapterItem);
			break;
		default:
			break;
		}

		// if(!isMove) {
		// DataBase.reSetProcess(id, i,
		// BookshelfActivity.this);
		// }

	}

	/**
	 * @author 作者 : sun
	 * @date 创建时间：2015-12-31 下午5:42:26
	 * @return
	 * @description 电子书的支付
	 */

	String purchaseTime = null;
	String moneyNum = null;
	View payView = null;
	CheckBox one, two, three, four;
	ImageButton sure_buy;
	String desc;
	private void buyBook() {

		payView = getLayoutInflater().inflate(R.layout.buy_book, null);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( width/7*4, height/10*8);
		View containerId = findViewById(R.id.containerMagId);
		relativeLayout = new RelativeLayout(this);
		relativeLayout.setBackgroundColor(Color.parseColor("#00000000"));
		RelativeLayout.LayoutParams paramsNew = 
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
		relativeLayout.setClickable(true);
		relativeLayout.setLayoutParams(paramsNew);
		relativeLayout.addView(payView);
		
		relativeLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				relativeLayout.setVisibility(View.GONE);
				relativeLayout.setClickable(false);
			}
		});
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		payView.setLayoutParams(params);
		RelativeLayout conLayout = (RelativeLayout) findViewById(R.id.containerMagId);
		conLayout.addView(relativeLayout);
		
		String userId = userCache.getUserId();
		one = (CheckBox) payView.findViewById(R.id.check1);
		two = (CheckBox) payView.findViewById(R.id.check2);
		three = (CheckBox) payView.findViewById(R.id.check3);
		four = (CheckBox) payView.findViewById(R.id.check4);
		tvOne = (TextView) payView.findViewById(R.id.one);
		tvTwo = (TextView) payView.findViewById(R.id.two);
		tVthree = (TextView) payView.findViewById(R.id.three);
		tVfour = (TextView) payView.findViewById(R.id.four);
		sure_buy = (ImageButton) payView.findViewById(R.id.sure_buy);
		wxPay = (ImageButton) payView.findViewById(R.id.sure_buy_wePay);
		one.setOnCheckedChangeListener(this);
		two.setOnCheckedChangeListener(this);
		three.setOnCheckedChangeListener(this);
		four.setOnCheckedChangeListener(this);
		getPrice(tvOne,tvTwo,tVthree,tVfour);
		TextView closePayView = (TextView)payView. findViewById(R.id.close);
		closePayView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				payView.setVisibility(View.GONE);
			}
		});
		//支付宝支付
		sure_buy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!one.isChecked() && !two.isChecked() && !three.isChecked() && !four.isChecked()){
					ToastUtil.showShortToast(BookMagazineActivity.this, "您当前未勾选,请勾选!");
					return;
				}
				
				if(one.isChecked() && !two.isChecked() && !three.isChecked() && !four.isChecked()){
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++){
							PriceBean priceBean = Constants.priceBeans.get(i);
							if(priceBean.getCycle().equals("1")){
								moneyNum = priceBean.getPrice();
								//moneyNum = "0.01";
								purchaseTime = priceBean.getCycle();
								desc = priceBean.getCycle()+"个月会员";
							}
						}
						
					}else {
						purchaseTime = "1";
						moneyNum = "25";
					}
					 
				}
				if(!one.isChecked() && two.isChecked() && !three.isChecked() && !four.isChecked()){
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++){
							PriceBean priceBean = Constants.priceBeans.get(i);
							if(priceBean.getCycle().equals("3")){
								moneyNum = priceBean.getPrice();
								purchaseTime = priceBean.getCycle();
								desc = priceBean.getCycle()+"个月会员";
							}
						}
					}else {
						purchaseTime = "3";
						moneyNum = "60";
					}
					
				}
				if(!one.isChecked() && !two.isChecked() && three.isChecked() && !four.isChecked()){
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++){
							PriceBean priceBean = Constants.priceBeans.get(i);
							if(priceBean.getCycle().equals("6")){
								moneyNum = priceBean.getPrice();
								purchaseTime = priceBean.getCycle();
								desc = priceBean.getCycle()+"个月会员";
							}
						}
					}else {
						purchaseTime = "6";
						moneyNum = "105";
					}
					
				}
				if(!one.isChecked() && !two.isChecked() && !three.isChecked() && four.isChecked()){
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++){
							PriceBean priceBean = Constants.priceBeans.get(i);
							if(priceBean.getCycle().equals("12")){
								moneyNum = priceBean.getPrice();
								purchaseTime = priceBean.getCycle();
								desc = priceBean.getCycle()+"个月会员";
							}
						}
					}else {
						purchaseTime = "12";
						moneyNum = "168";
					}
					
				}
				pay(moneyNum, desc);
			}
		});

		
		
		
		//微信支付
		wxPay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if(!one.isChecked() && !two.isChecked() && !three.isChecked() && !four.isChecked()){
					ToastUtil.showShortToast(BookMagazineActivity.this, "您当前未勾选,请勾选!");
					return;
				}
				
				if(one.isChecked() && !two.isChecked() && !three.isChecked() && !four.isChecked()){
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++){
							PriceBean priceBean = Constants.priceBeans.get(i);
							if(priceBean.getCycle().equals("1")){
								moneyNum = priceBean.getPrice();
								//moneyNum = "0.01";
								purchaseTime = priceBean.getCycle();
								desc = priceBean.getCycle()+"个月会员";
							}
						}
						
					}else {
						purchaseTime = "1";
						moneyNum = "25";
					}
					 
				}
				if(!one.isChecked() && two.isChecked() && !three.isChecked() && !four.isChecked()){
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++){
							PriceBean priceBean = Constants.priceBeans.get(i);
							if(priceBean.getCycle().equals("3")){
								moneyNum = priceBean.getPrice();
								purchaseTime = priceBean.getCycle();
								desc = priceBean.getCycle()+"个月会员";
							}
						}
					}else {
						purchaseTime = "3";
						moneyNum = "60";
					}
					
				}
				if(!one.isChecked() && !two.isChecked() && three.isChecked() && !four.isChecked()){
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++){
							PriceBean priceBean = Constants.priceBeans.get(i);
							if(priceBean.getCycle().equals("6")){
								moneyNum = priceBean.getPrice();
								purchaseTime = priceBean.getCycle();
								desc = priceBean.getCycle()+"个月会员";
							}
						}
					}else {
						purchaseTime = "6";
						moneyNum = "105";
					}
					
				}
				if(!one.isChecked() && !two.isChecked() && !three.isChecked() && four.isChecked()){
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++){
							PriceBean priceBean = Constants.priceBeans.get(i);
							if(priceBean.getCycle().equals("12")){
								moneyNum = priceBean.getPrice();
								purchaseTime = priceBean.getCycle();
								desc = priceBean.getCycle()+"个月会员";
							}
						}
					}else {
						purchaseTime = "12";
						moneyNum = "168";
					}
					
				}
				LoadingDialog.isLoading(BookMagazineActivity.this);
				WePayUtils.getPayInfo(wePayHandler, moneyNum,BookMagazineActivity.this,WePay.class,desc);
				Constants.isVipOrMod = "vip";
			}
		});
		
	}
	
	
	
	Handler wePayHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				LoadingDialog.finishLoading();
				 WePay wePay = (WePay) msg.obj;
				 Log.i("info","Wepay的字段为"+wePay.toString());
				 PayReq req = new PayReq();
                 req.packageValue = "Sign=WXPay";
                 req.sign = wePay.getSign();
                 req.appId = wePay.getAppid();
                 req.prepayId = wePay.getPrepayid();
                 req.partnerId = wePay.getPartnerid();
                 req.timeStamp = wePay.getTimestamp();
                 req.nonceStr = wePay.getNoncestr();
                 Constants.nonceStr =  wePay.getNoncestr();
                 api.registerApp(wePay.getAppid());
                 api.sendReq(req);
				break;

			default:
				break;
			}
		};
	};
	
	private void getPrice(TextView one,TextView two,TextView three,TextView four){
		if(null != Constants.priceBeans){
			for (int i = 0; i < Constants.priceBeans.size(); i++) {
				PriceBean priceBean = Constants.priceBeans.get(i);
				if(priceBean.getCycle().equals("1")){
					String content = getContentPrice(priceBean);
					one.setText(content);
				}else if(priceBean.getCycle().equals("3")) {
					String content = getContentPrice(priceBean);
					two.setText(content);
				}else if(priceBean.getCycle().equals("6")) {
					String content = getContentPrice(priceBean);
					three.setText(content);
				}else if(priceBean.getCycle().equals("12")) {
					String content = getContentPrice(priceBean);
					four.setText(content);
				}
				
			}
		} 
		
	}

	private String getContentPrice(PriceBean priceBean) {
		//PriceBean priceBean = Constants.priceBeans.get(i);
		String content = null;
		moneyNum = priceBean.getPrice();
		purchaseTime = priceBean.getCycle();
		if(purchaseTime.equals("6")){
			 content = "半年会员:"+moneyNum+"人民币";
		}else if (purchaseTime.equals("12")) {
			content = "年费会员:"+moneyNum+"人民币";
		}else {
			content = purchaseTime+"个月会员:"+moneyNum+"人民币";
		}
		return content;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		one.setChecked(false);
		two.setChecked(false);
		three.setChecked(false);
		four.setChecked(false);
        switch (buttonView.getId()) {
        case R.id.check1:
        	one.setChecked(isChecked);
        	tvOne.setTextColor(Color.parseColor("#ef6944"));
			tvTwo.setTextColor(Color.parseColor("#000000"));
			tVthree.setTextColor(Color.parseColor("#000000"));
			tVfour.setTextColor(Color.parseColor("#000000"));
            break;
        case R.id.check2:
        	two.setChecked(isChecked);
        	tvOne.setTextColor(Color.parseColor("#000000"));
			tvTwo.setTextColor(Color.parseColor("#ef6944"));
			tVthree.setTextColor(Color.parseColor("#000000"));
			tVfour.setTextColor(Color.parseColor("#000000"));
            break;
        case R.id.check3:
        	three.setChecked(isChecked);
        	tvOne.setTextColor(Color.parseColor("#000000"));
			tvTwo.setTextColor(Color.parseColor("#000000"));
			tVthree.setTextColor(Color.parseColor("#ef6944"));
			tVfour.setTextColor(Color.parseColor("#000000"));
            break;
        case R.id.check4:
        	four.setChecked(isChecked);
        	tvOne.setTextColor(Color.parseColor("#000000"));
			tvTwo.setTextColor(Color.parseColor("#000000"));
			tVthree.setTextColor(Color.parseColor("#000000"));
			tVfour.setTextColor(Color.parseColor("#ef6944"));
            break;
        default:
            break;
        }
	}

	/**
	 * 
	 * @author 作者 : sun
	 * @date 创建时间：2015-12-31 下午7:58:25
	 * @return
	 * @description 获取设备唯一id
	 */
	String deviceId = null;

	private void getImieStatus() {
		String android_id = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);
		deviceId = android_id;
		Log.i("info", "deviceid的数值为:" + deviceId);
	}

	/**
	 * 
	 * @author 作者 : sun
	 * @date 创建时间：2015-12-31 下午7:43:31
	 * @return
	 * @description 获取本机ip地址
	 */

	String ip = null;

	public void getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						ip = inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(String moneyNum, String purchaseTime) {
		if (TextUtils.isEmpty(AliPayUtils.PARTNER)
				|| TextUtils.isEmpty(AliPayUtils.RSA_PRIVATE)
				|| TextUtils.isEmpty(AliPayUtils.SELLER)) {
			new AlertDialog.Builder(BookMagazineActivity.this)
					.setTitle("警告")
					.setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									//
									finish();
								}
							}).show();
			return;
		}
		
		if(!AliPayUtils.isAvilible(BookMagazineActivity.this,alipayName)){
			Toast.makeText(BookMagazineActivity.this,
					"该设备不存在支付宝终端，请安装", Toast.LENGTH_SHORT).show();
			return;
		}
//		
//		if(!AliPayUtils.isAppInstalled(BookMagazineActivity.this,"com.eg.android.AlipayGphone")){
//			Toast.makeText(BookMagazineActivity.this,
//					"该设备不存在支付宝终端，请安装", Toast.LENGTH_SHORT).show();
//			return;
//		}
		// 订单 allPrice + ""
		String orderInfo = getOrderInfo(purchaseTime + "", "该测试商品的详细描述", moneyNum
				+ "");

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Log.e("MyorderActivity", "payInfo==>  " + payInfo);

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(BookMagazineActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler2.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(BookMagazineActivity.this);
		String version = payTask.getVersion();
		Toast.makeText(BookMagazineActivity.this, version, Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + AliPayUtils.PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + AliPayUtils.SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"http://szcblm.horner.cn:8080/alliance/paperbook/alipay?tradId=\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		Log.e("MyorderActivity", "orderInfo ==>" + orderInfo);
		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, AliPayUtils.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	private Handler mHandler2 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				Log.e("MyorderActivity", resultStatus + "   " + resultInfo);

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(BookMagazineActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					// 支付成功之后给服务器的反馈信息
					
					if(isBaby){
						isBaby = false;
						BuyCategory("221");
					}else {
						toBase_ZHU();
					}
					
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(BookMagazineActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(BookMagazineActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				if (!(Boolean) msg.obj) {
					Toast.makeText(BookMagazineActivity.this,
							",,该设备不存在支付宝终端，请安装", Toast.LENGTH_SHORT).show();
					// return;
				}

				break;
			}
			default:
				break;
			}
		};
	};
	private String userId;
	private int width;
	private int height;
	private TextView tvOne;
	private TextView tvTwo;
	private TextView tVthree;
	private TextView tVfour;
	private RelativeLayout relativeLayout;
	private ImageButton wxPay;

	/**
	 * 
	 * @author 作者 : SUN
	 * @date 创建时间：2015-12-31 下午8:17:04
	 * @return
	 * @description 支付成功之后给服务器的欣欣
	 */
	public void toBase_ZHU() {
		LoadingDialog.isLoading(BookMagazineActivity.this);
		AsyncTask<String, String, String> loadDataTask = new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("userId", userCache.getUserId());
				param.put("rechargePrice", moneyNum);
				param.put("ip", ip);
				param.put("alipayNum", getOutTradeNo());
				param.put("recordMac", deviceId);
				param.put("memberMonth", purchaseTime);
				param.put("officeId","40");
				Log.i("info", "param:" + param.toString());
				String result = HttpManager.postDataToUrl(Constants.BASE_URL
						+ "vip/vipRecord", param);
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				LoadingDialog.finishLoading();
				if (!StringUtils.isEmpty(result)) {
					try {
						Log.i("info", "登录成功之后返回的数值为:" + result);
						JSONObject object = new JSONObject(result);
						String code = object.getString("code");
						if (code != null && code.equals("1000")) {
							ToastUtil.showLongToast(mContext, "会员购买成功!");
							UserCache userCache = new UserCache(
									BookMagazineActivity.this);
							MyApplication.hasBuyMag = true; // 购买vip
							userCache.setVipType("1");
//							if (null != relativeLayout) {
//								relativeLayout.setVisibility(View.GONE);
//							}
							
						} else {
							if (code.equals("1001")) {
								ToastUtil.showLongToast(mContext, "用户ID为空!");
								return;
							} else if (code.equals("1002")) {
								ToastUtil.showLongToast(mContext, "充值金额为空!");
								return;
							} else if (code.equals("1003")) {
								ToastUtil.showLongToast(mContext, "用户IP为空!");
								return;
							} else if (code.equals("1004")) {
								ToastUtil
										.showLongToast(mContext, "支付宝交易流水号为空!");
								return;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					boolean internet = HttpManager
							.isConnectingToInternet(BookMagazineActivity.this);
					if (internet) {
						Toast.makeText(BookMagazineActivity.this, "充值失败,请重试!",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(BookMagazineActivity.this,
								"登录失败,请检查网络是否可用!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
		loadDataTask.execute("");
	}

	@Override
	protected void onStop() {
		
		super.onStop();
	}
	
	@Override
	protected void onPause() {
		isDownOver = true;
		Message msg = Message.obtain();
		msg.what = 250250250;
		mHandler.sendMessageDelayed(msg, 2000);

		saveState();
		
		super.onPause();
	}
	
	private void saveState() {
		switch(selectItem) {
		case 1:
			saveListToLocal("1", mDatas1);
			break;
		case 2:
			saveListToLocal("2", mDatas2);
			break;
		case 3:
			saveListToLocal("3", mDatas3);
			break;
		case 4:
			saveListToLocal("4", mDatas4);
			break;
		case 5:
			saveListToLocal("5", mDatas5);
			break;
		}
		
	}
	
	class SpacesItemDecoration extends RecyclerView.ItemDecoration {
		  private int space;

		  public SpacesItemDecoration(int space) {
		    this.space = space;
		  }

		  @Override
		  public void getItemOffsets(Rect outRect, View view, 
		      RecyclerView parent, RecyclerView.State state) {
		    outRect.left = space;
		    outRect.right = space;
		    outRect.bottom = space;

		    // Add top margin only for the first item to avoid double space between items
		    if(parent.getChildLayoutPosition(view) == 0)
		        outRect.top = space;
		  }
		}

	class WrapContentLinearLayoutManager extends GridLayoutManager {
	    public WrapContentLinearLayoutManager(Context context, int size) {
	        super(context, size);
	    }

	    public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
	        super(context, attrs, defStyleAttr, defStyleRes);
	    }

	    @Override
	    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
	        try {
	            super.onLayoutChildren(recycler, state);
	        } catch (IndexOutOfBoundsException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	private void checkDownState() {
		switch(selectItem) {
		case 1:
			for (int i = 0; i < mDatas1.size(); i++) {
				for (int j = 0; j < MyApplication.downList.size(); j++) {
					if (mDatas1.get(i).equals(MyApplication.downList.get(j))) {
						MyApplication.downList.get(j).isServiceDown = false;
						mDatas1.get(i).mDownloadCount = (mDatas1.get(i).mDownloadCount >= MyApplication.downList.get(j).mDownloadCount) ? mDatas1.get(i).mDownloadCount : MyApplication.downList.get(j).mDownloadCount;
						//if(mDatas1.get(i).mState != 1) {
							mAdapter1.notifyItemChanged(i);
							if(mDatas1.get(i).mDownloadCount > 0 && mDatas1.get(i).mDownloadCount == mDatas1.get(i).mBookSize)
								setProgressData(1, i, 100, mDatas1.get(i).mBookID);
							String downLoadPath = Constants.DOWNLOAD_URL
									+ mDatas1.get(i).mBookurl;
							startDownBook(mDatas1.get(i).mBookID,
									downLoadPath, mHandler,
									BookMagazineActivity.this, 1, i);
						//}
					}
				}
			}
			break;
		case 2:
			for (int i = 0; i < mDatas2.size(); i++) {
				for (int j = 0; j < MyApplication.downList.size(); j++) {
					if (mDatas2.get(i).equals(MyApplication.downList.get(j))) {
						MyApplication.downList.get(j).isServiceDown = false;
						mDatas2.get(i).mDownloadCount = (mDatas2.get(i).mDownloadCount >= MyApplication.downList.get(j).mDownloadCount) ? mDatas2.get(i).mDownloadCount : MyApplication.downList.get(j).mDownloadCount;
						//if(mDatas2.get(i).mState != 1) {
							mAdapter1.notifyItemChanged(i);
							if(mDatas2.get(i).mDownloadCount > 0 && mDatas2.get(i).mDownloadCount == mDatas2.get(i).mBookSize)
								setProgressData(2, i, 100, mDatas2.get(i).mBookID);
							String downLoadPath = Constants.DOWNLOAD_URL
									+ mDatas2.get(i).mBookurl;
							startDownBook(mDatas2.get(i).mBookID,
									downLoadPath, mHandler,
									BookMagazineActivity.this, 2, i);
						//}
					}
				}
			}
			break;
		case 3:
			for (int i = 0; i < mDatas3.size(); i++) {
				for (int j = 0; j < MyApplication.downList.size(); j++) {
					if (mDatas3.get(i).equals(MyApplication.downList.get(j))) {
						MyApplication.downList.get(j).isServiceDown = false;
						mDatas3.get(i).mDownloadCount = (mDatas3.get(i).mDownloadCount >= MyApplication.downList.get(j).mDownloadCount) ? mDatas3.get(i).mDownloadCount : MyApplication.downList.get(j).mDownloadCount;
						//if(mDatas3.get(i).mState != 1) {
							mAdapter1.notifyItemChanged(i);
							if(mDatas3.get(i).mDownloadCount > 0 && mDatas3.get(i).mDownloadCount == mDatas3.get(i).mBookSize)
								setProgressData(3, i, 100, mDatas3.get(i).mBookID);
							String downLoadPath = Constants.DOWNLOAD_URL
									+ mDatas3.get(i).mBookurl;
							startDownBook(mDatas3.get(i).mBookID,
									downLoadPath, mHandler,
									BookMagazineActivity.this, 3, i);
						//}
					}
				}
			}
			break;
		case 4:
			for (int i = 0; i < mDatas4.size(); i++) {
				for (int j = 0; j < MyApplication.downList.size(); j++) {
					if (mDatas4.get(i).equals(MyApplication.downList.get(j))) {
						MyApplication.downList.get(j).isServiceDown = false;
						mDatas4.get(i).mDownloadCount = (mDatas4.get(i).mDownloadCount >= MyApplication.downList.get(j).mDownloadCount) ? mDatas4.get(i).mDownloadCount : MyApplication.downList.get(j).mDownloadCount;
						//if(mDatas4.get(i).mState != 1) {
							mAdapter1.notifyItemChanged(i);
							if(mDatas4.get(i).mDownloadCount > 0 && mDatas4.get(i).mDownloadCount == mDatas4.get(i).mBookSize)
								setProgressData(4, i, 100, mDatas4.get(i).mBookID);
							String downLoadPath = Constants.DOWNLOAD_URL
									+ mDatas4.get(i).mBookurl;
							startDownBook(mDatas4.get(i).mBookID,
									downLoadPath, mHandler,
									BookMagazineActivity.this, 4, i);
						//}
					}
				}
			}
			break;
		case 5:
			for (int i = 0; i < mDatas5.size(); i++) {
				for (int j = 0; j < MyApplication.downList.size(); j++) {
					if (mDatas5.get(i).equals(MyApplication.downList.get(j))) {
						MyApplication.downList.get(j).isServiceDown = false;
						mDatas5.get(i).mDownloadCount = (mDatas5.get(i).mDownloadCount >= MyApplication.downList.get(j).mDownloadCount) ? mDatas5.get(i).mDownloadCount : MyApplication.downList.get(j).mDownloadCount;
						//if(mDatas5.get(i).mState != 1) {
							mAdapter1.notifyItemChanged(i);
							if(mDatas5.get(i).mDownloadCount > 0 && mDatas5.get(i).mDownloadCount == mDatas5.get(i).mBookSize)
								setProgressData(5, i, 100, mDatas5.get(i).mBookID);
							String downLoadPath = Constants.DOWNLOAD_URL
									+ mDatas5.get(i).mBookurl;
							startDownBook(mDatas5.get(i).mBookID,
									downLoadPath, mHandler,
									BookMagazineActivity.this, 5, i);
						//}
					}
				}
			}
			break;
		}
		
	}
	
	public void UnZipFolder(String zipFileString, String outPathString)
			throws Exception {
		android.util.Log.v("XZip", "UnZipFolder(String, String)");
		java.util.zip.ZipInputStream inZip = new java.util.zip.ZipInputStream(
				new java.io.FileInputStream(zipFileString));
		java.util.zip.ZipEntry zipEntry;
		String szName = "";
		File file1 = new File(outPathString);
		if (!file1.exists()) {
			file1.mkdirs();
		}
		while ((zipEntry = inZip.getNextEntry()) != null) {
			szName = zipEntry.getName();

			if (zipEntry.isDirectory()) {

				// get the folder name of the widget
				szName = szName.substring(0, szName.length() - 1);
				java.io.File folder = new java.io.File(outPathString
						+ java.io.File.separator + szName);
				folder.mkdirs();

			} else {

				java.io.File file = new java.io.File(outPathString
						+ java.io.File.separator + szName);
				file.createNewFile();
				// get the output stream of the file
				java.io.FileOutputStream out = new java.io.FileOutputStream(
						file);
				int len;
				byte[] buffer = new byte[1024];
				// read (len) bytes into buffer
				while ((len = inZip.read(buffer)) != -1) {
					// write (len) byte from buffer at the position 0
//					if(isDownOver) {
//						break;
//					}
					out.write(buffer, 0, len);
					out.flush();
				}
				out.close();
			}
		}// end of while

		inZip.close();

	}
	
	private int clickRunBehavior;
	private int clickRunBehaviorItem1 = 1;
	private int clickRunBehaviorItem2;
	private float xx = 0f;
	private float xx2 = 0f;
	private float yy2 = 0f;
	private boolean isRight = false;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			if(!isRight) {
				isRight = true;
				clickRunBehaviorItem2 = 0;
			}
			
			mRecyclerView1.scrollToPosition(clickRunBehaviorItem2++);
			mAdapter1.notifyDataSetChanged();
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			if(clickRunBehaviorItem2 == 0) {
				isRight = false;
			} else {
				mRecyclerView1.scrollToPosition(clickRunBehaviorItem2--);
				mAdapter1.notifyDataSetChanged();
			}
			
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if(!isRight) {
				clickRunBehaviorItem2 = 0;
				if(clickRunBehaviorItem1 > 0) {
					clickRunBehaviorItem1--;
				} else {
					clickRunBehaviorItem1 = 5;
				}
				showBehavior();
			}
				
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			if(!isRight) {
				clickRunBehaviorItem2 = 0;
				if(clickRunBehaviorItem1 < 5) {
					clickRunBehaviorItem1++;
				} else {
					clickRunBehaviorItem1 = 0;
				}
				showBehavior();
			}
				
		} else if(keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_A || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			if(!isRight && clickRunBehaviorItem1 == 0) {
				finish();
			}
//			jump();
//			
			if(isRight) {
				int[] location = new int[2];  
				mRecyclerView1.getLocationOnScreen(location);  
	            int x = location[0];  
	            int y = location[1]; 
	            
	            Log.e("czb", x + "  " + y);
	            
	            try{
	            	xx2 = mRecyclerView1.getChildAt(clickRunBehaviorItem2).getLeft();
	            	yy2 = mRecyclerView1.getChildAt(clickRunBehaviorItem2).getTop();
	            } catch(Exception e) {
	            	
	            }
	            
	            Log.e("czb", xx2 + "  " + yy2);
	            
	            try {   
	                Runtime.getRuntime().exec("input tap " + (xx2 + x + 40) + " " + (yy2 + y + 40) + "\n");   
	            } catch (IOException e) {   
	                // TODO Auto-generated catch block   
	                e.printStackTrace();   
	            } 
	            
			}
				
			
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	private void showBehaviorLine1() {
		switch(clickRunBehavior) {
		case 1:
			break;
		case 2:
			if(clickRunBehaviorItem1 > 1) {
				mRecyclerView1.scrollToPosition(clickRunBehaviorItem1-2);
				mAdapter1.notifyDataSetChanged();
				
				clickRunBehaviorItem1 --;
			} else {
				clickRunBehavior = 1;
				mAdapter1.notifyDataSetChanged();
			}
			break;
		case 3:
//			if(clickRunBehaviorItem2 > 1) {
//				mRecyclerView2.scrollToPosition(clickRunBehaviorItem2-2);
//				mAdapter2.notifyDataSetChanged();
//				
//				clickRunBehaviorItem2 --;
//			} else {
//				clickRunBehavior = 2;
//				clickRunBehaviorItem1 = 2;
//				mAdapter1.notifyDataSetChanged();
//				mAdapter2.notifyDataSetChanged();
//			}
			
			break;
		case 4:
			break;
		}
	}
	
	private void showBehaviorLine2() {
		switch(clickRunBehavior) {
		case 1:
			break;
		case 2:
//			if(clickRunBehaviorItem1 < mDatas1.size()) {
//				mRecyclerView1.scrollToPosition(clickRunBehaviorItem1);
//				mAdapter1.notifyDataSetChanged();
//				
//				clickRunBehaviorItem1++;
//			} else {
//				clickRunBehavior = 3;
//				clickRunBehaviorItem2 = 0;
//				
//				mAdapter1.notifyDataSetChanged();
//				mAdapter2.notifyDataSetChanged();
//				showBehaviorLine2();
//			}
			break;
		case 3:
//			if(clickRunBehaviorItem2 < mDatas2.size()) {
//				mRecyclerView2.scrollToPosition(clickRunBehaviorItem2);
//				mAdapter2.notifyDataSetChanged();
//				
//				clickRunBehaviorItem2++;
//			} else {
//				clickRunBehavior = 4;
//				
//				
//				mAdapter2.notifyDataSetChanged();
//			}
			
			break;
		case 4:
			break;
		}
	}
	
	private void showBehavior() {
		mBackImg.setImageResource(R.drawable.baby_back);
		switch (clickRunBehaviorItem1) {
		case 0:
			mBackImg.setImageResource(R.drawable.baby_back2);
			break;
		case 1:
			clearData();
			saveState();
			selectItem = 1;
			changeItemBg();
			getCata();
			break;
		case 2:
			clearData();
			saveState();
			selectItem = 2;
			changeItemBg();
			getCata();
			break;
		case 3:
			clearData();
			saveState();
			selectItem = 3;
			changeItemBg();
			getCata();
			break;
		case 4:
			clearData();
			saveState();
			selectItem = 4;
			changeItemBg();
			getCata();
			break;
		case 5:
			clearData();
			saveState();
			selectItem = 5;
			isBaby = true;
			changeItemBg();
			if(!WifiUtils.isNetWorkActive(BookMagazineActivity.this,"网络连接超时,请重新连接")){
			} else {
				isBuyCategory("221");
			}
				
			break;
		default:
			break;
		}
	}
	
	private void jump() {
		Intent intent;
		switch(clickRunBehavior) {
		case 1:
			finish();
			break;
		case 2:
			
			break;
		case 3:
			break;
		case 4:
			break;
		}
	}
}
