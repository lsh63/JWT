package org.xxx.security.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.xxx.model.UsersRelationship;
/*
 * 用户
 * */
@Entity
@Table(name = "USER")
public class User {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_seq")//生成一个唯一标识的主键
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)//生成策略
    private Long id;

    @Column(name = "USERNAME", length = 50, unique = true)//唯一
    @NotNull
    @Size(min = 4, max = 50)
    private String username;

    @Column(name = "PASSWORD", length = 100)
    @NotNull
    @Size(min = 4, max = 100)
    private String password;

    @Column(name = "FIRSTNAME", length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    private String firstname;

    @Column(name = "LASTNAME", length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    private String lastname;

    @Column(name = "EMAIL", length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    private String email;

    @Column(name = "ENABLED")
    @NotNull
    private Boolean enabled;

    @Column(name = "LASTPASSWORDRESETDATE")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date lastPasswordResetDate;
    
    @Column(name = "IMGURL")
    @NotNull
    private String imgurl;//用户头像地址

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")})
    private List<Authority> authorities;//权限列表
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fromUser")
    public List<UsersRelationship> fromusers;//用户关系列表fromuser

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "toUser")
    public List<UsersRelationship> tousers;//用户关系列表touser

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
	public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }
    
    public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	
	public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

	public List<UsersRelationship> getFromusers() {
		return fromusers;
	}

	public void setFromusers(List<UsersRelationship> fromusers) {
		this.fromusers = fromusers;
	}

	public List<UsersRelationship> getTousers() {
		return tousers;
	}

	public void setTousers(List<UsersRelationship> tousers) {
		this.tousers = tousers;
	}
	

	/**
     * The default constructor only exists for the sake of JPA. You won’t use it directly, 
     * so it is designated as protected.
     */
    public User() {
    }

    public User(String username,String password,String firstname,String lastname,String email,
    		Boolean enabled,Timestamp lastPasswordResetDate, String imgurl) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.enabled = enabled;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.imgurl = imgurl;
    }
}