package com.horner.xsm.bean;

import java.io.Serializable;

/**
 * 书本
 */
public class Book implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8980974202021715103L;
	public static final int DOWNLOADING = 0;// 代表下载未结束
	public static final int DOWNLOAD_SUCCESS = 1;// 代表下载成功
	public static final int DELETED = -1;// 代表被删除
	public static final int CONNECT_ERROR = -2;// 代表网络连接错误
	public static final int CONTENT_ERROR = -3;// 代表下载书籍内容有误
	// 名字
	public String mName; // 书籍名字�?本地存储书籍封面名字
	public String mBookID;// 书籍id
	public String mCover; // 书籍对应的数据文件本地位置?
	public String mPath;// 书籍对应的本地地址?
	public String mShelfdate;// 日期
	public String mCoverurl; // net 下载路径�?
	public String mDownurl;// 下载的路径zip
	public String mDesc;// 描述
	public String mSize;// 大小
	public int mOrder = -1;// indext�?
	public int state = DOWNLOADING;// 0代表下载未结束，1代表下载成功,-1代表被删�?-2网络连接错误,-3下载书籍内容有误
	public String mSingleID;
	public Double mCurrentRate = 0.00;
	public int mProbationRate;
	public String bookProgress;//
	public String mVerid;
	public String mVersion;
	public String mBookCode;
	public int mBookType;
	public String mResourceType;
	public String mPublishType;
	public String mCategoryid;
	public String mBookCatagoryId;
	public String mBundleid;
	public String mBookKeywords;
	public String mBookauthors;// 作者
	public String mDescription;
	public String mIsbn;
	public String mPublishdt;
	public String mPublishername;
	public String mPrice; // 原价
	public String mIphonePrice;
	public String mPaperPrice;
	public String mIpadprice;
	public String mBookurl; // 下载地址
	public String mOfficeId;
	public String mUpdatetime;
	public String mUpdater;
	public String mBookState;
	public String mCheckId;
	public String mMemo;
	public String mPlatform;
	public String mSeq;
	public String mCataname;
	public String mCommentsum;
	public String mCommentLevel;
	public String mFree;
	public String mWordnumber;
	public String mPromotionPrice;
	public String mNowPrice;// 现价
	public String mPurchaseTime;// 购买日期
	public boolean isSeleted = false;
	public String isLocal;
	public String mIsCollection;// 是否收藏 true false
	public String mIsBuy;// 是否在购物车true false
	public String mHasBuy;// 是否已经购买true false
	public String mCatalogue;// 目录
	public int mIsRead = 0; // 已购图书，0 下载，1下载中，2阅读。
	public String mLastReadTime;
	public int mDownloadProgress;// 已下载进度
	/* public RelativeLayout bookView; */
	public static final int BOOK_TYPE_PDF = 11;
	public static final int BOOK_TYPE_EPUB = 12;
	public static final int BOOK_TYPE_MEB = 10;
	public static final int BOOK_TYPE_TXT = 13;

	public int mState;
	public String LOCALPATH;
	public int mDownloadCount;
	public int mBookSize;
	public boolean isServiceDown = false;
	
	//public boolean mIsDowning = false;
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		Book b = (Book) o;
		if (mBookID != null && b.mBookID != null) {
			return mBookID.equals(b.mBookID);
		}
		return false;
	}
}
