package org.xxx.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xxx.security.jwt.JwtTokenUtil;
import org.xxx.security.jwt.JwtUser;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders="Authorization")
@RestController
@RequestMapping(value = "jwtuser")
public class JwtUserRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    /*
     * 获取用户信息
     * http://127.0.0.1:8080/jwtuser/info
     * */
    
    @RequestMapping(value = "info", method = RequestMethod.GET)
    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
    	//根据request请求对象获得token
        String token = request.getHeader(tokenHeader).substring(7);
        //根据token获得username
        String username = jwtTokenUtil.getUsernameFromToken(token);
        //根据username获得user对象
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);;
		if(jwtTokenUtil.validateToken(token, userDetails)) {
        	System.out.println("有效");
        }else {
        	System.out.println("无效");
        }
        
        return user;
    }
    
    @RequestMapping(value = "validata", method = RequestMethod.GET)
    public Map<String, Object> getValidateToken(HttpServletRequest request){
    	Map<String, Object> map = new HashMap<String, Object>();
    	String token = request.getHeader(tokenHeader).substring(7);
    	String username = jwtTokenUtil.getUsernameFromToken(token);
    	UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    	if(jwtTokenUtil.validateToken(token, userDetails)) {
    		map.put("status", 200);
        	System.out.println("有效");
        }else {
        	map.put("status", 500);
        	System.out.println("无效");
        }
		return map;	
    }
    
    
    /*
     * 获取用户信息
     * http://127.0.0.1:8080/jwtuser/info
     * */
    /*
    @RequestMapping(value = "info", method = RequestMethod.GET)
    public Map<String, Object> getAuthenticatedUser(HttpServletRequest request) {
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	//根据request请求对象获得token
        String token = request.getHeader(tokenHeader).substring(7);
        //根据token获得username
        String username = jwtTokenUtil.getUsernameFromToken(token);
        
        //判断token是否有效
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		if(jwtTokenUtil.validateToken(token, userDetails)) {
        	System.out.println("有效");
        }else {
        	System.out.println("无效");
        }
        
        //根据username获得user对象
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        map.put("user", user);
        
        return map;
    }
    */
    
    

}
