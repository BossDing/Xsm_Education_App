package com.horner.xsm.bean;

import java.io.Serializable;

/**
 * 书库分类
 * 
 */
public class BookCategory implements Serializable{
	public String mCatagoryCode; 
	public String mCatagoryName;// 类名
	public String mIcon; // icon
  public String mCatCode;
    public String mParentId;
  public String mSum;
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		BookCategory b = (BookCategory) o;
		return mCatagoryCode.equals(mCatagoryCode);
	}
}
