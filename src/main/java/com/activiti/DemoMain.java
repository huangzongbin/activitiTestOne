package com.activiti;

import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


/**
 * @Author :黄宗滨
 * @date 2019/12/18
 * @Description
 */
public class DemoMain {

	private static Logger log = LoggerFactory.getLogger(DemoMain.class);

	public static void main(String[] args) throws ParseException {
		log.info("启动程序");
		// 创建流程引擎,获取流程引擎配置对象
		ProcessEngine processEngine = getProcessEngine();

		// 部署流程定义文件
		ProcessDefinition processDefinition = getProcessDefinition(processEngine);

		// 启动运行流程,运行时对象
		ProcessInstance processInstance = getProcessInstance(processEngine, processDefinition);

		// 处理流程任务  获取taskService
		Scanner scanner = new Scanner(System.in);
		while (processInstance != null && !processInstance.isEnded()) {
			TaskService taskService = processEngine.getTaskService();
			// 获取处理任务列表
			List<Task> list = taskService.createTaskQuery().list();
			if (list.size()==0)break;
			log.info("处理任务数量[{}]", list.size());
			for (Task task : list) {
				log.info("待处理任务[{}]", task.getName());

				//获取表单的参数
				FormService formservice = processEngine.getFormService();
				TaskFormData taskFormData = formservice.getTaskFormData(task.getId());
				// 获取参数列表
				List<FormProperty> formProperties = taskFormData.getFormProperties();
				HashMap<String, Object> map = new HashMap<String, Object>(5);
				for (FormProperty formProperty : formProperties) {
					String line = null;
					if (StringFormType.class.isInstance(formProperty.getType())) {
						log.info("请输入{}", formProperty.getName());
						line = scanner.nextLine();
						map.put(formProperty.getId(), line);
					} else if (DateFormType.class.isInstance(formProperty.getType())) {
						log.info("您要输入{}格式为(yyyy-MM-dd)", formProperty.getName());
						line = scanner.nextLine();
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						Date date = format.parse(line);
						map.put(formProperty.getId(), date);
					} else {
						log.info("不支持该类型{}", formProperty.getType());
					}
					log.info("您输入的内容是[{}]", line);
				}

				// 单个流程结束
				taskService.complete(task.getId(), map);
				// 查看当前流程实例状态
				processEngine.getRuntimeService()
						.createProcessInstanceQuery()
						.processInstanceId(processInstance.getId())
						.singleResult();
			}
		}
		log.info("结束程序");
	}

	/**
	 * 启动运行流程
	 *
	 * @param processEngine
	 * @param processDefinition
	 * @return
	 */
	private static ProcessInstance getProcessInstance(ProcessEngine processEngine, ProcessDefinition processDefinition) {
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		log.info("启动流程{}", processInstance.getProcessDefinitionKey());
		return processInstance;
	}

	/**
	 * 获取流程定义文件
	 * <p>
	 * 管理流程定义文件xml及静态资源的服务,对特定流程的暂停和激活
	 * 流程定义启动权限管理
	 * 类内部重要的成员有：
	 * deploymentBuilder 部署文件构造器
	 * deploymentQuery 部署文件查询器
	 * ProcessDefinitionQuery 流程定义文件查询对象
	 * Deployment 流程部署文件对象
	 * ProcessDefinition 流程定义文件对象
	 * BpmnModel 流程定义的java格式
	 *
	 * @param processEngine
	 * @return
	 */
	private static ProcessDefinition getProcessDefinition(ProcessEngine processEngine) {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("second_approve2.bpmn");
		Deployment deploy = deploymentBuilder.deploy();
		// 通过id获取流程定义对象id
		String deployId = deploy.getId();
		// 使用repositoryService 获取单个流程定义对象
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();

		// 使用 repositoryService 获取流程定义列表
		// ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).asc().listPage(0,100)
		log.info("流程定义文件:{}，流程定义id:{}", processDefinition.getName(), processDefinition.getId());
		return processDefinition;
	}

	/**
	 * 获取流程引擎的方法
	 *
	 * @return
	 */
	private static ProcessEngine getProcessEngine() {
		ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
		// 获取流程引擎
		ProcessEngine processEngine = cfg.buildProcessEngine();
		// 获取流程引擎的name
		String name = processEngine.getName();
		// 获取流程引擎的版本
		String version = ProcessEngine.VERSION;
		log.info("流程引擎名字{}和版本{}", name, version);
		return processEngine;
	}
}
