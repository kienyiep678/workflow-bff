package dc.icdc.workflow.helper;

import dc.icdc.workflow.service.WorkflowDataCacheService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.spring.boot.starter.event.ProcessApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ServiceInitializer implements ApplicationListener<ProcessApplicationStartedEvent> {

    private final WorkflowDataCacheService workflowDataCacheService;

    public ServiceInitializer(WorkflowDataCacheService workflowDataCacheService) {
        this.workflowDataCacheService = workflowDataCacheService;
    }

    @Override
    public void onApplicationEvent(ProcessApplicationStartedEvent event) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine(); //NOSONAR
        workflowDataCacheService.initialize();
    }
}
