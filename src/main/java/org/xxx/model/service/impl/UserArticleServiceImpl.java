package org.xxx.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxx.model.Article;
import org.xxx.model.repository.ArticleRepository;

/*
 * 用户-文章业务实现
 * */
@Service
public class UserArticleServiceImpl {
	
	@Autowired
	private ArticleRepository articleRepository;
	
	/*
	 * JPA添加文章
	 * */
	public Article addArticleService(Article article) {
		return articleRepository.save(article);
		}
	
	/*
	 * 根据USERID返回文章列表
	 * */
	public List<Article> findArticleByUserIdService(Long userid) {
		List<Article> articlelist = articleRepository.findArticleByUserId(userid);
		return articlelist;	
	}
	
	/*
	 * 根据ID和USERID删除文章
	 * */
	public int delArticleByUserIdService(Long id,Long userid) {
		int d = articleRepository.delArticle(id, userid);
		return d;	
	}

}
