package com.horner.xsm.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.alipay.sdk.app.PayTask;
import com.hl.android.controller.BookController;
import com.horner.nread.framework.MyApplication;
import com.horner.xsm.R;
import com.horner.xsm.alipay.PayResult;
import com.horner.xsm.alipay.SignUtils;
import com.horner.xsm.base.BaseActivity;
import com.horner.xsm.bean.BookCategory;
import com.horner.xsm.bean.PriceBean;
import com.horner.xsm.bean.WePay;
import com.horner.xsm.constants.Constants;
import com.horner.xsm.data.BookDataManager;
import com.horner.xsm.data.DataBase;
import com.horner.xsm.data.ThirdVipUserCache;
import com.horner.xsm.data.UserCache;
import com.horner.xsm.net.AsyncHttpClient;
import com.horner.xsm.net.RequestParams;
import com.horner.xsm.utils.AliPayUtils;
import com.horner.xsm.utils.CalculateUtil;
import com.horner.xsm.utils.GetPriceUtils;
import com.horner.xsm.utils.LoadingDialog;
import com.horner.xsm.utils.SysEnvUtils;
import com.horner.xsm.utils.ToastUtil;
import com.horner.xsm.utils.WePayUtils;
import com.horner.xsm.utils.WifiUtils;
import com.mouee.common.HttpManager;
import com.mouee.common.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @author Administrator
 * 
 *         首页界面 登录完成进入
 * 
 */
public class HomeActivity extends BaseActivity implements OnClickListener {

	private LinearLayout mStudyImg, mStudyImg2, mSocialImg, mReadImg, mSingImg,
			mSeeImg, mSeeImg2, mChildLinkImg, layoutBg, mChildLinkImg2;
	private FrameLayout mSetting;
	private Context mContext;
	private UserCache cache;
	private String userId;
	private AsyncHttpClient client;
	private ImageView imgTou;
	private ImageView imga1, imga2, imga3, imga4, imga5, imga6;

	@Override
	public void findView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// TODO Auto-generated method stub

		setContentView(R.layout.activity_home2);
		mVideoView = (VideoView) findViewById(R.id.videoView1);
//		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//				RelativeLayout.LayoutParams.MATCH_PARENT);
//		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//		mVideoView.setLayoutParams(params);
		// 宝宝爱学
		mStudyImg = (LinearLayout) findViewById(R.id.layout_study);
		// 互动社区
		// mSocialImg = (LinearLayout) findViewById(R.id.layout5);
		// 亲子阅读
		// mReadImg = (LinearLayout) findViewById(R.id.layout_read);
		mRead = (LinearLayout) findViewById(R.id.layout_read_img);
		// 宝宝爱听
		mSingImg = (LinearLayout) findViewById(R.id.layout_hear);
		// 宝宝爱看
		mSeeImg = (LinearLayout) findViewById(R.id.layout_see);
		// 幼小衔接
		mChildLinkImg = (LinearLayout) findViewById(R.id.layout_baby);

		mSetting = (FrameLayout) findViewById(R.id.setting_layout);

		layoutBg = (LinearLayout) findViewById(R.id.layoutBg);
		img = (ImageView) findViewById(R.id.imgview);
		img.setVisibility(View.VISIBLE);

		imga1 = (ImageView) findViewById(R.id.imga1);
		imga2 = (ImageView) findViewById(R.id.imga2);
		imga3 = (ImageView) findViewById(R.id.imga3);
		imga4 = (ImageView) findViewById(R.id.imga4);
		imga5 = (ImageView) findViewById(R.id.imga5);
		imga6 = (ImageView) findViewById(R.id.imga6);

		imgTou = (ImageView) findViewById(R.id.imgTou);
		CalculateUtil.calculateViewSize(imgTou, 100, 100);
		thirdUserCache = new ThirdVipUserCache(this);
		UserCache cache = new UserCache(this);

