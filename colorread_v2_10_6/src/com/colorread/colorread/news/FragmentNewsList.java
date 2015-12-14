package com.colorread.colorread.news;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.colorread.colorread.R;
import com.colorread.colorread.bean.XmlNewsBean;
import com.colorread.colorread.utils.StringRequest;
import com.colorread.colorread.utils.WebDisplay;
import com.colorread.colorread.utils.XMLParser;
import com.grr.reader.db.DataBaseHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentNewsList extends Fragment implements OnRefreshListener<ListView> {
	private PullToRefreshListView listView;
	private TextView textView;
	// 解析出来的新闻对象存放在list中
	private List<XmlNewsBean> list = new ArrayList<XmlNewsBean>();
	private DataBaseHelper helper;
	private String path;
	private String chnnel;
	private XMLNewsAdapter adapter;
	private int totalPage;
	private int currentPage = 1;
	boolean isButtom = false;
	boolean canRefresh = false;
	private ProgressDialog dialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news_list, null);
		// 注册ListView
		listView = (PullToRefreshListView) view.findViewById(R.id.newslist);
		textView = (TextView) view.findViewById(R.id.textView_newslist_title);
		// 获取传入的新闻地址和频道
		Bundle bundle = getArguments();
		path = bundle.getString("path");
		chnnel = bundle.getString("chnnel");
		textView.setText(chnnel);

		listView.setMode(Mode.PULL_FROM_START);
		helper = new DataBaseHelper(getActivity());
		// 把查询总页数放到这里，每次进入该Fragment时查询一次。
		getTotalPage();
		// 如果数据库能够获取数据，直接加载
		list.clear();
		list = getDataFromDatabase(chnnel, currentPage);
		// 先设置一个空的adapter，防止adapter.notifyDataChanged空指针异常.
		adapter = new XMLNewsAdapter(getActivity(), list);
		listView.setAdapter(adapter);

		// 初始化进度条,首次进入没有新闻的时候显示一个进度条
		dialog = new ProgressDialog(getActivity());
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setTitle("正在加载");
		dialog.setMessage("第一次加载较慢，请稍等一下");
		if (list.isEmpty()) {
			// 从网络加载数据，并在本方法里设置Adapter；
			getDataFromVolley();
			dialog.show();
		}
		// 设置Item点击监听
		ItemListener();
		// 设置滑动监听
		NextPage();
		// 设置刷新监听
		refreshListener();
		return view;
	}

	// 点击Item监听
	public void ItemListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//position开始为1，导致新闻最后一条数组越界
				XmlNewsBean news = list.get(position-1);
				String newsPath = news.getLink();
				Intent intent = new Intent(getActivity(), WebDisplay.class);
				intent.putExtra("url", newsPath);
				startActivity(intent);
			}
		});
	}

	// 刷新监听
	public void refreshListener() {
		// 代码太多，以后转义到一个函数中
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String str = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				if (refreshView.isHeaderShown()) {
					listView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
					listView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
					listView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
					refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(str);
					getDataFromVolley();
				}
			}
		});
	}

	// 继承方法，无用，但不能删
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
	}

	// 从数据库中获取数据
	public List<XmlNewsBean> getDataFromDatabase(String chnnel, int page) {
		SQLiteDatabase db = helper.getReadableDatabase();
		// 分页查询
		String sql = "select * from xml where chnnel =? limit ?,? ";
		Cursor cursor = db.rawQuery(sql, new String[] { chnnel, (page - 1) * 20 + "", 20 + "" });
		while (cursor.moveToNext()) {
			XmlNewsBean news = new XmlNewsBean();
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String link = cursor.getString(cursor.getColumnIndex("link"));
			String pubDate = cursor.getString(cursor.getColumnIndex("pubDate"));
			news.setTitle(title);
			news.setLink(link);
			news.setPubDate(pubDate);
			list.add(news);
		}
		cursor.close();
		return list;
	}

	public void getDataFromVolley() {
		// 如果数据库中没有值，Volley获取xml数据
		RequestQueue mQueue = Volley.newRequestQueue(getActivity());
		// 导入重写的Request方法，解决乱码
		StringRequest stringRequest = new StringRequest(path, "utf-8", new Listener<String>() {
			@Override
			public void onResponse(String response) {
				new XMLParser(response, chnnel, getActivity());
				// 解析并放入数据库
				XMLParser.getXmlText();
				// 在这里清除list，防止下拉数据消失。
				list.clear();
				// 解析出来之后添加到list中。
				list = getDataFromDatabase(chnnel, 1);
				listView.onRefreshComplete();
				adapter.notifyDataSetChanged();
				// 查询新闻之后更新当前总页数，解决第一次打开应用时数据库无数据，总页数为零无法下拉加载的bug。
				getTotalPage();
				dialog.dismiss();
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// 请求错误
			}
		});
		// 将请求放入请求队列中
		mQueue.add(stringRequest);
	}

	public void getTotalPage() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor mcursor = db.query("xml", new String[] { "title", "link", "pubDate" }, "chnnel=?",
				new String[] { chnnel }, null, null, null);
		//解决第一次使用不能上拉加载更多的bug
		totalPage = (int) Math.ceil(mcursor.getCount() / (float) 20);
		db.close();
	}

	public void NextPage() {
		listView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (isButtom && (scrollState == OnScrollListener.SCROLL_STATE_IDLE)) {
					if (currentPage < totalPage) {
						currentPage++;
						getDataFromDatabase(chnnel, currentPage);
						adapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				isButtom = (firstVisibleItem + visibleItemCount) == totalItemCount;
			}
		});
	}

}
