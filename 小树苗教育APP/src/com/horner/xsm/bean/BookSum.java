package com.horner.xsm.bean;

import java.util.ArrayList;

public class BookSum {
	public String bookSum;
	public ArrayList<Book> bookList = new ArrayList<Book>();
	public String getBookSum() {
		return bookSum;
	}
	public void setBookSum(String bookSum) {
		this.bookSum = bookSum;
	}
	public ArrayList<Book> getBookList() {
		return bookList;
	}
	public void setBookList(ArrayList<Book> bookList) {
		this.bookList = bookList;
	}
}
