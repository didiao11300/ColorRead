package com.colorread.colorread;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.colorread.colorread.bean.BigNews;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.WindowManager;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class LoadingActivity extends Activity{
	private boolean notFirst=true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		ActionBar bar = getActionBar();
		bar.hide();
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Bmob.initialize(LoadingActivity.this, "7dba48d154a17b9b57e53c413bc889de");
		SharedPreferences preferences = this.getSharedPreferences("notFirst",MODE_PRIVATE);
		
		notFirst = preferences.getBoolean("notFirst", false);
		if(notFirst){
		getDataAndStartMain();
		}else{
			Intent intent =new Intent(LoadingActivity.this,FirstStart.class);
			startActivity(intent);
			System.out.println("Loading是第一次执行了");
			finish();
		}
		
	}
	

	public void getDataAndStartMain(){
			setContentView(R.layout.not_first);
			BmobQuery<BigNews> query = new BmobQuery<BigNews>();
			query.addWhereEqualTo("sort", "bigNews");
			query.order("important");
			query.findObjects(LoadingActivity.this, new FindListener<BigNews>(){
				@Override
				public void onError(int arg0, String arg1) {
					Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
					intent.putExtra("isHaveBigNews", false);
					startActivity(intent);
					finish();
				}

				@Override
				public void onSuccess(List<BigNews> list) {
					Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("BigNews", (Serializable) list);
					intent.putExtra("BigNewsBundle", bundle);
					intent.putExtra("isHaveBigNews", true);
					startActivity(intent);
					finish();
				}
			});
		}
	
}
