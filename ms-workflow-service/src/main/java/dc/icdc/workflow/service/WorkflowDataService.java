package dc.icdc.workflow.service;

import dc.icdc.workflow.helper.WorkflowComponent;
import dc.icdc.workflow.helper.WorkflowConstant;
import dc.icdc.workflow.model.dto.WorkflowDTO;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.impl.history.event.HistoricActivityInstanceEventEntity;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class WorkflowDataService {

    private final WorkflowComponent workflowComponent;

    private final HistoryService historyService;

    private final RepositoryService repositoryService;


    @Autowired
    public WorkflowDataService(WorkflowComponent workflowComponent, HistoryService historyService, RepositoryService repositoryService) {
        this.workflowComponent = workflowComponent;
        this.historyService = historyService;
        this.repositoryService = repositoryService;
    }

    public List<WorkflowDTO> getWorkflowDataForEveryVersion(){ //NOSONAR

        log.info("Start");

        RepositoryService camundaRepositoryService =
                ProcessEngines.getDefaultProcessEngine().getRepositoryService();
        List<ProcessDefinition> processDefinitionList = camundaRepositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(WorkflowConstant.General.MAKER_CHECKER_WORKFLOW_KEY).list();

        List<WorkflowDTO> workflowAllVersion = new ArrayList<>();

        processDefinitionList = processDefinitionList.stream().sorted(Comparator.comparing(ProcessDefinition::getVersion)).toList();

        for (ProcessDefinition processDefinition:  processDefinitionList) {

            BpmnModelInstance bpmnModelInstance = camundaRepositoryService.getBpmnModelInstance(processDefinition.getId());

            // Get all flow elements in the process
            Collection<FlowElement> flowElements = bpmnModelInstance.getModelElementsByType(FlowElement.class);

            List<WorkflowDTO.WorkflowStage> workflowStageList = new ArrayList<>();

            log.info("Workflow Version: " + processDefinition.getVersion());


            for (FlowElement flowElement : flowElements) {
                // If the flow element is an activity (i.e., a task or subprocess)
                if (flowElement instanceof Activity activity) {

                    WorkflowDTO.WorkflowStage workflowStage = new WorkflowDTO.WorkflowStage();

                    log.debug("Activity Name: " + activity.getName());
                    log.debug("Activity Id: " + activity.getId());

                    workflowStage.setStageName(activity.getName());
                    workflowStage.setStageCode(activity.getId());

                    // Get the incoming and outgoing sequence flows for the activity
                    Collection<SequenceFlow> outgoingFlows = activity.getOutgoing();

                    List<WorkflowDTO.WorkflowStage.WorkflowStageAction> workflowStageActionList = new ArrayList<>();

                    for (SequenceFlow outgoingFlow : outgoingFlows) {
                        FlowNode targetNode = outgoingFlow.getTarget();
                        if (targetNode instanceof Gateway gateway) {
                            log.debug("Connected gateway (outgoing): " + gateway.getId());


                            Gateway gatewayToCheck = (Gateway) bpmnModelInstance.getModelElementById(gateway.getAttributeValue("id"));

                            Collection<SequenceFlow> outgoingSequenceFlows = gatewayToCheck.getOutgoing();

                            for (SequenceFlow sequenceFlow : outgoingSequenceFlows) {
                                String conditionExpression = sequenceFlow.getConditionExpression().getTextContent();
                                if (conditionExpression != null && conditionExpression.contains("'") ) {
                                    String actionCode = conditionExpression.split("'")[1];
                                    String actionName = workflowComponent.getProperty(String.format("workflow.action.%s.name", actionCode));
                                    log.debug("Action Code contain: {}", actionCode);
                                    log.debug("Action Name contain: {}", actionName);

                                    WorkflowDTO.WorkflowStage.WorkflowStageAction workflowStageAction = new WorkflowDTO.WorkflowStage.WorkflowStageAction(actionName, actionCode);
                                    workflowStageActionList.add(workflowStageAction);

                                }
                            }
                        }
                    }
                    if (!workflowStageActionList.isEmpty()) {
                        workflowStage.setActionList(workflowStageActionList);
                    }

                    workflowStageList.add(workflowStage);
                }
                else if (flowElement instanceof EndEvent activity) {

                    log.debug("Activity Name: " + activity.getName());
                    log.debug("Activity Id: " + activity.getId());

                    WorkflowDTO.WorkflowStage workflowStage = new WorkflowDTO.WorkflowStage();

                    workflowStage.setStageName(activity.getName());
                    workflowStage.setStageCode(activity.getId());

                    workflowStageList.add(workflowStage);
                }

            }

            workflowAllVersion.add(new WorkflowDTO(processDefinition.getVersion(), workflowStageList));
        }

        log.info("End: Success");

        return workflowAllVersion;

    }

    public HistoricActivityInstanceEventEntity getLatestStageActivityInstance(String processInstanceId){
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();

        List<HistoricActivityInstanceEventEntity> historicActivityInstanceEventEntityList = historicActivityInstanceList.stream().map(x -> (HistoricActivityInstanceEventEntity)x).toList();

        // sort the list in descending order by sequence counter
        historicActivityInstanceEventEntityList = historicActivityInstanceEventEntityList.stream().sorted(Comparator.comparing(HistoricActivityInstanceEventEntity::getSequenceCounter).reversed()).toList();
        // get the first element of the sorted list, which will be the one with the highest sequence counter
        return historicActivityInstanceEventEntityList.get(0);
    }



    public int getTaskVersionByProcessInstanceId(String processInstanceId) {
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceEndTime()
                .desc()
                .listPage(0, 1)
                .get(0);

        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(historicTaskInstance.getProcessDefinitionId());

        log.debug("version: {}", processDefinition.getVersion());

        return processDefinition.getVersion();
    }

    public Object getTaskVariable(String processInstanceId, String variableName) {
        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .variableName(variableName)
                .singleResult();

        log.debug("variable name: {}, variable value: {}", variableName, historicVariableInstance.getValue());

        return historicVariableInstance.getValue();
    }

    public int getLatestVersion()
    {
        RepositoryService camundaRepositoryService =
                ProcessEngines.getDefaultProcessEngine().getRepositoryService();

        int version = camundaRepositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(WorkflowConstant.General.MAKER_CHECKER_WORKFLOW_KEY).latestVersion()
                .singleResult().getVersion();

        log.debug("Latest Version: {}", version);

        return version;
    }
}
