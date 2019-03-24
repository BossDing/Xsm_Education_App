package com.horner.xsm.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author 作者 : sun
 * @date 创建时间：2015-12-31 下午5:46:49
 * @version 1.0
 * @parameter
 * @return
 */

public class UserCache {
	private SharedPreferences userCacheconfiure;
	private static final String USERCONFIURENAME = "userconfiurename";
	public final SharedPreferences.Editor userEditor;

	/**
	 * 
	 * 
	 * @param context
	 */
	@SuppressLint("CommitPrefEdits")
	public UserCache(Context context) {
		userCacheconfiure = context.getSharedPreferences(USERCONFIURENAME,
				Context.MODE_PRIVATE);
		userEditor = userCacheconfiure.edit();
	}

	// 登录用户id
	public void setUserId(String thirdId) {
		userEditor.putString("userId", thirdId);
		userEditor.commit();
	}

	public String getUserId() {
		return userCacheconfiure.getString("userId", "");
	}

	// 登录用户会员等级
	public void setVipType(String vipType) {
		userEditor.putString("vipType", vipType);
		userEditor.commit();
	}

	public String getVipType() {
		return userCacheconfiure.getString("vipType", "");
	}

	// 登录用户会员开始时间
	public void setVipTypeStartTime(String startTime) {
		userEditor.putString("startTime", startTime);
		userEditor.commit();
	}

	public String getVipTypeStartTime() {
		return userCacheconfiure.getString("startTime", "");
	}

	// 登录用户会员结束时间
	public void setVipTypeEndTime(String endTime) {
		userEditor.putString("endTime", endTime);
		userEditor.commit();
	}

	public String getVipTypeEndTime() {
		return userCacheconfiure.getString("endTime", "");
	}

	// 用户注册时的手机号
	public void setUserPhone(String phone) {
		userEditor.putString("phone", phone);
		userEditor.commit();
	}

	public String getUserPhone() {
		return userCacheconfiure.getString("phone", "");
	}

	// 是否登录
	public void setIsLogin(Boolean isLogin) {
		userEditor.putBoolean("isLogin", isLogin);
		userEditor.commit();
	}

	public String getIsLogin() {
		return userCacheconfiure.getString("islogin", "");
	}

	// 登录密码
	public void setPassword(String password) {
		userEditor.putString("password", password);
		userEditor.commit();
	}

	public String getPassword() {
		return userCacheconfiure.getString("password", "");
	}

	// 用户注册时的手机号
	public void setPhone(String phone) {
		userEditor.putString("phone", phone);
		userEditor.commit();
	}

	public String getPhone() {
		return userCacheconfiure.getString("phone", "");
	}

	// 用户三方绑定的微博号
	public void setWeiBoState(String weiboState) {
		userEditor.putString("weiboState", weiboState);
		userEditor.commit();
	}

	public String getWeiBoState() {
		return userCacheconfiure.getString("weiboState", "");
	}

	// 用户三方绑定的微信号
	public void setWeiChatState(String weixinState) {
		userEditor.putString("weixinState", weixinState);
		userEditor.commit();
	}

	public String getWeiChatState() {
		return userCacheconfiure.getString("weixinState", "");
	}

	// 用户是否购买
	public void setHasBuy(Boolean hasBuy) {
		userEditor.putBoolean("hasBuy", hasBuy);
		userEditor.commit();
	}

	public Boolean getHasBuy() {
		return userCacheconfiure.getBoolean("hasBuy", false);
	}

	// 用户以哪一种方式登录
	public void setLoginMethods(String loginMethods) {
		userEditor.putString("loginMethods", loginMethods);
		userEditor.commit();
	}

	public String getLoginMethods() {
		return userCacheconfiure.getString("loginMethods", "");
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

	// 地址
	public void setUserAddress(String userAddress) {
		userEditor.putString("userAddress", userAddress);
		userEditor.commit();
	}

	public String getUserAddress() {
		return userCacheconfiure.getString("userAddress", "");
	}

	// 性别
	public void setUserSex(String userSex) {
		userEditor.putString("userSex", userSex);
		userEditor.commit();
	}

	public String getUserSex() {
		return userCacheconfiure.getString("userSex", "");
	}

}
