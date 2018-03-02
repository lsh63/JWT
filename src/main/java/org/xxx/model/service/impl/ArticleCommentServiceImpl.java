package org.xxx.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxx.model.ArticleComment;
import org.xxx.model.repository.ArticleCommentRepository;

/*
 * 文章评论业务实现
 * */
@Service
public class ArticleCommentServiceImpl {
	
	@Autowired
	private ArticleCommentRepository articleCommentRepository;
	
	/*
	 * JPA查询所有文章评论
	 * */
	public List<ArticleComment> findALLArticleCommentService(Long articleid){
		List<ArticleComment> list = articleCommentRepository.findArticleCommentByArticleId(articleid);
		return list;
	}

}
