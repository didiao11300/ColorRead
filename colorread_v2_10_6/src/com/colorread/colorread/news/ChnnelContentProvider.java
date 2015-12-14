package com.colorread.colorread.news;

import com.grr.reader.db.ChnnelDataBaseHelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ChnnelContentProvider extends ContentProvider {
	private final String TAG = "ChnnelContentProvider";
	private ChnnelDataBaseHelper helper;
	private static final UriMatcher Uri_matcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static final int QUERY = 1;
	private static final int UPDATE = 2;
	private static final int QUERYTRUE=3;

	static {
		Uri_matcher.addURI("com.colorread.colorread.news.ChnnelContentProvider", "query", QUERY);
		Uri_matcher.addURI("com.colorread.colorread.news.ChnnelContentProvider", "update", UPDATE);
	}

	@Override
	public boolean onCreate() {
		helper = new ChnnelDataBaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = null;
		if (Uri_matcher.match(uri) == QUERY) {
			cursor =  db.query("chnnel",projection, selection,  selectionArgs, sortOrder,null,null);
		}
		return cursor;
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int count = -1;
		SQLiteDatabase db = helper.getReadableDatabase();
		try {
			int flag = Uri_matcher.match(uri);
			switch (flag) {
			case UPDATE:
				count = db.update("chnnel", values, selection, selectionArgs);
				break;
			}
		} catch (Exception e) {
		}
		return count;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}


}
