package com.colorread.colorread.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class BigNews extends BmobObject{
	private String title,link,bigNews,html;
	private int improtant;
	public int getImprotant() {
		return improtant;
	}
	public void setImprotant(int improtant) {
		this.improtant = improtant;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getBigNews() {
		return bigNews;
	}
	public void setBigNews(String bigNews) {
		this.bigNews = bigNews;
	}
	private BmobFile pic;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public BmobFile getPic() {
		return pic;
	}
	public void setPic(BmobFile pic) {
		this.pic = pic;
	}
	
}
