package com.horner.xsm.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
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
import com.horner.xsm.data.UserCache;
import com.horner.xsm.service.DownloadService;
import com.horner.xsm.utils.AliPayUtils;
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

/**
 * 书架界面
 */
public class BookshelfActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener {

	private RecyclerView mRecyclerView1, mRecyclerView2;
	private GalleryAdapter mAdapter1, mAdapter2;
	private List<Book> mDatas1 = new ArrayList<Book>();
	private List<Book> mDatas2 = new ArrayList<Book>();
	private ImageView imageTitle, imgJingling;
	// private RelativeLayout containerId;
	// private LinearLayout layout1, layout2, layout3, layout4, layout5;

	// private RequestQueue requestQueue = null;

	private ImageView mBackImg, mQiKanImg, imageT1, imageT2;
	private AnimationDrawable anim1, anim2;
	private static Context mContext;
	private MyApplication myapp;
	private String category;
	private int progress_data = 0;
	private String cuId = "";
	private boolean isMove = false;

	public static String localbookbasepath = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/xsm/book/";
	private UserCache userCache;
	private boolean needGetData = false;
	private boolean isDownOver = false;

	private SparseArray<String> ZipedId = new SparseArray<String>();

	private SharedPreferences spLocal;
	private SharedPreferences.Editor editor;
	private LinearLayout layoutLine2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = BookshelfActivity.this;
		userCache = new UserCache(mContext);
		isDownOver = false;
		spLocal = getSharedPreferences("downBookName",
				Activity.MODE_WORLD_WRITEABLE);
		editor = spLocal.edit();
	}

	@Override
	protected void onStart() {
		imageT1 = (ImageView) findViewById(R.id.imageT1);
		anim1 = (AnimationDrawable) imageT1.getBackground();

		imageT2 = (ImageView) findViewById(R.id.imageT2);
		anim2 = (AnimationDrawable) imageT2.getBackground();
		anim1.start();
		anim2.start();
		super.onStart();
	}

	@Override
	public void findView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

		if ((heightPixels / widthPixels) < 0.58) {
			setContentView(R.layout.activity_bookself00);
		} else {
			setContentView(R.layout.activity_bookself11);
		}

		// requestQueue = Volley.newRequestQueue(this);
		mBackImg = (ImageView) findViewById(R.id.id_Leftback_img);
		mQiKanImg = (ImageView) findViewById(R.id.id_RightQiKan_img);

		rootView = findViewById(R.id.containerId);
		// rootView.setBackgroundDrawable(getBg(R.drawable.bs_bg1));
		rootView.setBackgroundDrawable(getBg(R.drawable.bs_bg1));

		mRecyclerView1 = (RecyclerView) findViewById(R.id.id_recyclerview_horizontal1);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mRecyclerView1.setLayoutManager(linearLayoutManager);
		mAdapter1 = new GalleryAdapter(this, 1);
		mRecyclerView1.setAdapter(mAdapter1);

		mRecyclerView2 = (RecyclerView) findViewById(R.id.id_recyclerview_horizontal2);
		linearLayoutManager2 = new LinearLayoutManager(this);
		linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
		mRecyclerView2.setLayoutManager(linearLayoutManager2);
		mAdapter2 = new GalleryAdapter(this, 2);
		mRecyclerView2.setAdapter(mAdapter2);
		imageTitle = (ImageView) findViewById(R.id.imageTitle);
		imgJingling = (ImageView) findViewById(R.id.imgJingling);

		layoutLine2 = (LinearLayout) findViewById(R.id.layoutLine2);

		// mRecyclerView3 = (RecyclerView)
		// findViewById(R.id.id_recyclerview_horizontal3);
		// LinearLayoutManager linearLayoutManager3 = new
		// LinearLayoutManager(this);
		// linearLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
		// mRecyclerView3.setLayoutManager(linearLayoutManager3);
		// mAdapter3 = new GalleryAdapter(this, 3);
		// mRecyclerView3.setAdapter(mAdapter3);

		// containerId = (RelativeLayout) findViewById(R.id.containerId);
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

		// layout1 = (LinearLayout) findViewById(R.id.layout1);
		// layout2 = (LinearLayout) findViewById(R.id.layout2);
		// layout3 = (LinearLayout) findViewById(R.id.layout3);
		// layout4 = (LinearLayout) findViewById(R.id.layout4);
		// layout5 = (LinearLayout) findViewById(R.id.layout5);
	}

	private IWXAPI api;

	@Override
	public void initData() {
		category = getIntent().getStringExtra("category");
		xin = getIntent().getStringExtra("xin");
		if (category.equals("217")) {
			imageTitle.setImageDrawable(getBg(R.drawable.bs_qzyd_a1));
			imgJingling.setImageDrawable(getBg(R.drawable.bs_qzyd_a2));
		} else if (category.equals("218")) {
			imageTitle.setImageDrawable(getBg(R.drawable.bs_bbax_a1));
			imgJingling.setImageDrawable(getBg(R.drawable.bs_bbax_a2));
		} else if (category.equals("219")) {
			imageTitle.setImageDrawable(getBg(R.drawable.bs_bbat_a1));
			imgJingling.setImageDrawable(getBg(R.drawable.bs_bbat_a2));
		} else if (category.equals("220")) {
			imageTitle.setImageDrawable(getBg(R.drawable.bs_bbak_a1));
			imgJingling.setImageDrawable(getBg(R.drawable.bs_bbak_a2));
		} else if (category.equals("221")) {
			imageTitle.setImageDrawable(getBg(R.drawable.bs_yxxj_a1));
			imgJingling.setImageDrawable(getBg(R.drawable.bs_yxxj_a2));
		}
		if (null != bitmap) {
			// bitmap.recycle();
			bitmap = null;
		}
		myapp = (MyApplication) getApplicationContext();
		getCata();
		getImieStatus();
		getLocalIpAddress();
		// 向微信注册小树苗的appId
		api = WXAPIFactory.createWXAPI(this, null);
		api.registerApp(MyApplication.WEIXIN_APPID);
	}

	Bitmap bitmap = null;

	private BitmapDrawable getBg(int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();

		opt.inPreferredConfig = Bitmap.Config.ALPHA_8;
		// opt.inSampleSize = 6;
		opt.inPurgeable = true;

		opt.inInputShareable = true;

		// 获取资源图片

		InputStream is = getResources().openRawResource(resId);

		bitmap = BitmapFactory.decodeStream(is, null, opt);

		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new BitmapDrawable(getResources(), bitmap);
	}

	@Override
	public void addClickListener() {
		mBackImg.setOnClickListener(this);
		mQiKanImg.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.id_Leftback_img:
			if (null != xin && !"".equals(xin)) {
				Intent intent = new Intent(this, HomeActivity.class);
				startActivity(intent);
			} else {
				// 返回键
				finish();
			}

			break;
		case R.id.id_RightQiKan_img:
			// 跳转书架
			Intent intent = new Intent(this, BookMagazineActivity.class);
			intent.putExtra("category", category);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void getCata() {
		if (!WifiUtils.isNetWorkActive(this, "请检查网络是否可用!")) {
			Intent it = new Intent(this, LoginActivity.class);
			startActivity(it);
			finish();
		}
		LoadingDialog.isLoading(BookshelfActivity.this);
		AsyncTask<String, String, String> loadCataTask = new AsyncTask<String, String, String>() {
			@Override
			protected String doInBackground(String... params) {
				ArrayList<Book> bookTopList = BookDataManager
						.getBookListByTop(category);
				mDatas1.clear();
				if (bookTopList != null && mDatas1 != null)
					mDatas1.addAll(bookTopList);

				ArrayList<Book> bookVipList = BookDataManager
						.getBookListByCatagoryVipType(category,
								userCache.getUserId());
				mDatas2.clear();

				// mDatas3.clear();
				if (bookVipList != null)
					for (int i = 0; i < bookVipList.size(); i++) {
						if (!mDatas1.contains(bookVipList.get(i)))
							// if (i % 2 == 0) {
							mDatas2.add(bookVipList.get(i));
						// } else {
						// mDatas3.add(bookVipList.get(i));
						// }
					}

				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				List<Book> datas1Tmp = readListToLocal(category + "1");
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

				List<Book> datas2Tmp = readListToLocal(category + "2");
				Log.e("czb", (mDatas2 == null) + "   000   "
						+ (datas2Tmp == null));
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

				checkDownState();
				mAdapter1.notifyDataSetChanged();
				LoadingDialog.finishLoading();
			}
		};
		loadCataTask.execute("");
	}

	@Override
	protected void onResume() {
		// if(ZipedId != null)
		// ZipedId.clear();
		if (needGetData) {
			getCata();
			needGetData = false;
		}
		isDownOver = false;

		List<Book> datas1Tmp = readListToLocal(category + "1");
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

		List<Book> datas2Tmp = readListToLocal(category + "2");
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

		checkDownState();
		mAdapter1.notifyDataSetChanged();
		mAdapter2.notifyDataSetChanged();

		super.onResume();
	}

	private DisplayImageOptions options = null;

	class GalleryAdapter extends
			RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

		private LayoutInflater mInflater;
		private int adapterId;

		// private RequestQueue requestQueue = null;
		// private ImageLoader imageLoader = null;

		public GalleryAdapter(Context context, int id) {
			adapterId = id;
			mInflater = LayoutInflater.from(context);
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.moreinfo_photo)
					.showImageForEmptyUri(R.drawable.moreinfo_photo)
					.showImageOnFail(R.drawable.moreinfo_photo)
					.cacheInMemory(true).cacheOnDisc(true)
					.considerExifParams(true)
					.displayer(new RoundedBitmapDisplayer(20)).build();
		}

		class ViewHolder extends RecyclerView.ViewHolder {
			public ViewHolder(View arg0) {
				super(arg0);
			}

			ImageView mBigImg, mFlagImg, mDownImg, mRestartImg, mPauseImg,
					mZipImg, mQueueImg;
			ProgressBar mLoadBar;
			RelativeLayout layout_item;
			FrameLayout layout_item_all;
			// TextView mTxt;
		}

		@Override
		public int getItemCount() {

			switch (adapterId) {
			case 1:
				return mDatas1.size();
			case 2:
				return mDatas2.size();
				// case 3:
				// return mDatas3.size();
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
			// if (screenType == $1920x1080) {
			view = mInflater.inflate(R.layout.activity_bookself_item,
					viewGroup, false);
			// } else {
			// view = mInflater.inflate(R.layout.activity_bookself_item1,
			// viewGroup, false);
			// }
			ViewHolder viewHolder = new ViewHolder(view);

			viewHolder.layout_item_all = (FrameLayout) view
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
			viewHolder.mPauseImg = (ImageView) view
					.findViewById(R.id.id_pause_image);
			viewHolder.mZipImg = (ImageView) view
					.findViewById(R.id.id_zip_image);
			viewHolder.mQueueImg = (ImageView) view
					.findViewById(R.id.id_queue_image);

			int size = viewGroup.getHeight() * 4 / 5;
			LayoutParams restartParams = new LayoutParams(size, size);
			// restartParams.addRule(FrameLayout.CENTER_HORIZONTAL);
			restartParams.gravity = Gravity.CENTER;
			viewHolder.mBigImg.setLayoutParams(restartParams);
			viewHolder.mRestartImg.setLayoutParams(restartParams);
			viewHolder.mZipImg.setLayoutParams(restartParams);
			viewHolder.mPauseImg.setLayoutParams(restartParams);
			viewHolder.mQueueImg.setLayoutParams(restartParams);
			viewHolder.mLoadBar = new ProgressBar(BookshelfActivity.this, null,
					android.R.attr.progressBarStyleHorizontal);
			RelativeLayout.LayoutParams loadParams = new RelativeLayout.LayoutParams(
					size - 10, ScreenAdapter.calcWidth(15));
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
		// float price = 0;
		// public RequestQueue requestQueue = null;;
		//
		public ImageLoader imageLoader;
		Book book;

		@Override
		public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
			// requestQueue = Volley.newRequestQueue(BookshelfActivity.this);
			// imageLoader = new ImageLoader(requestQueue,
			// BitmapCache.getInstance());

			imageLoader = ImageLoader.getInstance();

			switch (adapterId) {
			case 1:
				book = mDatas1.get(i);
				break;
			case 2:
				book = mDatas2.get(i);
				break;
			// case 3:
			// book = mDatas3.get(i);
			// break;
			default:
				book = mDatas1.get(i);
			}

			// Log.i("info","书本的名字"+book.mName+"连接为;"+book.mCoverurl);
			if (book != null) {
				price = Float.parseFloat(book.mPrice);
				if (price > 0.0) {
					// viewHolder.mFlagImg.setBackgroundResource();
					viewHolder.mFlagImg.setBackgroundResource(R.drawable.vip);
				} else {
					viewHolder.mFlagImg.setBackgroundResource(R.drawable.free);
				}

				viewHolder.layout_item_all
						.setBackgroundColor(Color.TRANSPARENT);
				
				
				switch (adapterId) {
				case 1:
					if(clickRunBehaviorItem1 - 1 == i && clickRunBehavior == 2) {
						viewHolder.layout_item_all.setBackgroundColor(Color.YELLOW);
					}
					break;
				case 2:
					if(clickRunBehaviorItem2 - 1 == i && clickRunBehavior == 3) {
						viewHolder.layout_item_all.setBackgroundColor(Color.YELLOW);
					}
					break;
				}
//				if (turnStyle.equals("horizontal")) {
//					switch (adapterId) {
//					
//					case 1:
//						if(clickRunBehaviorItem1 - 1 == i && clickRunBehavior == 3) {
//							viewHolder.layout_item_all.setBackgroundColor(Color.YELLOW);
//						}
//						break;
//					case 2:
//						if(clickRunBehaviorItem2 - 1 == i && clickRunBehavior == 4) {
//							viewHolder.layout_item_all.setBackgroundColor(Color.YELLOW);
//						}
//						break;
//					}
//				}else if(turnStyle.equals("vertical")){
//					switch (adapterId) {
//					
//					case 1:
//						if(clickRunBehaviorItem1 - 1 == i && clickRunBehavior == 2) {
//							viewHolder.layout_item_all.setBackgroundColor(Color.YELLOW);
//						}
//						break;
//					case 2:
//						if(clickRunBehaviorItem2 - 1 == i && clickRunBehavior == 3) {
//							viewHolder.layout_item_all.setBackgroundColor(Color.YELLOW);
//						}
//						break;
//					}
//				}
//				

				String coverurl = book.mCoverurl;
				if (!StringUtils.isEmpty(coverurl)) {

					// ImageListener listener = ImageLoader.getImageListener(
					// viewHolder.mBigImg, R.drawable.moreinfo_photo,
					// R.drawable.moreinfo_photo);
					//
					//
					// imageLoader.get(Constants.DOWNLOAD_URL + coverurl,
					// listener, 250, 250);
					imageLoader.displayImage(Constants.DOWNLOAD_URL + coverurl,
							viewHolder.mBigImg, options,
							new SimpleImageLoadingListener() {
								@Override
								public void onLoadingStarted(String imageUri,
										View view) {

								}

							}, new ImageLoadingProgressListener() {
								@Override
								public void onProgressUpdate(String imageUri,
										View view, int current, int total) {

								}
							});

				} else {
					viewHolder.mBigImg
							.setImageResource(R.drawable.moreinfo_photo);
				}

				// final String localPath = DataBase.getLocalPath(
				// BookshelfActivity.this, book.mBookID);
				// final String localPath = spLocal.getString(book.mBookID, "");
				final String localPath = localbookbasepath + book.mBookID
						+ "/book";
				File f = null;
				if (localPath != null) {
					f = new File(localPath);
				}

				// Log.e("czb", "state " + adapterId + " " + i + " " +
				// book.mState);

				File f1 = new File(localPath + "/book.dat");
				File f2 = new File(localPath + "/book.xml");
				File f3 = new File(localPath + "/hash.dat");

				if (localPath != null && f1.exists() && f2.exists()
						&& f3.exists()) {
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
					viewHolder.mZipImg.setVisibility(View.GONE);
					viewHolder.mQueueImg.setVisibility(View.GONE);
					// }
				} else if (book.mState == 3) {
					viewHolder.mRestartImg.setVisibility(View.VISIBLE);
					viewHolder.mPauseImg.setVisibility(View.GONE);
					viewHolder.mLoadBar.setVisibility(View.VISIBLE);
					viewHolder.mLoadBar.setProgress(book.mDownloadProgress);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mZipImg.setVisibility(View.GONE);
					viewHolder.mQueueImg.setVisibility(View.GONE);
					// }
				} else if (book.mState == 2) {
					viewHolder.mLoadBar.setVisibility(View.GONE);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mPauseImg.setVisibility(View.GONE);
					viewHolder.mRestartImg.setVisibility(View.GONE);
					viewHolder.mLoadBar.setVisibility(View.GONE);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mZipImg.setVisibility(View.GONE);
					viewHolder.mQueueImg.setVisibility(View.GONE);
					// }
				} else if (book.mState == 5) {
					viewHolder.mLoadBar.setVisibility(View.GONE);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mPauseImg.setVisibility(View.GONE);
					viewHolder.mRestartImg.setVisibility(View.GONE);
					viewHolder.mLoadBar.setVisibility(View.GONE);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mZipImg.setVisibility(View.VISIBLE);
					viewHolder.mQueueImg.setVisibility(View.GONE);
					// }
				} else if (book.mState == 6) {
					viewHolder.mLoadBar.setVisibility(View.GONE);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mPauseImg.setVisibility(View.GONE);
					viewHolder.mRestartImg.setVisibility(View.GONE);
					viewHolder.mLoadBar.setVisibility(View.GONE);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mZipImg.setVisibility(View.GONE);
					viewHolder.mQueueImg.setVisibility(View.GONE);
					// }
				} else if (book.mState == 4) {
					viewHolder.mRestartImg.setVisibility(View.GONE);
					viewHolder.mPauseImg.setVisibility(View.GONE);
					viewHolder.mLoadBar.setVisibility(View.GONE);
					// viewHolder.mLoadBar.setProgress(book.mDownloadProgress);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mZipImg.setVisibility(View.GONE);
					viewHolder.mQueueImg.setVisibility(View.VISIBLE);
					// }
				} else {
					viewHolder.mLoadBar.setVisibility(View.GONE);
					viewHolder.mDownImg.setVisibility(View.GONE);
					viewHolder.mPauseImg.setVisibility(View.GONE);
					viewHolder.mRestartImg.setVisibility(View.GONE);
					viewHolder.mLoadBar.setVisibility(View.GONE);
					viewHolder.mDownImg.setVisibility(View.VISIBLE);
					viewHolder.mZipImg.setVisibility(View.GONE);
					viewHolder.mQueueImg.setVisibility(View.GONE);
				}
				final Book bookItem = book;
				// 下载图书
				viewHolder.mBigImg.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						switch (adapterId) {
						case 1:
							book = mDatas1.get(i);
							break;
						case 2:
							book = mDatas2.get(i);
							break;
						// case 3:
						// book = mDatas3.get(i);
						// break;
						default:
							book = mDatas1.get(i);
						}
						price = Float.parseFloat(book.mPrice);
						if (price == 0.0) { // 免费书籍直接可以下载查看
							Log.e("czb", " localPath " + localPath
									+ " book.mState " + book.mState);
							File f1 = new File(localPath + "/book.dat");
							File f2 = new File(localPath + "/book.xml");
							File f3 = new File(localPath + "/hash.dat");

							if (localPath != null && f1.exists() && f2.exists()
									&& f3.exists()) {
								File file = new File(localPath);
								if (file.exists()) {
									if (category.equals("219")) {
										Intent intent = new Intent(
												BookshelfActivity.this,
												DetailBabyFavSingActivity.class);
										intent.putExtra("path", localPath);
										intent.putExtra("name", book.mName);
										startActivity(intent);
									} else if (category.equals("217")) {
										needGetData = true;
										HLReader.show(BookshelfActivity.this,
												localPath, true);
									} else {
										needGetData = true;
										HLReader.show(BookshelfActivity.this,
												localPath, false);
									}
									book.mState = 2;
									return;
								} else {
									// DataBase.deleteSearchRecord(
									// BookshelfActivity.this,
									// bookItem.mBookID);
									editor.putString(bookItem.mBookID, "");
									editor.commit();
								}

							}

							Log.e("czb",
									"MyApplication.getInstance().getDownList().size() "
											+ MyApplication.downList.size());
							if (MyApplication.downList.size() >= 2) {

								Toast.makeText(mContext, "已加入下载队列",
										Toast.LENGTH_SHORT).show();
								for (int i = 0; i < ZipedId.size(); i++) {
									if (ZipedId.get(i).equals(bookItem.mBookID)) {
										ZipedId.remove(i);
									}
								}
								switch (adapterId) {
								case 1:
									mDatas1.get(i).mState = 4;
									MyApplication.getInstance().addDownList(
											mDatas1.get(i));
									notifyItemChanged(i);
									break;
								case 2:
									mDatas2.get(i).mState = 4;
									MyApplication.getInstance().addDownList(
											mDatas2.get(i));
									notifyItemChanged(i);
									break;
								}

							} else if (bookItem.mState != 1) {
								Toast.makeText(mContext, "正在下载...",
										Toast.LENGTH_SHORT).show();

								for (int i = 0; i < ZipedId.size(); i++) {
									if (ZipedId.get(i).equals(bookItem.mBookID)) {
										ZipedId.remove(i);
									}
								}

								String downLoadPath = "";
								// String isPay = DataUtils.getPreference(
								// BookshelfActivity.this, "userType", "");
								downLoadPath = Constants.DOWNLOAD_URL
										+ bookItem.mBookurl;
								editor.putString(bookItem.mBookID,
										localbookbasepath + bookItem.mBookID
												+ "/book");
								editor.commit();
								switch (adapterId) {
								case 1:
									mDatas1.get(i).mDownloadCount = 0;
									MyApplication.getInstance().addDownList(
											mDatas1.get(i));
									startDownBook(bookItem.mBookID,
											downLoadPath, mHandler,
											BookshelfActivity.this, 1, i);
									notifyItemChanged(i);
									break;
								case 2:
									mDatas2.get(i).mDownloadCount = 0;
									MyApplication.getInstance().addDownList(
											mDatas2.get(i));
									startDownBook(bookItem.mBookID,
											downLoadPath, mHandler,
											BookshelfActivity.this, 2, i);
									notifyItemChanged(i);
									break;
								}

								// if (!DataBase.checkIsExist(
								// BookshelfActivity.this,
								// bookItem.mBookID)) {
								// DataBase.insertSearchRecord(
								// BookshelfActivity.this, bookItem,
								// localbookbasepath + bookItem.mBookID +
								// "/book");
								// }
								editor.putString(bookItem.mBookID,
										localbookbasepath + bookItem.mBookID
												+ "/book");
								editor.commit();

							} else if (bookItem.mState == 1) {
								viewHolder.mRestartImg
										.setVisibility(View.VISIBLE);
								switch (adapterId) {
								case 1:
									mDatas1.get(i).mState = 3;
									break;
								case 2:
									mDatas2.get(i).mState = 3;
									break;
								// case 3:
								// mDatas3.get(i).mState = 3;
								// break;
								}
							}
						} else {
							if (userCache.getVipType().equals("0")) { // 不是会员，则购买,否则直接下载查看
								buyBook(); // 购买之后，才能进行查看
							}

							if (!userCache.getVipType().equals("0")) { // 如果购买之后则可以免费下载
								// String localPath = DataBase.getLocalPath(
								// BookshelfActivity.this,
								// bookItem.mBookID);

								String localPath = spLocal.getString(
										bookItem.mBookID, "");
								File f1 = new File(localPath + "/book.dat");
								File f2 = new File(localPath + "/book.xml");
								File f3 = new File(localPath + "/hash.dat");
								if (localPath != null && f1.exists()
										&& f2.exists() && f3.exists()) {
									File f = new File(localPath);

									if (f.exists()) {
										if (category.equals("219")) {
											Intent intent = new Intent(
													BookshelfActivity.this,
													DetailBabyFavSingActivity.class);
											intent.putExtra("path", localPath);
											intent.putExtra("name", book.mName);
											startActivity(intent);
										} else if (category.equals("217")) {
											needGetData = true;
											HLReader.show(
													BookshelfActivity.this,
													localPath, true);
										} else {
											needGetData = true;
											HLReader.show(
													BookshelfActivity.this,
													localPath, false);
										}
										book.mState = 2;
										return;
									} else {
										// DataBase.deleteSearchRecord(
										// BookshelfActivity.this,
										// bookItem.mBookID);

										editor.putString(bookItem.mBookID, "");
										editor.commit();
									}

								}

								Log.e("czb",
										"MyApplication.getInstance().getDownList().size() "
												+ MyApplication.downList.size());
								if (MyApplication.downList.size() >= 2) {

									Toast.makeText(mContext, "已加入下载队列",
											Toast.LENGTH_SHORT).show();
									for (int i = 0; i < ZipedId.size(); i++) {
										if (ZipedId.get(i).equals(
												bookItem.mBookID)) {
											ZipedId.remove(i);
										}
									}
									switch (adapterId) {
									case 1:
										mDatas1.get(i).mState = 4;
										MyApplication.getInstance()
												.addDownList(mDatas1.get(i));
										notifyItemChanged(i);
										break;
									case 2:
										mDatas2.get(i).mState = 4;
										MyApplication.getInstance()
												.addDownList(mDatas2.get(i));
										notifyItemChanged(i);
										break;
									}

								} else if (bookItem.mState != 1) {
									Toast.makeText(mContext, "正在下载...",
											Toast.LENGTH_SHORT).show();
									for (int i = 0; i < ZipedId.size(); i++) {
										if (ZipedId.get(i).equals(
												bookItem.mBookID)) {
											ZipedId.remove(i);
										}
									}
									String downLoadPath = "";
									// String isPay = DataUtils.getPreference(
									// BookshelfActivity.this, "userType", "");
									downLoadPath = Constants.DOWNLOAD_URL
											+ bookItem.mBookurl;
									switch (adapterId) {
									case 1:
										MyApplication.getInstance()
												.addDownList(mDatas1.get(i));
										startDownBook(bookItem.mBookID,
												downLoadPath, mHandler,
												BookshelfActivity.this, 1, i);
										break;
									case 2:
										MyApplication.getInstance()
												.addDownList(mDatas2.get(i));
										startDownBook(bookItem.mBookID,
												downLoadPath, mHandler,
												BookshelfActivity.this, 2, i);
										break;
									}

									// if (!DataBase.checkIsExist(
									// BookshelfActivity.this,
									// bookItem.mBookID)) {
									// DataBase.insertSearchRecord(
									// BookshelfActivity.this,
									// bookItem, localbookbasepath
									// + bookItem.mBookID
									// + "/book");
									// }

									editor.putString(bookItem.mBookID,
											localbookbasepath
													+ bookItem.mBookID
													+ "/book");
									editor.commit();
								} else if (bookItem.mState == 1) {
									viewHolder.mRestartImg
											.setVisibility(View.VISIBLE);
									switch (adapterId) {
									case 1:
										mDatas1.get(i).mState = 3;
										break;
									case 2:
										mDatas2.get(i).mState = 3;
										break;
									// case 3:
									// mDatas3.get(i).mState = 3;
									// break;
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
								if (MyApplication.downList.size() >= 2) {

									Toast.makeText(mContext, "已加入下载队列",
											Toast.LENGTH_SHORT).show();
									switch (adapterId) {
									case 1:
										mDatas1.get(i).mState = 4;
										MyApplication.getInstance()
												.addDownList(mDatas1.get(i));
										notifyItemChanged(i);
										break;
									case 2:
										mDatas2.get(i).mState = 4;
										MyApplication.getInstance()
												.addDownList(mDatas2.get(i));
										notifyItemChanged(i);
										break;
									}

								} else {
									viewHolder.mRestartImg
											.setVisibility(View.GONE);
									viewHolder.mPauseImg
											.setVisibility(View.VISIBLE);
									Toast.makeText(mContext, "正在下载...",
											Toast.LENGTH_SHORT).show();

									String downLoadPath = Constants.DOWNLOAD_URL
											+ bookItem.mBookurl;
									switch (adapterId) {
									case 1:
										MyApplication.getInstance()
												.addDownList(mDatas1.get(i));
										startDownBook(bookItem.mBookID,
												downLoadPath, mHandler,
												BookshelfActivity.this, 1, i);
										break;
									case 2:
										MyApplication.getInstance()
												.addDownList(mDatas2.get(i));
										startDownBook(bookItem.mBookID,
												downLoadPath, mHandler,
												BookshelfActivity.this, 2, i);
										break;
									}
								}

							}

						});

				viewHolder.mPauseImg.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						viewHolder.mRestartImg.setVisibility(View.VISIBLE);
						viewHolder.mPauseImg.setVisibility(View.GONE);
						switch (adapterId) {
						case 1:
							MyApplication.getInstance().overDownListByBookId(
									mDatas1.get(i).mBookID);
							mDatas1.get(i).mState = 3;
							break;
						case 2:
							MyApplication.getInstance().overDownListByBookId(
									mDatas2.get(i).mBookID);
							mDatas2.get(i).mState = 3;
							break;
						// case 3:
						// mDatas3.get(i).mState = 1;
						// startDownBook(bookItem.mBookID,
						// downLoadPath, mHandler,
						// BookshelfActivity.this, 3, i);
						// break;
						}

						checkDownState();

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
							MyApplication.getInstance().overDownList(
									mDatas1.get(i));
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
							MyApplication.getInstance().overDownList(
									mDatas2.get(i));
							checkDownState();
						}
					}
					mAdapter2.notifyDataSetChanged();
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
				// DataBase.deleteSearchRecord(ac, bij);
				// freshenData();
				break;
			case 250250250:
				for (Book bk1 : mDatas1) {
					if (bk1.mState == 1) {
						MyApplication.getInstance().addDownList(bk1);
						for (int j = 0; j < MyApplication.downList.size(); j++) {
							if (bk1.equals(MyApplication.downList.get(j))) {
								MyApplication.downList.get(j).mDownloadCount = bk1.mDownloadCount;
								MyApplication.downList.get(j).isServiceDown = true;
								Intent intent = new Intent(
										BookshelfActivity.this,
										DownloadService.class);
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
							if (bk2.equals(MyApplication.downList.get(j))) {
								MyApplication.downList.get(j).mDownloadCount = bk2.mDownloadCount;
								MyApplication.downList.get(j).isServiceDown = true;
								Intent intent = new Intent(
										BookshelfActivity.this,
										DownloadService.class);
								intent.putExtra("serviceDownBook", bk2);
								intent.putExtra("command", 1);
								startService(intent);
								bk2.mState = 3;
							}
						}

					}
				}

				for (Book bk : mDatas1) {
					if (bk.mState == 1) {
						bk.mState = 3;
					}
				}
				for (Book bk : mDatas2) {
					if (bk.mState == 1) {
						bk.mState = 3;
					}
				}

				saveListToLocal(category + "1", mDatas1);
				saveListToLocal(category + "2", mDatas2);
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
		if (errorDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(str + "，下载出错，请重新下载！")
					.setCancelable(false)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
								}
							});
			errorDialog = builder.create();
		}

		if (!errorDialog.isShowing()) {
			errorDialog.show();
		}
	}

	/**
	 * 
	 * @author 作者 : sun
	 * @date 创建时间：2016-1-5 下午3:19:04
	 * @return
	 * @description 获得详情书籍的集合
	 */

	// public void getDetailsBook(final Book book, final int index) {
	//
	// AsyncTask<String, String, String> loadCataTask = new AsyncTask<String,
	// String, String>() {
	// @Override
	// protected String doInBackground(String... params) {
	// String mBookId = book.mBookID;
	// Book detailBook = null;
	// ArrayList<Book> bookDetailList = BookDataManager
	// .getBookListByDetail(userId, mBookId);
	// Log.i("info", bookDetailList + "大小");
	//
	// if (bookDetailList != null) {
	// detailBook = bookDetailList.get(index);
	// Message msg = Message.obtain();
	// msg.obj = detailBook.mNowPrice;
	// bookHandler.sendMessage(msg);
	//
	// }
	// return null;
	// }
	//
	// };
	// loadCataTask.execute("");
	//
	// }

	// Handler bookHandler = new Handler() {
	// public void handleMessage(Message msg) {
	// Book detailBook = (Book) msg.obj;
	// if (detailBook != null) {
	// float price = Float.parseFloat(detailBook.mNowPrice);
	// if (price > 0.0) {
	// viewHolder.mFlagImg.setBackgroundResource(R.drawable.vip1);
	// }
	// }
	// };
	// };

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
			if (mDatas1.get(adapterItem).mState == 1) {
				return;
			} else {
				mDatas1.get(adapterItem).mState = 1;
			}
			break;
		case 2:
			if (mDatas2.get(adapterItem).mState == 1) {
				return;
			} else {
				mDatas2.get(adapterItem).mState = 1;
			}
			break;
		}
		String tempPath = "";
		Log.d("czb", "____" + downUrl);
		// TODO Auto-generated method stub
		// String isPay = DataUtils.getPreference(ac, "userType", "");
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
								// TODO Auto-generated method stub
								Message msg = Message.obtain();
								msg.obj = bookId;
								msg.arg1 = adapterP;
								msg.what = Constants.OVER_DOWN;
								try {

									if (ZipedId.indexOfValue(bookId) == -1) {
										// while(true) {
										// if(zipingCount < 2) {
										// zipingCount++;
										// break;
										// }
										// Thread.sleep(2000);
										// }

										ZipedId.append(ZipedId.size(), bookId);
										// if(ZipedId.indexOfValue(bookId) ==
										// -1) {
										Log.e("czb", "isZiping " + bookId);
										Log.e("czb", "11111");
										UnZipFolder(zipPath, localbookbasepath
												+ bookId);
										Log.e("czb", "22222");
										// }

										// zipingCount--;
										switch (adapterP) {
										case 1:
											mDatas1.get(adapterItem).mState = 2;
											break;
										case 2:
											mDatas2.get(adapterItem).mState = 2;
											break;
										}
										mHandler.sendMessage(msg);
									}

								} catch (Exception e) {
									String name = "";
									switch (adapterP) {
									case 1:
										mDatas1.get(adapterItem).mState = 0;
										mDatas1.get(adapterItem).mDownloadCount = 0;
										mDatas1.get(adapterItem).mBookSize = 0;
										name = mDatas1.get(adapterItem).mName;
										break;
									case 2:
										mDatas2.get(adapterItem).mState = 0;
										mDatas2.get(adapterItem).mDownloadCount = 0;
										mDatas2.get(adapterItem).mBookSize = 0;
										name = mDatas2.get(adapterItem).mName;
										break;
									}

									Bundle bundle = new Bundle();
									bundle.putString("bookName", name);
									Message msg2 = Message.obtain();
									msg2.what = 180180180;
									msg2.setData(bundle);
									mHandler.sendMessage(msg2);
									// Toast.makeText(BookshelfActivity.this,
									// "下载出错，请重新下载！",
									// Toast.LENGTH_SHORT).show();
									File f = new File(localbookbasepath
											+ bookId);
									DataCleanManager.deleteFolderFile(
											f.getAbsolutePath(), true);
									Log.e("czb", "This is ZipUtil Exception : "
											+ f.getAbsolutePath()
											+ " been delete", e);

								} finally {
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
			// case 3:
			// connection.setRequestProperty("range",
			// "bytes=" + mDatas3.get(adapterItem).mDownloadCount
			// + "-");
			// out.seek(mDatas3.get(adapterItem).mDownloadCount);
			// break;
			}

			Log.e("czb", connection.getResponseCode() + "  ff");
			if (connection.getResponseCode() == 416) {
				return progressCall.downOver(0);
			}
			// 如果网络地址上存在这个文件，直接下载，如果不存在，返回false，下载失败
			if (connection.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {
				in = connection.getInputStream();
				int contentLength = connection.getContentLength();
				Log.i("czb", "申请下载数据资源大小是" + contentLength);
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
				// case 3:
				// if (mDatas3.get(adapterItem).mBookSize == 0) {
				// mDatas3.get(adapterItem).mBookSize = contentLength;
				// }
				// break;
				}

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
					// case 3:
					// mDatas3.get(adapterItem).mDownloadCount += read;
					// boolean b3 = progressCall.doProgressAction(
					// mDatas3.get(adapterItem).mBookSize,
					// mDatas3.get(adapterItem).mDownloadCount);
					// if (mDatas3.get(adapterItem).mState == 3) {
					// return false;
					// }
					// break;
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
			mDatas1.get(adapterItem).mDownloadProgress = i;
			if (mAdapter1 != null)
				mAdapter1.notifyItemChanged(adapterItem);
			break;
		case 2:
			mDatas2.get(adapterItem).mDownloadProgress = i;
			if (mAdapter2 != null)
				mAdapter2.notifyItemChanged(adapterItem);
			break;
		// case 3:
		// mDatas3.get(adapterItem).mDownloadProgress = i;
		// if (mAdapter3 != null)
		// mAdapter3.notifyDataSetChanged();
		// break;
		default:
			break;
		}

		// if(!isMove) {
		// DataBase.reSetProcess(id, i,
		// BookshelfActivity.this);
		// }

	}

	private void getPayViewSize(View view, RelativeLayout.LayoutParams params) {
		int width = getResources().getDisplayMetrics().widthPixels;
		int height = getResources().getDisplayMetrics().heightPixels;
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

	private void buyBook() {
		int width = getResources().getDisplayMetrics().widthPixels;
		int height = getResources().getDisplayMetrics().heightPixels;
		payView = getLayoutInflater().inflate(R.layout.buy_book, null);

		View containerId = findViewById(R.id.containerId);
		containerId.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (null != payView) {
						payView.setVisibility(View.GONE);
					}

					break;

				default:
					break;
				}
				return false;
			}
		});
		// android.view.ViewGroup.LayoutParams params =
		// payView.getLayoutParams();
		// params.height = height/10*8;
		// params.width = width/7*3;
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				width / 7 * 4, height / 10 * 8);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		payView.setLayoutParams(params);
		RelativeLayout conLayout = (RelativeLayout) findViewById(R.id.containerId);

		relativeLayout = new RelativeLayout(this);
		relativeLayout.setBackgroundColor(Color.parseColor("#00000000"));
		RelativeLayout.LayoutParams paramsNew = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
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

		conLayout.addView(relativeLayout);

		userId = userCache.getUserId();

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
		getPrice(tvOne, tvTwo, tVthree, tVfour);
		final TextView closePayView = (TextView) payView
				.findViewById(R.id.close);

		closePayView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				payView.setVisibility(View.GONE);
			}
		});
		// 支付宝支付
		sure_buy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!one.isChecked() && !two.isChecked() && !three.isChecked()
						&& !four.isChecked()) {
					ToastUtil.showShortToast(BookshelfActivity.this,
							"您当前未勾选,请勾选!");
					return;
				}
				if (one.isChecked() && !two.isChecked() && !three.isChecked()
						&& !four.isChecked()) {
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++) {
							PriceBean priceBean = Constants.priceBeans.get(i);
							if (priceBean.getCycle().equals("1")) {
								moneyNum = priceBean.getPrice();
								// moneyNum = "0.01";
								purchaseTime = priceBean.getCycle();
								break;
							}
						}
					} else {
						purchaseTime = "1";
						moneyNum = "25";
					}

				}
				if (!one.isChecked() && two.isChecked() && !three.isChecked()
						&& !four.isChecked()) {
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++) {
							PriceBean priceBean = Constants.priceBeans.get(i);
							if (priceBean.getCycle().equals("3")) {
								moneyNum = priceBean.getPrice();
								purchaseTime = priceBean.getCycle();
								break;
							}
						}
					} else {
						purchaseTime = "3";
						moneyNum = "60";
					}

				}
				if (!one.isChecked() && !two.isChecked() && three.isChecked()
						&& !four.isChecked()) {
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++) {
							PriceBean priceBean = Constants.priceBeans.get(i);
							if (priceBean.getCycle().equals("6")) {
								moneyNum = priceBean.getPrice();
								purchaseTime = priceBean.getCycle();
								break;
							}
						}
					} else {
						purchaseTime = "6";
						moneyNum = "105";
					}

				}
				if (!one.isChecked() && !two.isChecked() && !three.isChecked()
						&& four.isChecked()) {
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++) {
							PriceBean priceBean = Constants.priceBeans.get(i);
							if (priceBean.getCycle().equals("12")) {
								moneyNum = priceBean.getPrice();
								purchaseTime = priceBean.getCycle();
								break;
							}
						}
					} else {
						purchaseTime = "12";
						moneyNum = "168";
					}

				}
				pay(moneyNum, purchaseTime);
			}
		});

		// 微信支付
		wxPay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ToastUtil.showShortToast(BookshelfActivity.this, "微信支付");
				if (!one.isChecked() && !two.isChecked() && !three.isChecked()
						&& !four.isChecked()) {
					ToastUtil.showShortToast(BookshelfActivity.this,
							"您当前未勾选,请勾选!");
					return;
				}
				if (one.isChecked() && !two.isChecked() && !three.isChecked()
						&& !four.isChecked()) {
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++) {
							PriceBean priceBean = Constants.priceBeans.get(i);
							if (priceBean.getCycle().equals("1")) {
								moneyNum = priceBean.getPrice();
								// moneyNum = "0.01";
								purchaseTime = priceBean.getCycle();
								break;
							}
						}
					} else {
						purchaseTime = "1";
						moneyNum = "25";
					}

				}
				if (!one.isChecked() && two.isChecked() && !three.isChecked()
						&& !four.isChecked()) {
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++) {
							PriceBean priceBean = Constants.priceBeans.get(i);
							if (priceBean.getCycle().equals("3")) {
								moneyNum = priceBean.getPrice();
								purchaseTime = priceBean.getCycle();
								break;
							}
						}
					} else {
						purchaseTime = "3";
						moneyNum = "60";
					}

				}
				if (!one.isChecked() && !two.isChecked() && three.isChecked()
						&& !four.isChecked()) {
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++) {
							PriceBean priceBean = Constants.priceBeans.get(i);
							if (priceBean.getCycle().equals("6")) {
								moneyNum = priceBean.getPrice();
								purchaseTime = priceBean.getCycle();
								break;
							}
						}
					} else {
						purchaseTime = "6";
						moneyNum = "105";
					}

				}
				if (!one.isChecked() && !two.isChecked() && !three.isChecked()
						&& four.isChecked()) {
					if (null != Constants.priceBeans) {
						for (int i = 0; i < Constants.priceBeans.size(); i++) {
							PriceBean priceBean = Constants.priceBeans.get(i);
							if (priceBean.getCycle().equals("12")) {
								moneyNum = priceBean.getPrice();
								purchaseTime = priceBean.getCycle();
								break;
							}
						}
					} else {
						purchaseTime = "12";
						moneyNum = "168";
					}

				}
				// LoadingDialog.isLoading(BookshelfActivity.this);
				WePayUtils.getPayInfo(wePayHandler, moneyNum,
						BookshelfActivity.this, WePay.class, purchaseTime);
				Constants.isVipOrMod = "vip";
			}
		});
	}

	Handler wePayHandler = new Handler() {
		public void handleMessage(Message msg) {
			// WePay wePay = new WePay();
			switch (msg.what) {
			case 0:
				LoadingDialog.finishLoading();
				WePay wePay = (WePay) msg.obj;
				Log.i("info", "Wepay的字段为" + wePay.toString());
				PayReq req = new PayReq();
				req.packageValue = "Sign=WXPay";
				req.sign = wePay.getSign();
				req.appId = wePay.getAppid();
				req.prepayId = wePay.getPrepayid();
				req.partnerId = wePay.getPartnerid();
				req.timeStamp = wePay.getTimestamp();
				req.nonceStr = wePay.getNoncestr();
				Constants.nonceStr = wePay.getNoncestr();
				api.registerApp(wePay.getAppid());
				api.sendReq(req);
				break;

			default:
				break;
			}
		};
	};

	private void getPrice(TextView one, TextView two, TextView three,
			TextView four) {
		if (null != Constants.priceBeans) {
			for (int i = 0; i < Constants.priceBeans.size(); i++) {
				PriceBean priceBean = Constants.priceBeans.get(i);
				if (priceBean.getCycle().equals("1")) {
					moneyNum = priceBean.getPrice();
					purchaseTime = priceBean.getCycle();
					String content = purchaseTime + "个月会员:" + moneyNum + "人民币";
					one.setText(content);
				} else if (priceBean.getCycle().equals("3")) {
					moneyNum = priceBean.getPrice();
					purchaseTime = priceBean.getCycle();
					String content = purchaseTime + "个月会员:" + moneyNum + "人民币";
					two.setText(content);
				} else if (priceBean.getCycle().equals("6")) {
					moneyNum = priceBean.getPrice();
					purchaseTime = priceBean.getCycle();
					String content = "半年会员:" + moneyNum + "人民币";
					three.setText(content);
				} else if (priceBean.getCycle().equals("12")) {
					moneyNum = priceBean.getPrice();
					purchaseTime = priceBean.getCycle();
					String content = "年费会员:" + moneyNum + "人民币";
					four.setText(content);
				}

			}
		}

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
			new AlertDialog.Builder(BookshelfActivity.this)
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

		if (!AliPayUtils.isAvilible(BookshelfActivity.this,
				"com.eg.android.AlipayGphone")) {
			Toast.makeText(BookshelfActivity.this, "该设备不存在支付宝终端，请安装",
					Toast.LENGTH_SHORT).show();
			return;
		}

		// 订单 allPrice + ""
		String orderInfo = getOrderInfo("商品的名字" + "", "该测试商品的详细描述", moneyNum
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
		if (MyApplication.isExistAccount) {
			Runnable payRunnable = new Runnable() {

				@Override
				public void run() {
					// 构造PayTask 对象
					PayTask alipay = new PayTask(BookshelfActivity.this);
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

	}

	public void checkAccount() {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(BookshelfActivity.this);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();
				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler2.sendMessage(msg);

			}
		};
		// 必须异步调用
		Thread payThread = new Thread(checkRunnable);
		payThread.start();
	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(BookshelfActivity.this);
		String version = payTask.getVersion();
		Toast.makeText(BookshelfActivity.this, version, Toast.LENGTH_SHORT)
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
					Toast.makeText(BookshelfActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					// 支付成功之后给服务器的反馈信息
					toBase_ZHU();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(BookshelfActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(BookshelfActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				if (!(Boolean) msg.obj) {
					Toast.makeText(BookshelfActivity.this, "该设备不存在支付宝终端，请安装",
							Toast.LENGTH_SHORT).show();
					MyApplication.isExistAccount = false;
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
	private TextView tvOne;
	private TextView tvTwo;
	private TextView tVthree;
	private TextView tVfour;
	private RelativeLayout relativeLayout;
	private ImageButton wxPay;
	private String xin;
	private View rootView;

	/**
	 * 
	 * @author 作者 : SUN
	 * @date 创建时间：2015-12-31 下午8:17:04
	 * @return
	 * @description 支付成功之后给服务器的欣欣
	 */
	public void toBase_ZHU() {
		LoadingDialog.isLoading(BookshelfActivity.this);
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
						JSONObject object = new JSONObject(result);
						String code = object.getString("code");
						String vipType = object.getString("vipType");
						if (code != null && code.equals("1000")) {
							ToastUtil.showLongToast(mContext, "购买成功!");
							UserCache userCache = new UserCache(
									BookshelfActivity.this);
							MyApplication.hasBuy = true; // 购买vip
							userCache.setVipType(vipType);
							if (payView != null) {
								payView.setVisibility(View.GONE);
							}

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
							.isConnectingToInternet(BookshelfActivity.this);
					if (internet) {
						Toast.makeText(BookshelfActivity.this, "充值失败,请重试!",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(BookshelfActivity.this,
								"登录失败,请检查网络是否可用!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
		loadDataTask.execute("");
	}

	@Override
	protected void onStop() {
		anim1.stop();
		anim2.stop();
		anim1 = null;
		anim2 = null;
		super.onStop();
	}

	@Override
	protected void onPause() {
		isDownOver = true;
		Message msg = Message.obtain();
		msg.what = 250250250;
		mHandler.sendMessageDelayed(msg, 2000);

		super.onPause();
	}

	private void checkDownState() {
		for (int i = 0; i < mDatas1.size(); i++) {
			for (int j = 0; j < MyApplication.downList.size(); j++) {
				if (mDatas1.get(i).equals(MyApplication.downList.get(j))) {
					Log.e("czb", mDatas1.get(i).mState + "   aaa");
					MyApplication.downList.get(j).isServiceDown = false;
					mDatas1.get(i).mDownloadCount = (mDatas1.get(i).mDownloadCount >= MyApplication.downList
							.get(j).mDownloadCount) ? mDatas1.get(i).mDownloadCount
							: MyApplication.downList.get(j).mDownloadCount;
					// if(mDatas1.get(i).mState != 1) {
					mAdapter1.notifyItemChanged(i);
					if (mDatas1.get(i).mDownloadCount > 0
							&& mDatas1.get(i).mDownloadCount == mDatas1.get(i).mBookSize)
						setProgressData(1, i, 100, mDatas1.get(i).mBookID);
					String downLoadPath = Constants.DOWNLOAD_URL
							+ mDatas1.get(i).mBookurl;
					startDownBook(mDatas1.get(i).mBookID, downLoadPath,
							mHandler, BookshelfActivity.this, 1, i);
					// }
				}
			}
		}

		for (int i = 0; i < mDatas2.size(); i++) {
			for (int j = 0; j < MyApplication.downList.size(); j++) {
				if (mDatas2.get(i).equals(MyApplication.downList.get(j))) {
					MyApplication.downList.get(j).isServiceDown = false;
					mDatas2.get(i).mDownloadCount = (mDatas2.get(i).mDownloadCount >= MyApplication.downList
							.get(j).mDownloadCount) ? mDatas2.get(i).mDownloadCount
							: MyApplication.downList.get(j).mDownloadCount;
					// if(mDatas2.get(i).mState != 1) {
					mAdapter2.notifyItemChanged(i);
					if (mDatas2.get(i).mDownloadCount > 0
							&& mDatas2.get(i).mDownloadCount == mDatas2.get(i).mBookSize)
						setProgressData(2, i, 100, mDatas2.get(i).mBookID);
					String downLoadPath = Constants.DOWNLOAD_URL
							+ mDatas2.get(i).mBookurl;
					startDownBook(mDatas2.get(i).mBookID, downLoadPath,
							mHandler, BookshelfActivity.this, 2, i);
					// }
				}
			}
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
					// if(isDownOver) {
					// break;
					// }
					out.write(buffer, 0, len);
					out.flush();
				}
				out.close();
			}
		}// end of while

		inZip.close();

	}
	private int clickRunBehavior;
	private int clickRunBehaviorItem1;
	private int clickRunBehaviorItem2;
	private float xx = 0f;
	private float xx2 = 0f;
	int xScrool;
	private LinearLayoutManager linearLayoutManager2;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
		if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			switch(clickRunBehavior) {
			case 0:
			case 1:
				clickRunBehavior ++;
				break;
			case 2:
			case 3:
				showBehaviorLine2();
				break;
			case 4:
				clickRunBehavior = 1;
				break;
			}
			showBehavior();
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			switch(clickRunBehavior) {
			case 0:
			case 1:
				clickRunBehavior = 4;
				break;
			case 2:
			case 3:
				showBehaviorLine1();
				break;
			case 4:
				clickRunBehavior --;
				showBehaviorLine1();
				break;
			}
			showBehavior();
		} else if(keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_A || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			jump();
			
			xScrool = 0;
			if(clickRunBehaviorItem2 > 0 && clickRunBehavior == 3) {
				int[] location = new int[2];  
				mRecyclerView2.getLocationOnScreen(location);  
	            int x = location[0];  
	            int y = location[1]; 
	            
	            //ToastUtil.showLongToast(BookshelfActivity.this,"x和y"+x+y);
	            float mLast = 1f;
	            try{
	            	//Log.i("info","当前x的位置为:"+(clickRunBehaviorItem2 - 1));
	            	//linearLayoutManager2
	            	xx = linearLayoutManager2.findViewByPosition(clickRunBehaviorItem2 - 1).getLeft();
//	            	mLast = xx;
//	            	if (xScrool != 0 && xScrool >0) {
//						xx = xx + mLast;
//					}
//		            // //recyclerView向右滑动
//		            if (xScrool != 0 && xScrool < 0) {
//		            	xx = xx - mLast;
//					}
	            	// ToastUtil.showLongToast(BookshelfActivity.this,"双x的数值为:"+xx+"item2的数值为:"+clickRunBehaviorItem2);
	            } catch(Exception e) {
	            	ToastUtil.showShortToast(getApplicationContext(),"获取当前位置异常");
	            	//recyclerView向左滑动
	            }
	            
	            try {   
	                Runtime.getRuntime().exec("input tap " + (xx + x +21) + " " + (y + 20) + "\n");   
	            } catch (IOException e) {   
	                // TODO Auto-generated catch block   
	                e.printStackTrace();   
	            } 
			} else if(clickRunBehaviorItem1 > 0 && clickRunBehavior == 2) {
				int[] location = new int[2];  
				mRecyclerView1.getLocationOnScreen(location);  
	            int x = location[0];  
	            int y = location[1]; 
	            
	            try{
	            	xx2 = mRecyclerView1.getChildAt(clickRunBehaviorItem1 - 1).getLeft();
	            } catch(Exception e) {
	            	
	            }
	            
	            try {   
	                Runtime.getRuntime().exec("input tap " + (xx2 + x + 20) + " " + (y + 20) + "\n");   
	            } catch (IOException e) {   
	                // TODO Auto-generated catch block   
	                e.printStackTrace();   
	            } 
	            
			}
				
			
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			switch(clickRunBehavior) {
			case 0:
			case 1:
				clickRunBehavior = 4;
				break;
			case 2:
				//clickRunBehavior = 1;
				//break;
			case 3:
				clickRunBehavior--;
				showBehaviorLine1();
				//clickRunBehavior--;
				break;
			case 4:
				clickRunBehavior --;
				//showBehaviorLine2();
				break;
			}
			showBehavior();
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			switch(clickRunBehavior) {
			case 0:
			case 1:
				clickRunBehavior++;
				break;
			case 2:
				//clickRunBehavior++;
				//showBehaviorLine1();
				//break;
			case 3:
				showBehaviorLine2();
				clickRunBehavior++;
				break;
			case 4:
				clickRunBehavior = 1;
				break;
			}
			showBehavior();
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
				mAdapter2.notifyDataSetChanged();
				//ToastUtil.showShortToast(BookshelfActivity.this,"111"+clickRunBehavior);
				clickRunBehaviorItem1 --;
			} else {
				clickRunBehavior = 1;
				mAdapter1.notifyDataSetChanged();
				//ToastUtil.showShortToast(BookshelfActivity.this,"222"+clickRunBehavior);
			}
			break;
		case 3:
			if(clickRunBehaviorItem2 > 1) {
				mRecyclerView2.scrollToPosition(clickRunBehaviorItem2);
				mAdapter2.notifyDataSetChanged();
				clickRunBehaviorItem2--;
				//clickRunBehaviorItem2 -= 2;
				//ToastUtil.showShortToast(BookshelfActivity.this,"333"+clickRunBehaviorItem2);
			} else {
				//clickRunBehaviorItem2 = 0;
				clickRunBehavior = 2;
				clickRunBehaviorItem1 = 2;
				mAdapter1.notifyDataSetChanged();
				mAdapter2.notifyDataSetChanged();
				//ToastUtil.showShortToast(BookshelfActivity.this,"444"+clickRunBehavior);
			}
			
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
			if(clickRunBehaviorItem1 < mDatas1.size()) {
				mRecyclerView1.scrollToPosition(clickRunBehaviorItem1);
				mAdapter1.notifyDataSetChanged();
				mAdapter2.notifyDataSetChanged();
				clickRunBehaviorItem1++;
				//ToastUtil.showShortToast(BookshelfActivity.this,"555"+clickRunBehaviorItem1);
			} else {
				clickRunBehavior = 3;
				clickRunBehaviorItem2 = 0;
				
				mAdapter1.notifyDataSetChanged();
				mAdapter2.notifyDataSetChanged();
				showBehaviorLine2();
				//ToastUtil.showShortToast(BookshelfActivity.this,"666"+clickRunBehaviorItem1);
			}
			break;
		case 3:
			if(clickRunBehaviorItem2 < mDatas2.size()) {
				mRecyclerView2.scrollToPosition(clickRunBehaviorItem2);
				mAdapter2.notifyDataSetChanged();
				
				clickRunBehaviorItem2++;
				//ToastUtil.showShortToast(BookshelfActivity.this,"777"+clickRunBehaviorItem2);
			} else {
				clickRunBehavior = 4;
				
				
				mAdapter2.notifyDataSetChanged();
				//ToastUtil.showShortToast(BookshelfActivity.this,"888"+clickRunBehaviorItem2);
			}
			
			break;
		case 4:
			break;
		}
	}
	
	private void showBehavior() {
		mBackImg.setImageResource(R.drawable.back1);
		mQiKanImg.setImageResource(R.drawable.qk1);
		switch(clickRunBehavior) {
		case 1:
			mBackImg.setImageResource(R.drawable.back11);
			break;
		case 2:
			
			break;
		case 3:
			
			break;
		case 4:
			mQiKanImg.setImageResource(R.drawable.qk11);
			break;
		}
	}
	
	private void jump() {
		Intent intent;
		switch(clickRunBehavior) {
		case 1:
			if (null != xin && !"".equals(xin)) {
				intent = new Intent(this, HomeActivity.class);
				startActivity(intent);
			}else {
				// 返回键
				finish();
			}
			break;
		case 2:
			
			break;
		case 3:
			break;
		case 4:
			intent = new Intent(this, BookMagazineActivity.class);
			intent.putExtra("category", category);
			startActivity(intent);
			break;
		}
	}
	
}
