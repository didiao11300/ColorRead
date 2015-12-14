package com.colorread.colorread.news;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.colorread.colorread.R;
import com.colorread.colorread.bean.BigNews;
import com.colorread.colorread.utils.BigNewsShow;
import com.colorread.colorread.utils.WebDisplay;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;

public class FragmentNews extends Fragment {
	private GridView gridView;
	private ViewPager viewPager;
	private ImageView iv_add_chnnel;
	Uri uri_query = Uri.parse("content://com.colorread.colorread.news.ChnnelContentProvider/query");
	Uri uri_update = Uri.parse("content://com.colorread.colorread.news.ChnnelContentProvider/update");
	ContentResolver contentResolver;
	GridViewCursorAdapter adapter;
	Cursor chnnelCursor;
	List<ImageView> list = new ArrayList<ImageView>();
	List<BigNews> bigNews = new ArrayList<BigNews>();
	MyPagerAdapter Pageradapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 加载布局
		View view = inflater.inflate(R.layout.fragment_news, null);
		iv_add_chnnel = (ImageView) view.findViewById(R.id.iv_add_chnnel);
		gridView = (GridView) view.findViewById(R.id.news_gridView);
		viewPager = (ViewPager) view.findViewById(R.id.news_viewPager);
		contentResolver = getActivity().getContentResolver();
		Bmob.initialize(getContext(), "7dba48d154a17b9b57e53c413bc889de");

		Bundle bundle = getArguments();
		boolean haveBigNews = bundle.getBoolean("BundleHaveBigNews");

		Pageradapter = new MyPagerAdapter(list);
		viewPager.setAdapter(Pageradapter);
		// 获取图片和连接
		if (haveBigNews) {
			// 从Loading传过来的，也是醉了。
			bigNews = (List<BigNews>) bundle.getSerializable("BigNews");
			ViewPagerSetNewAdapter();
		} else {
			getPagerData();
		}
		getChnnel();
		// adapter
		adapter = new GridViewCursorAdapter(getContext(), chnnelCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		gridView.setAdapter(adapter);

		// 添加GridViewItem监听
		ItemClick();
		// 添加按钮设置监听事件
		addChnnel();
		// 添加gridV长按监听
		DeleteChnnel();
		// 添加ViewPager监听
		watchBigNews();
		return view;
	}

	public void getPagerData() {
		// 获取图片和连接
		BmobQuery<BigNews> query = new BmobQuery<BigNews>();
		query.order("important");
		query.addWhereEqualTo("sort", "bigNews");
		query.findObjects(getActivity(), new FindListener<BigNews>() {
			@Override
			public void onSuccess(List<BigNews> arg0) {
				bigNews = arg0;
				ViewPagerSetNewAdapter();
			}

			@Override
			public void onError(int code, String arg1) {
				System.out.println("查询失败" + code);
			}
		});
	}

	public void ViewPagerSetNewAdapter() {
		//解决进入新闻列表出来之后viewPager变长的bug
		list.clear();
		for (int i = 0; i < bigNews.size(); i++) {
			final BigNews tempNews = bigNews.get(i);
			BmobFile file = tempNews.getPic();
			final ImageView img = new ImageView(getActivity());
			img.setScaleType(ScaleType.FIT_XY);

			ImageLoader.getInstance().displayImage(file.getFileUrl(getActivity()), img, new ImageLoadingListener() {
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
				}

				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				}

				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					img.setImageBitmap(arg2);
				}

				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
				}
			});

			//给ViewPager里的图片添加监听
			img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String link = tempNews.getLink();
					String html = tempNews.getHtml();
					Intent intent = new Intent(getActivity(), BigNewsShow.class);
					intent.putExtra("url", link);
					intent.putExtra("html", html);
					intent.putExtra("title", tempNews.getTitle());
					startActivity(intent);
				}
			});
			list.add(img);
			Pageradapter.notifyDataSetChanged();
		}
	}

	public void watchBigNews() {
	}

	// 点击监听，跳转到NewsList
	public void ItemClick() {
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				FragmentManager manager = getFragmentManager();
				FragmentTransaction transaction = manager.beginTransaction();
				Fragment fragment = new FragmentNewsList();
				Bundle bundle = new Bundle();
				// 从地址集中获取当前点击的地址和频道，然后传入另外一个fragment中。当前fragment放入回退栈。
				Cursor cursor = (Cursor) gridView.getItemAtPosition(position);
				String link = cursor.getString(cursor.getColumnIndex("link"));
				String chnnel = cursor.getString(cursor.getColumnIndex("chnnel"));
				bundle.putString("path", link);
				bundle.putString("chnnel", chnnel);
				fragment.setArguments(bundle);
				transaction.replace(R.id.id_fragments, fragment);
				transaction.addToBackStack(null);
				transaction.commit();
			}
		});
	}

	public void DeleteChnnel() {
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("是否删除这个条目");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Cursor cursor = (Cursor) gridView.getItemAtPosition(position);
						int _id = cursor.getInt(cursor.getColumnIndex("_id"));
						ContentValues values = new ContentValues();
						values.put("isadd", "false");
						contentResolver.update(uri_update, values, "_id=?", new String[] { _id + "" });
						getChnnel();
						adapter = new GridViewCursorAdapter(getContext(), chnnelCursor,
								CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
						gridView.setAdapter(adapter);
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				builder.show();
				return true;
			}
		});
	}

	public void addChnnel() {
		iv_add_chnnel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(getActivity());
				View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_chnnel, null);
				dialog.setContentView(view);
				final ListView chnnelList = (ListView) view.findViewById(R.id.dialog_add_chnnel);
				dialog.setContentView(view);
				List<Map<String, Object>> list = getNotAddChnnel();
				AddChnnelAdapter madapter = new AddChnnelAdapter(getActivity(), list);
				chnnelList.setAdapter(madapter);
				dialog.setTitle("添加新闻");
				dialog.show();
				chnnelList.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Map<String, Object> map = (Map<String, Object>) chnnelList.getItemAtPosition(position);
						int _id = (Integer) map.get("_id");
						ContentValues values = new ContentValues();
						values.put("isadd", "true");
						contentResolver.update(uri_update, values, "_id=?", new String[] { _id + "" });
						dialog.dismiss();
						getChnnel();
						adapter = new GridViewCursorAdapter(getContext(), chnnelCursor,
								CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
						gridView.setAdapter(adapter);

					}
				});
			}
		});
	}

	// 从数据库中获得频道
	public void getChnnel() {
		chnnelCursor = contentResolver.query(uri_query, new String[] { "_id", "chnnel", "link", "img", "isadd" },
				"isadd=?", new String[] { "true" }, null);

	}

	// 检索所有未添加的频道
	public List<Map<String, Object>> getNotAddChnnel() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Cursor cursor = contentResolver.query(uri_query, new String[] { "_id", "chnnel", "link", "img", "isadd" },
				"isadd=?", new String[] { "false" }, null);
		if (cursor.getColumnCount() > 0) {
			while (cursor.moveToNext()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("_id", cursor.getInt(cursor.getColumnIndex("_id")));
				map.put("chnnel", cursor.getString(cursor.getColumnIndex("chnnel")));
				list.add(map);
			}
		}
		return list;
	}

}
