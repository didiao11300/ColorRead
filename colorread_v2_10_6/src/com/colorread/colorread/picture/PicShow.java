package com.colorread.colorread.picture;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.colorread.colorread.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.datatype.BmobFile;

public class PicShow extends Activity{
	private ImageView imgView;
	private TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// 透明状态栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// 透明导航栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		super.onCreate(savedInstanceState);
	setContentView(R.layout.pic_show_layout);
	imgView = (ImageView) findViewById(R.id.imgView_pic_show);
	textView =(TextView) findViewById(R.id.textView_picshow);
	Intent intent = getIntent();
	BmobFile file = (BmobFile) intent.getSerializableExtra("url");
	String text = intent.getStringExtra("title");
	textView.setText(text);
	String url ="http://file.bmob.cn/"+ file.getUrl();
	System.out.println(url);
	final ProgressDialog dialog = new ProgressDialog(this);
	dialog.setTitle("图片加载中。。。");
	dialog.setMessage("图片越大，加载的时间也就越长");
	dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	dialog.show();
	
	ImageRequest request = new ImageRequest(url, 
			new Response.Listener<Bitmap>() {
				@Override
				public void onResponse(Bitmap bt) {
					imgView.setImageBitmap(bt);
					dialog.dismiss();
				}
			},
			0, 0, Config.RGB_565, 
			new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError arg0) {
					
				}
			});
	RequestQueue mQueue = Volley.newRequestQueue(this);
	mQueue.add(request);
	
	}
	

}
