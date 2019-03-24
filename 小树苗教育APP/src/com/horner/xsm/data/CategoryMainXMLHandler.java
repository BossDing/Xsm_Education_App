package com.horner.xsm.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.horner.xsm.bean.BookCategory;
import com.horner.xsm.bean.BookChildCategory;
import com.horner.xsm.bean.BookMainCatagory;

public class CategoryMainXMLHandler implements ContentHandler {

	private String val = "";
	private String tagName = null;
	private BookCategory curBook;
	private ArrayList<BookChildCategory> currentList;
	private BookChildCategory bookChildCategory;
	private ArrayList<BookMainCatagory> mList;
	private HashMap<String, ArrayList<BookChildCategory>> map;
	private BookMainCatagory bookMainCatagory;

	CategoryMainXMLHandler(ArrayList<BookMainCatagory> modelList) {
		this.mList = modelList;
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
		this.tagName = arg1;
		if (tagName.toUpperCase(Locale.ENGLISH).equals("CATAGORY")) {
			bookMainCatagory = new BookMainCatagory();
			curBook = new BookCategory();
			bookMainCatagory.setmCatagory(curBook);
			currentList = new ArrayList<BookChildCategory>();
			map = new HashMap<String, ArrayList<BookChildCategory>>();
			bookMainCatagory.setMap(map);
		}else if (tagName.toUpperCase(Locale.ENGLISH).equals("CHILD")) {
		    bookChildCategory = new BookChildCategory();
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		this.tagName = localName.toUpperCase(Locale.ENGLISH);
		if (tagName.equals("CATAGORY")) {
			mList.add(bookMainCatagory);
		} else if (tagName.equals("CATAGORYCODE")) {
			map.put(val, currentList);
			curBook.mCatagoryCode = val;
		} else if (tagName.equals("CATAGORYNAME")) {
			curBook.mCatagoryName = val;
		} else if (tagName.equals("CATAICON")) {
			curBook.mIcon =  val;
		} else if (tagName.equalsIgnoreCase("catCode")) {
			curBook.mCatCode =  val;
		} else if (tagName.equals("CATAGORYPARENTID")) {
			curBook.mParentId =  val;
		} else if (tagName.equals("SUM")) {
			curBook.mSum =  val;
		} else if (tagName.equals("CHILD")) {
			currentList.add(bookChildCategory);
		}else if (tagName.equals("CHILDCATID")) {
			bookChildCategory.mChildCatagoryCode = val;
		}else if (tagName.equals("CHILDCATNAME")) {
			bookChildCategory.mChildCatagoryName = val;
		}else if (tagName.equals("CHILDCATCODE")) {
			bookChildCategory.mChildCatCode = val;
		}else if (tagName.equals("CHILDLPARENTID")) {
			bookChildCategory.mChildParentId = val;
		}else if (tagName.equals("CHILDLCATICON")) {
			bookChildCategory.mChildIcon = val;
		}else if (tagName.equals("CHILDDESC")) {
			bookChildCategory.mChildDes = val;
		}else if (tagName.equals("CHILDSUM")) {
			bookChildCategory.mChildSum = val;
		}
		val = "";
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		val = new String(ch, start, length);
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
