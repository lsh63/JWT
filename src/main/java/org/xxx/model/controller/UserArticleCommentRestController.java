package org.xxx.model.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.xxx.model.ArticleComment;
import org.xxx.model.service.impl.RequestServiceImpl;
import org.xxx.model.service.impl.UserArticleCommentServiceImpl;
import org.xxx.security.jwt.JwtUser;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders="Authorization")//跨域设置
@RestController
@RequestMapping(value="userarticlecomment")
public class UserArticleCommentRestController {
	
	@Autowired
	UserArticleCommentServiceImpl userArticleCommentServiceImpl;
	@Autowired
	RequestServiceImpl requestServiceImpl;
	
	/*
	 * 添加评论
	 * http://127.0.0.1:8080/userarticlecomment/add
	 * */
	@RequestMapping(value="add", method = RequestMethod.POST )
	public Map<String,String> addArticleCommentService(
			HttpServletRequest request,
			@RequestParam(value="content", required=true, defaultValue="1") String content,
			@RequestParam(value="articleid", required=true, defaultValue="1") Long articleid) {
		Map<String, String> map = new HashMap<String, String>();
		Timestamp createDate = new Timestamp(new Date().getTime());
        JwtUser user = requestServiceImpl.getUserByRequest(request);
        if(user!=null) {
        	Long userid = user.getId();
        	ArticleComment articleComment = userArticleCommentServiceImpl.addArticleCommentService(new ArticleComment(userid, content, createDate, articleid));
        	if(articleComment != null) {
        		map.put("data", "添加成功");
        	}else {
        		map.put("data", "添加失败");
        	}
        }else {
        	map.put("data", "用户验证失败，请重新登录");
        }
		return map;	
	}

}
