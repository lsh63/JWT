package org.xxx.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.xxx.model.ArticleFloorComment;

public interface ArticleFloorCommentRepository extends 
JpaRepository<ArticleFloorComment, Long>,JpaSpecificationExecutor<ArticleFloorComment> {

	//根据ARTICLEID获取文章评论
	@Query(value = "select * from articlefloorcomment where commentid=?1 ", nativeQuery = true)
	public List<ArticleFloorComment> findArticleFloorCommentByCommentId(Long commentid);

}
