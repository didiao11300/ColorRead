package com.colorread.colorread;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

public class FirstStart extends FragmentActivity {
	private ViewPager viewPager;
	private List<Fragment> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//全屏模式
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getActionBar().hide();
		setContentView(R.layout.first_start);
		SharedPreferences sharedPreferences = this.getSharedPreferences("notFirst", MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("notFirst", true);
		editor.commit();
		System.out.println("执行了");
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		list = new ArrayList<Fragment>();
		getFragment();
		
		MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(),list);
		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(2);
	}
	
	public void getFragment(){
		Fragment fragment1 = new LuncherFragment1();
		Fragment fragment2 = new LuncherFragment2();
		Fragment fragment3 = new LuncherFragment3();
		list.add(fragment1);
		list.add(fragment2);
		list.add(fragment3);
	}
	
}
