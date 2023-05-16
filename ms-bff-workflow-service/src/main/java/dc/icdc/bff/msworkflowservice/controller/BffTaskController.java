package dc.icdc.bff.msworkflowservice.controller;

import dc.icdc.bff.msworkflowservice.service.BffTaskService;
import dc.icdc.lib.common.model.dto.CommonResponse;
import dc.icdc.workflow.helper.ValidateCreateTask;
import dc.icdc.workflow.helper.ValidateRouteTask;
import dc.icdc.workflow.model.dto.*;
import dc.icdc.workflow.service.WorkflowRestClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/bff/task")
@Slf4j
public class BffTaskController {

    @Autowired
    private BffTaskService bffTaskService;


    @GetMapping("/list/{type}")
    /* Method to get tasks list */
    public CommonResponse<TaskListDTO> getTaskLists(
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
        return bffTaskService.getTaskLists(name, status, taskType, referenceNo, pageNumber, pageSize, sortBy, order, userId, type);
    }

    @GetMapping(value = "/get/{task-id}")
    /* To get JSON response for detail with action names
       After receiving confirmation and information from workflow engine */
    public CommonResponse<TaskDetailDTO> getTaskById(
            @PathVariable(name = "task-id") String taskId,
            @RequestParam(required = false, defaultValue = "false") boolean history) {
        return bffTaskService.getTaskById(taskId, history);
    }

    @PostMapping("/create")
    public CommonResponse<Map<String, Object>> createTask(@Validated({ValidateCreateTask.class}) @RequestBody TaskRouteDTO requestData) throws Exception {
        return bffTaskService.createTask(requestData);
    }

    @PostMapping("/route")
    /* Method to execute actions. Eg: SUBMIT, CANCEL, APPROVE, REJECT, RETURN TO TASK CREATION. */
    public CommonResponse<Map<String, Object>> routeTask(@Validated({ValidateRouteTask.class}) @RequestBody TaskRouteDTO requestData) {
        return bffTaskService.routeTask(requestData);
    }

    @PostMapping(value = "/claim")
    /* Method to claim one or more tasks from the task pool. */
    public CommonResponse<TaskClaimResponseDTO> claimTask(@Validated @RequestBody TaskClaimRequestDTO requestData) {
        return bffTaskService.claimTask(requestData);
    }

    @PostMapping(value = "/save")
    public CommonResponse<Void> saveTask(@RequestBody TaskRouteDTO requestData) {
        return bffTaskService.saveTask(requestData);
    }

    @GetMapping(value = "/get-type")
    public CommonResponse<List<TaskTypeResponseDTO>> getType(@RequestParam String path, @RequestParam(required = false, name = "user-id") String userId) {
        return bffTaskService.getType(path, userId);
    }

    @GetMapping(value = "/get-status")
    public CommonResponse<List<WorkflowDTO.WorkflowStage>> getStatus(@RequestParam String path, @RequestParam(required = false, name = "user-id") String userId) {
        return bffTaskService.getStatus(path, userId);
    }

}
