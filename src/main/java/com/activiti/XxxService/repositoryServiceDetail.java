package com.activiti.XxxService;


import com.sun.corba.se.impl.orbutil.RepositoryIdUtility;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.RepositoryType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * @Author :huangZB
 * @date 2020/1/8
 * @Description
 */
public class repositoryServiceDetail {

	private  static  final Logger log=LoggerFactory.getLogger(RepositoryService.class);

	/**
	 * 部署单个流程
	 */
	public static void getRepositoryServiceDetail()  {

		RepositoryService repositoryService = getRepositoryService();
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		// 接下来可以对与这个流程部署文件进行命名
		deploymentBuilder.name("测试部署");
		// 部署两个资源文件

		deploymentBuilder.addClasspathResource("simple_approve.bpmn")
				.addClasspathResource("second_approve2.bpmn");
		// 文件部署后，就可以进行发布操作
		Deployment deploy = deploymentBuilder.deploy();
		log.info("流程部署文件查看{}",deploy);
		// 可以通过这个方法获取流程部署文件查询器  如果是一个流程可以直接不用这方法
		DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
		Deployment deployment = deploymentQuery.deploymentId(deploy.getId()).singleResult();
		log.info("流程部署文件查看方式二{}",deployment);
		// 查询流程定义的实体list   获取流程定义列表   deploymentId(deploy.getId())
		List<ProcessDefinition> definitionList = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).listPage(0, 100);
		
		// 接下来对这个流程定义列表打印出来
		for (ProcessDefinition processDefinition : definitionList) {
			log.info("processDefinition={},version={}",processDefinition,processDefinition.getVersion());
		}

	}

	/**
	 * 部署两个流程
	 */
	public static void getRepositoryServiceDetail2(){
		RepositoryService repositoryService = getRepositoryService();

		DeploymentBuilder deploymentBuilder2 = repositoryService.createDeployment().addClasspathResource("simple_approve.bpmn").name("第二个流程");
		// 其中 DeploymentBuilder  中包含一个 enableDuplicateFiltering 方法， 这个方法属性设置为true时，会去对象的名称去查询最后一条部署记录，如果最后一条记录于现在部署的一致就不重复部署了。
		deploymentBuilder2.enableDuplicateFiltering();
		Deployment deploy2 = deploymentBuilder2.deploy();

		// 获取流程部署文件 所有部署过的文件。
		DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
		List<Deployment> deployments = deploymentQuery.orderByDeploymenTime().asc().listPage(0, 100);
		log.info("deployment的数量={}",deployments.size());
		for (Deployment deployment : deployments) {
			log.info("deployment={}",deployment);
		}
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy2.getId()).singleResult();

		repositoryService.suspendProcessDefinitionById(processDefinition.getId());
		log.info("流程进行终止");
		repositoryService.activateProcessDefinitionById(processDefinition.getId());
		log.info("流程激活");

			log.info("processDefinition={},version={}",processDefinition,processDefinition.getVersion());




	}

	/**
	 *  与用户组进行关联
	 */
	public static void getRepositoryServiceDetail3(){

		RepositoryService repositoryService = getRepositoryService();
		DeploymentBuilder deployment = repositoryService.createDeployment();
		Deployment deploy = deployment.addClasspathResource("simple_approve.bpmn").name("添加用户组流程").deploy();

		//获取流程定义实体
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();


		repositoryService.addCandidateStarterUser(processDefinition.getId(),"user1");
		repositoryService.addCandidateStarterUser(processDefinition.getId(),"user2");

		// 创建

	}


	/**
	 *  获取 RepositoryService
	 * @return
	 */
	private static RepositoryService getRepositoryService() {
		ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti_druid.cfg.xml");
		ProcessEngine processEngine = configuration.buildProcessEngine();
		return processEngine.getRepositoryService();
	}

	public static void main(String[] args) {
		//getRepositoryServiceDetail();
		//getRepositoryServiceDetail2();

		getRepositoryServiceDetail3();
	}
}
