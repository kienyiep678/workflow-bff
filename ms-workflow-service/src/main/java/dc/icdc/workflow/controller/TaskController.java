package dc.icdc.workflow.controller;

import dc.icdc.lib.common.advice.CommonResponseBody;
import dc.icdc.lib.common.model.dto.CommonResponse;
import dc.icdc.workflow.model.dto.*;
import dc.icdc.workflow.helper.ValidateCreateTask;
import dc.icdc.workflow.helper.ValidateRouteTask;
import dc.icdc.workflow.service.TaskActionService;
import dc.icdc.workflow.service.TaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/task")
@CommonResponseBody
public class TaskController {

    private final TaskListService taskListService;
    private final TaskActionService taskActionService;

    @Autowired
    /* Construct to initialise variables. */
    public TaskController(TaskListService taskListService, TaskActionService taskActionService) {
        this.taskListService = taskListService;
        this.taskActionService = taskActionService;
    }

    @GetMapping("/list/{type}")
    /* Method to get tasks list */
    public TaskListDTO getTaskLists(
            @RequestParam(required = false, defaultValue = "") String name
            , @RequestParam(required = false, defaultValue = "") String status
            , @RequestParam(required = false, defaultValue = "", name = "task-type") String taskType
            , @RequestParam(required = false, defaultValue = "", name = "reference-no") String referenceNo
            , @RequestParam(required = false, defaultValue = "0", name = "page-number") int pageNumber
            , @RequestParam(required = false, defaultValue = "10", name = "page-size") int pageSize
            , @RequestParam(required = false, defaultValue = "dateCreated", name = "sort-by") String sortBy
            , @RequestParam(required = false, defaultValue = "asc") String order
            , @RequestParam(required = false, name = "user-id") String userId
            , @PathVariable String type) {

        return taskListService.getListOfTasks(order, sortBy, pageNumber, pageSize, type, name, status, referenceNo, userId, taskType);
    }

    @GetMapping(value = "/get/{task-id}")
    /* To get JSON response for detail with action names
       After receiving confirmation and information from workflow engine */
    public CommonResponse<TaskDetailDTO> getTaskById(
            @PathVariable(name = "task-id") String taskId,
            @RequestParam(required = false, defaultValue = "false") boolean history) {

        TaskDetailDTO taskDetail = taskListService.getContentByTaskId(taskId, history);

        // if history is empty
        if (history && taskDetail.getHistory() == null) {
            return CommonResponse.<TaskDetailDTO>builder()
                    .code(Integer.toString(HttpStatus.PARTIAL_CONTENT.value()))
                    .message("History not found")
                    .data(taskDetail)
                    .httpStatus(HttpStatus.PARTIAL_CONTENT)
                    .success(true)
                    .build();
        }

        return CommonResponse.<TaskDetailDTO>builder()
                .code(Integer.toString(HttpStatus.OK.value()))
                .message("Complete detail is sent")
                .data(taskDetail)
                .httpStatus(HttpStatus.OK)
                .success(true)
                .build();
    }

    @PostMapping("/create")
    /* Method to create new task manually or from system */
    public Map<String,Object> createTask(@Validated({ValidateCreateTask.class}) @RequestBody TaskRouteDTO requestData){

        return taskActionService.startNewTask(requestData);
    }

    @PostMapping("/route")
    /* Method to execute actions. Eg: SUBMIT, CANCEL, APPROVE, REJECT, RETURN TO TASK CREATION. */
    public Map<String,Object> routeTask(@Validated({ValidateRouteTask.class}) @RequestBody TaskRouteDTO requestData) {

        return taskActionService.routeTask(requestData);
    }

    @PostMapping(value = "/claim")
    /* Method to claim one or more tasks from the task pool. */
    public CommonResponse<TaskClaimResponseDTO> claimTask(@Validated @RequestBody TaskClaimRequestDTO requestData){

        TaskClaimResponseDTO taskClaimResponse =  taskActionService.claimTask(requestData);

        // if all task fails
        if(taskClaimResponse.getFail().size() == requestData.getListToClaim().size()){
            return CommonResponse.<TaskClaimResponseDTO>builder()
                    .code(Integer.toString(HttpStatus.BAD_REQUEST.value()))
                    .message("Claims failed")
                    .data(taskClaimResponse)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .build();
        }
        // if some task fails
        else if (!taskClaimResponse.getFail().isEmpty() && !taskClaimResponse.getSuccess().isEmpty()) {
            return CommonResponse.<TaskClaimResponseDTO>builder()
                    .code(Integer.toString(HttpStatus.PARTIAL_CONTENT.value()))
                    .message("Claims partially failed")
                    .data(taskClaimResponse)
                    .httpStatus(HttpStatus.PARTIAL_CONTENT)
                    .success(true)
                    .build();
        }

        // all task success
        return CommonResponse.<TaskClaimResponseDTO>builder()
                .code(Integer.toString(HttpStatus.OK.value()))
                .message("Claims success")
                .data(taskClaimResponse)
                .httpStatus(HttpStatus.OK)
                .success(true)
                .build();
    }

    @PostMapping(value = "/save")
    public CommonResponse<Void> saveTask(@RequestBody TaskRouteDTO requestData){
        return taskActionService.saveTask(requestData);
    }

    @GetMapping(value = "/get-type")
    public List<TaskTypeResponseDTO> getType(@RequestParam String path, @RequestParam(required = false, name = "user-id") String userId)
    {
        return taskListService.getListOfType(path , userId);
    }

    @GetMapping(value = "/get-status")
    public List<WorkflowDTO.WorkflowStage> getStatus(@RequestParam String path, @RequestParam(required = false, name = "user-id") String userId)
    {
        return taskListService.getListOfStage(path, userId);
    }

    @GetMapping(value = "/testget")
    public CommonResponse<Void> testGet()
    {
        return CommonResponse.<Void>success();
    }

    @PostMapping(value = "/testpost")
    public CommonResponse<Void> testPost(@RequestBody Map<String,Object> x)
    {
        return CommonResponse.<Void>success();
    }
}