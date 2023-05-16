package dc.icdc.workflow.service;

import dc.icdc.lib.common.model.dto.CommonResponse;
import dc.icdc.workflow.exception.MsWorkflowException;
import dc.icdc.workflow.helper.WorkflowConstant;
import dc.icdc.workflow.model.dto.*;
import dc.icdc.workflow.model.entity.TaskHistory;
import dc.icdc.workflow.model.entity.TaskItem;
import dc.icdc.workflow.repository.TaskHistoryRepository;
import dc.icdc.workflow.repository.TaskItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class TaskActionService {

    private final ModelMapper map = new ModelMapper();
    private final WorkflowTaskService workflowTaskService;
    private final TaskItemRepository taskItemRepository;
    private final TaskHistoryRepository taskHistoryRepository;
    private final WorkflowDataCacheService workflowDataCacheService;
    private final WorkflowDataService workflowDataService;

    @Autowired
    /* Constructor to initialise all variables */
    public TaskActionService(WorkflowTaskService workflowTaskService, TaskItemRepository taskItemRepository, TaskHistoryRepository taskHistoryRepository, WorkflowDataCacheService workflowDataCacheService, WorkflowDataService workflowDataService) {
        this.workflowTaskService = workflowTaskService;
        this.taskItemRepository = taskItemRepository;
        this.taskHistoryRepository = taskHistoryRepository;
        this.workflowDataCacheService = workflowDataCacheService;
        this.workflowDataService = workflowDataService;
    }

    /* Method to execute action by routing task. Eg: SUBMIT, CANCEL, APPROVE, REJECT, RETURN TO TASK CREATION. */
    public Map<String, Object> routeTask(TaskRouteDTO requestData) throws MsWorkflowException { //NOSONAR

        log.info("START: action routing request.");

        // response object
        Map<String, Object> response = new HashMap<>();
        boolean routeFlag = true;

        try {

            // get task item by task id
            Optional<TaskItem> workflowTask = taskItemRepository.findById(requestData.getId());

            if (workflowTask.isPresent()) {

                TaskItem taskItem = workflowTask.get();

                log.debug("Task Reference No: " + taskItem.getReferenceNo());
                log.debug("Current Task Stage: " + taskItem.getCurrentStageCode());

                // create new history table
                TaskHistory taskHistory = new TaskHistory();
                log.info("Create new task history table.");
                log.debug("Task History ID: " + taskHistory.getTaskHistoryId());

                // set task history table
                taskHistory.setStageFrom(taskItem.getCurrentStageCode());
                taskHistory.setHolderFromUserId(taskItem.getCurrentHolderUserId());

                // if from task creation
                if (taskItem.getCurrentStageCode().equals(WorkflowConstant.WorkflowStageCd.TASK_CREATION)) {
                    log.info("Task is from TASK CREATION stage. ");

                    // check if from RETURN FROM APPROVAL
                    if (!taskItem.getUpdatedBy().isEmpty() || taskItem.getDateUpdated() != null) {

                        // task name must be present
                        if (!requestData.getName().isEmpty()) {

                            // set submit for approval
                            taskItem.setName(requestData.getName());
                            taskItem.setDescription(requestData.getDescription());
                            taskItem.setCurrentHolderUserId(null);
                            taskHistory.setDateStart(taskItem.getDateUpdated());

                        } else {
                            throw new MsWorkflowException(
                                    Integer.toString(HttpStatus.BAD_REQUEST.value()),
                                    "Task Name is null.",
                                    null,
                                    HttpStatus.BAD_REQUEST);
                        }
                    }
                    // if from Manual Task Creation
                    else{
                        taskHistory.setDateStart(taskItem.getDateCreated());
                    }
                }

                // not from task creation
                else{
                    taskHistory.setDateStart(taskItem.getDateUpdated());
                }

                // update remarks for task item
                if (requestData.getRemarkMessage() != null && !requestData.getRemarkMessage().isEmpty())
                    taskItem.setRemarkMessage(requestData.getRemarkMessage());

                // get task item version for stage name
                int version = workflowDataService.getTaskVersionByProcessInstanceId(taskItem.getCamundaProcessInstanceId());
                List<WorkflowDTO.WorkflowStage> stageList = workflowDataCacheService.getFullStageListPerVersion(version);

                // call workflow end to route action task

                // if Task is from Task Creation
                if (taskItem.getCurrentStageCode().equals(WorkflowConstant.WorkflowStageCd.TASK_CREATION)) {
                    log.info("Send task from TASK CREATION to route in workflow.");
                    workflowTaskService.routeTask(taskItem, requestData.getAction(), requestData.getUserId(), null);
                    taskHistory.setHolderToUserId(WorkflowConstant.TaskApiKey.NOT_ASSIGNED_USER);
                } else {
                    log.info("Send task to route in workflow.");
                    workflowTaskService.routeTask(taskItem, requestData.getAction(), requestData.getUserId(), requestData.getAssignedUserId());
                    taskHistory.setHolderToUserId(taskItem.getCurrentHolderUserId());
                }

                // save task item into repo
                taskItemRepository.save(taskItem);

                // update and set task history table
                // Note : when SUBMIT, action will not be updated (only CANCEL will)
                taskHistory.setActionCode(requestData.getAction());
                taskHistory.setStageTo(taskItem.getCurrentStageCode());
                taskHistory.setDateEnd(taskItem.getDateUpdated());
                taskHistory.setTaskId(taskItem.getId());

                // save task history into repo
                taskHistoryRepository.save(taskHistory);

                // set response API
                response.put(WorkflowConstant.TaskApiKey.TASK_REFERENCE_NO, taskItem.getReferenceNo());

                // get updated stage name
                String stageCode = taskItem.getCurrentStageCode();
                Optional<WorkflowDTO.WorkflowStage> stage = stageList.stream()
                        .filter(data -> Objects.equals(data.getStageCode(), stageCode))
                        .findFirst();

                if (stage.isPresent()) {
                    String stageName = stage.get().getStageName();

                    // check if stage is Process Exception
                    if(stageCode.equals(WorkflowConstant.WorkflowStageCd.PROCESS_EXCEPTION)){
                        routeFlag = false;
                    }

                    // set updated stage name
                    response.put(WorkflowConstant.TaskApiKey.TASK_STAGE_NAME, stageName);
                    log.debug("Updated Task Stage: " + stageName);
                }

                // set flag to prompt front-end for process exception error message
                response.put(WorkflowConstant.TaskApiKey.ROUTE_FLAG, routeFlag);

            } else {
                // Task ID cannot be found. add into serialized list.
                throw new MsWorkflowException(
                        Integer.toString(HttpStatus.BAD_REQUEST.value()),
                        "Task ID is invalid.",
                        null,
                        HttpStatus.BAD_REQUEST);
            }

        } catch (MsWorkflowException e) {
            log.error("Task cannot be routed. Reason: " + e.getMessage());
            throw e;

        } catch (Exception exception) {
            log.error("Task cannot be routed. Reason: " + exception.getMessage());
            throw new MsWorkflowException(Integer.toString(HttpStatus.BAD_REQUEST.value()), "Task cannot be routed.", null, HttpStatus.BAD_REQUEST);
        }

        log.info("END: action routing request.");
        return response;
    }

    public Map<String, Object> startNewTask(TaskRouteDTO requestData) {

        log.info("START: Create new task.");

        // response object
        Map<String, Object> response = new HashMap<>();

        try {

            // create task and map new task details
            TaskItem workflowTask = map.map(requestData, TaskItem.class);
            log.debug("New Task ID: " + workflowTask.getId());
            log.debug("New Task Reference No: " + workflowTask.getReferenceNo());

            // call workflow end to start new task
            log.info("Send task from TASK CREATION to route in workflow.");
            workflowTaskService.startNewTask(workflowTask, requestData.getUserId());

            // save task item into repo
            taskItemRepository.save(workflowTask);

            // refresh and update task item
            taskItemRepository.refresh(workflowTask);

            // create new task history table
            TaskHistory taskHistory = new TaskHistory();
            log.debug("New Task History ID: " + taskHistory.getTaskHistoryId());

            // set and update task history table
            taskHistory.setActionCode(null);
            taskHistory.setStageFrom(null);
            taskHistory.setStageTo(workflowTask.getCurrentStageCode());
            taskHistory.setHolderFromUserId(null);
            taskHistory.setHolderToUserId(workflowTask.getCurrentHolderUserId());
            taskHistory.setDateStart(workflowTask.getDateCreated());
            taskHistory.setDateEnd(workflowTask.getDateCreated());
            taskHistory.setTaskId(workflowTask.getId());

            // save and store task history table
            taskHistoryRepository.save(taskHistory);

            if (Objects.equals(requestData.getTaskCreateType(), WorkflowConstant.TaskApiKey.SYSTEM_CREATE)) {

                log.info("Send SYSTEM task to route to pool.");

                // create new history (for SUBMIT FOR APPROVAL stage)
                TaskHistory updateTaskHistory = new TaskHistory();
                log.debug("New Task History ID Update: " + taskHistory.getTaskHistoryId());

                // set task history table
                updateTaskHistory.setStageFrom(workflowTask.getCurrentStageCode());
                updateTaskHistory.setHolderFromUserId(workflowTask.getCurrentHolderUserId());
                updateTaskHistory.setDateStart(workflowTask.getDateCreated());

                // call workflow end for routing submit action to pool (PENDING APPROVAL)
                log.info("Send task to route in workflow end.");
                workflowTaskService.routeTask(workflowTask, WorkflowConstant.WorkflowAction.SUBMIT_FOR_APPROVAL, requestData.getUserId(), null);

                // save task item into repo
                taskItemRepository.save(workflowTask);

                // refresh and update task item
                taskItemRepository.refresh(workflowTask);

                // set and update task history table
                // create from system will automatically be SUBMIT
                updateTaskHistory.setActionCode(WorkflowConstant.WorkflowAction.SUBMIT_FOR_APPROVAL);
                updateTaskHistory.setStageTo(workflowTask.getCurrentStageCode());
                updateTaskHistory.setHolderToUserId(WorkflowConstant.TaskApiKey.NOT_ASSIGNED_USER);
                updateTaskHistory.setDateEnd(workflowTask.getDateUpdated());
                updateTaskHistory.setTaskId(workflowTask.getId());

                // save and store task history table
                taskHistoryRepository.save(updateTaskHistory);
            }

            // response object with reference number
            response.put(WorkflowConstant.TaskApiKey.TASK_REFERENCE_NO, workflowTask.getReferenceNo());

        } catch (MsWorkflowException e) {

            log.error("Task cannot be created. Reason: " + e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("Task cannot be created. Reason: " + e.getMessage());
            throw new MsWorkflowException(Integer.toString(HttpStatus.BAD_REQUEST.value()), e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
        log.info("END: Create new task.");
        return response;
    }

    /* Method to claim one or more tasks from the task pool. */
    public TaskClaimResponseDTO claimTask(TaskClaimRequestDTO requestData) throws MsWorkflowException {

        log.info("Receive claim request.");
        log.debug("Claiming tasks : " + requestData.getListToClaim());
        log.debug("Claiming user : " + requestData.getUserId());
        TaskClaimResponseDTO taskClaimResponse = new TaskClaimResponseDTO();

        if (requestData.getUserId().equals("")) {
            log.info("User ID is invalid");
            throw new MsWorkflowException(Integer.toString(HttpStatus.BAD_REQUEST.value()), "User ID is invalid.", null, HttpStatus.BAD_REQUEST);
        }

        List<ClaimResponseDTO> successClaims = new ArrayList<>();
        List<ClaimResponseDTO> failClaims = new ArrayList<>();

        // map through the claiming id to claim the task by id
        requestData.getListToClaim().stream().forEach(taskId -> {

            TaskItem temporaryTask = null;
            Optional<TaskItem> item = taskItemRepository.findById(UUID.fromString(taskId));
            try {
                // check existence of task
                if (item.isPresent()) {
                    temporaryTask = item.get();

                    // create new history table
                    TaskHistory taskHistory = new TaskHistory();

                    // set task history table
                    taskHistory.setStageFrom(temporaryTask.getCurrentStageCode());
                    taskHistory.setHolderFromUserId(WorkflowConstant.TaskApiKey.NOT_ASSIGNED_USER);
                    taskHistory.setDateStart(temporaryTask.getDateUpdated());

                    // claim the task
                    workflowTaskService.claimTask(temporaryTask, requestData.getUserId());

                    // set updated in task history
                    temporaryTask.setDateUpdated(LocalDateTime.now());
                    temporaryTask.setUpdatedBy(requestData.getUserId());
                    taskItemRepository.save(temporaryTask);

                    // update and set task history table
                    // Note : when SUBMIT, action will not be updated (only CANCEL will)
                    taskHistory.setActionCode(WorkflowConstant.WorkflowAction.CLAIM);
                    taskHistory.setStageTo(temporaryTask.getCurrentStageCode());
                    taskHistory.setHolderToUserId(temporaryTask.getCurrentHolderUserId());
                    taskHistory.setDateEnd(temporaryTask.getDateUpdated());
                    taskHistory.setTaskId(temporaryTask.getId());

                    // save task history into repo
                    taskHistoryRepository.save(taskHistory);

                    successClaims.add(ClaimResponseDTO.builder().referenceNo(temporaryTask.getReferenceNo()).build());

                } else {
                    // fail claim goes into fail list
                    failClaims.add(ClaimResponseDTO.builder()
                            .referenceNo(temporaryTask.getReferenceNo())
                            .responseMessage("Task item not found.")
                            .build());
                }
            } catch (Exception exception) {
                log.error("Task " + temporaryTask.getReferenceNo() + " failed to be claimed " +
                        ", Reason : " + exception.getMessage());
                failClaims.add(ClaimResponseDTO.builder()
                        .referenceNo(temporaryTask.getReferenceNo())
                        .responseMessage(exception.getMessage())
                        .build());
            }
        });

        // set success and fail list
        taskClaimResponse.setSuccess(successClaims);
        taskClaimResponse.setFail(failClaims);

        return taskClaimResponse;
    }

    /* Method to save a draft */
    public CommonResponse<Void> saveTask(TaskRouteDTO requestData) throws MsWorkflowException {

        TaskItem taskItem;

        try{
            // get task item (check if not exist, create new task)
            Optional<TaskItem> workflowTask = taskItemRepository.findById(requestData.getId());

            if(workflowTask.isPresent()){
                taskItem = workflowTask.get();

                // update general data
                taskItem.setUpdatedBy(requestData.getUserId());
                taskItem.setDateUpdated(LocalDateTime.now());
                taskItem.setCurrentHolderUserId(requestData.getUserId());

                // update default data
                if(taskItem.getCurrentStageCode().equals(WorkflowConstant.WorkflowStageCd.TASK_CREATION)){
                    taskItem.setName(requestData.getName());
                    taskItem.setDescription(requestData.getDescription());
                }
                else if (taskItem.getCurrentStageCode().equals(WorkflowConstant.WorkflowStageCd.PENDING_APPROVAL)){
                    taskItem.setRemarkMessage(requestData.getRemarkMessage());
                }

                taskItemRepository.save(taskItem);
            }
        }
        catch (Exception exception) {
            log.error("Task cannot be saved as draft. Reason: " + exception.getMessage());
            throw new MsWorkflowException(Integer.toString(HttpStatus.BAD_REQUEST.value()), exception.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return CommonResponse.success();
    }
}