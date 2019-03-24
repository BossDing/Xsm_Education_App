package com.horner.xsm.data;

import java.util.ArrayList;
import java.util.Locale;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.horner.xsm.bean.BookCategory;

public class CategoryXMLHandler implements ContentHandler {

	private String val = "";
	private String tagName = null;

	private ArrayList<BookCategory> mModelList;
	private BookCategory curBook;

	CategoryXMLHandler(ArrayList<BookCategory> modelList) {
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
		this.tagName = arg1;
		if (tagName.toUpperCase(Locale.ENGLISH).equals("CATAGORY")) {
			curBook = new BookCategory();
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		this.tagName = localName.toUpperCase(Locale.ENGLISH);
		if (tagName.equals("CATAGORY")) {
			mModelList.add(curBook);
		} else if (tagName.equals("CATAGORYCODE")) {
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
