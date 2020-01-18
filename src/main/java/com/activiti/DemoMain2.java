package com.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author :黄宗滨
 * @date 2019/12/21
 * @Description
 *  实现整合数据库的引用实例
 */
public class DemoMain2 {

	private static Logger log = LoggerFactory.getLogger(DemoMain2.class);


	/**
	 *  创建使用默认的文件名和路径用mysql创建表。
	 */
	public static void getDefaultConfiguration(){
		ProcessEngineConfiguration configuration =ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResourceDefault();
		log.info("获取的流程configuration-{}",configuration);
		ProcessEngine processEngine=configuration.buildProcessEngine();
		log.info("获取的流程引擎{}",processEngine.getName());
		processEngine.close();
	}

	/**
	 * 使用了连接池进行设置文件
	 */
	public static  void getConfigurationDruid(){
		ProcessEngineConfiguration configuration=ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti_druid.cfg.xml");
		log.info("获取流程引擎configuration-{}", configuration);
		ProcessEngine processEngine = configuration.buildProcessEngine();
		log.info("获取流程引擎{}",processEngine);
		processEngine.close();
	}

	/**
	 *  监听事件的配置
	 * @param
	 */

	public  static  void  getConfigurationListen(){
		ProcessEngineConfiguration configuration = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti_druid.cfg.xml");
		ProcessEngine processEngine = configuration.buildProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		DeploymentBuilder deployment = repositoryService.createDeployment();
		deployment.addClasspathResource("simple_approve.bpmn");
		Deployment deploy = deployment.deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
		//这才是获取到本次流程的实例
		ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceById(processDefinition.getId());
		log.info("启动成功");
		TaskService taskService = processEngine.getTaskService();
		Task task = taskService.createTaskQuery().singleResult();
		taskService.complete(task.getId());
		//
	}

	/**
	 *  设置含有job配置的执行单例
	 */
	public static void getConfigurationJob(){
		ProcessEngineConfiguration configuration = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti_job.cfg.xml");
		ProcessEngine processEngine = configuration.buildProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		DeploymentBuilder deployment = repositoryService.createDeployment().addClasspathResource("simple_approve.bpmn");;
		Deployment deploy = deployment.deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
		log.info("启动成功");
		//这才是获取到本次流程的实例
		ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceById(processDefinition.getId());

		TaskService taskService = processEngine.getTaskService();
		Task task = taskService.createTaskQuery().singleResult();
		taskService.complete(task.getId());
	}

	public static void main(String[] args) {

		// 创建使用mysql创建表。
		// getDefaultConfiguration();
		//getConfigurationDruid();
		getConfigurationListen();

	}



}