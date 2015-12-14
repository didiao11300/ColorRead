package com.colorread.colorread.select;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.colorread.colorread.R;
import com.colorread.colorread.bean.SelectBean;
import com.colorread.colorread.utils.WebDisplay;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FragmentSelect extends Fragment {
	private String url = "http://v.juhe.cn/weixin/query?key=d116bdf968ff311ff552fc3c80c4e5b8&ps=30&pno=";
	private PullToRefreshListView lv_select;
	private int pageNum=1;//当前页
	private String selectPath=url+pageNum;
	private SelectAdapter adapter ;
	private List<SelectBean> totallist=new ArrayList<SelectBean>();
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_select, null);
		lv_select = (PullToRefreshListView) view.findViewById(R.id.lv_select);
		lv_select.setMode(Mode.BOTH);

		lv_select.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()) {
					lv_select.getLoadingLayoutProxy(false, true).setPullLabel("下拉可以刷新...");
					lv_select.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在刷新...");
					lv_select.getLoadingLayoutProxy(false, true).setReleaseLabel("松开之后更新数据...");
					getData();
				} else {
					lv_select.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多...");
					lv_select.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
					lv_select.getLoadingLayoutProxy(false, true).setReleaseLabel("松开之后更新数据...");
					getNextPage();
				}

			}
		});
		getData();
		return view;
	}
	
	@Override
	public void onResume() {
		
		super.onResume();
		getDataFromDB();
		
		
	}

	
	/**
	 * 数据库获取数据
	 */
	private void getDataFromDB() {
		
		
	}

	/**
	 * 加载更多数据
	 */
	protected void getNextPage() {
		pageNum++;
		selectPath=url+pageNum;
		getData();
	}

	/**
	 * 获取数据
	 */
	private void getData() {

		// 使用Volley获取JSON数据
		RequestQueue mQueue = Volley.newRequestQueue(getActivity());
		// Volley构造方法的三个参数
		StringRequest stringRequest = new StringRequest(selectPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// 请求成功,使用fastJSON解析数据
				JSONObject jsonObject = JSONObject.parseObject(response);
				JSONObject jsonObject1 = jsonObject.getJSONObject("result");
				JSONArray jsonArray = jsonObject1.getJSONArray("list");
				final List<SelectBean> list = JSONArray.parseArray(jsonArray.toString(), SelectBean.class);
				totallist.addAll(list);
				if (pageNum==1) {
					adapter = new SelectAdapter(getActivity(), totallist);
					lv_select.setAdapter(adapter);
				}else {
					adapter = new SelectAdapter(getActivity(), totallist);
					adapter.notifyDataSetChanged();
				}
				lv_select.onRefreshComplete();
				lv_select.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						String url = totallist.get(position - 1).getUrl();
						String title=totallist.get(position-1).getTitle();
						int pos=position;
						Intent intent = new Intent(getActivity(), WebDisplay.class);
						//intent.putExtra("pos", pos);
						intent.putExtra("url", url);
						intent.putExtra("title", title);
						startActivity(intent);
					}
				});
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// 请求失败
			}
		});
		mQueue.add(stringRequest);

	}

}
