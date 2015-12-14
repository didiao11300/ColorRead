package com.colorread.colorread;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> list;
	public MyPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public MyPagerAdapter(FragmentManager fm,List<Fragment> list) {
		super(fm);
		this.list=list;
	}
	
	@Override
	public int getCount() {
		return  list.size();//设置里面有无数条
	}

	@Override
	public Fragment getItem(int position) {
		return list.get(position);
	}
}