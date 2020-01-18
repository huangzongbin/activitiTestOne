package com.activiti.XxxService;

import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

/**
 * @Author :huangZB
 * @date 2020/1/18
 * @Description
 */
public class identityServiceDetail {

	private  static  final Logger log=LoggerFactory.getLogger(RepositoryService.class);


	/**
	 *
	 * 设置用户组
	 */
	public static void setMemebershipDetail(){

		ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti_druid.cfg.xml").buildProcessEngine();
		IdentityService identityService = processEngine.getIdentityService();
		// 创建用户1
		User user = createUser(identityService, UUID.randomUUID().toString(), "huang1", "ZB", "820118@qq.com", "abc");
		User user2 = createUser(identityService, UUID.randomUUID().toString(), "huang2", "ZB", "820118@qq.com", "abc");
		// 创建用户组
		Group grop = createGrop(identityService, UUID.randomUUID().toString(), "经理", "manager");
		identityService.createMembership(user.getId(),grop.getId());
		identityService.createMembership(user2.getId(),grop.getId());
		List<User> userList = identityService.createUserQuery().memberOfGroup(grop.getId()).list();
		// 查看该用户组的成员
		for (User user1 : userList) {
			log.info(user1.getFirstName()+user1.getLastName());
		}


	}

	/**
	 *  创建用户
	 * @param identityService
	 * @param id
	 * @param first
	 * @param last
	 * @param email
	 * @param pwd
	 */
	static User createUser(IdentityService identityService, String id, String first, String last, String email, String pwd){
		User user = identityService.newUser(id);
		user.setEmail(email);
		user.setFirstName(first);
		user.setLastName(last);
		user.setPassword(pwd);
		// 创建保存用户
		identityService.saveUser(user);
		// 获取返回用户
		return identityService.createUserQuery().userId(user.getId()).singleResult();
	}

	static Group createGrop(IdentityService identityService, String id, String name, String type){
		Group group = identityService.newGroup(id);
		group.setName(name);
		group.setType(type);
		// 保存用户组
		identityService.saveGroup(group);
		// 获取用户组
		return identityService.createGroupQuery().groupId(group.getId()).singleResult();
	}

	public static void main(String[] args) {
		setMemebershipDetail();
	}
}
