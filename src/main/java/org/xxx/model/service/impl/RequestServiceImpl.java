package org.xxx.model.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.xxx.security.jwt.JwtTokenUtil;
import org.xxx.security.jwt.JwtUser;

/*
 * 身份验证业务类
 * */
@Service
public class RequestServiceImpl {
	
	@Value("${jwt.header}")
    private String tokenHeader;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    
    /*
     * 身份验证：通过请求（头）返回用户
     * */
    public JwtUser getUserByRequest(HttpServletRequest request) {
    	//根据request请求对象获得token
        String token = request.getHeader(tokenHeader).substring(7);
        //根据token获得username
        String username = jwtTokenUtil.getUsernameFromToken(token);
        //根据username获得user对象
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
		return user; 	
    }

}
