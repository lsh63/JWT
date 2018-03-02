package org.xxx.model.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxx.model.repository.UserRepository;
import org.xxx.security.model.User;

/*
 * 用户业务实现
 * */
@Service
public class UserServiceImpl {
	
	@Autowired
	private UserRepository userrepository;
	
	/*
	 * JPA查询所有用户
	 * */
	public List<User> findAllUser(){
		return userrepository.findAll();
	}
	
	/*
	 * JPA添加用户
	 * */
	public User saveUser(User user) {
		return userrepository.save(user);
	}
	
	/*
	 * JPA根据用户名查询用户对象
	 * */
	public User findUserByUsername(String username) {
		return userrepository.findByUsername(username);
	}
	
	/*
	 * SQL添加用户
	 * */
	public int addUser(String username,String password,String firstname,String lastname,String email,
    		Boolean enabled,Timestamp lastPasswordResetDate) {
		int result = userrepository.addUser(username, password, firstname, lastname, email, enabled, lastPasswordResetDate);
		return result;
	}
	
	/*
	 * SQL关键词搜索用户
	 * */
	public List<User> findUserByKey(String key){
		return userrepository.findUserByKey(key);		
	}
	
	

}
