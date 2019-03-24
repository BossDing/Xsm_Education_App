package com.horner.xsm.data;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;

import com.horner.xsm.bean.Book;
import com.mouee.common.DataUtils;
import com.umeng.socialize.utils.Log;

/**
 * 执行与数据库相关的操作
 * 
 * @author Administrator
 * 
 */
public class DataBase {

	public static final String Guner_Book_Name = "xsm_book";
	// 搜索记录表
	private static final String Delete_Record_SQL = "delete  from xsm_book where BOOKID = ?";
	private static final String Selected_Record_SQL = "select * from xsm_book where USERID = ?";
	private static final String IsCheck_SQL = "select * from xsm_book where BOOKID = ?";
	private static final String Guner_Tab_Create_SQL = "create table xsm_book(bookorder INTEGER,BOOKID TEXT,COVERURL TEXT,LOCALPATH Text,STATE Text,BOOKNAME TEXT,USERID TEXT,VERID TEXT,DOWNPROCESS INTEGER)";
	private static final String Selected_localpath_SQL = "select LOCALPATH from xsm_book where BOOKID = ?";

	/**
	 * 执行数据库的表检测以及建表操作
	 */
	public static void initDataBase(Activity activity) {
		detectAndCreateBookTable(activity);
	}

	/**
	 * 检查书籍表是否存在，如果不存在那就重新创建
	 * 
	 * @param activity
	 */
	public static synchronized void detectAndCreateBookTable(Activity activity) {

		boolean result = DataUtils.checkTableExists(activity, Guner_Book_Name);
		if (!result) {
			try {
				// 创建书籍表
				DataUtils.execSQL(activity, Guner_Tab_Create_SQL);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * @param activity
	 * @param text
	 *            删除一条搜索记录
	 */
	public static void deleteSearchRecord(Activity activity, String bookID) {
		try {
			DataUtils.execSQL(activity, Delete_Record_SQL,
					new String[] { bookID });
		} catch (Exception e) {
		}
	}

	/**
	 * @param activity
	 * @param text
	 *            插入一条搜索记录
	 */
	public static void insertSearchRecord(Activity activity,
			Book zine, String localPath) {
		try {
			ContentValues values = new ContentValues();
			values.put("BOOKID", zine.mBookID);
			values.put("COVERURL", zine.mCoverurl);
			values.put("LOCALPATH", localPath);
			values.put("BOOKNAME", zine.mName);
			values.put("USERID", "1");
			values.put("VERID", zine.mVerid);
			DataUtils.insertTableValue(activity, Guner_Book_Name, null, values);
		} catch (Exception e) {
		}
	}

	/**
	 * @param activity
	 * @return 获取搜索记录
	 */
	public static ArrayList<BookDetailEntity> getSearchRecord(
			Activity activity, String userId) {
		ArrayList<BookDetailEntity> list = new ArrayList<BookDetailEntity>();
		Cursor cursor = null;
		try {
			cursor = DataUtils.rawQuery(activity, Selected_Record_SQL, userId);
			while (cursor.moveToNext()) {
				String bookid = cursor.getString(cursor
						.getColumnIndex("BOOKID"));
				String iconPath = cursor.getString(cursor
						.getColumnIndex("COVERURL"));
				String bookPath = cursor.getString(cursor
						.getColumnIndex("LOCALPATH"));
				String bookname = cursor.getString(cursor
						.getColumnIndex("BOOKNAME"));
				String userID = cursor.getString(cursor
						.getColumnIndex("USERID"));
				String verID = cursor.getString(cursor.getColumnIndex("VERID"));
				String state = cursor.getString(cursor.getColumnIndex("STATE"));
				if (state == null) {
					state = "";
				}
				if (bookPath == null) {
					bookPath = "";
				}
				BookDetailEntity searchRecord = new BookDetailEntity(bookid,
						bookname, "", "", "", "", "", "", iconPath, "", "", "",
						"", bookPath, state, verID, userID, null);
				list.add(searchRecord);
			}
			Collections.reverse(list);
		} catch (Exception e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}
	
	
	public static String getLocalPath(
			Activity activity, String bookId) {
		ArrayList<BookDetailEntity> list = new ArrayList<BookDetailEntity>();
		Cursor cursor = null;
		String bookPath = null;
		try {
			cursor = DataUtils.rawQuery(activity, Selected_localpath_SQL, bookId);
			while (cursor.moveToNext()) {
				bookPath = cursor.getString(cursor
						.getColumnIndex("LOCALPATH"));
			}
			Collections.reverse(list);
		} catch (Exception e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return bookPath;
	}

	/**
	 * 判断书籍是否在表中存在
	 * 
	 * @param activity
	 * @param bookId
	 * @return
	 */
	public static boolean checkIsExist(Activity activity, String bookId) {
		boolean flag = false;
		Cursor cursor = cursor = DataUtils.rawQuery(
				activity,
				IsCheck_SQL,
				new String[] { bookId});

		try {
			if (cursor.moveToNext())//
			{
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return flag;
	}

	/**
	 * 修改书籍下载状态
	 * 
	 * @param tid
	 * @param state
	 * @param activity
	 */
	public static void reSetState(String tid, String state, Activity activity) {
		try {
			ContentValues cv = new ContentValues();
			cv.put("STATE", state);
			String where = "BOOKID=?";
			String[] whereValue = { tid };
			DataUtils.update(activity, "xsm_book", cv, where, whereValue);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	/**
	 * 修改书籍下载进度
	 * 
	 * @param tid
	 * @param state
	 * @param activity
	 */
	public static void reSetProcess(String tid, int process, Activity activity) {
		try {
			ContentValues cv = new ContentValues();
			cv.put("DOWNPROCESS", process);
			String where = "BOOKID=?";
			String[] whereValue = { tid };
			DataUtils.update(activity, "xsm_book", cv, where, whereValue);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	/**
	 * 修改书籍本地地址
	 * 
	 * @param tid
	 * @param state
	 * @param activity
	 */
	public static void reSetLocalPath(String tid, String localPath,
			Activity activity) {
		try {
			ContentValues cv = new ContentValues();
			cv.put("LOCALPATH", localPath);
			String where = "BOOKID=?";
			String[] whereValue = { tid };
			DataUtils.update(activity, "xsm_book", cv, where, whereValue);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

}
