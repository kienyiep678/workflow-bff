package dc.icdc.bff.msworkflowservice.service;

import dc.icdc.lib.common.model.dto.CommonResponse;
import dc.icdc.workflow.exception.WorkflowSDKException;
import dc.icdc.workflow.model.dto.*;
import dc.icdc.workflow.service.TaskSDKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BffTaskService {

    @Autowired
    private TaskSDKService taskSDKService;

    public CommonResponse<TaskListDTO> getTaskLists(String name, String status, String taskType, String referenceNo, int pageNumber, int pageSize, String sortBy, String order, String userId, String type) throws WorkflowSDKException {

        return taskSDKService.getTaskLists(name, status, taskType, referenceNo, pageNumber, pageSize, sortBy, order, userId, type);
    }
    public CommonResponse<TaskDetailDTO> getTaskById(String taskId, boolean history) throws WorkflowSDKException {

        return taskSDKService.getTaskById(taskId, history);
    }

    public CommonResponse<Map<String, Object>> createTask(TaskRouteDTO requestData) throws WorkflowSDKException {

        return taskSDKService.createTask(requestData);
    }

    public CommonResponse<Map<String, Object>> routeTask(TaskRouteDTO requestData) throws WorkflowSDKException {

        return taskSDKService.routeTask(requestData);
    }

    public CommonResponse<TaskClaimResponseDTO> claimTask(TaskClaimRequestDTO requestData) throws WorkflowSDKException {

        return taskSDKService.claimTask(requestData);
    }

    public CommonResponse<Void> saveTask(TaskRouteDTO requestData) throws WorkflowSDKException {

        return taskSDKService.saveTask(requestData);

    }

    public CommonResponse<List<TaskTypeResponseDTO>> getType(String path, String userId) throws WorkflowSDKException {

        return taskSDKService.getType(path, userId);
    }

    public CommonResponse<List<WorkflowDTO.WorkflowStage>> getStatus(String path, String userId) throws WorkflowSDKException {

        return taskSDKService.getStatus(path, userId);
    }


}
