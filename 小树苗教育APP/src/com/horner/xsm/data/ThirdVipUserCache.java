package com.horner.xsm.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/*
 * 
 * @version 1.0
 * 
 */
public class ThirdVipUserCache {

	private SharedPreferences userCacheconfiure;
	private static final String USERCONFIURENAME = "userconfiurename";
	public final SharedPreferences.Editor userEditor;

	/**
	 * 
	 * 
	 * @param context
	 */
	@SuppressLint("CommitPrefEdits")
	public ThirdVipUserCache(Context context) {
		userCacheconfiure = context.getSharedPreferences(USERCONFIURENAME,
				Context.MODE_PRIVATE);
		userEditor = userCacheconfiure.edit();
	}

	// 登录性别
	public void setUserSex(String userSex) {
		userEditor.putString("userSex", userSex);
		userEditor.commit();
	}

	public String getUserSex() {
		return userCacheconfiure.getString("userSex", "");
	}

	// 登录地址
	public void setUserAddress(String userAddress) {
		userEditor.putString("userAddress", userAddress);
		userEditor.commit();
	}

	public String getUserAddress() {
		return userCacheconfiure.getString("userAddress", "");
	}

	// 登录id
	public void setThirdId(String thirdId) {
		userEditor.putString("thirdId", thirdId);
		userEditor.commit();
	}

	public String getThirdId() {
		return userCacheconfiure.getString("thirdId", "");
	}

	// 登录类型
	public void setThirdType(String thirdType) {
		userEditor.putString("thirdType", thirdType);
		userEditor.commit();
	}

	public String getThirdType() {
		return userCacheconfiure.getString("thirdType", "");
	}

	// 登录昵称
	public void setNickName(String name) {
		userEditor.putString("userNickName", name);
		userEditor.commit();
	}

	public String getNickName() {
		return userCacheconfiure.getString("userNickName", "");
	}

	// 登录头像
	public void setMultipartFile(String multipartFile) {
		userEditor.putString("multipartFile", multipartFile);
		userEditor.commit();
	}

	public String getMultipartFile() {
		return userCacheconfiure.getString("multipartFile", "");
	}

	// 出生日期
	public void setBirthDate(String birthDate) {
		userEditor.putString("birthDate", birthDate);
		userEditor.commit();
	}

	public String getBirthDate() {
		return userCacheconfiure.getString("birthDate", "");
	}

	// 三方登录是手机号的验证
	public void setPhone(String phone) {
		userEditor.putString("thirdPhone", phone);
		userEditor.commit();
	}

	public String getPhone() {
		return userCacheconfiure.getString("thirdPhone", "");
	}

}
