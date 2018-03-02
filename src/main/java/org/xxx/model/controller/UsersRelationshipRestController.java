package org.xxx.model.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxx.model.UsersRelationship;
import org.xxx.model.service.impl.RequestServiceImpl;
import org.xxx.model.service.impl.UserServiceImpl;
import org.xxx.model.service.impl.UsersRelationshipServiceImpl;
import org.xxx.security.jwt.JwtUser;
import org.xxx.security.model.User;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders="Authorization")//跨域设置
@RestController
@RequestMapping(value="userrelationship")
public class UsersRelationshipRestController {
	
	@Autowired
	UsersRelationshipServiceImpl usersRelationshipServiceImpl;
	@Autowired
	RequestServiceImpl requestServiceImpl;
	@Autowired
	UserServiceImpl userServiceImpl;
	
	/*
	 * 根据request返回用户好友列表
	 * http://127.0.0.1:8080/userrelationship/s2list
	 * */
	@RequestMapping(value="s2list", method = RequestMethod.GET,
			produces = {"application/JSON"}//指定映射类型,结合@ResponseBody注解用请求的媒体类型来产生对象
			)
	@ResponseBody
	public List<UsersRelationship> findUsersRelationshipByFromuserIdAndS2(
			HttpServletRequest request){
		JwtUser user = requestServiceImpl.getUserByRequest(request);
		Long userid = user.getId();
		return usersRelationshipServiceImpl.findUsersRelationshipByFromuserIdAndS2Service(userid);	
	}
	
	/*
	 * 根据request返回用户待通过好友列表
	 * http://127.0.0.1:8080/userrelationship/s1list
	 * */
	@RequestMapping(value="s1list", method = RequestMethod.GET, produces = {"application/JSON"})
	@ResponseBody
	public List<UsersRelationship> findUsersRelationshipByFromuserIdAndS1(
			HttpServletRequest request){
		JwtUser user = requestServiceImpl.getUserByRequest(request);
		Long userid = user.getId();
		return usersRelationshipServiceImpl.findUsersRelationshipByFromuserIdAndS1Service(userid);	
	}
	
	/*
	 * 根据request返回用户拉黑好友列表
	 * http://127.0.0.1:8080/userrelationship/s3list
	 * */
	@RequestMapping(value="s3list", method = RequestMethod.GET, produces = {"application/JSON"})
	@ResponseBody
	public List<UsersRelationship> findUsersRelationshipByFromuserIdAndS3(
			HttpServletRequest request){
		JwtUser user = requestServiceImpl.getUserByRequest(request);
		Long userid = user.getId();
		return usersRelationshipServiceImpl.findUsersRelationshipByFromuserIdAndS3Service(userid);	
	}
	
	/*
	 * 申请添加好友
	 * http://127.0.0.1:8080/userrelationship/add
	 * */
	@RequestMapping(value="add", method = RequestMethod.POST)
	public Map<String, String> addUsersRelationship(
			HttpServletRequest request,
			@RequestParam(value="tousername", required=true) String tousername,//对方用户名
			@RequestParam(value="ps", required=true) String ps) {
		Map<String, String> map = new HashMap<String, String>();
		JwtUser user = requestServiceImpl.getUserByRequest(request);
		String username = user.getUsername();
		User fromuser = userServiceImpl.findUserByUsername(username);
		Long fromuserid = fromuser.getId();
		User touser = userServiceImpl.findUserByUsername(tousername);
		Long touserid = touser.getId();
		Integer status = 1;//状态为待通过
		Timestamp addtime = new Timestamp(new Date().getTime());
		List<UsersRelationship> isUsersRelationship = usersRelationshipServiceImpl
				.findUsersRelationshipByFromuserIdAndToUserIdService(fromuserid, touserid);
		if(isUsersRelationship!=null) {
			map.put("data", "已经是好友！");
		}
		UsersRelationship usersRelationship = usersRelationshipServiceImpl
				.addUsersRelationshipService(new UsersRelationship(fromuser,touser,ps,status,addtime));
		if(usersRelationship!=null) {
			map.put("data", "申请成功！");
		}else {
			map.put("data", "申请失败！");
		}
		return map;
	}
	
	/*
	 * 删除好友
	 * http://127.0.0.1:8080/userrelationship/delete
	 * */
	@RequestMapping(value="delete", method = RequestMethod.DELETE)
	public Map<String, String> deleteUsersRelationship(
			HttpServletRequest request,
			@RequestParam(value="tousername", required=true) String tousername){
		Map<String, String> map = new HashMap<String, String>();
		JwtUser user = requestServiceImpl.getUserByRequest(request);
		String username = user.getUsername();
		User fromuser = userServiceImpl.findUserByUsername(username);
		Long fromuserid = fromuser.getId();
		User touser = userServiceImpl.findUserByUsername(tousername);
		Long touserid = touser.getId();
		List<UsersRelationship> isUsersRelationship = usersRelationshipServiceImpl
				.findUsersRelationshipByFromuserIdAndToUserIdService(fromuserid, touserid);
		UsersRelationship usersRelationship = (org.xxx.model.UsersRelationship) isUsersRelationship;
		usersRelationshipServiceImpl.deleteUsersRelationshipService(usersRelationship);
		List<UsersRelationship> isDelUsersRelationship = usersRelationshipServiceImpl
				.findUsersRelationshipByFromuserIdAndToUserIdService(fromuserid, touserid);
		if(isDelUsersRelationship!=null) {
			map.put("data", "删除失败");
		}else {
			map.put("data", "删除成功！");
		}
		return map;
	}

}
