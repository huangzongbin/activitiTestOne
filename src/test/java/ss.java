import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author :黄宗滨
 * @date 2019/12/21
 * @Description
 */
public class ss {

	private  static Logger log= LoggerFactory.getLogger(ss.class);

	@Test
	public void testConfig2(){
		ProcessEngineConfiguration configuration= ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResourceDefault();
		log.info("创建流程引擎{}",configuration);
	};

}
