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
 * 文章楼层评论
 * */
@Entity
@Table(name = "ARTICLEFLOORCOMMENT")
public class ArticleFloorComment {
	
	@Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "article_floor_comment_seq")
    @SequenceGenerator(name = "article_floor_comment_seq", sequenceName = "article_floor_comment_seq", allocationSize = 1)
	private Long id;//文章楼层评论ID
	
	@Column(name = "USERID")
    @NotNull
	private Long userid;//发布者用户ID
	
	@Column(name = "CONTENT")
    @NotNull
    @Size(min = 4, max = 5000)
	private String content;//文章楼层评论内容
	
	@Column(name = "COMMENTID")
    @NotNull
	private Long commentid;//评论ID
	
	@Column(name = "CREATEDATA")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
	private Date createDate;//文章评论发布日期
	
	public ArticleFloorComment() {//无参构造方法
		
	}
	
	public ArticleFloorComment(Long userid,String content,
			Date createDate,Long commentid) {//有参构造方法
		this.userid=userid;
		this.content=content;
		this.createDate=createDate;
		this.commentid=commentid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getCommentid() {
		return commentid;
	}

	public void setComtentid(Long commentid) {
		this.commentid = commentid;
	}
	
	

}
