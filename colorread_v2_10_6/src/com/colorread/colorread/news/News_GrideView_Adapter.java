package com.colorread.colorread.news;

import java.util.List;
import java.util.Map;

import com.colorread.colorread.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class News_GrideView_Adapter extends BaseAdapter{
	private Context context;
	private List<Map<String,Object>> list;
	private ViewHolder holder;
	
	public News_GrideView_Adapter(Context context,List<Map<String,Object>> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView==null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.news_gridview_item, null);
			holder.imageView = (ImageView) convertView.findViewById(R.id.grid_image);
			holder.textView = (TextView) convertView.findViewById(R.id.grid_text);
			convertView.setTag(holder);
		}else{
			holder =(ViewHolder) convertView.getTag();
		}
		Map<String,Object> map =list.get(position);
		holder.imageView.setImageResource((Integer) map.get("pic"));
		holder.textView.setText((CharSequence) map.get("news"));
		return convertView;
	}

	class ViewHolder{
		private ImageView imageView;
		private TextView textView;
	}
	
}
