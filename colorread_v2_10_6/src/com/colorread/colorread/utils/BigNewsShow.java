package com.colorread.colorread.utils;

import com.colorread.colorread.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public class BigNewsShow extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.big_news);		
		// 透明状态栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// 透明导航栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		getActionBar().hide();
		TextView text = (TextView) findViewById(R.id.textView_bignews);
		WebView web = (WebView) findViewById(R.id.big_news);
		Intent intent = getIntent();
		String data = intent.getStringExtra("html");
		String title =intent.getStringExtra("title");
		text.setText(title);
		SharedPreferences preferences = this.getSharedPreferences("isHavePic", MODE_PRIVATE);
		boolean isHavePic = preferences.getBoolean("isHavePic", true);
		if(!isHavePic){
		WebSettings webset = web.getSettings();
		webset.setBlockNetworkImage(true);
		}
		web.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
	}

}
