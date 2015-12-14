package com.colorread.colorread.picture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.colorread.colorread.R;
import com.colorread.colorread.bean.Imginfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.datatype.BmobFile;

public class PicAdapter extends BaseAdapter {
	private Context context;
	private List<Imginfo> list = new ArrayList<Imginfo>();

	public PicAdapter(Context context, List<Imginfo> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SharedPreferences preferences = context.getSharedPreferences("isHavePic", 0);
		boolean isHavePic = preferences.getBoolean("isHavePic", true);
		final ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_pic, null);
			holder = new ViewHolder();
			holder.iv_firstImg = (ImageView) convertView.findViewById(R.id.imgView_pic);
			holder.tv_title = (TextView) convertView.findViewById(R.id.text_pic_title);
			holder.tv_source = (TextView) convertView.findViewById(R.id.text_pic_description);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Imginfo data = list.get(position);
		holder.tv_title.setText(data.getTitle());
		holder.tv_source.setText(data.getDescription());
		BmobFile file = data.getFile();
		if (isHavePic) {
			ImageLoader.getInstance().displayImage(file.getFileUrl(context), holder.iv_firstImg,
					new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String arg0, View arg1) {
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
							holder.iv_firstImg.setImageBitmap(arg2);
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
						}
					});
		}
		return convertView;
	}

	class ViewHolder {
		ImageView iv_firstImg;
		TextView tv_title, tv_source;
	}
}
