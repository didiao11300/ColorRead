package com.colorread.colorread.bbs;

import com.colorread.colorread.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FragmentBBS extends Fragment{
	private WebView  web;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.tab04, container, false);
		web = (WebView) view.findViewById(R.id.bbs);
		web.loadUrl("http://xinanlt.10g.me");
		web.getSettings().setJavaScriptEnabled(true);// 支持运行javascript

		web.setWebChromeClient(new WebChromeClient());// 支持运行特殊的javascript（例如：alert()）

		web.setWebViewClient(new WebViewClient());// 当点击超链地址后不会新打开浏览器来访问，而是始终在本app中浏览页面

		return view;
	}
}
