package org.xxx.model.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxx.model.Article;
import org.xxx.model.service.impl.ArticleServiceImpl;
import org.xxx.model.service.impl.RequestServiceImpl;
import com.alibaba.fastjson.JSON;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders="Authorization")//跨域设置
@RestController
@RequestMapping(value="article")
public class ArticleRestController {
	
	@Autowired
	ArticleServiceImpl articleServiceImpl;
	@Autowired
	RequestServiceImpl requestServiceImpl;
	
	/*
	 * 查询所有文章返回列表
	 * http://127.0.0.1:8080/article/list
	 * */
	@RequestMapping(value="list",method=RequestMethod.GET,
			produces = {"application/JSON"}//指定映射类型,结合@ResponseBody注解用请求的媒体类型来产生对象
			)
	@ResponseBody//将controller的方法返回的对象通过适当的转换器转换为指定的格式
	//将controller的方法返回的对象通过适当的转换器转换为指定的格式之后，
	//写入到response对象的body区,通常用来返回JSON数据或者是XML
	public List<Article> findArticleAll() {
		List<Article> listdata = articleServiceImpl.findALLArticleService();
		return listdata;
	}
	
	/*
	 * 查询文章返回分页
	 * http://127.0.0.1:8080/article/page
	 * */
	@RequestMapping(value="page",method=RequestMethod.GET, produces = {"application/JSON"})
	@ResponseBody
	public Page<Article> findArticleNoCriteria(
			@RequestParam(value="page", required=true, defaultValue="1") Integer page,//必须传参，默认为1
			@RequestParam(value="size", required=true, defaultValue="2") Integer size) {
		Page<Article> pagedata  = articleServiceImpl.findArticleNoCriteriaService(page, size);
		return pagedata;
		}

	/*
	 * 查询文章返回json字符串
	 * http://127.0.0.1:8080/article/page_str
	 * */
	@RequestMapping(value="page_str",method=RequestMethod.GET, produces = {"application/JSON"})
	@ResponseBody
	public String findArticleNoCriteriaStr(
			@RequestParam(value="page", required=true, defaultValue="1") Integer page,
			@RequestParam(value="size", required=true, defaultValue="2") Integer size) {
		Page<Article> pagedata = articleServiceImpl.findArticleNoCriteriaService(page, size);
		return JSON.toJSONString(pagedata);
	}
	
	/*
	 * 根据ID返回文章(详情)
	 * http://127.0.0.1:8080/article/bean
	 * */
	@RequestMapping(value="bean",method=RequestMethod.GET, produces = {"application/JSON"})
	@ResponseBody
	public Article findArticleById(
			@RequestParam(value="id", required=true, defaultValue="1") Long id) {
		System.out.println(id);
		Article article = articleServiceImpl.findArticleByIdService(id);
		return article;	
	}
	
	
	
}
