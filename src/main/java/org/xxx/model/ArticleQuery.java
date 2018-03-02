package org.xxx.model;

import java.util.Date;

public class ArticleQuery {
	
	//private Long id;//文章ID
	private Long userId;//用户ID
	private String title;//标题
	private String content;//文章内容
	private Date createDate;//文章发布日期
	private String lable; // 帖子标签
	private Long status;//文章地位
	
	
	/*
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	*/
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	
	

}
