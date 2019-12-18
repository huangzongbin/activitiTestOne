package com.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;


/**
 * @Author :黄宗滨
 * @date 2019/12/18
 * @Description
 */
public class DemoMain {

	private  static Logger log= LoggerFactory.getLogger(DemoMain.class);

	public static void main(String[] args) {
	log.info("启动程序");
		// 创建流程引擎
			// 创建流程引擎配置对象
		ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
			// 获取流程引擎
		ProcessEngine processEngine=cfg.buildProcessEngine();
			// 获取流程引擎的name
		String name =processEngine.getName();
			// 获取流程引擎的版本
		String version=ProcessEngine.VERSION;
		log.info("流程引擎名字{}和版本{}",name,version);

		// 部署流程定义文件
		// 启动运行流程
		// 处理流程任务
	log.info("结束程序");
	}
}
