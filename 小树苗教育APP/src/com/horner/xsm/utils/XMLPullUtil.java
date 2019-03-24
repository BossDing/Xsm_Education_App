package com.horner.xsm.utils;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * xml解析工具
 * 
 * @author zhaozhichao
 * 
 */

public class XMLPullUtil {

	/**
	 * 用户登录解析
	 */
	public static com.horner.xsm.bean.User getUser(InputStream open) {
		com.horner.xsm.bean.User entity = null;
		XmlPullParserFactory factory;
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(open, "UTF-8");
			int evtType = xpp.getEventType();
			// 一直循环，直到文档结束
			while (evtType != XmlPullParser.END_DOCUMENT) {

				String tag = xpp.getName();
				switch (evtType) {
				case XmlPullParser.START_TAG:
					if (tag.equals("USER")) {
						entity = new com.horner.xsm.bean.User();
					}
					if (tag.equals("USERID")) {
						entity.setUSERID(xpp.nextText());
					}
					if (tag.equals("USERNAME")) {
						entity.setUSERNAME(xpp.nextText());
					}
					if (tag.equals("USERNICKNAME")) {
						entity.setUSERNICKNAME(xpp.nextText());
					}
					if (tag.equals("USEREMAIL")) {
						entity.setEmail(xpp.nextText());
					}
					if (tag.equals("userType")) {
						entity.setUserType(xpp.nextText());
					}
					if (tag.equals("unitId")) {
						entity.setUnitId(xpp.nextText());
					}
					if (tag.equals("udid")) {
						entity.setUdid(xpp.nextText());
					}
					if (tag.equals("userStatus")) {
						entity.setUserStatus(xpp.nextText());
					}
					if (tag.equals("lastlogintime")) {
						entity.setLastlogintime(xpp.nextText());
					}
					if (tag.equals("UNITNAME")) {
						entity.setUNITNAME(xpp.nextText());
					}
					if (tag.equals("UNITCOVER")) {
						entity.setUNITCOVER(xpp.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					if (entity != null && tag.equals("USER")) {
					}
					break;
				default:
					break;
				}
				evtType = xpp.next();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entity;
	}

	public static String getStatus(InputStream open) {
		String status = null;
		XmlPullParserFactory factory;
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(open, "UTF-8");
			int evtType = xpp.getEventType();
			// 一直循环，直到文档结束
			while (evtType != XmlPullParser.END_DOCUMENT) {

				String tag = xpp.getName();
				switch (evtType) {
				case XmlPullParser.START_TAG:
					if (tag.equals("STATUS")) {
						status = xpp.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					if (status != null && tag.equals("STATUS")) {
					}
					break;
				default:
					break;
				}
				evtType = xpp.next();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}

}
