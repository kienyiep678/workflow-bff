package dc.icdc.workflow.service;

import dc.icdc.workflow.exception.MsWorkflowException;
import dc.icdc.workflow.helper.WorkflowConstant;
import dc.icdc.workflow.model.dto.TaskHistoryDTO;
import dc.icdc.workflow.model.dto.WorkflowDTO;
import dc.icdc.workflow.model.entity.TaskHistory;
import dc.icdc.workflow.model.entity.TaskItem;
import dc.icdc.workflow.repository.TaskHistoryRepository;
import dc.icdc.workflow.repository.TaskItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
@Slf4j
@Service
public class TaskHistoryService {

    private final TaskHistoryRepository taskHistoryRepository;
    private final WorkflowDataCacheService workflowDataCacheService;
    private final WorkflowDataService workflowDataService;
    private final TaskItemRepository taskItemRepository;
    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    /* Constructor to initialise all variables */
    public TaskHistoryService(TaskItemRepository taskItemRepository, TaskHistoryRepository taskHistoryRepository, WorkflowDataCacheService workflowDataCacheService, WorkflowDataService workflowDataService) {
        this.taskHistoryRepository = taskHistoryRepository;
        this.workflowDataCacheService = workflowDataCacheService;
        this.workflowDataService = workflowDataService;
        this.taskItemRepository = taskItemRepository;
    }


    /* Method to get task history by task id. */
    public List<TaskHistoryDTO> getTaskHistoryByTaskId(String taskId){

        log.info("Receive request of task history. ");
        log.debug("Task ID: " + taskId);

        // Read task history list using task id
        List<TaskHistory> listOfHistory = taskHistoryRepository.findByTaskId(UUID.fromString(taskId));

        // throw exception if no history found
        if(listOfHistory.isEmpty())
            throw new MsWorkflowException(Integer.toString(HttpStatus.PARTIAL_CONTENT.value()) , "No history records found.",null,HttpStatus.PARTIAL_CONTENT);

        // map through list and assign names for code columns
        return listOfHistory.stream()
                .map(history -> {
                    // to store the new object
                    TaskHistoryDTO taskHistory = new TaskHistoryDTO();
                    taskHistory = mapper.map(history, TaskHistoryDTO.class);

                    // for store name value of stages
                    String stageName = "";

                    TaskItem task = taskItemRepository.findById(UUID.fromString(taskId)).get();
                    // get "stage from" name
                    try{
                         stageName = workflowDataCacheService.getStageNameByCode(history.getStageFrom());
                    }catch (Exception e){
                        log.error("Error while reading stage name with code : " + history.getStageFrom());
                        stageName = null;
                    }
                    taskHistory.setStageFrom(stageName);
                    // get "stage to" name
                    try{
                        stageName = workflowDataCacheService.getStageNameByCode(history.getStageTo());
                    } catch (Exception e){
                        log.error("Error while reading stage name with code : " + history.getStageTo());
                        stageName = null;
                    }
                    taskHistory.setStageTo(stageName);

                    // check action column if null
                    //      - can be null when just created
                    if(history.getActionCode() == null)
                        taskHistory.setActionName(null);
                    else
                    {
                        // get name value of action
                        //  return code when not found
                        try{
                            if(taskHistory.getActionCode().equalsIgnoreCase("CLAIM"))
                            {
                                taskHistory.setActionName(WorkflowConstant.TaskApiKey.CLAIM_ACTION_NAME);
                            }
                            else {
                                WorkflowDTO.WorkflowStage workflowDataDTO = workflowDataCacheService.getActionListPerStage(history.getStageFrom()
                                        , workflowDataService.getTaskVersionByProcessInstanceId(task.getCamundaProcessInstanceId()));
                                String actionName = workflowDataDTO
                                        .getActionList()
                                        .stream()
                                        .filter(action -> action.getActionValue().equals(history.getActionCode()))
                                        .findFirst()
                                        .get()
                                        .getActionName();
                                taskHistory.setActionName(actionName);
                            }

                        }
                        catch (Exception e)
                        {
                            log.error("Error while reading action name with code : " + history.getActionCode());
                            taskHistory.setActionName(history.getActionCode());
                        }
                    }

                    return taskHistory;
                })
                // collect returned items into list
                .toList();
    }
}