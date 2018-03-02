package org.xxx.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.xxx.model.UsersRelationship;

public interface UsersRelationshipRepository 
extends JpaRepository<UsersRelationship, Long>,JpaSpecificationExecutor<UsersRelationship> {

	//根据fromuserid获取用户关系列表之已添加好友ID列表
	@Query(value = "select touser from users_relationship where fromuser=?1 and status=2", nativeQuery = true)
	public List<UsersRelationship> findUsersRelationshipByFromuserIdAndS2(Long fromuserid);

	//根据fromuserid获取用户关系列表之待确认好友ID列表
	@Query(value = "select touser from users_relationship where fromuser=?1 and status=1", nativeQuery = true)
	public List<UsersRelationship> findUsersRelationshipByFromuserIdAndS1(Long fromuserid);

	//根据fromuserid获取用户关系列表之拉黑好友ID列表
	@Query(value = "select touser from users_relationship where fromuser=?1 and status=3", nativeQuery = true)
	public List<UsersRelationship> findUsersRelationshipByFromuserIdAndS3(Long fromuserid);

	//根据fromuserid和touserid查询UsersRelationship是否存在，用户判断两位用户是否为好友
	@Query(value = "select * from users_relationship where fromuser=?1 and touserid=?2", nativeQuery = true)
	public List<UsersRelationship> findUsersRelationshipByFromuserIdAndToUserId(Long fromuserid,Long touserid);

	//根据fromuserid和touserid更新好友关系
	@Query(value="update users_relationship set status=?1 where fromuserid=?2 and touserid=?3", nativeQuery = true)
	public int updateUsersRelationshipStatusByFromuserIdAndToUserId(String status,Long fromuserid,Long touserid);

}
