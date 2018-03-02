package org.xxx.model.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.xxx.security.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	//JPA根据解析方法名称自动实现接口，如下：
	User findByUsername(String username);
	
	/*data-JPA自定义SQL*/
	/*
	 * 不使用@Param注解来声明参数时，必须使用使用 #{}方式。如果使用 ${} 的方式，会报错
	 * 不使用@Param注解时，参数只能有一个，并且是Javabean
	 */
	@Query(value="insert into user(username,password,firstname,lastname,email,enabled,lastPasswordResetDate) "
			+ "values(#{username},#{password}),#{firstname},#{lastname},#{email},#{enabled},#{lastPasswordResetDate}", 
			nativeQuery = true)//使用原生的sql语句（根据数据库的不同，在sql的语法或结构方面可能有所区别）进行查询数据库的操作
    int addUser(@Param("username") String username,@Param("password") String password,
    		@Param("firstname") String firstname,@Param("lastname") String lastname,@Param("email") String email,
    		@Param("enabled") Boolean enabled,@Param("lastPasswordResetDate") Timestamp lastPasswordResetDate);
	
	
	
	
	/*
	 * 关键词搜索
	 * */
	//@Query(value="select * from user where username like 1?", nativeQuery = true)
	@Query(value="select * from user where username like '%'1?'%'", nativeQuery = true)
	List<User> findUserByKey(String key);
	
}
