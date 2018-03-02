package org.xxx.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/*
 * 文章
 * */
@Entity
@Table(name = "ARTICLE")
public class Article{
	
	@Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "article_seq")
    @SequenceGenerator(name = "article_seq", sequenceName = "article_seq", allocationSize = 1)
	private Long id;//文章ID
	
	@Column(name = "USERID")
	//@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @NotNull
	private Long userId;//用户ID
	
	@Column(name = "TITLE", length = 50)
    @NotNull
    @Size(min = 4, max = 50)
	private String title;//标题
	
	@Column(name = "CONTENT")
    @NotNull
    @Size(min = 4, max = 21845)
	private String content;//文章内容
	
	@Column(name = "CREATEDATA")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
	private Date createDate;//文章发布日期
	
	@Column(name = "LABLE", length = 50)
    @Size(min = 4, max = 50)
	private String lable; // 帖子标签
	
	@Column(name = "STATUS", length = 50)
	private Integer status; // 文章地位，置顶:1、加精:2、加精且置顶:3
	
	public Article() {//无参构造方法
		
	}
	
	public Article(Long userId,String title,String content,
			Date createDate,String lable) {//有参构造方法
		this.userId=userId;
		this.title=title;
		this.content=content;
		this.createDate=createDate;
		this.lable=lable;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	

}
