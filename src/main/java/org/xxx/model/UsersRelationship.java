package org.xxx.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.xxx.security.model.User;
/*
 * 用户
 * */
@Entity
@Table(name = "USERS_RELATIONSHIP")
public class UsersRelationship {
	
	@Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "usersrelationship_seq")//生成一个唯一标识的主键
    @SequenceGenerator(name = "usersrelationship_seq", sequenceName = "usersrelationship_seq", allocationSize = 1)//生成策略
	private Long id;//ID
	
	@JoinColumn(name = "FROMUSER", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    public User fromUser;

    @JoinColumn(name = "TOUSER", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    public User toUser;
	
	@Column(name = "PS", length = 50)
    @Size(min = 1, max = 50)
	private String ps;//附言
	
	@Column(name = "STATUS", length = 1)
	private Integer status;//状态(1：请求添加对方为好友待确认;2：好友;3：把对方拉黑;)
	
	@Column(name = "ADDTIME")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
	private Date addtime;//添加好友的申请时间
	
	public UsersRelationship() {
		
	}
	
	public UsersRelationship(User fromUser,User toUser,String ps,Integer status,Date addtime) {
		this.fromUser=fromUser;
		this.toUser=toUser;
		this.ps=ps;
		this.status=status;
		this.addtime=addtime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getFromUser() {
		return fromUser;
	}

	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}

	public User getToUser() {
		return toUser;
	}

	public void setToUser(User toUser) {
		this.toUser = toUser;
	}

	public String getPs() {
		return ps;
	}

	public void setPs(String ps) {
		this.ps = ps;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	

}
