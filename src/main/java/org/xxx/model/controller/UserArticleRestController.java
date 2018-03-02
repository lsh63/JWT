package org.xxx.model.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxx.model.Article;
import org.xxx.model.service.impl.RequestServiceImpl;
import org.xxx.model.service.impl.UserArticleServiceImpl;
import org.xxx.security.jwt.JwtUser;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders="Authorization")//跨域设置
@RestController
@RequestMapping(value="userarticle")
public class UserArticleRestController {
	
	@Autowired
	UserArticleServiceImpl userArticleServiceImpl;
	@Autowired
	RequestServiceImpl requestServiceImpl;
	
	/*
	 * 添加文章
	 * http://127.0.0.1:8080/userarticle/add
	 * */
	//@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders="Authorization")//跨域设置也可以在方法上
	@RequestMapping(value="add", method = RequestMethod.POST)
	public Map<String,String> addArticleService(
			HttpServletRequest request,
			@RequestParam(value="title", required=true, defaultValue="1") String title,
			@RequestParam(value="content", required=true, defaultValue="1") String content,
			@RequestParam(value="lable", required=true, defaultValue="1") String lable) {
		Map<String, String> map = new HashMap<String, String>();
		Timestamp createDate = new Timestamp(new Date().getTime());
        JwtUser user = requestServiceImpl.getUserByRequest(request);
        if(user!=null) {
        	Long userid = user.getId();
        	Article article = userArticleServiceImpl.addArticleService(new Article(userid, title, content, createDate, lable));
        	if((article!=null)) {
        		map.put("data", "添加成功");
        	}else {
        		map.put("data", "添加失败");
        	}
        }else {
        	map.put("data", "用户验证失败，请重新登录");
        }
		return map;	
	}
	
	/*
	 * 根据USERID返回文章列表
	 * http://127.0.0.1:8080/userarticle/list
	 * */
	@RequestMapping(value="list", method = RequestMethod.GET,//指定请求方法
			//,headers = {"content-type=text/plain","content-type=text/html"}//指定消息头
			produces = {"application/JSON"}//指定映射类型,结合@ResponseBody注解用请求的媒体类型来产生对象
			)
	@ResponseBody//将controller的方法返回的对象通过适当的转换器转换为指定的格式
	public Map<String,Object> findArticleByUserId(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		JwtUser user = requestServiceImpl.getUserByRequest(request);
		if(user!=null) {
        	Long userid = user.getId();
        	List<Article> articlelist = userArticleServiceImpl.findArticleByUserIdService(userid);
        	map.put("data", articlelist);
        }else {
        	map.put("data", "用户验证失败，请重新登录");
        }
		return map;
	}
	
	/*
	 * 根据ID+USERID删除文章
	 * http://127.0.0.1:8080/userarticle/del
	 * */
	@RequestMapping(value="del", method = RequestMethod.DELETE)
	public Map<String,String> delArticle(
			HttpServletRequest request,
			@RequestParam(value="id", required=true, defaultValue="1") Long id){
		Map<String, String> map = new HashMap<String, String>();
		JwtUser user = requestServiceImpl.getUserByRequest(request);
		if(user!=null) {
        	Long userid = user.getId();
        	int d = userArticleServiceImpl.delArticleByUserIdService(id, userid);
        	if(d>0) {
        		map.put("data", "删除成功");
        	}else {
        		map.put("data", "删除失败");
        	}
        }else {
        	map.put("data", "用户验证失败，请重新登录");
        }
		return map;
	}
	

}
