package com.colorread.colorread.picture;

import java.util.List;

import com.colorread.colorread.R;
import com.colorread.colorread.bean.Imginfo;
import com.colorread.colorread.utils.CallBackInfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class FragmentPictureSelect extends Fragment {
	private String sort="test";
	private ListView list_pic;
	List<Imginfo> imginfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_picture_select, container, false);
		Bmob.initialize(getContext(), "7dba48d154a17b9b57e53c413bc889de");
		list_pic = (ListView) view.findViewById(R.id.grid_pic);
		getData();
		list_pic.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Imginfo info = imginfo.get(position);
				Intent intent = new Intent(getActivity(),PicShow.class);
				intent.putExtra("url", info.getFile());
				String description = info.getDescription();
				intent.putExtra("title", description);
				startActivity(intent);
			}
		});
		refreshPic();
		return view;
	}

	//从服务器获取图片信息，并直接放入imginfo中。
	public void getData() {
		BmobQuery<Imginfo> query = new BmobQuery<Imginfo>();
		query.addWhereEqualTo("sort", sort);
		//倒序查询，让最新上传的显示在最前面
		query.order("-createdAt");
		query.findObjects(getContext(), new FindListener<Imginfo>() {
			@Override
			public void onError(int code, String arg1) {
			}

			@Override
			public void onSuccess(List<Imginfo> result) {
				PicAdapter adapter = new PicAdapter(getContext(), result);
				imginfo = result;
				list_pic.setAdapter(adapter);
			}
		});

	}

	//上传成功之后的回调，刷新当前页面。
	public void refreshPic(){
		FragmentPicture.selectGetcall(new CallBackInfo() {
			@Override
			public String getInfo(String info) {
				System.out.println(info);
				getData();
				return null;
			}
		});
	}
}
