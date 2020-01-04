package com.activiti.config;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Author :黄宗滨
 * @date 2020/1/4
 * @Description
 */
public class ConfigEventListener implements ActivitiEventListener {

	private  static final Logger log = LoggerFactory.getLogger(ConfigEventListener.class);

	@Override
	public void onEvent(ActivitiEvent activitiEvent) {

		ActivitiEventType eventType = activitiEvent.getType();
		if (ActivitiEventType.PROCESS_STARTED.equals(eventType)){
			log.info("流程启动{}",eventType);
		}else if (ActivitiEventType.PROCESS_COMPLETED.equals(eventType)){
			log.info("流程结束{}",eventType);
		}
	}

	@Override
	public boolean isFailOnException() {
		return false;
	}
}
