package com.colorread.colorread.utils;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.colorread.colorread.bean.XmlNewsBean;
import com.grr.reader.db.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class XMLParser {
	static List<XmlNewsBean> list = new ArrayList<XmlNewsBean>();
	private static DataBaseHelper helper;
	private static  String chnnel;
	private static String xml;
	private static Context context;
	
	//创建构造函数，方便传值
	public XMLParser(String xml,String chnnel,Context context){
		XMLParser.xml=xml;
		XMLParser.chnnel = chnnel;
		XMLParser.context =context;
	}

	public static List<XmlNewsBean> getXmlText() {
		//每次解析的时候将list清空，防止上次的结果仍然存在list中
		list.clear();
		helper = new DataBaseHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		//每次解析的时候删除传入chnnel的所有数据，保留其他chnnel的数据。
		deleteByChnnel(db);
		XmlNewsBean news = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser pullParser = factory.newPullParser();
			pullParser.setInput(new StringReader(xml));
			int eventType = pullParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					String nodeName = pullParser.getName();
					if (nodeName.equals("item")) {
					}
					if (nodeName.equals("title")) {
						String title = pullParser.nextText();
						news = new XmlNewsBean();
						news.setTitle(title);
					}
					if (nodeName.equals("link")) {
						news.setLink(pullParser.nextText());
					}
					if (nodeName.equals("pubDate")) {
						String beautiful = "";
						String temp = pullParser.nextText();
						if(temp.contains("+")){
							//如果是手机中国进行格式解析
							beautiful  = ParserTime(temp);
						}else if(temp.contains("-")){
							//人民网默认
							beautiful=temp;
						}
						news.setPubDate(beautiful);
					}
					if (nodeName.equals("description")) {
						news.setSource(pullParser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					String nodeName1 = pullParser.getName();
					if (nodeName1.equals("item")) {
						//解析成功之后添加进入数据库。
						ContentValues values = new ContentValues();
						values.put("title", news.getTitle());
						values.put("link", news.getLink());
						if(news.getPubDate()!=null){
						values.put("pubDate", news.getPubDate());
						}else{
							//新华网直接放置来源吧，没有时间伤不起
							values.put("pubDate", "新华网");
						}
						values.put("chnnel", chnnel);
						db.insert("xml", null, values);
						list.add(news);
					}
				default:
					break;
				}
				eventType = pullParser.next();
			}
		} catch (Exception e) {

		}
		return list;
	}
	
	private static String ParserTime(String date) {
		Date date1 = new Date(date);
		SimpleDateFormat myFromat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss") ;
		return  myFromat.format(date1);
	}

	public static void deleteByChnnel(SQLiteDatabase db){
		db.delete("xml", "chnnel=?", new String[]{chnnel});
		Toast.makeText(context, "刷新成功", 0).show();
	}
	

}
