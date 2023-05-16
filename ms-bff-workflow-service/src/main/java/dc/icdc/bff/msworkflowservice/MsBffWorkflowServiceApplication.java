package dc.icdc.bff.msworkflowservice;

import dc.icdc.lib.common.config.EnableCommonLib;
import dc.icdc.lib.hi.config.EnableHostIntegrationLib;
import dc.icdc.workflow.config.EnableWorkflowSDK;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableWorkflowSDK
@EnableCommonLib
@EnableHostIntegrationLib
@SpringBootApplication
public class MsBffWorkflowServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsBffWorkflowServiceApplication.class, args);
	}

}
