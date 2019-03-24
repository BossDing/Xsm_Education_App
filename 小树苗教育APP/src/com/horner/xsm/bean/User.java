package com.horner.xsm.bean;

public class User {
	private String USERID;
	private String USERNAME;
	private String USERNICKNAME;
	private String email;
	private String password;
	private String userType;
	private String unitId;
	private String udid;
	private String code;
	private String userStatus;
	private String lastlogintime;
	private String UNITNAME;
	private String UNITCOVER;
	private String phonenum;

	public String getPhonenum() {
		return phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUSERID() {
		return USERID;
	}

	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}

	public String getUSERNAME() {
		return USERNAME;
	}

	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}

	public String getUSERNICKNAME() {
		return USERNICKNAME;
	}

	public void setUSERNICKNAME(String uSERNICKNAME) {
		USERNICKNAME = uSERNICKNAME;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getLastlogintime() {
		return lastlogintime;
	}

	public void setLastlogintime(String lastlogintime) {
		this.lastlogintime = lastlogintime;
	}

	public String getUNITNAME() {
		return UNITNAME;
	}

	public void setUNITNAME(String uNITNAME) {
		UNITNAME = uNITNAME;
	}

	public String getUNITCOVER() {
		return UNITCOVER;
	}

	public void setUNITCOVER(String uNITCOVER) {
		UNITCOVER = uNITCOVER;
	}

	public User(String uSERID, String uSERNAME, String uSERNICKNAME,
			String phone, String userType, String unitId, String udid,
			String code, String userStatus, String lastlogintime,
			String uNITNAME, String uNITCOVER) {
		super();
		USERID = uSERID;
		USERNAME = uSERNAME;
		USERNICKNAME = uSERNICKNAME;
		this.email = phone;
		this.userType = userType;
		this.unitId = unitId;
		this.udid = udid;
		this.code = code;
		this.userStatus = userStatus;
		this.lastlogintime = lastlogintime;
		UNITNAME = uNITNAME;
		UNITCOVER = uNITCOVER;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "User [USERID=" + USERID + ", USERNAME=" + USERNAME
				+ ", USERNICKNAME=" + USERNICKNAME + ", email=" + email
				+ ", userType=" + userType + ", unitId=" + unitId + ", udid="
				+ udid + ", code=" + code + ", userStatus=" + userStatus
				+ ", lastlogintime=" + lastlogintime + ", UNITNAME=" + UNITNAME
				+ ", UNITCOVER=" + UNITCOVER + "]";
	}

}
