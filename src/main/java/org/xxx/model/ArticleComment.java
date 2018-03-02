package org.xxx.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/*
 * 文章评论
 * */
@Entity
@Table(name = "ARTICLECOMMENT")
public class ArticleComment {
	
	@Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "article_comment_seq")
    @SequenceGenerator(name = "article_comment_seq", sequenceName = "article_comment_seq", allocationSize = 1)
	private Long id;//评论ID
	
	@Column(name = "USERID")
    @NotNull
	private Long userid;//发布者用户ID
	
	@Column(name = "CONTENT")
    @NotNull
    @Size(min = 4, max = 5000)
	private String content;//文章评论内容
	
	@Column(name = "ARTICLEID")
    @NotNull
	private Long articleid;//文章ID
	
	@Column(name = "CREATEDATA")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
	private Date createDate;//文章评论发布日期
	
	//private User replyer;//回复
	
	public ArticleComment() {//无参构造方法
		
	}
	
	public ArticleComment(Long userid,String content,
			Date createDate,Long articleid) {//有参构造方法
		this.userid=userid;
		this.content=content;
		this.createDate=createDate;
		this.articleid=articleid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getArticleid() {
		return articleid;
	}

	public void setArticleid(Long articleid) {
		this.articleid = articleid;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



}
