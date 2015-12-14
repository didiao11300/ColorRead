package com.colorread.colorread.news;

import com.colorread.colorread.R;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewCursorAdapter extends CursorAdapter{

	public GridViewCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView text= (TextView) view.findViewById(R.id.grid_text);
		ImageView img = (ImageView) view.findViewById(R.id.grid_image);
		text.setText(cursor.getString(cursor.getColumnIndex("chnnel")));
		img.setImageResource(cursor.getInt(cursor.getColumnIndex("img")));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup arg2) {
		return LayoutInflater.from(context).inflate(R.layout.news_gridview_item, null);
	}

}
