package com.horner.xsm.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParserFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.horner.xsm.bean.Book;
import com.horner.xsm.bean.BookCategory;
import com.horner.xsm.constants.Constants;
import com.horner.xsm.net.AsyncHttpClient;
import com.horner.xsm.net.AsyncHttpResponseHandler;
import com.horner.xsm.net.RequestParams;
import com.horner.xsm.utils.SysEnvUtils;
import com.horner.xsm.utils.ToastUtil;
import com.mouee.common.DataUtils;
import com.mouee.common.FileUtils;
import com.mouee.common.HttpManager;
import com.mouee.common.StringUtils;
import com.mouee.common.ZipUtil;

public class BookDataManager {
	private static ArrayList<Book> books = new ArrayList<Book>();
	private static String NReader_List_SQL = " select bookorder,iconPath,bookPath,bookid,state,iconUrl,downUrl,bookdesc,bookname,booktype,book_progress,probationrate,last_read_time,is_local,book_author,download_progress from table_book order by bookorder desc";
	private static String NReader_TimeList_SQL = " select bookorder,iconPath,bookPath,bookid,state,iconUrl,downUrl,bookdesc,bookname,booktype,book_progress,probationrate,last_read_time,is_local,book_author,download_progress  from table_book where last_read_time  is not null order by last_read_time desc limit 4";
	public static String NReader_Insert_SQL = "insert into table_book(bookorder,iconPath,bookPath,bookid,state,iconUrl,downUrl,bookdesc,bookname,booktype,book_progress,probationrate,last_read_time,is_local,book_author,download_progress)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String NReader_MaxOrder_SQL = "select max(bookorder) from table_book";
	private static String NReader_Delete_SQL = " delete from table_book where bookid=?";

	// 修改书籍阅读时间
	private static String NReader_Update_Book_ReadTime_SQL = " update table_book set last_read_time =? where bookid=?";
	// 修改书籍状态
	private static String NReader_Update_Book_State_SQL = " update table_book set state=? where bookid=?";
	// 修改书籍阅读试读进度
	private static String NReader_Update_Book_Probationrate_SQL = " update table_book set probationrate=0 where bookid=?";
	// 查询一本书的阅读进度
	private static String NReader_Progress_SQL = " select bookorder,iconPath,bookPath,bookid,state,iconUrl,downUrl,bookdesc,bookname,booktype,book_progress,probationrate,last_read_time,is_local,book_author,download_progress from table_book where bookid=?";
	// 查询一本书是否已经存在
	private static String NReader_Exist_SQL = " select bookorder,iconPath,bookPath,bookid,state,iconUrl,downUrl,bookdesc,bookname,booktype,book_progress,probationrate,last_read_time,is_local,book_author,download_progress from table_book where bookPath=?";
	// 查询一本书是否已经存在
	private static String NReader_Search_SQL = " select bookorder,iconPath,bookPath,bookid,state,iconUrl,downUrl,bookdesc,bookname,booktype,book_progress,probationrate,last_read_time,is_local,book_author,download_progress from table_book where bookname like ?";

	private static String NReader_Update_Book_DownloadProgress_SQL = " update table_book set download_progress=? where bookid=?";

	/**
	 * 删除数据库书籍
	 * 
	 * @param activity
	 * @param book
	 */
	public static void deleteBook(Activity activity, Book book) {
		synchronized (books) {
			book.state = -1;
			Object[] p = new Object[1];
			p[0] = book.mBookID;
			DataUtils.execSQL(activity, NReader_Delete_SQL, p);
			getBookList(activity).remove(book);
			String bookBasePath = book.mPath;
			FileUtils.delFolder(bookBasePath);
		}
	}

	public static ArrayList<Book> getBookList(Activity activity) {
		return books;
	}

	/**
	 * 下载完成修改数据库状态
	 * 
	 * @param activity
	 * @param book
	 */
	public static void updateBookState(Activity activity, String bookId,
			int state) {
		Object[] p = new Object[2];
		p[0] = state;
		p[1] = bookId;
		DataUtils.execSQL(activity, NReader_Update_Book_State_SQL, p);
	}

	public static void updateBookReadTime(Activity activity, Book book,
			long time) {
		book.state = 1;
		Object[] p = new Object[2];
		p[0] = time + "";
		p[1] = book.mBookID;
		DataUtils.execSQL(activity, NReader_Update_Book_ReadTime_SQL, p);
	}

	public static void updateBookProbationrate(Activity activity, Book book) {
		book.state = 1;
		Object[] p = new Object[1];
		p[0] = book.mBookID;
		DataUtils.execSQL(activity, NReader_Update_Book_Probationrate_SQL, p);
	}

	/**
	 * @param activity
	 * @param book
	 *            更新书籍下载进度
	 */
	public static void updateBookDownloadProgress(Activity activity,
			String bookId, int progress) {
		Object[] p = new Object[2];
		p[0] = progress + "";
		p[1] = bookId;
		DataUtils
				.execSQL(activity, NReader_Update_Book_DownloadProgress_SQL, p);
	}

