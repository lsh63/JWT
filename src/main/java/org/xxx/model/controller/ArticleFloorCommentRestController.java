package org.xxx.model.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxx.model.ArticleFloorComment;
import org.xxx.model.service.impl.ArticleFloorCommentServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders="Authorization")//跨域设置
@RestController
@RequestMapping(value="articlefloorcomment")
public class ArticleFloorCommentRestController {
	
	@Autowired
	ArticleFloorCommentServiceImpl articleFloorCommentServiceImpl;
	
	/*
	 * 根据评论ID查询所有楼层内评论返回列表
	 * http://127.0.0.1:8080/articlefloorcomment/list
	 * */
	@RequestMapping(value="list",method=RequestMethod.GET,
			produces = {"application/JSON"}//指定映射类型,结合@ResponseBody注解用请求的媒体类型来产生对象
			)
	@ResponseBody
	public List<ArticleFloorComment> findALLArticleCommentService(
			@RequestParam(value="articleid", required=true, defaultValue="1") Long commentid){
		List<ArticleFloorComment> listdata = articleFloorCommentServiceImpl.findALLArticleFloorCommentService(commentid);
		return listdata;
	}

}
