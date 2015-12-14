package com.colorread.colorread.utils;



import com.colorread.colorread.R;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class WebDisplay extends Activity {

	private WebView web;
	private ImageView iv_share;
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

	String appId = "wx967daebe835fbeac";
	String appSecret = "5fa9e68ca3970e87a1f83e563c8dcbce";
	//private int pos; 
	private String url;
	private String title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.webview);

		initLayout();

		initWebView();

		initShare();
	}

	private void initLayout() {
		// 透明状态栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// 透明导航栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	}

	private void initShare() {
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(WebDisplay.this, "1104819871", "uwPxPLJVBlwteKeK");
		wxHandler.addToSocialSDK();
		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(WebDisplay.this, "1104819871", "uwPxPLJVBlwteKeK");
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		
		/**
		 * 分享到QQ好友和控件
		 */
		// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(WebDisplay.this, "1104819871",
				"uwPxPLJVBlwteKeK");
		qqSsoHandler.addToSocialSDK();
		QQShareContent qqShareContent = new QQShareContent();
		//设置分享文字
		qqShareContent.setShareContent("来自彩阅的分享-彩阅,精彩你的每一天!");
		//设置分享title
		qqShareContent.setTitle(title);
		//设置分享图片
		//qqShareContent.setShareImage(new UMImage(getActivity(), R.drawable.icon));
		//设置点击分享内容的跳转链接
		qqShareContent.setTargetUrl(url);
		mController.setShareMedia(qqShareContent);
		
		// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(WebDisplay.this, "1104819871",
				"uwPxPLJVBlwteKeK");
		qZoneSsoHandler.addToSocialSDK();
		QZoneShareContent qzone = new QZoneShareContent();
		//设置分享文字
		qzone.setShareContent("来自彩阅的分享-彩阅,精彩你的每一天!");
		//设置点击消息的跳转URL
		qzone.setTargetUrl(url);
		//设置分享内容的标题
		qzone.setTitle(title);
		//设置分享图片
		//qzone.setShareImage(urlImage);
		mController.setShareMedia(qzone);
		
	}

	private void initWebView() {

		web = (WebView) findViewById(R.id.web);

		Intent intent = getIntent();
		url=intent.getStringExtra("url");
		title=intent.getStringExtra("title");
		SharedPreferences preferences = this.getSharedPreferences("isHavePic", MODE_PRIVATE);
		boolean isHavePic = preferences.getBoolean("isHavePic", true);
		if(!isHavePic){
		WebSettings webset = web.getSettings();
		webset.setBlockNetworkImage(true);
		}
		web.loadUrl(url);

		findViewById(R.id.iv_share).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mController.openShare(WebDisplay.this, false);
			}
		});

		// 设置分享内容
		mController.setShareContent(title+"\n彩阅,精彩你的每一天! 猛戳阅读:" + url);

		web.getSettings().setJavaScriptEnabled(true);// 支持运行javascript

		web.setWebChromeClient(new WebChromeClient());// 支持运行特殊的javascript（例如：alert()）

		web.setWebViewClient(new WebViewClient());// 当点击超链地址后不会新打开浏览器来访问，而是始终在本app中浏览页面
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}

	}

}
