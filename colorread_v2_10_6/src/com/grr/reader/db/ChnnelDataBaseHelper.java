package com.grr.reader.db;

import com.colorread.colorread.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ChnnelDataBaseHelper extends SQLiteOpenHelper{
	private static String  NAME="chnnel.db";
	private static int VERSION = 1;

	public ChnnelDataBaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	public ChnnelDataBaseHelper(Context context){
		this(context, NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table chnnel(_id  integer primary key autoincrement,chnnel varchar,link varchar,img int,isadd varchar default 'false')");
		ContentValues values = new ContentValues();
		values.put("chnnel", "国内新闻");
		values.put("link", "http://www.xinhuanet.com/politics/news_politics.xml");
		values.put("img", R.drawable.guonei_s);
		values.put("isadd", "true");
		db.insert("chnnel", null, values);
		values = new ContentValues();
		values.put("chnnel", "国际新闻");
		values.put("link", "http://www.xinhuanet.com/world/news_world.xml");
		values.put("img", R.drawable.guoji_s);
		db.insert("chnnel", null, values);
		values.put("isadd", "true");
		values = new ContentValues();
		values.put("chnnel", "科技新闻");
		values.put("link", "http://www.cnmo.com/rss/doc.xml");
		values.put("img", R.drawable.keji_s);
		values.put("isadd", "true");
		db.insert("chnnel", null, values);
		values = new ContentValues();
		values.put("chnnel", "财经新闻");
		values.put("link", "http://www.xinhuanet.com/fortune/news_fortune.xml");
		values.put("img", R.drawable.caijing_s);
		values.put("isadd", "true");
		db.insert("chnnel", null, values);
		values = new ContentValues();
		values.put("chnnel", "体育新闻");
		values.put("link", "http://www.people.com.cn/rss/sports.xml");
		values.put("img", R.drawable.tiyu_s);
		values.put("isadd", "true");
		db.insert("chnnel", null, values);
		values = new ContentValues();
		values.put("chnnel", "娱乐新闻");
		values.put("link", "http://www.people.com.cn/rss/ent.xml");
		values.put("img", R.drawable.yule_s);
		values.put("isadd", "true");
		db.insert("chnnel", null, values);
		values = new ContentValues();
		values.put("chnnel", "海峡两岸");
		values.put("link", "http://www.people.com.cn/rss/haixia.xml");
		values.put("img", R.drawable.haixia_s);
		db.insert("chnnel", null, values);
		values = new ContentValues();
		values.put("chnnel", "求真");
		values.put("link", "http://www.people.com.cn/rss/235996.xml");
		values.put("img", R.drawable.qiuzhen_s);
		db.insert("chnnel", null, values);
		values = new ContentValues();
		values.put("chnnel", "军事新闻");
		values.put("link", "http://www.xinhuanet.com/mil/news_mil.xml");
		values.put("img", R.drawable.junshi_s);
		db.insert("chnnel", null, values);
	}
	
	public void TotalChnnel(SQLiteDatabase db){
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
