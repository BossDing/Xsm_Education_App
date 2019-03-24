package com.horner.xsm.data;

import java.util.ArrayList;
import java.util.Locale;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import twitter4j.ExtendedMediaEntity.Variant;

import android.util.Log;

import com.horner.xsm.bean.Book;

public class BookListXMLHandler implements ContentHandler {

	//private String value = "";
	
    private StringBuilder sb = new StringBuilder();  
    
	private String tagName = null;

	private ArrayList<Book> mModelList;
	private Book curBook;

	public BookListXMLHandler(ArrayList<Book> modelList) {
		this.mModelList = modelList;
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startElement(String arg0, String arg1, String arg2,
			Attributes arg3) throws SAXException {
		sb.setLength(0);
		this.tagName = arg1;
		if (tagName.toUpperCase(Locale.ENGLISH).equals("BOOK")) {
			curBook = new Book();
		} else if (tagName.equalsIgnoreCase("BOOKSUM")) {

		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		 //(4)原来在characters中取值，现改在此取值  
        String value = sb.toString();
		
		this.tagName = localName.toUpperCase(Locale.ENGLISH);
		if (tagName.equalsIgnoreCase("BOOK")) {
			mModelList.add(curBook);
		} else if (tagName.equalsIgnoreCase("BOOKID")) {
			curBook.mBookID = value;
		} else if (tagName.equalsIgnoreCase("BOOKNAME")) {
			curBook.mName = value;
		} else if (tagName.equalsIgnoreCase("VERID")) {
			curBook.mVerid = value;
		} else if (tagName.equalsIgnoreCase("VERSION")) {
			curBook.mVersion = value;
		} else if (tagName.equalsIgnoreCase("bookCode")) {
			curBook.mBookCode = value;
		} else if (tagName.equalsIgnoreCase("bookType")) {
			try {
				curBook.mBookType = Integer.valueOf(value);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else if (tagName.equalsIgnoreCase("resourceType")) {
			curBook.mResourceType = value;
		} else if (tagName.equalsIgnoreCase("publishType")) {
			curBook.mPublishType = value;
		} else if (tagName.equalsIgnoreCase("CATEGORYID")) {
			curBook.mCategoryid = value;
		} else if (tagName.equalsIgnoreCase("bookCatagoryId")) {
			curBook.mBookCatagoryId = value;
		} else if (tagName.equalsIgnoreCase("BUNDLEID")) {
			curBook.mBundleid = value;
		} else if (tagName.equalsIgnoreCase("bookKeywords")) {
			curBook.mBookKeywords = value;
		} else if (tagName.equalsIgnoreCase("BOOKAUTHORS")) {
			curBook.mBookauthors = value;
		} else if (tagName.equalsIgnoreCase("AUTHORSSUMMARY")) {
			curBook.mDesc = value;
		} else if (tagName.equalsIgnoreCase("DESCRIPTION")) {
			curBook.mDescription = value;
		} else if (tagName.equalsIgnoreCase("isbn")) {
			curBook.mIsbn = value;
		} else if (tagName.equalsIgnoreCase("PUBLISHDT")) {
			curBook.mPublishdt = value;
		} else if (tagName.equalsIgnoreCase("PUBLISHERNAME")) {
			curBook.mPublishername = value;
		} else if (tagName.equalsIgnoreCase("PRICE")) {
			curBook.mPrice = value;
		} else if (tagName.equalsIgnoreCase("iphonePrice")) {
			curBook.mIphonePrice = value;
		} else if (tagName.equalsIgnoreCase("IPADPRICE")) {
			curBook.mIpadprice = value;
		} else if (tagName.equalsIgnoreCase("PROMOTIONPRICE")) {
			curBook.mPromotionPrice = value;
		} else if (tagName.equalsIgnoreCase("COVERURL")) {
			curBook.mCoverurl = value;
			Log.i("info","val的数值为:"+value);
		} else if (tagName.equalsIgnoreCase("BOOKURL")) {
			curBook.mBookurl = value;
		} else if (tagName.equalsIgnoreCase("SIZE")) {
			curBook.mSize = value;
		} else if (tagName.equalsIgnoreCase("officeId")) {
			curBook.mOfficeId = value;
		} else if (tagName.equalsIgnoreCase("updatetime")) {
			curBook.mUpdatetime = value;
		} else if (tagName.equalsIgnoreCase("updater")) {
			curBook.mUpdater = value;
		} else if (tagName.equalsIgnoreCase("bookState")) {
			curBook.mBookState = value;
		} else if (tagName.equalsIgnoreCase("checkId")) {
			curBook.mCheckId = value;
		} else if (tagName.equalsIgnoreCase("memo")) {
			curBook.mMemo = value;
		} else if (tagName.equalsIgnoreCase("platform")) {
			curBook.mPlatform = value;
		} else if (tagName.equalsIgnoreCase("seq")) {
			curBook.mSeq = value;
		} else if (tagName.equalsIgnoreCase("CATANAME")) {
			curBook.mCataname = value;
		} else if (tagName.equalsIgnoreCase("COMMENTSUM")) {
			curBook.mCommentsum = value;
		} else if (tagName.equalsIgnoreCase("FREE")) {
			curBook.mFree = value;
		} else if (tagName.equalsIgnoreCase("WORDNUMBER")) {
			curBook.mWordnumber = value;
		} else if (tagName.equalsIgnoreCase("PROBATIONRATE")) {
			try {
				curBook.mProbationRate = Integer.valueOf(value.trim());
			} catch (Exception e) {
			}
		} else if (tagName.equalsIgnoreCase("NOWPRICE")) {
			curBook.mNowPrice = value;
		} else if (tagName.equalsIgnoreCase("purchaseTime")) {
			curBook.mPurchaseTime = value;
		}
		value = "";
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		 sb.append(ch, start, length);  
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub

	}

}
