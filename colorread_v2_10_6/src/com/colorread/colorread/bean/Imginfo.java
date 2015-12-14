package com.colorread.colorread.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Imginfo extends BmobObject{
	private String title,description,sort;
	private BmobFile file;
	
	public Imginfo(){
		
	}
	
	public Imginfo(String title, String description, String sort, BmobFile file) {
		this.title = title;
		this.description = description;
		this.sort = sort;
		this.file = file;
	}


	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public BmobFile getFile() {
		return file;
	}

	public void setFile(BmobFile string) {
		this.file = string;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
