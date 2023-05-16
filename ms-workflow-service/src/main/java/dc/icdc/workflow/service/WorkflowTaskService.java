package dc.icdc.workflow.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dc.icdc.workflow.exception.MsWorkflowException;
import dc.icdc.workflow.helper.WorkflowConstant;
import dc.icdc.workflow.model.dto.WorkflowDTO;
import dc.icdc.workflow.model.entity.TaskItem;
import dc.icdc.workflow.model.entity.TaskTypeConfig;
import dc.icdc.workflow.repository.TaskTypeConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.history.event.HistoricActivityInstanceEventEntity;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WorkflowTaskService {

    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final WorkflowDataService workflowDataService;
    private final WorkflowDataCacheService workflowDataCacheService;
    private final TaskTypeConfigRepository taskTypeConfigRepository;

    @Autowired
    public WorkflowTaskService(RuntimeService runtimeService, TaskService taskService, WorkflowDataService workflowDataService, WorkflowDataCacheService workflowDataCacheService, TaskTypeConfigRepository taskTypeConfigRepository) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.workflowDataService = workflowDataService;
        this.workflowDataCacheService = workflowDataCacheService;
        this.taskTypeConfigRepository = taskTypeConfigRepository;
    }



    public void startNewTask(TaskItem workflowTask, String assignToUserId) throws Exception {

        try {

            log.info("Start, Ref No = {}", workflowTask.getReferenceNo());//NOSONAR
            log.debug("workflowTask: {}", workflowTask);//NOSONAR
            log.debug("assignToUserId: {}", assignToUserId);//NOSONAR

            //Map all the workflow data into list of camunda variables
            Map<String, Object> variables = mapDataToVariables(workflowTask, "", true);

            //Start Process
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(WorkflowConstant.General.MAKER_CHECKER_WORKFLOW_KEY, variables);

            //Check if task generated in camunda engine
            Task userTask = taskService
                    .createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .singleResult();

            postRouteHandling(workflowTask, userTask, assignToUserId, assignToUserId, true);

            log.info("End: Success");


        } catch (Exception e){

            log.error("error: " + e.getMessage());//NOSONAR
            throw e;
        }
    }

    public void routeTask(TaskItem workflowTask, String actionVal, String loggedInUserId, String assignToUserId) throws Exception {

        try {
            log.info("Start, Ref No = {}", workflowTask.getReferenceNo());
            log.debug("workflowTask: {}", workflowTask);
            log.debug("actionVal: {}", actionVal);
            log.debug("loggedInUserId: {}", loggedInUserId);
            log.debug("assignToUserId: {}", assignToUserId);

            //Store the previous stage code for later use
            String prevStageCd = workflowTask.getCurrentStageCode();

            //Map all the workflow data into list of camunda variables
            Map<String, Object> variables = mapDataToVariables(workflowTask, actionVal, false);

            //Validate whether the action value is exist in the workflow process according to its version
            if(validateActionValue(workflowTask, actionVal)) {

                //Find the task in camunda engine
                Task userTask = taskService
                        .createTaskQuery()
                        .processInstanceId(workflowTask.getCamundaProcessInstanceId())
                        .singleResult();

                if (userTask != null) {

                    //Check if the assignee is the logged-in user
                    if (userTask.getAssignee() == null || userTask.getAssignee().isEmpty() || userTask.getAssignee().equals(loggedInUserId)) {

                        //Route the task with the variables
                        taskService.complete(userTask.getId(), variables);

                        //Refresh after routing
                        userTask = taskService
                                .createTaskQuery()
                                .processInstanceId(workflowTask.getCamundaProcessInstanceId())
                                .singleResult();


                        postRouteHandling(workflowTask, userTask, loggedInUserId, assignToUserId, false);

                        postRouteIntercept(workflowTask, userTask, prevStageCd, actionVal, loggedInUserId);

                    } else {

                        throw new MsWorkflowException(Integer.toString(HttpStatus.BAD_REQUEST.value()), "The logged in user id does not match with the assignee of the task.", HttpStatus.BAD_REQUEST);

                    }
                } else {

                    throw new MsWorkflowException(Integer.toString(HttpStatus.BAD_REQUEST.value()), "The execution Id is not found camunda engine.", HttpStatus.BAD_REQUEST);

                }
            } else {

                throw new MsWorkflowException(Integer.toString(HttpStatus.BAD_REQUEST.value()), "The action value is not found in camunda.", HttpStatus.BAD_REQUEST);

            }

            log.debug("workflowTask: {}", workflowTask);
            log.info("End: Success, Ref No = {}", workflowTask.getReferenceNo());

        } catch (Exception e){

            log.error("error: " + e.getMessage());
            throw e;
        }
    }

    public void claimTask(TaskItem workflowTask, String loggedInUser) throws Exception {

        try {

            log.info("Start, Ref No = {}", workflowTask.getReferenceNo());

            //Find the task in camunda engine
            Task userTask = taskService
                    .createTaskQuery()
                    .processInstanceId(workflowTask.getCamundaProcessInstanceId())
                    .singleResult();

            if(userTask!= null) {

                if(userTask.getAssignee() == null || userTask.getAssignee().isEmpty()) {

                    assignTaskToUser(userTask.getId(), workflowTask, loggedInUser);

                } else {

                    throw new MsWorkflowException(Integer.toString(HttpStatus.BAD_REQUEST.value()), "The task(s) has been claimed by others.", HttpStatus.BAD_REQUEST);

                }

            } else {

                throw new MsWorkflowException(Integer.toString(HttpStatus.BAD_REQUEST.value()), "The task is not found in the camunda engine", HttpStatus.BAD_REQUEST);

            }

            log.debug("workflowTask: {}", workflowTask);
            log.info("End: Success, Ref No = {}", workflowTask.getReferenceNo());

        } catch (Exception e) {

            log.error("error: " + e.getMessage());
            throw e;
        }

    }

    public Map<String, Object> mapDataToVariables(TaskItem workflowTask, String actionVal, boolean isInitial) throws Exception {

        log.debug("Start");

        Map<String, Object> variables = null;
        try {
            variables = new HashMap<>();
            variables.put(WorkflowConstant.WorkflowVariableKey.TASK_NAME, workflowTask.getName());
            variables.put(WorkflowConstant.WorkflowVariableKey.TASK_DESCRIPTION, workflowTask.getDescription());
            variables.put(WorkflowConstant.WorkflowVariableKey.TASK_TYPE, workflowTask.getTaskTypeCode());
            variables.put(WorkflowConstant.WorkflowVariableKey.TASK_JSON, new ObjectMapper().writeValueAsString(workflowTask.getTaskUrlJsonObject()));
            variables.put(WorkflowConstant.WorkflowVariableKey.ACTION, actionVal);

            //Initialise the task type config into camunda
            if(isInitial){

                TaskTypeConfig taskTypeConfigResult = taskTypeConfigRepository.findByCode(workflowTask.getTaskTypeCode());

                if(taskTypeConfigResult == null){

                    throw new MsWorkflowException(Integer.toString(HttpStatus.BAD_REQUEST.value()), "Task Type is not found in database.", HttpStatus.BAD_REQUEST);

                }

                variables.put(WorkflowConstant.WorkflowVariableKey.APPROVAL_MATRIX_RESULT, false);
                variables.put(WorkflowConstant.WorkflowVariableKey.NUMBER_OF_APPROVAL, taskTypeConfigResult.getNumberOfApproval());
                variables.put(WorkflowConstant.WorkflowVariableKey.CURRENT_APPROVAL, 0);
                variables.put(WorkflowConstant.WorkflowVariableKey.TASK_DECISION, "");

            }

            //If task got returned, rejected and cancelled set the number of current approval back to 0
            if(workflowTask.getCurrentStageCode() != null && !workflowTask.getCurrentStageCode().trim().isEmpty() &&
                    ((workflowTask.getCurrentStageCode().equals(WorkflowConstant.WorkflowStageCd.TASK_CREATION) && actionVal.equals(WorkflowConstant.WorkflowAction.CANCEL))
                    || (workflowTask.getCurrentStageCode().equals(WorkflowConstant.WorkflowStageCd.PENDING_APPROVAL) && (actionVal.equals(WorkflowConstant.WorkflowAction.REJECT) || actionVal.equals(WorkflowConstant.WorkflowAction.RETURN_FROM_APPROVAL)))
                    || (workflowTask.getCurrentStageCode().equals(WorkflowConstant.WorkflowStageCd.PROCESS_EXCEPTION) && (actionVal.equals(WorkflowConstant.WorkflowAction.CANCEL) || actionVal.equals(WorkflowConstant.WorkflowAction.RETURN_FROM_EXCEPTION))))){
                variables.put(WorkflowConstant.WorkflowVariableKey.CURRENT_APPROVAL, 0);
                variables.put(WorkflowConstant.WorkflowVariableKey.TASK_DECISION, "");
            }

            log.debug("variables: {}", variables);
            log.debug("End");

        } catch (Exception e) {

            log.error("error: " + e.getMessage());
            throw e;
        }

        return variables;
    }

    public void assignTaskToUser(String taskId, TaskItem workflowTask, String userId) throws Exception {

        log.debug("userId: {}", userId);

        try {
            // Set the assignee of the task using the user ID
            taskService.setAssignee(taskId, userId);
            workflowTask.setCurrentHolderUserId(userId);

        } catch (Exception e) {

            log.error("error: " + e.getMessage());
            throw e;
        }
    }

    public boolean validateActionValue(TaskItem workflowTask, String actionVal) throws Exception {

        log.debug("actionVal: {}", actionVal);
        log.debug("workflowTask.getCurrentStageCode(): {}", workflowTask.getCurrentStageCode());

        boolean result = false;

        try {

            int version = workflowDataService.getTaskVersionByProcessInstanceId(workflowTask.getCamundaProcessInstanceId());

            WorkflowDTO.WorkflowStage workflowStage = workflowDataCacheService.getActionListPerStage(workflowTask.getCurrentStageCode(), version);

            if(workflowStage != null) {
                if(workflowStage.getActionList() != null && !workflowStage.getActionList().isEmpty()){

                    List<String> listOfActions = workflowStage.getActionList().stream().map(x -> x.getActionValue()).toList();
                    result = listOfActions.contains(actionVal);

                } else if(actionVal.isEmpty()){

                    result = true;
                }

            } else {

                throw new MsWorkflowException(Integer.toString(HttpStatus.BAD_REQUEST.value()), "The stage is not found in camunda engine.", HttpStatus.BAD_REQUEST);

            }

        } catch (Exception e) {

            log.error("error: " + e.getMessage());
            throw e;

        }

        return result;
    }

    public void postRouteHandling(TaskItem workflowTask, Task userTask, String loggedInUserId, String assignToUserId, boolean isInitial) throws Exception {

        if (userTask != null) {

            log.info("Task found in TaskService after route");

            //Set the latest stage into the workflow dto
            workflowTask.setCurrentStageCode(userTask.getTaskDefinitionKey());

            log.info("Task Routed to: {}", workflowTask.getCurrentStageCode());

            if(isInitial){

                //Set the process instance id
                workflowTask.setCamundaProcessInstanceId(userTask.getProcessInstanceId());

                //Set the created user and date into the workflow dto
                workflowTask.setCreatedBy(loggedInUserId);
                workflowTask.setDateCreated(userTask.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

            } else {

                //Set the latest modified user and date into the workflow dto
                workflowTask.setUpdatedBy(loggedInUserId);
                workflowTask.setDateUpdated(userTask.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            }

            //Assign to a user if specified, if the user is null it will go to pool
            assignTaskToUser(userTask.getId(), workflowTask, assignToUserId);

        } else if (!isInitial){

            //To check whether the task already reached end stage, the task will not appear in taskService if already ended or in service task
            HistoricActivityInstanceEventEntity activityInstance = workflowDataService.getLatestStageActivityInstance(workflowTask.getCamundaProcessInstanceId());

            if(activityInstance != null){

                log.info("Task not found in TaskService after route");

                if(activityInstance.getActivityType().equals("noneEndEvent")){
                    workflowTask.setEndStage(true);
                }

                //Set the latest stage into the workflow dto
                workflowTask.setCurrentStageCode(activityInstance.getActivityId());

                log.info("Task Routed to: {}", workflowTask.getCurrentStageCode());

                //Set the latest modified user and date into the workflow dto
                workflowTask.setUpdatedBy(loggedInUserId);
                workflowTask.setDateUpdated(activityInstance.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

            } else {

                throw new MsWorkflowException(Integer.toString(HttpStatus.BAD_REQUEST.value()), "Task has been routed, but there are no task found based on the given process instance id in the camunda engine.", HttpStatus.BAD_REQUEST);

            }
        } else {

            throw new MsWorkflowException(Integer.toString(HttpStatus.BAD_REQUEST.value()), "Task has been created, but there are no task found based on the given process instance id in the camunda engine.", HttpStatus.BAD_REQUEST);

        }
    }

    public void postRouteIntercept(TaskItem workflowTask, Task userTask, String prevStageCd, String actionVal, String loggedInUserId) throws Exception {

        String currStageCd = workflowTask.getCurrentStageCode();

        if((prevStageCd != null && !prevStageCd.trim().isEmpty()) && (currStageCd != null && !currStageCd.trim().isEmpty()) ) {

            //Check if the task got cancelled and assign the required value
            if (prevStageCd.equals(WorkflowConstant.WorkflowStageCd.TASK_CREATION) && actionVal.equals(WorkflowConstant.WorkflowAction.CANCEL)) {

                //Assign the task decision values
                workflowTask.setActionBy(loggedInUserId);
                workflowTask.setActionType(WorkflowConstant.TaskActionType.CANCEL_TASK.getValue());
                workflowTask.setDateAction(workflowTask.getDateUpdated());

            } else if (prevStageCd.equals(WorkflowConstant.WorkflowStageCd.PENDING_APPROVAL)) {

                //Check if the task got approved and assign the required value
                if (actionVal.equals(WorkflowConstant.WorkflowAction.APPROVE)) {

                    //Assign the task decision values
                    workflowTask.setActionBy(loggedInUserId);
                    workflowTask.setActionType(WorkflowConstant.TaskActionType.APPROVE_TASK.getValue());
                    workflowTask.setDateAction(workflowTask.getDateUpdated());

                    //Check if the task got rejected and assign the required value
                } else if (actionVal.equals(WorkflowConstant.WorkflowAction.REJECT)) {

                    //Assign the task decision values
                    workflowTask.setActionBy(loggedInUserId);
                    workflowTask.setActionType(WorkflowConstant.TaskActionType.REJECT_TASK.getValue());
                    workflowTask.setDateAction(workflowTask.getDateUpdated());

                }
            }

            //Check if task returned to task creation
            if(currStageCd.equals(WorkflowConstant.WorkflowStageCd.TASK_CREATION)){

                //If returned to task creation, overwrite assigned user to the user that created the task
                assignTaskToUser(userTask.getId(), workflowTask, workflowTask.getCreatedBy());

                //Reset all the task decision values
                workflowTask.setActionBy("");
                workflowTask.setActionType(null);
                workflowTask.setDateAction(null);

            }

        }  else {

            throw new MsWorkflowException(Integer.toString(HttpStatus.BAD_REQUEST.value()), "The previous or current stage code from the task is empty.", HttpStatus.BAD_REQUEST);

        }
    }

}