		Log.i("info", "HomeActivity的数值:" + Constants.pos);
		totalMemory  = SysEnvUtils.getTotalMemory(this);
		abs = Math.abs(totalMemory);
		Log.i("info","toalMemory的大小为："+totalMemory);
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
		if (MyApplication.diagonal > 13) {
			layoutBg.setBackgroundResource(R.drawable.home_phone111222);
		}else {
			if (abs <1800 ) {
				if (screenType == $1920x1080) {
					Log.i("info","screenType的数值为:"+screenType);
					img.setBackgroundResource(R.drawable.home_phone111222);
				}else {
					img.setBackgroundResource(R.drawable.homebg);
				}
			}else {
				initHomeVedio();
				if (frameBp != null) {
					if (screenType == $1920x1080) {
						img.setImageBitmap(frameBp);
					} else {
						img.setImageBitmap(frameBp);
					}
				}
			}
			
		}
	}

	private IWXAPI api;

	@Override
	public void initData() {
		MyApplication.addActivity(this);
		DataBase.initDataBase(this);
		mContext = this;
		cache = new UserCache(mContext);
		userId = cache.getUserId();
		GetPriceUtils.getPrice(mContext, PriceBean.class, priceHandler);
		getCata();
		initAlertDialog();
		client = new AsyncHttpClient(HomeActivity.this);
		upData();
		initSetting();
		// 向微信注册小树苗的appId
		api = WXAPIFactory.createWXAPI(this, null);
		api.registerApp(MyApplication.WEIXIN_APPID);
		String xinban = getIntent().getStringExtra("xinban");
		if (null != xinban && !"".equals(xinban)) {
			builder.show();
		}
	}

	@Override
	protected void onStart() {

		multipartFile = cache.getMultipartFile();
		coverUrl = thirdUserCache.getMultipartFile();
		Log.i("info","coverUrl的数值为1111:"+coverUrl);
		Log.i("info","coverUrl的数值为2222:"+multipartFile);
		super.onStart();
		if (coverUrl == null || coverUrl.equals("")) {
			coverUrl = multipartFile;
		}
		if (coverUrl != null) {

			if (!coverUrl.contains("http")) {
				coverUrl = Constants.BASE_PIC + coverUrl;
			}
			ImageLoader.getInstance().loadImage(coverUrl.trim(),
					new ImageLoadingListener() {

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1,
								Bitmap arg2) {
							imgTou.setImageBitmap(toRoundBitmap(arg2));
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1,
								FailReason arg2) {
						}

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
						}

					});
		}
		// if (frameBp != null) {
		// if (screenType == $1920x1080) {
		// img.setImageBitmap(frameBp);
		// } else {
		// img.setImageBitmap(frameBp);
		// }
		// }
		// playVideo();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (MyApplication.diagonal > 13) {
			layoutBg.setBackgroundResource(R.drawable.home_phone111222);
		}else {
			if (abs <1800 ) {
				if (screenType == $1920x1080) {
					img.setBackgroundResource(R.drawable.home_phone111222);
				}else {
					img.setBackgroundResource(R.drawable.homebg);
				}
			}else {
				playVideo();
			}
			
		}
		
	}

	@Override
	protected void onRestart() {
		Log.i("info", "onRestart");
		img.setVisibility(View.VISIBLE);
		super.onRestart();
	}

	 public  Bitmap toRoundBitmap(Bitmap bitmap) {
	        int width = bitmap.getWidth();
	        int height = bitmap.getHeight();
	        float roundPx;
	        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
	        if (width <= height) {
	            roundPx = width / 2;
	            top = 0;
	            bottom = width;
	            left = 0;
	            right = width;
	            height = width;
	            dst_left = 0;
	            dst_top = 0;
	            dst_right = width;
	            dst_bottom = width;
	        } else {
	            roundPx = height / 2;
	            float clip = (width - height) / 2;
	            left = clip;
	            right = width - clip;
	            top = 0;
	            bottom = height;
	            width = height;
	            dst_left = 0;
	            dst_top = 0;
	            dst_right = height;
	            dst_bottom = height;
	        }
	        Bitmap output = Bitmap.createBitmap(width,
	                height, Bitmap.Config.ARGB_8888);
	        Canvas canvas = new Canvas(output);
	        final int color = 0xff424242;
	        final Paint paint = new Paint();
	        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
	        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
	        final RectF rectF = new RectF(dst);
	        paint.setAntiAlias(true);
	        canvas.drawARGB(0, 0, 0, 0);
	        paint.setColor(color);
	        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
	        canvas.drawBitmap(bitmap, src, dst, paint);
	        return output;
	    }


	// 设置界面
	private void initSetting() {
		mSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, SettingActivity.class);
				startActivity(intent);
				// turnAnimation();
				if (null != mVideoView) {
					mVideoView.stopPlayback();
				}
			}
		});
	}

	private void upData() {
		final RequestParams param = new RequestParams();
		param.put("shelfId", "3");
		client.post(Constants.BASE_URL
				+ "updateVersion/getVerSionInfor?officeId="
				+ Constants.OFFICEID, param,
				new com.horner.xsm.net.AsyncHttpResponseHandler() {

					private String version;
					private String url_down;

					@Override
					public void onSuccess(String result) {

						Log.e("version_result:", result);

						super.onSuccess(result);
						LoadingDialog.finishLoading();
						try {
							JSONObject jsonObject = new JSONObject(result);
							version = jsonObject.getString("version");
							int newVersion = Integer.parseInt(version);
							url_down = jsonObject.getString("url_down");
							final Spanned html = Html.fromHtml(url_down);
							Log.d("url_down:", "url_down=" + html);
							Log.d("version:", "version==" + version);
							String currentapiVersion = getVersion();
							currentapiVersion = currentapiVersion.replaceAll(
									"\\.", "");
							// 获取版本号
							int nowVersion = Integer
									.parseInt(currentapiVersion);
							if (newVersion > nowVersion) {
								AlertDialog.Builder builder = new AlertDialog.Builder(
										HomeActivity.this);
								builder.setTitle("版本更新")
										.setMessage("是否立即更新")
										.setCancelable(false)
										.setPositiveButton(
												"立即更新",
												new DialogInterface.OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														// TODO Auto-generated
														// method stub
														Intent intent = new Intent(
																HomeActivity.this,
																UpDataActivity.class);
														intent.putExtra(
																"result",
																html.toString());
														startActivity(intent);
													}
												})
										.setNegativeButton(
												"下一次",
												new DialogInterface.OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {

													}
												}).show();
							}
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}

					/**
					 * 获取版本号
					 * 
					 * @return 当前应用的版本号
					 */
					public String getVersion() {
						try {
							PackageManager manager = HomeActivity.this
									.getPackageManager();
							PackageInfo info = manager.getPackageInfo(
									HomeActivity.this.getPackageName(), 0);
							String version = info.versionName;
							return version;
						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						boolean connectingToInternet = HttpManager
								.isConnectingToInternet(HomeActivity.this);
						if (!connectingToInternet) {
							Toast.makeText(HomeActivity.this,
									"请求失败,请检查网络是否可用。", Toast.LENGTH_SHORT)
									.show();
						} else {
							Toast.makeText(HomeActivity.this, "版本检查失败，请重试。",
									Toast.LENGTH_SHORT).show();

						}
					}
				});

	}

	private void initAlertDialog() {

		builder = new AlertDialog.Builder(mContext);
		builder.setMessage("此模块需要购买才能进行观看，你是否要购买！")
				.setPositiveButton("支付宝支付",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// price = "0.01";
								if (price != null) {
									pay(price, "幼小衔接");
								}

							}
						})
				.setNeutralButton("暂不购买",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
							}
						})
				.setNegativeButton("微信支付",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// LoadingDialog.isLoading(HomeActivity.this);
								WePayUtils.getPayInfo(wePayHandler, price,
										HomeActivity.this, WePay.class, "幼小衔接");
								Constants.isVipOrMod = "mod";
							}
						});
	}

	Handler wePayHandler = new Handler() {
		public void handleMessage(Message msg) {
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

	private void getCata() {
		if (!WifiUtils.isNetWorkActive(this, "请检查网络是否可用!")) {
			Intent it = new Intent(this, LoginActivity.class);
			startActivity(it);
			finish();
		}
		LoadingDialog.isLoading(HomeActivity.this);
		AsyncTask<String, String, String> loadCataTask = new AsyncTask<String, String, String>() {
			@Override
			protected String doInBackground(String... params) {
				ArrayList<BookCategory> bookCates = BookDataManager
						.getCategoryList();
				category.clear();
				if (bookCates != null)
					for (BookCategory bookCategory : bookCates) {
						category.put(bookCategory.mCatagoryName,
								bookCategory.mCatagoryCode);
						Log.e("czb", bookCategory.mCatagoryName + " "
								+ bookCategory.mCatagoryCode);
					}
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				LoadingDialog.finishLoading();

				if (category.get("宝宝爱听") != null) {
					mSingImg.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							Intent intent = new Intent(HomeActivity.this,
									com.horner.xsm.ui.BookshelfActivity.class);
							intent.putExtra("category", category.get("宝宝爱听"));
							startActivity(intent);
							// turnAnimation();
						}
					});
				}

				if (category.get("宝宝爱学") != null) {
					mStudyImg.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							Intent intent = new Intent(HomeActivity.this,
									com.horner.xsm.ui.BookshelfActivity.class);
							intent.putExtra("category", category.get("宝宝爱学"));
							startActivity(intent);
							// turnAnimation();
						}
					});
				}
				if (category.get("互动社区") != null) {
					mSocialImg.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// Intent intent = new Intent(HomeActivity.this,
							// com.horner.xsm.ui.BookshelfActivity.class);
							// intent.putExtra("category",
							// category.get("互动社区"));
							// startActivity(intent);
							showDialog();
						}
					});
				}
				if (category.get("亲子悦读") != null) {
					mRead.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {

							Intent intent = new Intent(HomeActivity.this,
									com.horner.xsm.ui.BookshelfActivity.class);
							intent.putExtra("category", category.get("亲子悦读"));
							startActivity(intent);
							// turnAnimation();
							Log.d("debug",
									"亲子阅读的category书籍为:" + category.get("亲子悦读"));
						}
					});
				}
				if (category.get("宝宝爱看") != null) {
					mSeeImg.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							Intent intent = new Intent(HomeActivity.this,
									com.horner.xsm.ui.BookshelfActivity.class);
							intent.putExtra("category", category.get("宝宝爱看"));
							startActivity(intent);
							// turnAnimation();
						}
					});
				}
				if (category.get("幼小衔接") != null) {
					mChildLinkImg.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {

							String categoryId = category.get("幼小衔接");
							// 用户必须先购买才能到这个界面
							Log.i("info", "幼小衔接的id为:" + categoryId);
							if (!WifiUtils.isNetWorkActive(HomeActivity.this,
									"网络连接超时,请重新连接")) {
								return;
							}
							isBuyCategory(categoryId);
						}
					});
				}
			}
		};
		loadCataTask.execute("");
	}

	/*
	 * 对幼小衔接的分类进行购买，购买之前先判断是否已经购买
	 */

	private Boolean hasBuy = false;

	private void isBuyCategory(final String categoryId) {

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
						// Log.i("in)
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
							.isConnectingToInternet(HomeActivity.this);
					if (internet) {
						Toast.makeText(HomeActivity.this, "出现异常,请重试!",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(HomeActivity.this, "请检查网络是否可用!",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
		loadDataTask.execute("");
	}

	private void BuyCategory(final String categoryId) {

		AsyncTask<String, String, String> loadDataTask = new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("userId", userId);
				param.put("categoryId", categoryId);
				param.put("officeId", "40");
				param.put("transNum", getOutTradeNo());
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
						// Log.i("in)
						String code = object.optString("code");
						if (code != null && code.equals("1000")) {
							ToastUtil.showShortToast(HomeActivity.this,
									"用户购买成功");
							String isBuy = object.optString("isBuy");
							if (isBuy != null) {
								Log.i("info", "isBuy购买成功的数值为:" + isBuy);
								UserCache cache = new UserCache(
										HomeActivity.this);
								cache.setHasBuy(true);
							}
						} else if (code.equals("1001")) {
							ToastUtil.showShortToast(HomeActivity.this,
									"用户ID为空");
							return;
						} else if (code.equals("1002")) {
							ToastUtil.showShortToast(HomeActivity.this,
									"分类ID为空");
							return;
						} else if (code.equals("1003")) {
							ToastUtil.showShortToast(HomeActivity.this,
									"交易流水号为空");
							return;
						} else if (code.equals("1004")) {
							ToastUtil.showShortToast(HomeActivity.this,
									"联盟成员ID为空");
							return;
						} else if (code.equals("1005")) {
							ToastUtil.showShortToast(HomeActivity.this,
									"已经购买过了");
							return;
						} else if (code.equals("1006")) {
							ToastUtil.showShortToast(HomeActivity.this,
									"购买价格不能为空或者零");
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					boolean internet = HttpManager
							.isConnectingToInternet(HomeActivity.this);
					if (internet) {
						Toast.makeText(HomeActivity.this, "出现异常,请重试!",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(HomeActivity.this, "请检查网络是否可用!",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
		loadDataTask.execute("");
	}

	String price = null;
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Boolean hasBuy = (Boolean) msg.obj;
				if (hasBuy) {
					Intent intent = new Intent(HomeActivity.this,
							com.horner.xsm.ui.BookshelfActivity.class);
					intent.putExtra("category", category.get("幼小衔接"));
					startActivity(intent);
					// turnAnimation();
				} else {
					builder.show();
				}
				break;

			default:
				break;
			}
		}
	};

	Handler priceHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Constants.priceBeans = (List<PriceBean>) msg.obj;
				getModePrice();
				break;
			case 1:

				break;
			case 2:

				break;
			case 3:

				break;
			case 4:

				break;

			default:
				break;
			}
		};
	};

	private void getModePrice() {
		if (null != Constants.priceBeans) {
			for (int i = 0; i < Constants.priceBeans.size(); i++) {
				PriceBean priceBean = Constants.priceBeans.get(i);
				if (priceBean.getType().equals("mod")) {
					price = priceBean.getPrice();
					builder.setTitle("购买" + "价格为:" + price + "人民币");
					break;
				}
			}
		} else {
			price = "25";
		}

	}

	@Override
	public void addClickListener() {
		// TODO Auto-generated method stub
		// mStudyImg.setOnClickListener(this);
		// mSocialImg.setOnClickListener(this);
		// mReadImg.setOnClickListener(this);
		// mSingImg.setOnClickListener(this);
		// mSeeImg.setOnClickListener(this);
		// mChildLinkImg.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (null != mVideoView) {
			mVideoView.stopPlayback();
			// mVideoView = null;
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.id_home_likeLinkImg:
			// 幼小衔接

			break;
		case R.id.id_home_likeReadImg:
			// 亲子阅读

			break;
		case R.id.id_home_likeSeeImg:
			// 宝宝爱看

			break;
		case R.id.id_home_likeSingImg:
			// 宝宝爱听

			break;
		case R.id.id_home_likeSocialImg: // 互动社区
			// 互动社区

			break;
		case R.id.id_home_likeStudyImg:
			// 宝宝爱学

		case R.id.layout5:
			showDialog();
			break;
		default:
			break;
		}
	}

	public void showDialog() {
		Dialog dialog = new Dialog(this, R.style.MyCustomDialog);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();

		params.width = LayoutParams.WRAP_CONTENT;
		params.height = LayoutParams.WRAP_CONTENT;
		window.setAttributes(params);
		View view = LayoutInflater.from(this).inflate(R.layout.diaogview, null);
		dialog.setContentView(view);

		// dialog.getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
		// | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

		// 点击空白处让对话框消失
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(String moneyNum, String desc) {
		if (TextUtils.isEmpty(AliPayUtils.PARTNER)
				|| TextUtils.isEmpty(AliPayUtils.RSA_PRIVATE)
				|| TextUtils.isEmpty(AliPayUtils.SELLER)) {
			new AlertDialog.Builder(HomeActivity.this)
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

		if (!AliPayUtils.isAvilible(HomeActivity.this,
				"com.eg.android.AlipayGphone")) {
			Toast.makeText(HomeActivity.this, "该设备不存在支付宝终端，请安装",
					Toast.LENGTH_SHORT).show();
			return;
		}

		// 订单 allPrice + ""
		String orderInfo = getOrderInfo(desc + "", "该测试商品的详细描述", moneyNum + "");

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
					PayTask alipay = new PayTask(HomeActivity.this);
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
				PayTask payTask = new PayTask(HomeActivity.this);
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
		PayTask payTask = new PayTask(HomeActivity.this);
		String version = payTask.getVersion();
		Toast.makeText(HomeActivity.this, version, Toast.LENGTH_SHORT).show();
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

				Log.e("HomeActivity", resultStatus + "   " + resultInfo);

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(HomeActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					// 支付成功之后给服务器的反馈信息
					BuyCategory(category.get("幼小衔接"));
					// ;
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(HomeActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(HomeActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				if (!(Boolean) msg.obj) {
					Toast.makeText(HomeActivity.this, "该设备不存在支付宝终端，请安装",
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
	private AlertDialog.Builder builder;
	private LinearLayout mRead;
	private String coverUrl;
	private String multipartFile;
	private ThirdVipUserCache thirdUserCache;
	private VideoView mVideoView;

	private int clickRunBehavior;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//MyApplication.clearActivities();
			com.horner.xsm.utils.ExitUtil.Exit(mContext);

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			if (clickRunBehavior < 6)
				clickRunBehavior++;
			else {
				clickRunBehavior = 1;
			}
			showBehavior();
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			if (clickRunBehavior > 1)
				clickRunBehavior--;
			else {
				clickRunBehavior = 6;
			}
			showBehavior();
		} else if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER
				|| keyCode == KeyEvent.KEYCODE_A) {
			jump();
		}

		return super.onKeyDown(keyCode, event);
	}

	private void showBehavior() {
		imga1.setVisibility(View.GONE);
		imga2.setVisibility(View.GONE);
		imga3.setVisibility(View.GONE);
		imga4.setVisibility(View.GONE);
		imga5.setVisibility(View.GONE);
		imga6.setVisibility(View.GONE);
		switch (clickRunBehavior) {
		case 1:
			imga1.setVisibility(View.VISIBLE);
			break;
		case 2:
			imga2.setVisibility(View.VISIBLE);
			break;
		case 3:
			imga3.setVisibility(View.VISIBLE);
			break;
		case 4:
			imga4.setVisibility(View.VISIBLE);
			break;
		case 5:
			imga5.setVisibility(View.VISIBLE);
			break;
		case 6:
			imga6.setVisibility(View.VISIBLE);
			break;
		}
	}

	private void jump() {
		Intent intent;
		switch (clickRunBehavior) {
		case 1:
			intent = new Intent(mContext, SettingActivity.class);
			startActivity(intent);
			if (null != mVideoView) {
				mVideoView.stopPlayback();
			}
			break;
		case 2:
			intent = new Intent(HomeActivity.this,
					com.horner.xsm.ui.BookshelfActivity.class);
			intent.putExtra("category", category.get("宝宝爱学"));
			startActivity(intent);
			break;
		case 3:
			intent = new Intent(HomeActivity.this,
					com.horner.xsm.ui.BookshelfActivity.class);
			intent.putExtra("category", category.get("宝宝爱听"));
			startActivity(intent);
			break;
		case 4:
			intent = new Intent(HomeActivity.this,
					com.horner.xsm.ui.BookshelfActivity.class);
			intent.putExtra("category", category.get("宝宝爱看"));
			startActivity(intent);
			break;
		case 5:
			intent = new Intent(HomeActivity.this,
					com.horner.xsm.ui.BookshelfActivity.class);
			intent.putExtra("category", category.get("亲子悦读"));
			startActivity(intent);
			break;
		case 6:
			String categoryId = category.get("幼小衔接");
			// 用户必须先购买才能到这个界面
			Log.i("info", "幼小衔接的id为:" + categoryId);
			if (!WifiUtils.isNetWorkActive(HomeActivity.this, "网络连接超时,请重新连接")) {
				return;
			}
			isBuyCategory(categoryId);
			break;
		}
	}

	String homeVideoSmall = "home1920_1080.mp4";
	String homeVideoBig = "home2048_1536.mp4";
	private ImageView img;
	Bitmap frameBp;
	private long totalMemory ;
	private long abs;

	public void initHomeVedio() {
		if (screenType == $1920x1080) {
			if (!storeVideoFile(homeVideoSmall)) {
				Log.i("info", "程序在这返回了");
				return;
			}
			frameBp = createVideoThumbnail(getFilesDir().getAbsolutePath()
					+ File.separator + homeVideoSmall);
			Log.i("info", "initHomeVedio");
		} else {
			if (!storeVideoFile(homeVideoBig)) {
				return;
			}
			frameBp = createVideoThumbnail(getFilesDir().getAbsolutePath()
					+ File.separator + homeVideoBig);
			Log.i("info", "initHomeVedio1");
		}
	}

	public boolean storeVideoFile(String videoName) {
		try {
			InputStream is = getResources().getAssets().open(videoName);

			// 注意,这里用 MODE_WORLD_READABLE 是因为播放Video的是MediaPlayer进程,不是本进程
			// 为了让, MediaPlayer进程能读取此文件,所以设置为: MODE_WORLD_READABLE
			FileOutputStream os = openFileOutput(videoName, MODE_WORLD_READABLE);

			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			is.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void playVideo() {
		try {
			if (screenType == $1920x1080) {
				prepareVideo(homeVideoSmall);
				Log.i("info", "init1");
			} else {
				prepareVideo(homeVideoBig);
				Log.i("info", "init2");
			}
		} catch (OutOfMemoryError error) {
			Log.i("swt", "内存溢出了");
			if (null != mVideoView) {
				mVideoView.stopPlayback();
				mVideoView = null;
			}

			if (screenType == $1920x1080) {
				// img.setImageBitmap(frameBp);
			} else {
				// img.setImageBitmap(frameBp);
			}
			if (img != null) {
				// img.setVisibility(View.VISIBLE);
			}
		}

	}

	private void prepareVideo(final String name) {
		mVideoView.setVideoPath(getFilesDir().getAbsolutePath()
				+ File.separator + name);

		// Bitmap bitmap = createVideoThumbnail(getFilesDir().getAbsolutePath()
		// + File.separator + name);
		mVideoView.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				img.setVisibility(View.INVISIBLE);
				// frameBp = null;
				mp.start();
				mp.setLooping(true);
			}
		});
		mVideoView.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
			}
		});
		mVideoView.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				return true;
			}
		});
	}

	// 获取视频文件的典型帧作为封面
	public static Bitmap createVideoThumbnail(String filePath) {
		Bitmap bitmap = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(filePath);
			bitmap = retriever.getFrameAtTime(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException ex) {
			}
		}
		return bitmap;
	}

}
