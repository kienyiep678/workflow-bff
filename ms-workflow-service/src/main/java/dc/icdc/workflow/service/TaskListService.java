package dc.icdc.workflow.service;

import dc.icdc.workflow.model.dto.*;
import dc.icdc.workflow.exception.MsWorkflowException;
import dc.icdc.workflow.helper.WorkflowConstant;
import dc.icdc.workflow.model.entity.TaskItem;
import dc.icdc.workflow.model.entity.TaskType;
import dc.icdc.workflow.repository.TaskItemRepository;
import dc.icdc.workflow.repository.TaskTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class TaskListService {

    // Create Object of Repository and Services
    private final TaskItemRepository taskItemRepository;
    private final WorkflowDataService workflowDataService;
    private final WorkflowDataCacheService workflowDataCacheService;
    private final TaskTypeRepository taskTypeRepository;
    private final TaskHistoryService taskHistoryService;
    private final TaskTypeConfigService taskTypeConfigService;
    private final ModelMapper mapper = new ModelMapper();

    // constant for error handling
    public static final String EXCEPTION_PERSONAL_LIST_REQUEST_WITHOUT_PERSONAL_USER_ID = "Exception : personal list request without personal user id";
    public static final String PERSONAL_LIST_REQUIRES_VALID_USER_IDENTIFICATION = "Personal list requires valid user identification.";

    @Autowired
    /* Constructor to initialise all variables */
    public TaskListService(TaskItemRepository taskItemRepository, WorkflowDataService workflowDataService, WorkflowDataCacheService workflowDataCacheService, TaskTypeRepository taskTypeRepository, TaskHistoryService taskHistoryService, TaskTypeConfigService taskTypeConfigService) {
        this.taskItemRepository = taskItemRepository;
        this.workflowDataService = workflowDataService;
        this.workflowDataCacheService = workflowDataCacheService;
        this.taskTypeRepository = taskTypeRepository;
        this.taskHistoryService = taskHistoryService;
        this.taskTypeConfigService = taskTypeConfigService;
    }

    /* Method to retrieve the details of single task by id */
    public TaskDetailDTO getContentByTaskId(String id, boolean history) {

        log.info("Client request input for task content");
        log.debug("Get content by id : " + id);
        log.debug("\tHistory : " + history);

        // Read from db
        Optional<TaskItem> details = taskItemRepository.findById(UUID.fromString(id));
        log.info("Reading task from database... ");

        // return error for no matching item for id
        if (details.isEmpty())
        {
            log.info("Bad request with invalid input");
            throw new MsWorkflowException(Integer.toString(HttpStatus.BAD_REQUEST.value()), "The task item is not found.", null, HttpStatus.BAD_REQUEST);
        }

        TaskDetailDTO taskDetail = mapper.map(details.get(), TaskDetailDTO.class);

        if(taskDetail.getCurrentStageCode().equalsIgnoreCase(WorkflowConstant.WorkflowStageCd.PROCESS_EXCEPTION))
        {
            log.info("Status : Process Exception");
            taskDetail.setProcessExceptionMessage(
                    (String) workflowDataService.getTaskVariable(details.get().getCamundaProcessInstanceId() , WorkflowConstant.CamundaKey.PROCESS_EXECUTION_EXCEPTION_ERROR_MESSAGE)
            );
            log.debug("Exception Message" + taskDetail.getProcessExceptionMessage());
        }

        // read the stage code for retrieving stage name & action list from workflow cache
        String taskStageCd = taskDetail.getCurrentStageCode();

        //get task type code
        Optional<TaskType> taskType = taskTypeRepository.findById(taskDetail.getTaskTypeCode());


        if (taskType.isPresent())
        {
            log.debug("Task type : " + taskType.get().getTaskName());
            taskDetail.setTaskTypeName(taskType.get().getTaskName());
        }

        getStageAndActions(taskDetail, details.get(), taskStageCd);

        //Check if history required
        if (history) {
            // read and set history into the json response object
            taskDetail.setHistory(taskHistoryService.getTaskHistoryByTaskId(id));
        }

        log.debug(taskDetail.toString());
        return taskDetail;
    }

    //Method to read stage name and actions from workflow engine
    private void getStageAndActions(TaskDetailDTO taskDetail, TaskItem details, String taskStageCd) {
        WorkflowDTO.WorkflowStage workflowActivity;
        try {
            // Reading stage-name & action list from workflow data cache service
            workflowActivity = workflowDataCacheService.getActionListPerStage(taskStageCd, workflowDataService.getTaskVersionByProcessInstanceId(details.getCamundaProcessInstanceId()));
            taskDetail.setStageName(workflowDataCacheService.getStageNameByCode(details.getCurrentStageCode()));
            taskDetail.setActionList(workflowActivity.getActionList());
            log.info("Retrieving stage name and action list.");
        } catch (Exception e) {
            log.error("Stage information cannot be found from workflow engine. Replying with %NOT FOUND% ");
            // stage name and action list can't be found
            taskDetail.setStageName(WorkflowConstant.TaskApiKey.NOT_FOUND);
        }
    }

    /* Method to retrieve a list of tasks. */
    public TaskListDTO getListOfTasks(String order, String sortBy, int pageNum, int pageSize, String type, String name, String status, String refNo, String userId, String taskType) //NOSONAR
    {
        log.info("Request of list \" " + type + "\":");
        log.debug("Order : " + order
                + ", Sorted By: " + sortBy
                + ", Page Number :" + pageNum
                + ", Page Size: " + pageSize
                + ", Name: " + name
                + ", Status: " + status
                + ", Task Type: " + taskType
                + ", Reference No: " + refNo
        );


        // Check for sorting requirement input
        //      default : ascending
        Sort sort = getSortByAbbreviation(order, sortBy);

        // Initialize the list variable to store
        //  - task items
        //  - count of pages
        List<TaskItem> tasks = new ArrayList<>();
        int countOfPages;

        // Paging object to assist in JPA pagination and sort
        Pageable paging = PageRequest.of(pageNum, pageSize, sort);

        //Count of task object
        int countOfElements = 0;

        TaskListDTO taskDetails = new TaskListDTO();

        // Types of query based on the input of path variable
        if (type.equalsIgnoreCase(WorkflowConstant.TaskApiKey.POOL)) {

            // Reading from the task list table whichever task claimable
            tasks = taskItemRepository.getPool(name, status, refNo, taskType, paging);

            // Read the count of list
            countOfElements = taskItemRepository.countInTaskPool(name, status, refNo, taskType);

        } else if (type.equalsIgnoreCase(WorkflowConstant.TaskApiKey.SEARCH)) {

            // Reading with condition (currently only apply to : name , status, reference number)
            tasks = taskItemRepository.findByNameStatusReferenceNo(name, status, refNo, taskType, paging);

            // Reading the count list
            countOfElements = taskItemRepository.countInFindByNameStatusReferenceNo(name, status, refNo, taskType);

        } else if (type.equalsIgnoreCase(WorkflowConstant.TaskApiKey.PERSONAL_LIST)) {

            // Check the userId before process to read the list from database
            //      personal list always requires username input
            if (userId == null|| userId.trim().equalsIgnoreCase("")) {
                log.error(EXCEPTION_PERSONAL_LIST_REQUEST_WITHOUT_PERSONAL_USER_ID);
                throw new MsWorkflowException(Integer.toString(HttpStatus.BAD_REQUEST.value()), PERSONAL_LIST_REQUIRES_VALID_USER_IDENTIFICATION, null, HttpStatus.BAD_REQUEST);
            }

            // Reading from the db
            tasks = taskItemRepository.findByCurrentHolderUserId(userId, name, status, refNo, taskType, paging);

            // count of items matches condition
            countOfElements = taskItemRepository.countInFindByCurrentHolderUserId(userId, name, status, refNo, taskType);
        }

        // Calculate number of pages
        countOfPages = calcCountOfPages(countOfElements, pageSize);
        log.debug("Number of pages : " + countOfPages);

        // throw no content when list is empty
        if (tasks == null || tasks.isEmpty())
            return taskDetails;

        // map tasks to simplified task dto with needed data only
        taskDetails.setTaskList(tasks.stream().map(task ->
            mapper.map(task, SimplifiedTaskDTO.class)
        ).toList());

        // Map the list of task to retrieve the stage name from workflow cache
        taskDetails.getTaskList().stream().forEach(task -> {
            try {
                task.setStageName(workflowDataCacheService.getStageNameByCode(task.getCurrentStageCode()));
            } catch (Exception e) {
                log.error(e.getMessage());
                // apply "%NOT FOUND%" where cache corrupted
                task.setStageName(WorkflowConstant.TaskApiKey.NOT_FOUND);
            }
        });

        // Map the list of task to retrieve the type name from task type config
        taskDetails.getTaskList().stream().forEach(task -> {
            try {
                task.setTaskTypeName(taskTypeConfigService.findMtTaskTypeByCode(task.getTaskTypeCode()).getTaskName());
            } catch (Exception e) {
                log.error(e.getMessage());
                // apply "%NOT FOUND%" where cache corrupted
                task.setTaskTypeName(WorkflowConstant.TaskApiKey.NOT_FOUND);
            }
        });

        // store page count of tasks
        taskDetails.setCountOfPages(countOfPages);

        log.info("Respond contains with list sent.");
        log.debug(taskDetails.toString());
        // send the response
        return taskDetails;
    }

    // Calculate the number of page by count and size
    public int calcCountOfPages(int countOfElements, int pageSize) {
        int countOfPages;
        if (countOfElements < pageSize + 1)
            countOfPages = 1;
        else if (countOfElements % pageSize == 0)
            countOfPages = countOfElements / pageSize;
        else
            countOfPages = (countOfElements / pageSize) + 1;
        return countOfPages;
    }

    // To read the abbreviation(asc, desc) and return a Sort object
    public Sort getSortByAbbreviation(String order, String sortBy) {
        Sort sort;
        if (order.equals("asc"))
            sort = Sort.by(sortBy).ascending();
        else if (order.equals("desc"))
            sort = Sort.by(sortBy).descending();
        else
            sort = Sort.by(sortBy).ascending();
        return sort;
    }

    public List<WorkflowDTO.WorkflowStage> getListOfStage(String path, String userId)
    {
        List<WorkflowDTO.WorkflowStage> stages;
        List<String> stageCodes;

        // Types of query based on the input of path variable
        if (path.equalsIgnoreCase(WorkflowConstant.TaskApiKey.POOL)) {
            stageCodes = taskItemRepository.findAllExistStageCodePool();

        } else if (path.equalsIgnoreCase(WorkflowConstant.TaskApiKey.SEARCH)) {
            stageCodes = taskItemRepository.findAllExistStageCode();

        } else if (path.equalsIgnoreCase(WorkflowConstant.TaskApiKey.PERSONAL_LIST)) {
            if (userId == null || userId.trim().equalsIgnoreCase("")) {
                log.error(EXCEPTION_PERSONAL_LIST_REQUEST_WITHOUT_PERSONAL_USER_ID);
                throw new MsWorkflowException(Integer.toString(HttpStatus.BAD_REQUEST.value()), PERSONAL_LIST_REQUIRES_VALID_USER_IDENTIFICATION, null, HttpStatus.BAD_REQUEST);
            }
            stageCodes = taskItemRepository.findAllExistStageCodeByCurrentHolder(userId);
        }
        else
            return new ArrayList<>();

        stages = stageCodes.stream().map(tmp -> {
            WorkflowDTO.WorkflowStage stage = new WorkflowDTO.WorkflowStage();
            try{
                stage.setStageCode(tmp);
                stage.setStageName(
                        workflowDataCacheService.getStageNameByCode(tmp)
                );
            }catch (Exception e)
            {
                log.error("Unable to find stage name of code :" + tmp);
                return null;
            }
            return stage;
        }).filter( x -> x != null).toList();
        return stages;
    }

    public List<TaskTypeResponseDTO> getListOfType(String path, String userId)
    {
        List<TaskTypeResponseDTO> taskTypes;
        List<String> typeCodes;

        // Types of query based on the input of path variable
        if (path.equalsIgnoreCase(WorkflowConstant.TaskApiKey.POOL)) {
            typeCodes = taskItemRepository.findAllExistTypeCodePool();

        } else if (path.equalsIgnoreCase(WorkflowConstant.TaskApiKey.SEARCH)) {
            typeCodes = taskItemRepository.findAllExistTypeCode();

        } else if (path.equalsIgnoreCase(WorkflowConstant.TaskApiKey.PERSONAL_LIST)) {

            if (userId == null || userId.trim().equalsIgnoreCase("")) {
                log.error(EXCEPTION_PERSONAL_LIST_REQUEST_WITHOUT_PERSONAL_USER_ID);
                throw new MsWorkflowException(
                        Integer.toString(HttpStatus.BAD_REQUEST.value()),
                        PERSONAL_LIST_REQUIRES_VALID_USER_IDENTIFICATION,
                        null, HttpStatus.BAD_REQUEST);
            }
            typeCodes = taskItemRepository.findAllExistTypeCodeByCurrentHolder(userId);
        }
        else
            return new ArrayList<>();

        taskTypes = typeCodes.stream().map(tmp -> {
            TaskTypeResponseDTO type = new TaskTypeResponseDTO();
            try{
                type.setTypeCode(tmp);
                type.setTypeName(
                        taskTypeConfigService.findMtTaskTypeByCode(tmp).getTaskName()
                );
            }catch (Exception e)
            {
                log.error("Unable to find type name of code :" + tmp);
                return null;
            }
            return type;
        }).filter( x -> x != null).toList();

        return taskTypes;
    }
}
