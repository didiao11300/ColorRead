package com.colorread.colorread.utils;


import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;
import android.util.LruCache;

public class BitmapCache implements ImageCache{
	private LruCache<String, Bitmap> mCache;
	
	public BitmapCache(){
		int maxSize = 10*1024*1024;
		mCache = new LruCache<String, Bitmap>(maxSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes()*value.getHeight();
			}
		};
	}

	@Override
	public Bitmap getBitmap(String url) {
		
		return mCache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		mCache.put(url, bitmap);
		
	}

}