	public static void UpdateBookName(Activity activity, Book book) {
		ContentValues values = new ContentValues();
		values.put("book_author", book.mBookauthors);
		DataUtils.update(activity, "table_book", values, "bookid=?",
				new String[] { book.mBookID });
	}

	public static void UpdateBookCover(Activity activity, Book book) {
		ContentValues values = new ContentValues();
		values.put("iconPath", book.mCover);
		DataUtils.update(activity, "table_book", values, "bookid=?",
				new String[] { book.mBookID });
	}

	/**
	 * 获取数据库中书籍信息
	 * 
	 * @param activity
	 * @return
	 */
	public static ArrayList<Book> initBookList(Activity activity) {
		ArrayList<Book> bookslist = new ArrayList<Book>();
		books = new ArrayList<Book>();
		Cursor cursor = DataUtils.rawQuery(activity, NReader_List_SQL);
		try {
			while (cursor.moveToNext()) {
				Book book = new Book();
				book.mOrder = cursor.getInt(0);
				book.mCover = cursor.getString(1);
				book.mPath = cursor.getString(2);
				book.mBookID = cursor.getString(3);
				book.state = cursor.getInt(4);
				book.mCoverurl = cursor.getString(5);
				book.mDownurl = cursor.getString(6);
				book.mDesc = cursor.getString(7);
				book.mName = cursor.getString(8);
				try {
					book.mBookType = Integer.valueOf(cursor.getString(9));
				} catch (Exception e) {
				}
				book.bookProgress = cursor.getString(10);
				book.mProbationRate = cursor.getInt(11);
				book.mLastReadTime = cursor.getString(12);
				book.isLocal = cursor.getString(13);
				book.mBookauthors = cursor.getString(14);
				book.mDownloadProgress = cursor.getInt(15);
				bookslist.add(book);
				books.add(book);
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return bookslist;
	}

	public static ArrayList<Book> getTimeBookList(Activity activity) {
		ArrayList<Book> books = new ArrayList<Book>();
		Cursor cursor = DataUtils.rawQuery(activity, NReader_TimeList_SQL);
		try {
			while (cursor.moveToNext()) {
				Book book = new Book();
				book.mOrder = cursor.getInt(0);
				book.mCover = cursor.getString(1);
				book.mPath = cursor.getString(2);
				book.mBookID = cursor.getString(3);
				book.state = cursor.getInt(4);
				book.mCoverurl = cursor.getString(5);
				book.mDownurl = cursor.getString(6);
				book.mDesc = cursor.getString(7);
				book.mName = cursor.getString(8);
				try {
					book.mBookType = Integer.valueOf(cursor.getString(9));
				} catch (Exception e) {
				}
				book.bookProgress = cursor.getString(10);
				book.mProbationRate = cursor.getInt(11);
				book.mLastReadTime = cursor.getString(12);
				book.isLocal = cursor.getString(13);
				book.mBookauthors = cursor.getString(14);
				book.mDownloadProgress = cursor.getInt(15);
				books.add(book);
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return books;
	}

	public static Book getBookProgress(Activity activity, String bookId) {
		Book book = new Book();
		Cursor cursor = DataUtils.rawQuery(activity, NReader_Progress_SQL,
				new String[] { bookId });
		try {
			if (cursor.moveToNext()) {
				book.mOrder = cursor.getInt(0);
				book.mCover = cursor.getString(1);
				book.mPath = cursor.getString(2);
				book.mBookID = cursor.getString(3);
				book.state = cursor.getInt(4);
				book.mCoverurl = cursor.getString(5);
				book.mDownurl = cursor.getString(6);
				book.mDesc = cursor.getString(7);
				book.mName = cursor.getString(8);
				try {
					book.mBookType = Integer.valueOf(cursor.getString(9));
				} catch (Exception e) {
				}
				book.bookProgress = cursor.getString(10);
				book.mProbationRate = cursor.getInt(11);
				book.mLastReadTime = cursor.getString(12);
				book.isLocal = cursor.getString(13);
				book.mBookauthors = cursor.getString(14);
				book.mDownloadProgress = cursor.getInt(15);
			}
		} catch (Exception e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return book;

	}

	/**
	 * @param activity
	 * @param bookPath
	 *            书籍是不是已经导入了
	 * @return
	 */
	public static boolean getBookIsExist(Activity activity, String bookPath) {
		Cursor cursor = DataUtils.rawQuery(activity, NReader_Exist_SQL,
				new String[] { bookPath });
		try {
			if (cursor.moveToNext()) {
				return true;
			}
		} catch (Exception e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return false;

	}

	/**
	 * 插入book数据
	 * 
	 * @param activity
	 */
	public static void createNewBook(Activity activity, Book book) {
		book.mOrder = BookDataManager.getNextOrder(activity);
		BookDataManager.setBookPath(activity, book);
		synchronized (books) {
			books.remove(book);
			Log.d("mouee", "add new book");
			// if (getBookByID(activity, book.bookID) == null) {

			books.add(book);
			Object[] p = new Object[16];
			p[0] = book.mOrder;
			p[1] = book.mCover;
			p[2] = book.mPath;
			p[3] = book.mBookID;
			p[4] = book.state;
			p[5] = book.mCoverurl;
			p[6] = book.mDownurl;
			p[7] = book.mDesc;
			p[8] = book.mName;
			p[9] = book.mBookType;
			p[10] = book.bookProgress;
			p[11] = book.mProbationRate;
			p[12] = book.mLastReadTime;
			p[13] = book.isLocal;
			p[14] = book.mBookauthors;
			p[15] = book.mDownloadProgress;
			DataUtils.execSQL(activity, NReader_Insert_SQL, p);
			// }
		}
	}

	/**
	 * @param activity
	 * @param book
	 *            创建本地新图书
	 */
	public static void createNewLocalBook(Activity activity, Book book) {
		book.mOrder = BookDataManager.getNextOrder(activity);
		synchronized (books) {
			// books.remove(book);
			Log.d("mouee", "add new book");
			// if (getBookByID(activity, book.bookID) == null) {

			books.add(book);
			Object[] p = new Object[16];
			p[0] = book.mOrder;
			p[1] = book.mCover;
			p[2] = book.mPath;
			p[3] = book.mBookID;
			p[4] = book.state;
			p[5] = book.mCoverurl;
			p[6] = book.mDownurl;
			p[7] = book.mDesc;
			p[8] = book.mName;
			p[9] = book.mBookType;
			p[10] = book.bookProgress;
			p[11] = book.mProbationRate;
			p[12] = book.mLastReadTime;
			p[13] = book.isLocal;
			p[14] = book.mBookauthors;
			p[15] = book.mDownloadProgress;
			DataUtils.execSQL(activity, NReader_Insert_SQL, p);
			// }
		}
	}

	public static int getNextOrder(Activity activity) {
		Cursor cursor = DataUtils.rawQuery(activity, NReader_MaxOrder_SQL);
		int maxOrder = 0;
		try {
			while (cursor.moveToNext()) {

				maxOrder = cursor.getInt(0) + 1;
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return maxOrder;

	}

	/**
	 * 设置书籍的书籍位置和图标位置
	 * 
	 * @param book
	 */
	public static void setBookPath(Activity activity, Book book) {
		book.mPath = Constants.localbookbasepath + book.mBookID + "/book";
	}

	/**
	 * 判断此书是否已经下载
	 * 
	 * @param activity
	 * @param id
	 *            书籍编号
	 * @return
	 */
	public static Book getBookByID(Activity activity, String id) {
		if (StringUtils.isEmpty(id))
			return null;
		// 检查书表是否存在

		for (Book book : getBookList(activity)) {
			if (book != null && book.mBookID != null && book.mBookID.equals(id)
					&& book.state != -1)
				return book;
		}
		return null;
	}

	/**
	 * 开始下载书
	 * 
	 * @param handler
	 * 
	 * @param book
	 */

	public static void startDownBook(final Book book, final Book downbook,
			final Handler handler, final Button v) {
		final String zipPath = Constants.localbookbasepath + downbook.mBookID
				+ "/book.zip";
		new Thread() {
			public void run() {
				String result = HttpManager.downLoadResource(Constants.DOWNURL
						+ downbook.mDownurl, zipPath);
				Object[] objects = { result, book, v };
				// 下载结果
				Message msg = Message.obtain();
				msg.what = 0;
				msg.obj = objects;
				handler.sendMessage(msg);
				try {
					ZipUtil.UnZipFolder(zipPath, Constants.localbookbasepath
							+ downbook.mBookID);
				} catch (Exception e) {
					Log.d("This is ZipUtil Exception : ", e.toString());
				}
			};
		}.start();
	}

	// public static ArrayList<Book> getSearchContent(String text, int
	// page,String userId) {
	// ArrayList<Book> list = new ArrayList<Book>();
	// HashMap<String, String> param = new HashMap<String, String>();
	// param.put("bookShelfId", Constants.shelfId);
	// param.put("userId", userId);
	// param.put("start", page + "");
	// param.put("pageSize", Constants.pageSize + "");
	// param.put("key", text);
	// String content = HttpManager.postDataToUrl(Constants.BASEURL
	// + "book/search?officeId=" + Constants.OFFICEID, param);
	// if (StringUtils.isEmpty(content)) {
	// return null;
	// }
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new BookListXMLHandler(list));
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// return list;
	// }
	//
	// public static ArrayList<HotWord> getHotWords() {
	// ArrayList<HotWord> list = new ArrayList<HotWord>();
	// String content = HttpManager.getUrlContent(Constants.HOT_WORD, "utf-8");
	// if (StringUtils.isEmpty(content)) {
	// return null;
	// }
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new HotWordXMLHandler(list));
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// return list;
	// }
	//
	// /**
	// * @param text
	// * @return 搜索本地图书
	// */
	// public static ArrayList<Book> getSearchLocalContent(Activity activity,
	// String text) {
	// ArrayList<Book> bookList = new ArrayList<Book>();
	// Cursor cursor = DataUtils.rawQuery(activity, NReader_Search_SQL,
	// new String[] { "%" + text + "%" });
	// try {
	// while (cursor.moveToNext()) {
	// Book book = new Book();
	// book.mOrder = cursor.getInt(0);
	// book.mCover = cursor.getString(1);
	// book.mPath = cursor.getString(2);
	// book.mBookID = cursor.getString(3);
	// book.state = cursor.getInt(4);
	// book.mCoverurl = cursor.getString(5);
	// book.mDownurl = cursor.getString(6);
	// book.mDesc = cursor.getString(7);
	// book.mName = cursor.getString(8);
	// try {
	// book.mBookType = Integer.valueOf(cursor.getString(9));
	// } catch (Exception e) {
	// }
	// book.bookProgress = cursor.getString(10);
	// book.mProbationRate = cursor.getInt(11);
	// book.mLastReadTime = cursor.getString(12);
	// book.isLocal = cursor.getString(13);
	// book.mBookauthors = cursor.getString(14);
	// book.mDownloadProgress = cursor.getInt(15);
	// bookList.add(book);
	// }
	// } catch (Exception e) {
	// } finally {
	// if (cursor != null) {
	// cursor.close();
	// }
	// }
	// return bookList;
	//
	// }
	//
	// /**
	// * 今日限免
	// */
	// public static ArrayList<Book> getFreeList(int page) {
	// ArrayList<Book> bookList = new ArrayList<Book>();
	// String url = Constants.FREE_LIST + "&start=" + page + "&pageSize="
	// + Constants.pageSize;
	// String content = HttpManager.getUrlContent(url, "utf-8");
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new BookListXMLHandler(bookList));
	// } catch (Exception e) {
	// return bookList;
	// }
	// return bookList;
	// }
	//
	// public static ArrayList<Book> getSingleFreeList(int page) {
	// ArrayList<Book> bookList = new ArrayList<Book>();
	// String url = Constants.FREE_LIST + "&start=" + page + "&pageSize=1";
	// String content = HttpManager.getUrlContent(url, "utf-8");
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new BookListXMLHandler(bookList));
	// } catch (Exception e) {
	// return bookList;
	// }
	// return bookList;
	// }
	//
	//
	// /**
	// * @param pageSize
	// * @return �ذ��Ƽ�
	// */
	// public static ArrayList<Book> getRecommendList(String pageSize) {
	// ArrayList<Book> bookList = new ArrayList<Book>();
	// String url = Constants.RECOMMEND_LIST + pageSize;
	// String content = HttpManager.getUrlContent(url, "utf-8");
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new BookListXMLHandler(bookList));
	// } catch (Exception e) {
	// return bookList;
	// }
	// return bookList;
	// }
	//
	// public static ArrayList<Book> getBookList(String content) {
	// ArrayList<Book> bookList = new ArrayList<Book>();
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new BookListXMLHandler(bookList));
	// } catch (Exception e) {
	// return bookList;
	// }
	// return bookList;
	// }
	//
	// public static ArrayList<Book> getSpecialList(String pageSize) {
	// ArrayList<Book> bookList = new ArrayList<Book>();
	// String url = Constants.SPECIAL_LIST + pageSize;
	// String content = HttpManager.getUrlContent(url, "utf-8");
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new BookListXMLHandler(bookList));
	// } catch (Exception e) {
	// return bookList;
	// }
	// return bookList;
	//
	// }
	//
	// public static ArrayList<Book> getNewestList(String pageSize) {
	// ArrayList<Book> bookList = new ArrayList<Book>();
	// String url = Constants.NEWEST_LIST + pageSize;
	// String content = HttpManager.getUrlContent(url, "utf-8");
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new BookListXMLHandler(bookList));
	// } catch (Exception e) {
	// return bookList;
	// }
	// return bookList;
	//
	// }
	//
	// public static ArrayList<AD> getAdList() {
	// ArrayList<AD> bookList = new ArrayList<AD>();
	// String url = Constants.AD_LIST;
	// String content = HttpManager.getUrlContent(url, "utf-8");
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new AdXMLHandler(bookList));
	// } catch (Exception e) {
	// return bookList;
	// }
	// return bookList;
	//
	// }
	//
	// public static ArrayList<AD> getAdList(String content) {
	// ArrayList<AD> bookList = new ArrayList<AD>();
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new AdXMLHandler(bookList));
	// } catch (Exception e) {
	// return bookList;
	// }
	// return bookList;
	// }
	//
	// public static ArrayList<AD> getRecommendAdList() {
	// ArrayList<AD> bookList = new ArrayList<AD>();
	// String url = Constants.MIDDLE_AD_LIST;
	// String content = HttpManager.getUrlContent(url, "utf-8");
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new AdXMLHandler(bookList));
	// } catch (Exception e) {
	// return bookList;
	// }
	// return bookList;
	// }
	//
	// public static ArrayList<AD> getCatagoryAdList() {
	// ArrayList<AD> bookList = new ArrayList<AD>();
	// String url = Constants.CATAGORY_AD_LIST;
	// String content = HttpManager.getUrlContent(url, "utf-8");
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new AdXMLHandler(bookList));
	// } catch (Exception e) {
	// return bookList;
	// }
	// return bookList;
	// }
	//
	// public static ArrayList<AD> getLatestAdList() {
	// ArrayList<AD> bookList = new ArrayList<AD>();
	// String url = Constants.BOOTTOM_AD_LIST;
	// String content = HttpManager.getUrlContent(url, "utf-8");
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new AdXMLHandler(bookList));
	// } catch (Exception e) {
	// return bookList;
	// }
	// return bookList;
	//
	// }
	//
	// public static ArrayList<Book> getCheckBook(String code) {
	// ArrayList<Book> list = new ArrayList<Book>();
	// String url = Constants.CHECK_BOOK + code;
	// String content = HttpManager.getUrlContent(url, "utf-8");
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new BookListXMLHandler(list));
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// return list;
	// }
	//
	// public static Book getBookDetail(String bookId, Context context) {
	// Book bookDetail = new Book();
	// String url = Constants.BOOK_DETAIL + bookId + "&userId="
	// + new VipUserCache(context).getUserId();
	// String content = HttpManager.getUrlContent(url, "utf-8");
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new BookDetailXMLHandler(bookDetail));
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// return bookDetail;
	// }
	// public static Book getBookDetail(String content) {
	// Book bookDetail = new Book();
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new BookDetailXMLHandler(bookDetail));
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// return bookDetail;
	// }
	// public static ArrayList<Book> getRelBookDetail(String bookid) {
	// ArrayList<Book> list = new ArrayList<Book>();
	// String url = Constants.BOOK_DETAIL_REL + bookid;
	// String content = HttpManager.getUrlContent(url, "utf-8");
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new BookListXMLHandler(list));
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// return list;
	// }
	//
	// *//**
	// * @param bookid
	// * 获取书籍详情评论
	// *//*
	// public static BookCommentsList getBookDetailComment(int page, String id)
	// {
	// String url = Constants.BOOK_DETAIL_ALLCOMMENTS + "&start=" + page
	// + "&pageSize=" + Constants.pageSize + "&bookId=" + id;
	// BookCommentsList list = new BookCommentsList();
	// String content = HttpManager.getUrlContent(url, "utf-8");
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new BookCommentsXMLHandler(list));
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// return list;
	//
	// }
	//
	// public static BookCommentsList getBookDetailShowComment(int page, String
	// id) {
	// String url = Constants.BOOK_DETAIL_ALLCOMMENTS + "&start=" + page
	// + "&pageSize=3&bookId=" + id;
	// BookCommentsList list = new BookCommentsList();
	// String content = HttpManager.getUrlContent(url, "utf-8");
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new BookCommentsXMLHandler(list));
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// return list;
	//
	// }
	// public static BookCommentsList getBookDetailShowComment( String content)
	// {
	// BookCommentsList list = new BookCommentsList();
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new BookCommentsXMLHandler(list));
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// return list;
	//
	// }
	//
	// public static ArrayList<Book> getBookList(int page, String id) {
	// ArrayList<Book> list = new ArrayList<Book>();
	// String url = Constants.SUBJECT_SUB_LIST + "&start=" + page
	// + "&pageSize=" + Constants.pageSize + "&columnId=" + id;
	// String content = HttpManager.getUrlContent(url, "utf-8");
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new BookListXMLHandler(list));
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// return list;
	// }
	//
	// public static ArrayList<Book> getRankingBestsellerList(int page) {
	// ArrayList<Book> list = new ArrayList<Book>();
	// String url = Constants.RANKING_BESTSELLER_LIST + "&start=" + page
	// + "&pageSize=" + Constants.pageSize;
	// String content = HttpManager.getUrlContent(url, "utf-8");
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new BookListXMLHandler(list));
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// return list;
	// }
	//
	// public static ArrayList<Book> getRankingPraisedList(int page) {
	// ArrayList<Book> list = new ArrayList<Book>();
	// String url = Constants.RANKING_PRAISED_LIST + "&start=" + page
	// + "&pageSize=" + Constants.pageSize;
	// String content = HttpManager.getUrlContent(url, "utf-8");
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new BookListXMLHandler(list));
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// return list;
	// }
	//
	// public static ArrayList<Book> getRankingLatestList(int page) {
	// ArrayList<Book> list = new ArrayList<Book>();
	// String url = Constants.RANKING_LATEST_LIST + "&start=" + page
	// + "&pageSize=" + Constants.pageSize;
	// String content = HttpManager.getUrlContent(url, "utf-8");
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new BookListXMLHandler(list));
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// return list;
	// }
	//
	// public static ArrayList<Book> getRankingFreeList(int page) {
	// ArrayList<Book> list = new ArrayList<Book>();
	// String url = Constants.RANKING_FREE_LIST + "&start=" + page
	// + "&pageSize=" + Constants.pageSize;
	// String content = HttpManager.getUrlContent(url, "utf-8");
	// if (StringUtils.isEmpty(content))
	// return null;
	// ByteArrayInputStream is;
	// try {
	// is = new ByteArrayInputStream(content.getBytes("utf-8"));
	// decodeModel(is, new BookListXMLHandler(list));
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// return list;
	// }
	/**
	 * @return 书库分类
	 */
	public static ArrayList<BookCategory> getCategoryList() {
		ArrayList<BookCategory> list = new ArrayList<BookCategory>();
		String url = Constants.CATEGORY_LIST;
		String content = HttpManager.getUrlContent(url, "utf-8");
		if (StringUtils.isEmpty(content))
			return null;
		ByteArrayInputStream is;
		try {
			is = new ByteArrayInputStream(content.getBytes("utf-8"));
			decodeModel(is, new CategoryXMLHandler(list));
		} catch (Exception e) {
			return list;
		}
		return list;
	}

	/**
	 * @return top
	 */
	public static ArrayList<Book> getBookListByTop(String category) {
		ArrayList<Book> bookList = new ArrayList<Book>();
		String url = Constants.BOOKLIST_BYTOP + category;
		String content = HttpManager.getUrlContent(url, "utf-8");
		if (StringUtils.isEmpty(content))
			return null;
		ByteArrayInputStream is;
		try {
			is = new ByteArrayInputStream(content.getBytes("utf-8"));
			decodeModel(is, new BookListXMLHandler(bookList));
		} catch (Exception e) {
			return bookList;
		}
		return bookList;
	}

	public static ArrayList<Book> getBookListByCatagoryVipType(String category,
			String userId) {
		ArrayList<Book> bookList = new ArrayList<Book>();
		String url = Constants.BOOKLIST_ByCatagoryVipType + category
				+ "&userId=" + userId;
		String content = HttpManager.getUrlContent(url, "utf-8");
		Log.e("czb", content);
		if (StringUtils.isEmpty(content))
			return null;
		ByteArrayInputStream is;
		try {
			is = new ByteArrayInputStream(content.getBytes("utf-8"));
			decodeModel(is, new BookListXMLHandler(bookList));
		} catch (Exception e) {
			return bookList;
		}
		return bookList;
	}

