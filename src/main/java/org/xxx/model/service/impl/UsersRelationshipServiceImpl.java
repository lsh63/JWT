package org.xxx.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxx.model.UsersRelationship;
import org.xxx.model.repository.UsersRelationshipRepository;

/*
 * 用户关系业务实现
 * */
@Service
public class UsersRelationshipServiceImpl {
	
	@Autowired
	UsersRelationshipRepository usersRelationshipRepository;
	
	/*
	 * 好友列表
	 * */
	public List<UsersRelationship> findUsersRelationshipByFromuserIdAndS2Service(Long fromuserid){
		return usersRelationshipRepository.findUsersRelationshipByFromuserIdAndS2(fromuserid);	
	}
	
	/*
	 * 待确认好友列表
	 * */
	public List<UsersRelationship> findUsersRelationshipByFromuserIdAndS1Service(Long fromuserid){
		return usersRelationshipRepository.findUsersRelationshipByFromuserIdAndS1(fromuserid);	
	}
	
	/*
	 * 拉黑好友列表
	 * */
	public List<UsersRelationship> findUsersRelationshipByFromuserIdAndS3Service(Long fromuserid){
		return usersRelationshipRepository.findUsersRelationshipByFromuserIdAndS3(fromuserid);	
	}
	
	/*
	 * 添加好友
	 * */
	public UsersRelationship addUsersRelationshipService(UsersRelationship usersRelationship) {
		return usersRelationshipRepository.save(usersRelationship);	
	}
	
	/*
	 * JPA删除好友
	 * */
	public void deleteUsersRelationshipService(UsersRelationship usersRelationship) {
		usersRelationshipRepository.delete(usersRelationship);
	}
	
	/*
	 * 是否已为好友
	 * */
	public List<UsersRelationship> findUsersRelationshipByFromuserIdAndToUserIdService(Long fromuserid,Long touserid) {
		return usersRelationshipRepository.findUsersRelationshipByFromuserIdAndToUserId(fromuserid, touserid);
	}
	
	/*
	 * 更新好友关系状态
	 * */
	public int updateUsersRelationshipStatusByFromuserIdAndToUserIdService(String status,Long fromuserid,Long touserid) {
		return usersRelationshipRepository
				.updateUsersRelationshipStatusByFromuserIdAndToUserId(status, fromuserid, touserid);
	}



}
