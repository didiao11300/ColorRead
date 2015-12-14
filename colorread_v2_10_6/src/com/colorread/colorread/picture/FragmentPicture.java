package com.colorread.colorread.picture;

import java.io.File;
import java.util.ArrayList;

import com.colorread.colorread.R;
import com.colorread.colorread.bean.Imginfo;
import com.colorread.colorread.utils.CallBackInfo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class FragmentPicture extends Fragment {
	// ViewPager
	private ViewPager mViewPager;
	// TextView
	private TextView tv_guid1, tv_guid2, tv_guid3, tv_guid4, tv_guid5, tv_cursor;
	// 当前页面下标
	int currentIndex = 0;
	// 存放Fragment
	ArrayList<Fragment> fragments;
	// 上传按钮
	private ImageView imgButton;
	private View dialogView;
	private AlertDialog.Builder builder;
	private String filePath = "";
	private ProgressDialog dialog;
	private ImageView img;
	private static CallBackInfo cartoonCall, selectCall, beautifulCall;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_picture, container, false);
		mViewPager = (ViewPager) view.findViewById(R.id.pic_viewpager);
		fragments = new ArrayList<Fragment>();
		imgButton = (ImageView) view.findViewById(R.id.upload);

		Fragment fragment1 = new FragmentPictureSelect();
		Fragment fragment2 = new FragmentPictureBeautiful();
		Fragment fragment3 = new FragmentPictureCartoon();

		fragments.add(fragment1);
		fragments.add(fragment2);
		fragments.add(fragment3);

		// 适配器
		PictureFragmentPagerAdapter adapter = new PictureFragmentPagerAdapter(getChildFragmentManager(), fragments);
		mViewPager.setAdapter(adapter);
		// 设置当前页面为第一页
		mViewPager.setCurrentItem(0);
		// 页面滑动监听
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				currentIndex = arg0;
				int i = currentIndex + 1;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// 取得该控件的实例
				LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) tv_cursor.getLayoutParams();

				if (currentIndex == arg0) {
					ll.leftMargin = (int) (currentIndex * tv_cursor.getWidth() + arg1 * tv_cursor.getWidth());
				} else if (currentIndex > arg0) {
					ll.leftMargin = (int) (currentIndex * tv_cursor.getWidth() - (1 - arg1) * tv_cursor.getWidth());
				}
				tv_cursor.setLayoutParams(ll);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		tv_cursor = (TextView) view.findViewById(R.id.tv_cursor);
		tv_guid1 = (TextView) view.findViewById(R.id.tv_guid1);
		tv_guid2 = (TextView) view.findViewById(R.id.tv_guid2);
		tv_guid5 = (TextView) view.findViewById(R.id.tv_guid5);
		tv_guid1.setOnClickListener(new MyOnClickListener(0));
		tv_guid2.setOnClickListener(new MyOnClickListener(1));
		tv_guid5.setOnClickListener(new MyOnClickListener(4));
		tv_cursor = (TextView) view.findViewById(R.id.tv_cursor);
		Display display = getActivity().getWindow().getWindowManager().getDefaultDisplay();
		// 得到显示屏宽度
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		// 1/3屏幕宽度
		int tabLineLength = metrics.widthPixels / 3;
		LayoutParams lp = (LayoutParams) tv_cursor.getLayoutParams();
		lp.width = tabLineLength;
		tv_cursor.setLayoutParams(lp);

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		// 右下角按钮，点击弹出上传文件对话框。
		imgButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 弹框，添加上传信息。
				uploadPic();
			}
		});
	}

	public void uploadPic() {
		builder = new AlertDialog.Builder(getContext());
		builder.setTitle("分享图片");
		dialogView = LayoutInflater.from(getContext()).inflate(R.layout.upload_file_dialog, null);
		builder.setView(dialogView);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 点击确定上传图片
				insertToCloud();
			}

		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.show();
		img = (ImageView) dialogView.findViewById(R.id.upload_file);
		img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 跳转到图库选择图片。
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_PICK);
				intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent.setType("image/*");
				startActivityForResult(intent, 1);
			}
		});

	}

	// 获得图库的返回结果
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 根据uri获取本地图片绝对路径
		if (requestCode == 1 && resultCode == -1) {
			Uri uri = data.getData();
			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			filePath = cursor.getString(column_index);
			Bitmap bt = BitmapFactory.decodeFile(filePath);

			img.setImageBitmap(bt);
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void insertToCloud() {
		// 非空判断
		EditText title = (EditText) dialogView.findViewById(R.id.upload_title);
		EditText description = (EditText) dialogView.findViewById(R.id.upload_description);
		RadioGroup radio = (RadioGroup) dialogView.findViewById(R.id.upload_sort);
		if (title.getText() == null || title.getText().toString().equals("")) {
			Toast.makeText(getActivity(), "标题为空", Toast.LENGTH_SHORT).show();
		} else if (description == null || description.getText().toString().equals("")) {
			Toast.makeText(getActivity(), "描述信息为空", Toast.LENGTH_SHORT).show();
		} else if (radio.getCheckedRadioButtonId() == -1) {
			Toast.makeText(getActivity(), "分类为空", Toast.LENGTH_SHORT).show();
		} else if (filePath == "") {
			Toast.makeText(getActivity(), "照片为空", Toast.LENGTH_SHORT).show();
		} else {
			// 创建一个进度条
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle("正在上传");
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.show();
			// 获得Dialog中的信息
			String upload_title = title.getText().toString();
			String upload_description = description.getText().toString();
			String sort = "";
			switch (radio.getCheckedRadioButtonId()) {
			case R.id.dialog_upload_beautiful:
				sort = "风景";
				break;
			case R.id.dialog_upload_test:
				sort = "test";
				break;
			case R.id.dialog_upload_cartoon:
				sort = "卡通";
				break;
			default:
				break;
			}
			// 根据绝对路径构造file对象
			File file = new File(filePath);
			BmobFile bmobFile = new BmobFile(file);
			// TODO radio未添加
			// 新建BombObject对象
			final Imginfo imginfo = new Imginfo(upload_title, upload_description, sort, bmobFile);
			// bmobFile上传成功后将imginfo中的数据插入到数据库中。
			bmobFile.upload(getActivity(), new UploadFileListener() {
				@Override
				public void onSuccess() {
					imginfo.save(getActivity(), new SaveListener() {
						@Override
						public void onSuccess() {
							System.out.println("上传成功");
							mViewPager.setCurrentItem(mViewPager.getCurrentItem());
							switch (mViewPager.getCurrentItem()) {
							case 0:
								selectCall.getInfo("成功了");
								break;
							case 1:
								beautifulCall.getInfo("成功了");
								break;
							case 2:
								cartoonCall.getInfo("成功了");
								break;
							default:
								break;
							}

							dialog.dismiss();
							Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onFailure(int code, String arg1) {
						}
					});
				}

				@Override
				public void onProgress(Integer value) {
					System.out.println(value);
					dialog.setProgress(value);
					super.onProgress(value);
				}

				@Override
				public void onFailure(int code, String arg1) {
					System.out.println("上传失败" + code);
					dialog.dismiss();
					Toast.makeText(getActivity(), "上传失败，错误代码" + code, Toast.LENGTH_SHORT).show();
				}
			});
		} // end text is null

	}// end function

	public class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mViewPager.setCurrentItem(index);
		}
	}

	// 为三个专题设置回调接口
	public static void cartoonGetcall(CallBackInfo arg0) {
		cartoonCall = arg0;
	}

	public static void selectGetcall(CallBackInfo arg0) {
		selectCall = arg0;
	}

	public static void beautifulGetcall(CallBackInfo arg0) {
		beautifulCall = arg0;
	}

}
