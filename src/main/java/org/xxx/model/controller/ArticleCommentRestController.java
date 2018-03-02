package org.xxx.model.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxx.model.ArticleComment;
import org.xxx.model.service.impl.ArticleCommentServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders="Authorization")//跨域设置
@RestController
@RequestMapping(value="articlecomment")
public class ArticleCommentRestController {
	
	@Autowired
	ArticleCommentServiceImpl articleCommentServiceImpl;
	
	/*
	 * 根据文章ID查询所有评论返回列表
	 * http://127.0.0.1:8080/articlecomment/list
	 * */
	@RequestMapping(value="list",method=RequestMethod.GET,
			produces = {"application/JSON"}//指定映射类型,结合@ResponseBody注解用请求的媒体类型来产生对象
			)
	@ResponseBody
	public List<ArticleComment> findALLArticleCommentService(
			@RequestParam(value="articleid", required=true, defaultValue="1") Long articleid){
		List<ArticleComment> listdata = articleCommentServiceImpl.findALLArticleCommentService(articleid);
		return listdata;
	}

}