//	public static ArrayList<Book> getBookListByDetail(String userId,
//			String bookId) {
//		ArrayList<Book> bookList = new ArrayList<Book>();
//		String url = Constants.BOOK_DETAIL + "?bookShelfId=" + 3 + "&bookId="
//				+ bookId + "&special=false" + "&officeId=" + 40;
//		String content = HttpManager.getUrlContent(url, "utf-8");
//		if (StringUtils.isEmpty(content))
//			return null;
//		ByteArrayInputStream is;
//		try {
//			is = new ByteArrayInputStream(content.getBytes("utf-8"));
//			decodeModel(is, new BookListXMLHandler(bookList));
//		} catch (Exception e) {
//			return bookList;
//		}
//		return bookList;
//	}
	
	//json数据返回
	public static void getBookListByDetail(final Handler handler,String bookId,final Context context) {
		AsyncHttpClient client = new AsyncHttpClient(context);
		RequestParams params = new RequestParams();
		params.put("bookShelfId","2");
		params.put("bookId",bookId);
		client.post("http://szcblm.horner.cn:8080/alliance/front/book/getCategoryByBookId",
				params,  new AsyncHttpResponseHandler(){
			private Message msg;

			@Override
			public void onSuccess(String result) {
				super.onSuccess(result);
				if (!StringUtils.isEmpty(result)) {
					try {
						msg = Message.obtain();
						msg.what = 2001;
						JSONObject object = new JSONObject(result);
						String code = object.optString("code");
						if (code != null && code.equals("1000")) {
							String categoryId = object.optString("categoryId");
							msg.obj = categoryId;
						}else{
							ToastUtil.showShortToast(context, "获取数据失败,请重试");
							return;
						}
					} catch (JSONException e) {
						msg.what = 2002;
					}
					handler.sendMessage(msg);
				} else {
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				msg = Message.obtain();
				boolean internet = HttpManager
						.isConnectingToInternet((Activity) context);
				if (internet) {
					Toast.makeText(context, "获取微信订单信息失败,请重试。",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "请检查网络是否可用。",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public static ArrayList<Book> getBookListByCatagory(String category) {
		ArrayList<Book> bookList = new ArrayList<Book>();
		String url = Constants.BOOKLIST_ByCatagoryByCatagory + category;
		String content = HttpManager.getUrlContent(url, "utf-8");
		System.out.println("content的内容为;" + content);
		// Log.e("info","content的内容为;"+content);
		if (StringUtils.isEmpty(content))
			return null;
		ByteArrayInputStream is;
		try {
			is = new ByteArrayInputStream(content.getBytes("utf-8"));
			decodeModel(is, new BookListXMLHandler(bookList));
		} catch (Exception e) {
			return bookList;
		}
		return bookList;
	}

	public static void decodeModel(InputStream xmlStream, ContentHandler handler) {
		if (null == xmlStream) {
			return;
		}
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			XMLReader reader = factory.newSAXParser().getXMLReader();
			reader.setContentHandler(handler);// set content hander
			reader.parse(new InputSource(xmlStream));
		} catch (Exception ex) {
			Log.e("mouee", ex.getMessage(), ex);
		} finally {
			try {
				xmlStream.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * @return 包含二级分类数据
	 */
	/*
	 * public static ArrayList<BookMainCatagory> getCategoryMainList() {
	 * ArrayList<BookMainCatagory> list = new ArrayList<BookMainCatagory>();
	 * String url = Constants.CATEGORY_MAIN_LIST; String content =
	 * HttpManager.getUrlContent(url, "utf-8"); if
	 * (StringUtils.isEmpty(content)) return null; ByteArrayInputStream is; try
	 * { is = new ByteArrayInputStream(content.getBytes("utf-8"));
	 * decodeModel(is, new CategoryMainXMLHandler(list)); } catch (Exception e)
	 * { return list; } return list; } public static ArrayList<BookMainCatagory>
	 * getCategoryMainList(String content) { ArrayList<BookMainCatagory> list =
	 * new ArrayList<BookMainCatagory>(); if (StringUtils.isEmpty(content))
	 * return null; ByteArrayInputStream is; try { is = new
	 * ByteArrayInputStream(content.getBytes("utf-8")); decodeModel(is, new
	 * CategoryMainXMLHandler(list)); } catch (Exception e) { return list; }
	 * return list; }
	 *//**
	 * @return 书库专题
	 */
	/*
	 * public static ArrayList<Subject> getLeftSubjectList(String type) {
	 * ArrayList<Subject> list = new ArrayList<Subject>(); String url =
	 * Constants.BASEURL + "subjectbook/shelfSubject?shelfId=3&officeId=" +
	 * Constants.OFFICEID + "&type=" + type; String content =
	 * HttpManager.getUrlContent(url, "utf-8"); if
	 * (StringUtils.isEmpty(content)) return null; ByteArrayInputStream is; try
	 * { is = new ByteArrayInputStream(content.getBytes("utf-8"));
	 * decodeModel(is, new SubjectXMLHandler(list)); } catch (Exception e) {
	 * return list; } return list; }
	 * 
	 * public static ArrayList<Subject> getLeftSubjectContentList(String
	 * content) { ArrayList<Subject> list = new ArrayList<Subject>(); if
	 * (StringUtils.isEmpty(content)) return null; ByteArrayInputStream is; try
	 * { is = new ByteArrayInputStream(content.getBytes("utf-8"));
	 * decodeModel(is, new SubjectXMLHandler(list)); } catch (Exception e) {
	 * return list; } return list; }
	 *//**
	 * @param userId
	 * @return 购物车中的图书
	 */
	/*
	 * public static ArrayList<Book> getPurchaseBookList(String userId) {
	 * ArrayList<Book> list = new ArrayList<Book>(); String url =
	 * Constants.BOOK_PURCHASE + userId; String content =
	 * HttpManager.getUrlContent(url, "utf-8"); if
	 * (StringUtils.isEmpty(content)) return null; ByteArrayInputStream is; try
	 * { is = new ByteArrayInputStream(content.getBytes("utf-8"));
	 * decodeModel(is, new BookListXMLHandler(list)); } catch (Exception e) {
	 * e.printStackTrace(); return null; } return list; }
	 *//**
	 * @param userId
	 *            orderType 1表示时间 2表示书名
	 * @param i
	 * @return 已经购买的图书
	 */
	/*
	 * public static BookSum getHasPurchaseBookList(String userId, String type,
	 * int page) { BookSum bookSum = new BookSum(); String url =
	 * Constants.BASEURL + "book/getBuyBooksByuserId?shelfId=3&userId=" + userId
	 * + "&orderType=" + type + "&start=" + page + "&pageSize=" +
	 * Constants.pageSize; String content = HttpManager.getUrlContent(url,
	 * "utf-8"); if (StringUtils.isEmpty(content)) return null;
	 * ByteArrayInputStream is; try { is = new
	 * ByteArrayInputStream(content.getBytes("utf-8")); decodeModel(is, new
	 * BookHasBuyListXMLHandler(bookSum)); } catch (Exception e) {
	 * e.printStackTrace(); return null; } return bookSum; } public static
	 * BookSum getHasPurchaseBookList(String content) { BookSum bookSum = new
	 * BookSum(); if (StringUtils.isEmpty(content)) return null;
	 * ByteArrayInputStream is; try { is = new
	 * ByteArrayInputStream(content.getBytes("utf-8")); decodeModel(is, new
	 * BookHasBuyListXMLHandler(bookSum)); } catch (Exception e) {
	 * e.printStackTrace(); return null; } return bookSum; }
	 * 
	 * public static ArrayList<Book> getCollectionBookList(String userId, int
	 * page) { ArrayList<Book> list = new ArrayList<Book>(); String url =
	 * Constants.BASEURL + "userBook/getUserFavoriteBooks?shelfId=" +
	 * Constants.shelfId + "&userId=" + userId + "&start=" + page + "&officeId="
	 * + Constants.OFFICEID + "&pageSize=" + Constants.pageSize; String content
	 * = HttpManager.getUrlContent(url, "utf-8"); if
	 * (StringUtils.isEmpty(content)) return null; ByteArrayInputStream is; try
	 * { is = new ByteArrayInputStream(content.getBytes("utf-8"));
	 * decodeModel(is, new BookListXMLHandler(list)); } catch (Exception e) {
	 * e.printStackTrace(); return null; } return list; }
	 * 
	 * public static ArrayList<Book> getBookCatagoryList(String id) {
	 * ArrayList<Book> list = new ArrayList<Book>(); String url =
	 * Constants.SUB_CATEGORY_LIST + id; String content =
	 * HttpManager.getUrlContent(url, "utf-8"); if
	 * (StringUtils.isEmpty(content)) return null; ByteArrayInputStream is; try
	 * { is = new ByteArrayInputStream(content.getBytes("utf-8"));
	 * decodeModel(is, new BookListXMLHandler(list)); } catch (Exception e) {
	 * e.printStackTrace(); return null; } return list; }
	 *//**
	 * @param id
	 * @param type
	 *            orderType参数值：11表示最新、22表示销量、33表示好评、44表示价格
	 * @return 分类书籍
	 */
	/*
	 * public static ArrayList<Book> getBookCatagoryByOrderList(String id,
	 * String type, int page) { ArrayList<Book> list = new ArrayList<Book>();
	 * String url = Constants.SUB_CATEGORY_LIST + id + "&orderType=" + type +
	 * "&start=" + page + "&pageSize=" + Constants.pageSize; String content =
	 * HttpManager.getUrlContent(url, "utf-8"); if
	 * (StringUtils.isEmpty(content)) return null; ByteArrayInputStream is; try
	 * { is = new ByteArrayInputStream(content.getBytes("utf-8"));
	 * decodeModel(is, new BookListXMLHandler(list)); } catch (Exception e) {
	 * e.printStackTrace(); return null; } return list; }
	 * 
	 * public static ArrayList<Book> getBookChildCatagoryByOrderList(String id,
	 * String type, int page) { ArrayList<Book> list = new ArrayList<Book>();
	 * String url = Constants.SUB_CHILD_CATEGORY_LIST + id + "&orderType=" +
	 * type + "&start=" + page + "&pageSize=" + Constants.pageSize; String
	 * content = HttpManager.getUrlContent(url, "utf-8"); if
	 * (StringUtils.isEmpty(content)) return null; ByteArrayInputStream is; try
	 * { is = new ByteArrayInputStream(content.getBytes("utf-8"));
	 * decodeModel(is, new BookListXMLHandler(list)); } catch (Exception e) {
	 * e.printStackTrace(); return null; } return list; }
	 * 
	 * public static ArrayList<Book> getThemeSubjectBookList(int page, String
	 * id) { ArrayList<Book> list = new ArrayList<Book>(); String url =
	 * Constants.BASEURL + "subjectbook/getBooksBySubject?subjectId=" + id +
	 * "&start=" + page + "&pageSize=" + Constants.pageSize; String content =
	 * HttpManager.getUrlContent(url, "utf-8"); if
	 * (StringUtils.isEmpty(content)) return null; ByteArrayInputStream is; try
	 * { is = new ByteArrayInputStream(content.getBytes("utf-8"));
	 * decodeModel(is, new BookListXMLHandler(list)); } catch (Exception e) {
	 * e.printStackTrace(); return null; } return list; }
	 */
}
