package com.colorread.colorread.news;

import java.util.List;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MyPagerAdapter extends PagerAdapter{
	List<ImageView> list;
	Handler handler;
	public MyPagerAdapter(List<ImageView> list) {
		this.list=list;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(list.get(position));
		return list.get(position);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(list.get(position));
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}
	
	

}
