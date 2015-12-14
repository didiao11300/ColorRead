package com.colorread.colorread.utils;

import java.io.UnsupportedEncodingException;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
/*
 * 重写Volley的request方法，设置编码格式。解决乱码
 * */
public class StringRequest extends Request<String>{
	private final Listener<String> mListener;
	private String charset = null;

	public StringRequest(int method, String url, Listener<String> listener, ErrorListener errorlistener) {
		super(method, url, errorlistener);
		mListener = listener;
	}
	
	public StringRequest(String url,String charset,Listener<String> listener,ErrorListener errorListener){
		this(Method.GET,url,listener,errorListener);
		this.charset = charset;
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String parsed;
		try {
			if(charset !=null){
				parsed = new String(response.data,charset);
			}else{
				parsed = new String(response.data,HttpHeaderParser.parseCharset(response.headers));
				
			}
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
	}
	
	public String getCharset(){
		return charset;
	}

	@Override
	protected void deliverResponse(String response) {
		mListener.onResponse(response);
	}

}
