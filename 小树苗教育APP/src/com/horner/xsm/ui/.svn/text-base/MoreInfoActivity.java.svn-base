package com.horner.xsm.ui;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.horner.nread.framework.MyApplication;
import com.horner.xsm.R;
import com.horner.xsm.base.BaseActivity;
import com.horner.xsm.constants.Constants;
import com.horner.xsm.data.ThirdVipUserCache;
import com.horner.xsm.data.UserCache;
import com.horner.xsm.utils.BitmapCache;
import com.horner.xsm.utils.NetUtils;
import com.horner.xsm.utils.ToastUtil;
import com.horner.xsm.utils.UriUtils;
import com.horner.xsm.utils.WifiUtils;
import com.horner.xsm.wheel.OnWheelChangedListener;
import com.horner.xsm.wheel.WheelView;
import com.horner.xsm.wheel.adapters.ArrayWheelAdapter;
import com.horner.xsm.widget.SegmentView;
import com.horner.xsm.widget.SegmentView.onSegmentViewClickListener;

public class MoreInfoActivity extends BaseActivity implements OnClickListener,
		OnWheelChangedListener, onSegmentViewClickListener {
	private static final String TAG = MoreInfoActivity.class.getSimpleName();
	private View titleView;
	private RelativeLayout leftButton;
	private TextView tvTitle;
	private TextView skip;
	private TextView saveInfo;
	private PopupWindow mPopupWindow;

	private ImageView uploadPhoto;
	private View popupView;
	private Button takePhotot, album, btnCancel;
	/**
	 * 有关日期的属性
	 */
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	private TextView showDate;

	private static final int SHOW_DATAPICK = 0;
	private static final int DATE_DIALOG_ID = 1;
	private static final int SHOW_TIMEPICK = 2;
	private static final int TIME_DIALOG_ID = 3;

	/**
	 * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
	 */
	private JSONObject mJsonObj;
	/**
	 * 省的WheelView控件
	 */
	private WheelView mProvince;
	/**
	 * 市的WheelView控件
	 */
	private WheelView mCity;
	/**
	 * 区的WheelView控件
	 */
	private WheelView mArea;

	/**
	 * 所有省
	 */
	private String[] mProvinceDatas;
	/**
	 * key - 省 value - 市s
	 */
	private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区s
	 */
	private Map<String, String[]> mAreaDatasMap = new HashMap<String, String[]>();

	/**
	 * 当前省的名称
	 */
	private String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	private String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	private String mCurrentAreaName = "";
	private String addressId;

	/**
	 * 省市区布局 以及显示省市区的布局
	 */
	View areaView;
	private TextView tvAv;

	@Override
	public void findView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏显示
		setContentView(R.layout.activity_moreinfo);
		titleView = findViewById(R.id.moreinfo_titleLayout);
		leftButton = (RelativeLayout) titleView.findViewById(R.id.left_btn);
		tvTitle = (TextView) titleView.findViewById(R.id.tv_title);
		
		
		
		RelativeLayout rootView = (RelativeLayout) findViewById(R.id.moreinfo_layout);
		rootView.setBackgroundDrawable(getBg(R.drawable.more_infobg));
		
		birth_arrow = (ImageView) findViewById(R.id.birth_arrow);
		city_arrow = (ImageView) findViewById(R.id.cityArrow);
		skip = (TextView) findViewById(R.id.skip);
		areaView = getLayoutInflater().inflate(R.layout.city_select, null);
		// 保存按钮
		saveInfo = (TextView) findViewById(R.id.info_save);
		// 出生日期
		showDate = (TextView) findViewById(R.id.showdate);
		// 地区
		tvAv = (TextView) findViewById(R.id.tvAv);
		segmentView = (SegmentView) findViewById(R.id.segview);
		// 上传头像
		uploadPhoto = (ImageView) findViewById(R.id.photo);
		// 昵称
		nickName = (EditText) findViewById(R.id.nickname);

		// 省市区的三个控件
		mProvince = (WheelView) areaView.findViewById(R.id.id_province);
		mCity = (WheelView) areaView.findViewById(R.id.id_city);
		mArea = (WheelView) areaView.findViewById(R.id.id_area);

		popupView = getLayoutInflater().inflate(R.layout.takephoto_view, null);
		progressBar = (ProgressBar) findViewById(R.id.proid);
		takePhotot = (Button) popupView.findViewById(R.id.take_photo);
		album = (Button) popupView.findViewById(R.id.album);
		btnCancel = (Button) popupView.findViewById(R.id.cancel);
		birth_arrow.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){  
					birth_arrow.setBackgroundResource(R.drawable.right_arrow);
                }else{  
                	birth_arrow.setBackgroundResource(R.drawable.right_arrow_yellow);
                }
			}
		});
		city_arrow.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){  
					city_arrow.setBackgroundResource(R.drawable.right_arrow);
                }else{  
                	city_arrow.setBackgroundResource(R.drawable.right_arrow_yellow);
                }
			}
		});
		saveInfo.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){  
					saveInfo.setTextColor(Color.YELLOW);
                }else{  
                	saveInfo.setTextColor(Color.WHITE);
                }
			}
		});
	}

	UserCache userCache;
	String loginMethods;

	
	
	private Bitmap bgBitmap;
	private BitmapDrawable bd;
	private BitmapDrawable getBg(int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();

		opt.inPreferredConfig = Bitmap.Config.ALPHA_8;
		//opt.inSampleSize = 6; 
		opt.inPurgeable = true; //可以被回收,当系统重新分配内存的时候

		opt.inInputShareable = true;

		// 获取资源图片

		InputStream is = getResources().openRawResource(resId);

		bgBitmap = BitmapFactory.decodeStream(is, null, opt);
		bd = new BitmapDrawable(getResources(),bgBitmap);

		try {
			is.close();
			return bd;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	@Override
	public void initData() {
		
		MyApplication.addActivity(this);
		if (bgBitmap != null ) {
			bgBitmap = null;
		}
		if (bd != null ) {
			bd = null;
		}
		thirdUserCache = new ThirdVipUserCache(this);
		//String coverUrl = thirdUserCache.getMultipartFile();
		
		//Log.i("info","coverUrl的数值为;"+coverUrl);
		//consImgCachePath();
		id = getIntent().getStringExtra("id");
		//pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 用于判断是从哪一个页面跳转过来
		source = getIntent().getStringExtra("from");
		Log.i("info","souce的数值为:"+source);
		userCache = new UserCache(this);
		loginMethods = userCache.getLoginMethods();
		Log.i("info", "loginMethods" + loginMethods);
		isHiddenSkip(source,loginMethods);
		mContext = this;
		Log.i("info", "id的数值为:" + id);
		tvTitle.setText("填写信息");
		initCalendar();
		choicePhoto();
		initJsonData();

		initDatas();

		mProvince.setViewAdapter(new ArrayWheelAdapter<String>(this,
				mProvinceDatas));

		mProvince.setVisibleItems(5);
		mCity.setVisibleItems(5);
		mArea.setVisibleItems(5);
		updateCities();
		updateAreas();
	}

	
	ProgressDialog pDialog;
	private void showProgressDialog(String msg) {
		if (null == pDialog) {
			pDialog = new ProgressDialog(this);
		}
		pDialog.setMessage(msg);
		pDialog.setCancelable(true);
		if(!pDialog.isShowing()){
			pDialog.show();
		}
	}
	
	private void dismissDialog() {
		if (null == pDialog) {
			pDialog = new ProgressDialog(this);
		}
		if(pDialog.isShowing()){
			pDialog.dismiss();
		}
	}
	
	String userIcon = null;
	public  RequestQueue requestQueue = null;;

	public  ImageLoader imageLoader;
//	public   Map<String,SoftReference<Bitmap>> imgCaches; //图片缓存
//	static{
//		imgCaches=new HashMap<String,SoftReference<Bitmap>>();
//	}
	// 根据不同页面判断是否隐藏跳过按钮,应该区分是用户上传的信息还是三方登录的信息
	private void isHiddenSkip(String source,String loginMethods) {
		requestQueue = Volley.newRequestQueue(this);
		imageLoader = new ImageLoader(requestQueue, BitmapCache.getInstance());
		if (loginMethods != null && loginMethods.equals("third_login")) {
			if (source != null && source.equals("setting")) { // 从设置界面跳转过来
				String nickThirdName = thirdUserCache.getNickName();
				String coverUrl = thirdUserCache.getMultipartFile();
				String birthDate = thirdUserCache.getBirthDate();
				Log.i("info", "thirdUserCache  coverUrl的数值为:" + coverUrl);
				String sex = thirdUserCache.getUserSex();
				Log.i("info","三方的性别为;"+sex);
				
				if (sex.equals("女")) {
					segmentView.checkState(1);
				}
				if (sex.equals("男")) {
					segmentView.checkState(0);
				}
				if(sex.equals("1")){  //表示男
					segmentView.checkState(0);
				}else if(sex.equals("0")){ //表示女
					segmentView.checkState(1);
				}else {
					
				}
				tvAv.setText(thirdUserCache.getUserAddress());
				nickName.setText(nickThirdName);
				showDate.setText(birthDate);
				skip.setVisibility(View.GONE);
				
				if (coverUrl != null && !coverUrl.equals("")) {
					
					if(!coverUrl.contains("http")){
						coverUrl = Constants.BASE_PIC + coverUrl;
					}
					photoImgCache(coverUrl);
				}

				//
			} else { // 从注册页面跳转过来
				skip.setVisibility(View.VISIBLE);
				leftButton.setVisibility(View.INVISIBLE);
				segmentView.checkState(0);
			}
		} else {
			if (source != null && source.equals("setting")) { // 从设置界面跳转过来
				String nickUserName = userCache.getNickName();
				String birthDate = userCache.getBirthDate();
				String userIcon = userCache.getMultipartFile();
				String userAddress = userCache.getUserAddress();
				String userSex = userCache.getUserSex();
				Log.i("info", "birthDate的数值为:" + birthDate);
				nickName.setText(nickUserName);
				showDate.setText(birthDate);
				tvAv.setText(userAddress);
				skip.setVisibility(View.GONE);
				Log.i("info", "coverUrl的数值为nnnkn:" + userIcon);
				if(userSex.equals("男")){
					segmentView.checkState(0);
				}else if(userSex.equals("女")) {
					segmentView.checkState(1);
				}
				
				if (userIcon != null && !userIcon.equals("")) {
					if(!userIcon.contains("http")){
						userIcon = Constants.BASE_PIC + userIcon;
					}
					photoImgCache(userIcon);
				}
				 
			} else { // 从注册页面跳转过来
				skip.setVisibility(View.VISIBLE);
				// 将微博的的头像设置到完善信息页面的头像上边
				leftButton.setVisibility(View.INVISIBLE);
				segmentView.checkState(0);
			}
		}

	}

	Boolean isGetFromServer = false;
	private void photoImgCache(final String userIcon) {
		Bitmap bitmap=null;
		Log.i("info", "photoImgCache:" + userIcon);
		//SoftReference<Bitmap> sBitmap=imgCaches.get(userIcon);
		 
//		if(sBitmap != null && (bitmap = sBitmap.get())!=null){
//			 uploadPhoto.setImageBitmap(bitmap);
//		}else {
			if(!WifiUtils.isConnectingToInternet(MoreInfoActivity.this)){
				ToastUtil.showShortToast(MoreInfoActivity.this,"当前网络未连接,请连接后再试");
				return;
			}
			
			if(!WifiUtils.isAvailable(MoreInfoActivity.this)){
				ToastUtil.showShortToast(MoreInfoActivity.this,"当前网络不可用,请重新连接再试");
				return;
			}
			
			showProgressDialog("正在加载中...");
			//pB
			ImageRequest imgRequest=new ImageRequest(userIcon, new Response.Listener<Bitmap>() {  
		        @Override  
		        public void onResponse(Bitmap arg0) {  
		        	  Bitmap bitmap = toRoundBitmap(arg0);
		        	  uploadPhoto.setImageBitmap(bitmap);
		        	  dismissDialog();
		        	  isGetFromServer = true;
		        	  //对下载下来的图片的本地化的存储
		        	  //toSaveLocalImage(bitmap);
		        	  
		        	  //imgCaches.put(userIcon, new SoftReference<Bitmap>(bitmap));
		        	  Log.i("info","网络中");
		        }  
		    }, 300, 200, Config.ARGB_8888, new ErrorListener(){  
		        @Override  
		        public void onErrorResponse(VolleyError arg0) {  
		        	//ToastUtil.showShortToast(MoreInfoActivity.this,"网络连接超时,请重试!");
		        	if (null != pDialog && pDialog.isShowing()) {
						pDialog.dismiss();
					}
		        }             
		    });  
		  requestQueue.add(imgRequest); 
		}

	 
	
	
	
	 /**
     * 转换图片成圆形
     * @param bitmap 传入Bitmap对象
     * @return Bitmap 返回处理后的bitmap图像
     */
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

	
	@Override
	public void addClickListener() {
		leftButton.setOnClickListener(this);
		skip.setOnClickListener(this);
		birth_arrow.setOnClickListener(this);
		city_arrow.setOnClickListener(this);
		uploadPhoto.setOnClickListener(this);
		saveInfo.setOnClickListener(this);
		takePhotot.setOnClickListener(this);
		album.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		// 添加change事件
		mProvince.addChangingListener(this);
		// 添加change事件
		mCity.addChangingListener(this);
		// 添加change事件
		mArea.addChangingListener(this);

		segmentView.setOnSegmentViewClickListener(this);

	}



	private void initCalendar() {
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
	}

	/**
	 * 日期控件的事件
	 */
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			//view.
			view.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						Log.i(TAG,"=====");
						//updateDateDisplay();
					}
				}
			});
			updateDateDisplay();
			Log.i(TAG,"日期控件的确定");
		}
	};

	/**
	 * 更新日期显示
	 */
	private void updateDateDisplay() {
		showDate.setText(new StringBuilder().append(mYear).append("-")
				.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
				.append("-").append((mDay < 10) ? "0" + mDay : mDay));
	}

	//private RelativeLayout birth_arrow;

	private boolean flag = true;

	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果

	public static final String IMAGE_UNSPECIFIED = "image/*";

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_btn:
			finish();
			break;
		case R.id.skip: // 跳转到登录界面
			Intent intent = new Intent(MoreInfoActivity.this,
					LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.photo: // 点击头像时选择图片或者打开照相机
			mPopupWindow.setAnimationStyle(R.style.dialogstyle);
			mPopupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.album:
			fromAlbum();
			break;
		case R.id.take_photo:
			takePhoto();
			break;
		case R.id.info_save:
			submitData(id, uploadFilepath);
			break;
		case R.id.cancel:
			if (mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			}
			break;
		case R.id.birth_arrow://选择出生日期
			chooseBirth(null);
			break;
		case R.id.cityArrow: //选择所在城市和地区
			//chooseCity(null);
			okChoose(null);
			break;
		default:
			break;
		}
	}

	/**
	 * @create 2015.12.28
	 * @author sun
	 * @return code 点击保存按钮 ，上传个人信息到服务器
	 */

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				try {
					Log.i("info",(String)msg.obj);
					JSONObject object = new JSONObject((String) msg.obj);
					String userId = object.optString("userId");
					String code = object.optString("code");
					if(!code.equals("1000")){
						ToastUtil.showShortToast(mContext, "保存信息失败");
						return;
					}
					ToastUtil.showShortToast(mContext, "信息保存成功");
					String nickName = object.optString("nickName");
					String birthday = object.optString("babyBirth");// userAddress
					String userAddress = object.optString("userAddress");
					String userSex = object.optString("userSex");
					saveUserInfo(userId,nickName, birthday,userAddress,userSex);
					if (source != null && source.equals("setting")) {
						startActivity(new Intent(mContext, HomeActivity.class));
					} else {
						startActivity(new Intent(mContext, LoginActivity.class));
						break;
					}
				} catch (JSONException e) {
					ToastUtil.showShortToast(mContext, "数据解析异常");
					e.printStackTrace();
				}

			default:
				break;
			}
		};
	};

	private void saveUserInfo(String userId,String nickName,
			String birthday, String userAddress,String userSex) {

		userCache.setUserId(userId);
		userCache.setIsLogin(true);
		userCache.setNickName(nickName);
		userCache.setBirthDate(birthday);
		userCache.setUserAddress(userAddress);
		userCache.setUserSex(userSex);
	}

	private void submitData(String id, String uploadFilepath) {

		// 出生日期
		String birthDate = showDate.getText() + "";
		// 地区
		String areaString = tvAv.getText() + "";

		// 昵称
		String userName = nickName.getText() + "";

		if (checkInfo(id, userName, birthDate)) {
			return;
		}
		String userSex;
		if(segmentView.getUserSex().equals("男")){
			userSex = "男";
		}else if(segmentView.getUserSex().equals("女")){
			userSex = "女";
		}else {
			userSex = "";
		}
			
		Log.d("info", "userSex的数值wei;"+userSex);
		Log.i("info", " " + birthDate + " " + userName + " " + areaString + " "
				+ userSex + " " + id);
		
		
		NetUtils.savePersonInfo(id, userName, userSex, birthDate, areaString, MoreInfoActivity.this, mHandler);
	}

	/**
	 * @author 作者 : sun
	 * @date 创建时间：2016-1-7 下午3:02:29
	 * @return
	 * @description 将头像上显示的默认图片保存到sd卡里边
	 */
	
	 
	
	//String localImgName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Imagessss/localImg.png";
	
	public String toSaveImage(Bitmap bmp ) {
		String localImg = MoreInfoActivity.this.getFilesDir().getAbsolutePath()+"/img";
		String localImgName = MoreInfoActivity.this.getFilesDir().getAbsolutePath()+"/img/localimg.png"; 
		File fileImg = new File(localImg);
		if (!fileImg.exists()) {
			fileImg.mkdirs();
		}
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(new File(localImgName)));
			//OutputStream ofot = openFileOutput("local_img.png", MODE_WORLD_WRITEABLE);
			bmp.compress(Bitmap.CompressFormat.JPEG,20, bos);
			return localImgName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @author 作者 : sun
	 * @date 创建时间：2016-1-7 上午10:07:26
	 * @return
	 * @description 对个人信息未填写的检查
	 */

	private Float dateFloat;

	private boolean checkInfo(String id, String nickName, String birthDate) {
		String dateString = new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date());
		Float curDateFloat = Float.valueOf(dateString.replaceAll("-", ""));
		if (nickName.equals("")) {
			ToastUtil.showShortToast(mContext, "昵称不能为空");
			return true;
		}
		if (!birthDate.equals("")) {
			dateFloat = Float.parseFloat(birthDate.replaceAll("-", ""));
			if (dateFloat > curDateFloat) {
				ToastUtil.showShortToast(mContext, "出生日期不能大于当前日期");
				return true;
			}
		}
		return false;
	}

	/**
	 * @author  作者 : sun
	 * @date 创建时间：2016-2-25 下午2:26:35    
	 * @return 
	 * @description 判断sd卡是否挂载
	 */
	private boolean isSdcardMount() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 拍照获取图片
	 */
	private void takePhoto() {
		if(isSdcardMount()){
			Intent intentTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intentTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri
					.fromFile(new File(Environment.getExternalStorageDirectory(),
							"temp.jpg")));
			System.out.println("============="
					+ Environment.getExternalStorageDirectory());
			startActivityForResult(intentTakePhoto, PHOTOHRAPH);
		}
		
	}

	/**
	 * 从相册选取图片
	 */
	private void fromAlbum() {
		Intent intentAlbum = new Intent(Intent.ACTION_GET_CONTENT, null);
		intentAlbum
				.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						IMAGE_UNSPECIFIED);
		startActivityForResult(intentAlbum, PHOTOZOOM);
	}

	/**
	 * 点击头像从相册后者拍照然后上传图片
	 */
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == NONE)
			return;
		// 拍照
		if (requestCode == PHOTOHRAPH) {
			// 设置文件保存路径这里 
			File picture = new File(Environment.getExternalStorageDirectory()
					+ "/temp.jpg");
			Log.i("info", "------------------------" + picture.getPath());
			startPhotoZoom(Uri.fromFile(picture));
			Log.i("info", "------------------------"
					+ Uri.fromFile(picture).toString());
		}

		if (data == null)
			return;

		// 读取相册缩放图片
		if (requestCode == PHOTOZOOM) {
			//得到图片的全路径
			Uri uris = data.getData();
			startPhotoZoom(uris);
		}
		// 处理结果
		if (requestCode == PHOTORESOULT) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				roundBitmap = toRoundBitmap(photo);
				uploadPhoto.setImageBitmap(roundBitmap);
				String path = toSaveImage(roundBitmap);
				if (path != null && !path.equals("")) {
					NetUtils.uploadImage(id, MoreInfoActivity.this, mHandler, path,progressBar);
				}
			}
		}
		if (mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void startPhotoZoom(Uri uri) {
		//uploadFilepath = UriUtils.getImageAbsolutePath(this, uri);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 80);
		intent.putExtra("outputY", 80);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
	}

	private void choicePhoto() {
		mPopupWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);

		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),
				(Bitmap) null));
		popupView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
				return false;
				
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	
	/**
	 * @author sun 对于显示出生日期 的日历控件的显示
	 * @param view
	 */
	public void chooseBirth(View view) {
		Message msg = new Message();
		msg.what = 0;
		dateandtimeHandler.sendMessage(msg);
	}
	
	/**
	 * 处理日期和时间控件的Handler
	 */
	Handler dateandtimeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				showDialog(DATE_DIALOG_ID);
				break;
			}
		}

	};
	private TextView area;
	private EditText sex;
	private EditText nickName;
	private String id;
	private String uploadFilepath;
	private Context mContext;
	private SegmentView segmentView;

	private Boolean isFirstAdd = true;

	public void chooseCity(View view) {
		android.widget.RelativeLayout.LayoutParams params = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		areaView.setLayoutParams(params);
		RelativeLayout rootView = (RelativeLayout) findViewById(R.id.moreinfo_layout);
		if (isFirstAdd) {
			rootView.addView(areaView);
			isFirstAdd = false;
		} else {
			areaView.setVisibility(View.VISIBLE);
		}
		InputMethodManager imm = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// 显示或者隐藏输入法
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

	}

	

	
	
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mProvince) {
			Log.i("swt", "选择省市");
			updateCities();
			updateAreas();
			newValue = 0;
		} else if (wheel == mCity) {
			updateAreas();
			newValue = 0;
		} else if (wheel == mArea) {
			
		}
		
		Log.i("swt","newValue的数值为:"+newValue+"oldValue的数值为:"+oldValue);
		if (mCurrentCityName != null && mAreaDatasMap != null
				&& mAreaDatasMap.get(mCurrentCityName) != null
				&& mAreaDatasMap.get(mCurrentCityName).length > 0
				&& mAreaDatasMap.get(mCurrentCityName).length > newValue)
			mCurrentAreaName = mAreaDatasMap.get(mCurrentCityName)[newValue];
		
		Log.i("swt","城市的名字:"+mCurrentCityName+"地区的名字:"+mCurrentAreaName);
	}

	
	public void okChoose(View view) {
		if(null == mCurrentProviceName){
			ToastUtil.showShortToast(MoreInfoActivity.this,"未选择地区,请重新选择");
			return;
		}
		areaView.setVisibility(View.GONE);
		if (mCurrentProviceName.equals("北京")
				|| mCurrentProviceName.equals("上海")
				|| mCurrentProviceName.equals("天津")
				|| mCurrentProviceName.equals("重庆")
			    || mCurrentProviceName.equals("香港")
			    || mCurrentProviceName.equals("澳门")
			    || mCurrentProviceName.equals("台湾"))
			tvAv.setText(mCurrentProviceName + "," + mCurrentCityName);
		else {
			tvAv.setText(mCurrentProviceName + "," + mCurrentCityName + ","
					+ mCurrentAreaName);
		}
	}
	
	
	
	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mCity.getCurrentItem();
		if(null != mCitisDatasMap){
			Log.i("info","mcurrr"+mCurrentProviceName);
			if(mCurrentProviceName != null){
				mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
				//Log.i("swt","当前")
				String[] areas = mAreaDatasMap.get(mCurrentCityName);

				if (areas == null) {
					areas = new String[] { "" };
				}
				mArea.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
				mArea.setCurrentItem(0);
			}
			
		}
		
		
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mCity.setCurrentItem(0);
		updateAreas();
	}

	public void chanelChoose(View view) {
		areaView.setVisibility(View.GONE);
	}
	
	/**
	 * 从assert文件夹中读取省市区的json文件，然后转化为json对象
	 */
	private void initJsonData() {
		String jsonData = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"city.json";
		try {
			StringBuffer sb = new StringBuffer();
			//ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream is = getAssets().open("city.json");
			int len = -1;
			byte[] buf = new byte[1024];
			while ((len = is.read(buf)) != -1) {
				sb.append(new String(buf, 0, len, "gbk"));
			}
			is.close();
			//baos.reset();
			mJsonObj = new JSONObject(sb.toString());
			sb.setLength(0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 解析整个Json对象，完成后释放Json对象的内存
	 */
	private void initDatas() {
		try {
			JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
			mProvinceDatas = new String[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
				String province = jsonP.getString("p");// 省名字

				mProvinceDatas[i] = province;

				JSONArray jsonCs = null;
				try {
					/**
					 * Throws JSONException if the mapping doesn't exist or is
					 * not a JSONArray.
					 */
					jsonCs = jsonP.getJSONArray("c");
				} catch (Exception e1) {
					continue;
				}
				String[] mCitiesDatas = new String[jsonCs.length()];
				for (int j = 0; j < jsonCs.length(); j++) {
					JSONObject jsonCity = jsonCs.getJSONObject(j);
					String city = jsonCity.getString("n");// 市名字
					mCitiesDatas[j] = city;
					JSONArray jsonAreas = null;
					try {
						/**
						 * Throws JSONException if the mapping doesn't exist or
						 * is not a JSONArray.
						 */
						jsonAreas = jsonCity.getJSONArray("a");
					} catch (Exception e) {
						continue;
					}

					String[] mAreasDatas = new String[jsonAreas.length()];// 当前市的所有区
					for (int k = 0; k < jsonAreas.length(); k++) {
						String area = jsonAreas.getJSONObject(k).getString("s");// 区域的名称
						mAreasDatas[k] = area;
					}
					mAreaDatasMap.put(city, mAreasDatas);
				}

				mCitisDatasMap.put(province, mCitiesDatas);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		mJsonObj = null;
	}
	

	private TextView tvSex;
	private ThirdVipUserCache thirdUserCache;
	private String source;

	TextView sexView;
	Boolean isClick = false;
	
	private ProgressDialog pBar;
	private Bitmap roundBitmap;
	private String cacheLocalImgName;
	private String cacheLocalImgPath;
	private ProgressBar progressBar;
	private ImageView birth_arrow;
	private ImageView city_arrow;

	@Override
	public void onSegmentViewClick(View v, int position) {
//		sexView = (TextView) v;
//		isClick = true;
//		Constants.pos = position;
	}
	
	@Override
	protected void onDestroy() {
		
		//释放HashMap所占的内存
		if (null != mCitisDatasMap) {
			Log.i("set","销毁");
			mCitisDatasMap.clear();
			mCitisDatasMap = null;
		}
		if (null != mAreaDatasMap) {
			Log.i("set","销毁1");
			mAreaDatasMap.clear();
			mAreaDatasMap = null;
		}
		if (mProvinceDatas != null) {
			mProvinceDatas = null;
		}
		mHandler.removeCallbacksAndMessages(null);
		dateandtimeHandler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}
	@Override
	protected void onStop() {
		super.onStop();
		if (requestQueue != null) {
			//requestQueue.cancelAll(null);
	    }
	}

}
