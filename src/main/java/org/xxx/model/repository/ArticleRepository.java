package org.xxx.model.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.xxx.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Long>,JpaSpecificationExecutor<Article> {

	/*data-JPA自定义JPQL*/
	//根据ID获取文章
	@Query(value = "select * from article where id=?1 ", nativeQuery = true)
	public Article findArticleById(Long id);

	//根据USERID获取文章列表
	@Query(value = "select * from article where userid=?1 ", nativeQuery = true)
	public List<Article> findArticleByUserId(Long userid);
	
	//添加文章
	@Modifying//在 @Query注解中编写JPQL语句，但必须使用@Modifying进行修饰.以通知SpringData,这是一个UPDATE或DELETE操作
	@Query(value="insert into article(userid,title,content,createdate,lable) values(?1,?2,?3,?4,?5)", nativeQuery = true)
	public int addArticle(Long userid,String title,String content,Timestamp createDate,String lable);
	
	//删除文章，userid用于token验证
	@Query(value="delete from article where id=?1 and userid=?2", nativeQuery = true)
	public int delArticle(Long id,Long userid);
	
	//更新文章地位
	@Query(value="update article set status=?1 where id=?2", nativeQuery = true)
	public int updateArticle(String status,Long id);


}
