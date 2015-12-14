package com.colorread.colorread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.colorread.colorread.bbs.FragmentBBS;
import com.colorread.colorread.news.FragmentNews;
import com.colorread.colorread.picture.FragmentPicture;
import com.colorread.colorread.select.FragmentSelect;
import com.tandong.swichlayout.BaseEffects;
import com.tandong.swichlayout.SwichLayoutInterFace;
import com.tandong.swichlayout.SwitchLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * 主框架 Fragment实现底部Tab
 * 
 * @author GRR
 *
 */
public class MainActivity extends FragmentActivity implements OnClickListener, SwichLayoutInterFace {

	private DrawerLayout mDrawerLayout;
	private ListView lv_left;
	private SimpleAdapter lAdapter;

	// 声明ImageView
	private ImageView mNews, mSelect, mPicture, mBBS;

	// Fragment
	private Fragment fNews, fSelect, fPicture, fBBS;
	// 上次按返回键的时间
	private long lastBackTime = 0;
	// 当前按返回键的时间
	private long currentBackTime = 0;
	// Fragment管理者
	FragmentManager manager;
	private boolean isHaveBigNews;
	private Intent intent;
	private Bundle bundleHaveBigNews = new Bundle();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// 去掉ActionBar 需要在onCrate()方法前实现
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		// 加载布局
		setContentView(R.layout.activity_main);

		// 透明状态栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// 透明导航栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

		// 初始化View
		initView();

		// 初始化Drawlayout;
		initDrawLayout();

		// 初始化事件
		initEvents();

		receiveBigNewsFromLoading();

		// 初始化选择
		initChoice(0);
		
	}

	/**
	 * 初始化Drawlayout;
	 */
	private void initDrawLayout() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawlayout);
		lv_left = (ListView) findViewById(R.id.lv_left);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("img", null);
		map.put("title", "");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.picture_normal);
		map.put("title", "有图模式");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.picture_none);
		map.put("title", "无图模式");
		list.add(map);
		lAdapter = new SimpleAdapter(getApplicationContext(), list, R.layout.item_left, new String[] { "img", "title" },
				new int[] { R.id.iv_left, R.id.tv_left });
		lv_left.setAdapter(lAdapter);
		lv_left.setOnItemClickListener(new OnItemClickListener() {
			SharedPreferences preferences = getSharedPreferences("isHavePic", MODE_PRIVATE);
			Editor editor = preferences.edit();
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				// 有图模式
				case 1:
					editor.putBoolean("isHavePic", true);
					Toast.makeText(MainActivity.this, "已经切换有图模式", Toast.LENGTH_SHORT).show();
					break;
				// 无图模式
				case 2:
					editor.putBoolean("isHavePic", false);
					Toast.makeText(MainActivity.this, "已经切换无图模式", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				editor.commit();
			}
		});
	}

	public void receiveBigNewsFromLoading() {
		intent = getIntent();
		isHaveBigNews = intent.getBooleanExtra("isHaveBigNews", false);
	}

	/**
	 * 初始化选择
	 */
	public void initChoice(int i) {
		manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		// 点击底部栏时清空Fragment回退栈（主要是清空FragmentNews中的入栈操作，防止多次入栈）。
		manager.popBackStack();
		// 隐藏Fragment
		hideFragment(transaction);
		switch (i) {
		case 0:
			fNews = new FragmentNews();
			transaction.replace(R.id.id_fragments, fNews);
			SwitchLayout.getFadingIn(mNews);
			// activity向Fragment传值。TODO 可能重新用底部栏点击的时候存在bug，看是否需要将isHaveBigNews置否
			if (isHaveBigNews) {
				bundleHaveBigNews = intent.getBundleExtra("BigNewsBundle");
				bundleHaveBigNews.putBoolean("BundleHaveBigNews", true);
			} else {
				bundleHaveBigNews.putBoolean("BundleHaveBigNews", false);
			}
			fNews.setArguments(bundleHaveBigNews);
			mNews.setImageResource(R.drawable.news_down);
			break;
		case 1:
			fSelect = new FragmentSelect();
			transaction.replace(R.id.id_fragments, fSelect);
			SwitchLayout.getFadingIn(mSelect);
			mSelect.setImageResource(R.drawable.select_down);
			break;
		case 2:
			fPicture = new FragmentPicture();
			transaction.replace(R.id.id_fragments, fPicture);
			SwitchLayout.getFadingIn(mPicture);
			mPicture.setImageResource(R.drawable.picture_down);
			break;
		case 3:
			fBBS = new FragmentBBS();
			transaction.replace(R.id.id_fragments, fBBS);
			SwitchLayout.getFadingIn(mBBS);
			mBBS.setImageResource(R.drawable.bbs_down);
			break;
		default:
			break;
		}
		transaction.commit();
	}

	/**
	 * 隐藏Fragment
	 * 
	 * @param transaction
	 */
	private void hideFragment(FragmentTransaction transaction) {
		if (fNews != null) {
			transaction.hide(fNews);
		}
		if (fSelect != null) {
			transaction.hide(fSelect);
		}
		if (fPicture != null) {
			transaction.hide(fPicture);
		}
		if (fBBS != null) {
			transaction.hide(fBBS);
		}

	}

	/**
	 * 初始化事件
	 */
	private void initEvents() {
		mNews.setOnClickListener(this);
		mSelect.setOnClickListener(this);
		mPicture.setOnClickListener(this);
		mBBS.setOnClickListener(this);

	}

	/**
	 * 初始化initView
	 */
	private void initView() {
		mNews = (ImageView) findViewById(R.id.id_iv_news);
		mSelect = (ImageView) findViewById(R.id.id_iv_select);
		mPicture = (ImageView) findViewById(R.id.id_iv_picture);
		mBBS = (ImageView) findViewById(R.id.id_iv_bbs);
	}

	/**
	 * ImageView 点击事件
	 */
	@Override
	public void onClick(View v) {
		// 重置图片
		resetImg();
		switch (v.getId()) {
		case R.id.id_iv_news:
			initChoice(0);
			break;
		case R.id.id_iv_select:
			initChoice(1);
			break;
		case R.id.id_iv_picture:
			initChoice(2);
			break;
		case R.id.id_iv_bbs:
			initChoice(3);
			break;
		default:
			break;
		}
	}

	/**
	 * 重置图片
	 */
	private void resetImg() {
		mNews.setImageResource(R.drawable.news_up);
		mSelect.setImageResource(R.drawable.select_up);
		mPicture.setImageResource(R.drawable.picture_up);
		mBBS.setImageResource(R.drawable.bbs_up);
	}

	// 再次按返回键退出
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 获得当前回退栈中保存的Fragment对象数量，防止在非主页面提示并且退出。
		int count = manager.getBackStackEntryCount();

		// 捕捉返回键按下的事件
		if (keyCode == KeyEvent.KEYCODE_BACK && count == 0) {
			currentBackTime = System.currentTimeMillis();
			if (currentBackTime - lastBackTime > 2000) {
				Toast.makeText(MainActivity.this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
				lastBackTime = currentBackTime;
			} else {
				setExitSwichLayout();
				finish();// 如果时间差小于2秒，退出程序

			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 动画
	 */
	@Override
	public void setEnterSwichLayout() {
		SwitchLayout.getSlideToLeft(this, false, BaseEffects.getMoreQuickEffect());

	}

	@Override
	public void setExitSwichLayout() {
		SwitchLayout.getSlideToLeft(this, true, BaseEffects.getMoreQuickEffect());
	}

}
