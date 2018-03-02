package org.xxx.model.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.xxx.model.Article;
import org.xxx.model.ArticleQuery;
import org.xxx.model.repository.ArticleRepository;

/*
 * 文章业务实现
 * */
@Service
public class ArticleServiceImpl {

	@Autowired
	private ArticleRepository articleRepository;

	/*
	 * JPA查询所有文章
	 * */
	public List<Article> findALLArticleService(){
		List<Article> list = articleRepository.findAll();
		return list;
	}

	/*
	 * JPA分页查询文章，不带查询条件
	 * */
	public Page<Article> findArticleNoCriteriaService(Integer page,Integer size){
		Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id"); 
		return articleRepository.findAll(pageable);
	}
	/*
	 * JPA分页查询文章，带查询条件
	 * */
	public Page<Article> findArticleCriteriaService(Integer page, Integer size, final ArticleQuery articleQuery) {  
		Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id");  
		Page<Article> articlePage = articleRepository.findAll(new Specification<Article>(){ 
			@SuppressWarnings("unlikely-arg-type")
			@Override
			public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, 
					CriteriaBuilder criteriaBuilder) {  
				List<Predicate> list = new ArrayList<Predicate>();  
				if(null!=articleQuery.getTitle()&&!"".equals(articleQuery.getTitle())){  
					list.add(criteriaBuilder.equal(root.get("name").as(String.class), 
							articleQuery.getTitle()));  
				}  
				if(null!=articleQuery.getUserId()&&!"".equals(articleQuery.getUserId())){  
					list.add(criteriaBuilder.equal(root.get("isbn").as(String.class), 
							articleQuery.getUserId()));  
				}  
				if(null!=articleQuery.getContent()&&!"".equals(articleQuery.getContent())){  
					list.add(criteriaBuilder.equal(root.get("author").as(String.class), 
							articleQuery.getContent()));  
				}  
				Predicate[] p = new Predicate[list.size()];  
				return criteriaBuilder.and(list.toArray(p));  
			}
		},pageable);  
		return articlePage;
	}
	
	/*
	 * 根据文章ID返回文章（详情）
	 * */
	public Article findArticleByIdService(Long id) {
		Article article = articleRepository.findArticleById(id);
		return article;	
	}

	

}
