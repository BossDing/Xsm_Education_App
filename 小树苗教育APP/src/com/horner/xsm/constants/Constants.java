package com.horner.xsm.constants;

import java.util.List;

import com.horner.xsm.bean.PriceBean;

import android.os.Environment;

public class Constants {
	// 服务器主机地址
	public static final String BASE_URL = "http://szcblm.horner.cn:8080/alliance/front/";

	//拼接图片的地址
	public static final String BASE_PIC = "http://szcblm.horner.cn/";
	
	// 注册时的短信验证
	public static final String GET_CODE = BASE_URL + "reg/getcode";

	// 完善信息页面
	public static final String SUPPLY_INFO = BASE_URL + "reg/infor";
	// 忘记密码
	public static final String FORGET_PWD = BASE_URL
			+ "readCard/sendPhonegetPWD";
	// 密码重置
	public static final String CHONGZHIPWD = BASE_URL + "login/resetPWD";
	
	//小树苗获取  会员价格和模块价格
	public static final String GET_PRICE = BASE_URL+"getPrice";

	//
	public static List<PriceBean> priceBeans;
	public final static String OFFICEID = "40";
	
	public   static String nonceStr = "";
	
	//选择男女的位置
	public static int pos;
	
	//是否 点击头像
	public static String isVipOrMod;
	
	//是否 点击头像
	public static Boolean isImgClicks = false;
	/**
	 * 检查是否对音频文件加密过
	 */
	public static Boolean isEncryptVedios = false;
	
	// 书籍详情
	// public static final String BOOK_DETAIL = BASEURL
	// + "book/getBookInforByBookIdAndUserId?bookShelfId=3&officeId=" + OFFICEID
	// + "&special=true&bookId=";
	public static final String BOOK_DETAIL = BASE_URL
			+ "book/getBookInforByBookIdAndUserId";

	// ?bookShelfId=3&special=true&bookId=

	public static final String CATEGORY_LIST = BASE_URL
			+ "book/getCatagoryList?bookShelfId=3&twoLevel=true&officeId=40";

	public static final String BOOKLIST_BYTOP = BASE_URL
			+ "book/getBookListByTop?bookShelfId=3&officeId=40&count=5&cataId=";
	public static final String BOOKLIST_ByCatagoryVipType = BASE_URL
			+ "book/getBookListByCatagoryVipType?bookShelfId=3&officeId=40&start=0&pageSize=50&orderType=11&cataId=";
	public static final String BOOKLIST_ByCatagoryByCatagory = BASE_URL
			+ "book/getBookListByCatagory?bookShelfId=3&officeId=40&start=0&pageSize=50&cataId=";

	public static String localbookbasepath = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/xsm/book/";
	public static final String DOWNURL = "http://222.197.183.36";

	public static final String DOWNLOAD_URL = "http://szcblm.horner.cn";

	public static final int SRART_DOWN = 200001;
	public static final int OVER_DOWN = 200002;
	public static final int LOAD_DOWN = 200003;
	public static final int DOWN_ERROR = 200004;
	
	public static final String XSM = "xsm";
	public static  String wePayOrderNo;
	
	public static String pay_order_type = null;
	public static String extraInfo;
	public static int  width;
	public static int  height;

}
