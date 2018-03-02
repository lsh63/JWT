package org.xxx.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxx.model.ArticleComment;
import org.xxx.model.repository.ArticleCommentRepository;

/*
 * 用户文章评论业务实现
 * */
@Service
public class UserArticleCommentServiceImpl {
	
	@Autowired
	private ArticleCommentRepository articleCommentRepository;
	
	/*
	 * JPA添加文章评论
	 * */
	public ArticleComment addArticleCommentService(ArticleComment articleComment) {
		return articleCommentRepository.save(articleComment);
	}

}
