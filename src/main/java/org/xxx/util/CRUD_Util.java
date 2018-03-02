package org.xxx.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.xxx.model.service.impl.ArticleServiceImpl;
import org.xxx.model.service.impl.UserServiceImpl;
import org.xxx.security.model.User;

public class CRUD_Util {
	
	@Autowired
	UserServiceImpl userServiceImpl;
	@Autowired
	ArticleServiceImpl articleServiceImpl;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		User user = new User();
		user.setUsername("1234567890");
		user.setPassword("$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi");
		user.setEmail("1234567890@123.com");
		user.setFirstname("123456");
		user.setLastname("123456");
	}

}
