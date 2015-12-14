package com.colorread.colorread.news;

import java.util.List;

import com.colorread.colorread.R;
import com.colorread.colorread.bean.XmlNewsBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class XMLNewsAdapter extends BaseAdapter{

	List<XmlNewsBean> list;
	Context context;

	public XMLNewsAdapter(Context context,List<XmlNewsBean> list){
	        this.list = list;
	        this.context = context;
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
		XmlNewsBean news = list.get(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.xml_news_item, null);
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.Date = (TextView) convertView.findViewById(R.id.pubDate);
			viewHolder.author = (TextView) convertView.findViewById(R.id.author);
			convertView.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.title.setText(news.getTitle());
		viewHolder.Date.setText(news.getPubDate());
		viewHolder.author.setText(news.getSource());

		return convertView;
	}

	public class ViewHolder {
		private TextView title, Date, author;
	}
}
