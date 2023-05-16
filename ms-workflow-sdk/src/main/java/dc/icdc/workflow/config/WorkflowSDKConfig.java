package dc.icdc.workflow.config;

import dc.icdc.lib.common.config.EnableCommonLib;
import dc.icdc.lib.hi.config.EnableHostIntegrationLib;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCommonLib
@EnableHostIntegrationLib
@ComponentScan({"dc.icdc.workflow"})
public class WorkflowSDKConfig {
}
