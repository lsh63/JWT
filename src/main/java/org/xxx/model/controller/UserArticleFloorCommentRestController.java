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
import org.xxx.model.ArticleFloorComment;
import org.xxx.model.service.impl.RequestServiceImpl;
import org.xxx.model.service.impl.UserArticleFloorCommentServiceImpl;
import org.xxx.security.jwt.JwtUser;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders="Authorization")//跨域设置
@RestController
@RequestMapping(value="userarticlefloorcomment")
public class UserArticleFloorCommentRestController {
	
	@Autowired
	UserArticleFloorCommentServiceImpl userArticleFloorCommentServiceImpl;
	@Autowired
	RequestServiceImpl requestServiceImpl;
	
	/*
	 * 添加楼层内评论
	 * http://127.0.0.1:8080/userarticlefloorcomment/add
	 * */
	@RequestMapping(value="add", method = RequestMethod.POST )
	public Map<String,String> addArticleFloorCommentService(
			HttpServletRequest request,
			@RequestParam(value="content", required=true, defaultValue="1") String content,
			@RequestParam(value="commentid", required=true, defaultValue="1") Long commentid) {
		Map<String, String> map = new HashMap<String, String>();
		Timestamp createDate = new Timestamp(new Date().getTime());
        JwtUser user = requestServiceImpl.getUserByRequest(request);
        if(user!=null) {
        	Long userid = user.getId();
        	ArticleFloorComment articleFloorComment = userArticleFloorCommentServiceImpl.addArticleFloorCommentService(new ArticleFloorComment(userid, content, createDate, commentid));
        	if(articleFloorComment != null) {
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
