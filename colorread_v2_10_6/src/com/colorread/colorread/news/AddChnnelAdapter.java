package com.colorread.colorread.news;

import java.util.List;
import java.util.Map;

import com.colorread.colorread.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AddChnnelAdapter extends BaseAdapter{
	Context context;
	List<Map<String,Object>> list;
	 public AddChnnelAdapter(Context context,List<Map<String,Object>> list) {
		 this.context=context;
		 this.list=list;
	 }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.add_chnnel, null);
		TextView text=(TextView) convertView.findViewById(R.id.add_chnnel);
		Map<String,Object>map = list.get(position);
		text.setText((CharSequence) map.get("chnnel"));
		return convertView;
	}
	

}
