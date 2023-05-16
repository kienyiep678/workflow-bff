package dc.icdc.workflow.service;

import dc.icdc.lib.common.model.dto.CommonResponse;
import dc.icdc.lib.hi.helper.HostIntegrationHelper;
import dc.icdc.lib.hi.model.dto.HostIntegrationDTO;
import dc.icdc.workflow.constant.WorkflowSDKConstant;
import dc.icdc.workflow.exception.WorkflowSDKException;
import dc.icdc.workflow.model.dto.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class TaskSDKService {

    @Autowired
    private HostIntegrationHelper hostIntegrationHelper;
    @Autowired
    private Validator validator;

    public CommonResponse<TaskListDTO> getTaskLists(String name, String status, String taskType, String referenceNo, int pageNumber, int pageSize, String sortBy, String order, String userId, String type) throws WorkflowSDKException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String,Object> pathVariables = new HashMap<>();
        pathVariables.put("type", type);

        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("name",name);
        queryParams.put("status",status);
        queryParams.put("task-type",taskType);
        queryParams.put("reference-no",referenceNo);
        queryParams.put("page-number",Integer.toString(pageNumber));
        queryParams.put("page-size",Integer.toString(pageSize));
        queryParams.put("sort-by",sortBy);
        queryParams.put("asc",order);
        queryParams.put("user-id",userId);

        HostIntegrationDTO<Void,CommonResponse<TaskListDTO>> integrationDTO =
                HostIntegrationDTO.<Void, CommonResponse<TaskListDTO>>builder()
                        .serviceName("task-list")
                        .outputParameterizedTypeReference(new ParameterizedTypeReference<CommonResponse<TaskListDTO>>() {})
                        .headers(headers)
                        .pathVariables(pathVariables)
                        .queryParams(queryParams)
                        .build();

        return hostIntegrationHelper.integrate(integrationDTO);
    }
    public CommonResponse<TaskDetailDTO> getTaskById(String taskId, boolean history) throws WorkflowSDKException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String,Object> pathVariables = new HashMap<>();
        pathVariables.put("task-id", taskId);

        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("history",Boolean.toString(history));

        HostIntegrationDTO<Void,CommonResponse<TaskDetailDTO>> integrationDTO =
                HostIntegrationDTO.<Void, CommonResponse<TaskDetailDTO>>builder()
                        .serviceName("task-get")
                        .outputParameterizedTypeReference(new ParameterizedTypeReference<CommonResponse<TaskDetailDTO>>() {})
                        .headers(headers)
                        .pathVariables(pathVariables)
                        .queryParams(queryParams)
                        .build();

        return hostIntegrationHelper.integrate(integrationDTO);
    }

    public CommonResponse<Map<String, Object>> createTaskFromSystem(TaskRouteDTO taskCreateRequestDTO) throws WorkflowSDKException {

        // set create type to system
        taskCreateRequestDTO.setTaskCreateType(WorkflowSDKConstant.General.CREATE_BY_SYSTEM);

        // check violations && validation
        Set<ConstraintViolation<TaskRouteDTO>> violations = validator.validate(taskCreateRequestDTO);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<TaskRouteDTO> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage() + "; ");
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }

        return createTask(taskCreateRequestDTO);
    }

    public CommonResponse<Map<String, Object>> createTask(TaskRouteDTO requestData) throws WorkflowSDKException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HostIntegrationDTO<TaskRouteDTO, CommonResponse<Map<String, Object>>> integrationDTO =
                HostIntegrationDTO.<TaskRouteDTO, CommonResponse<Map<String, Object>>>builder()
                        .serviceName("task-create")
                        .outputParameterizedTypeReference(new ParameterizedTypeReference<CommonResponse<Map<String, Object>>>() {
                        })
                        .headers(headers)
                        .requestBody(requestData)
                        .build();

        return hostIntegrationHelper.integrate(integrationDTO);
    }

    public CommonResponse<Map<String, Object>> routeTask(TaskRouteDTO requestData) throws WorkflowSDKException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HostIntegrationDTO<TaskRouteDTO, CommonResponse<Map<String, Object>>> integrationDTO =
                HostIntegrationDTO.<TaskRouteDTO, CommonResponse<Map<String, Object>>>builder()
                        .serviceName("task-route")
                        .outputParameterizedTypeReference(new ParameterizedTypeReference<CommonResponse<Map<String, Object>>>() {
                        })
                        .headers(headers)
                        .requestBody(requestData)
                        .build();

        return hostIntegrationHelper.integrate(integrationDTO);
    }

    public CommonResponse<TaskClaimResponseDTO> claimTask(TaskClaimRequestDTO requestData) throws WorkflowSDKException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HostIntegrationDTO<TaskClaimRequestDTO, CommonResponse<TaskClaimResponseDTO>> integrationDTO =
                HostIntegrationDTO.<TaskClaimRequestDTO, CommonResponse<TaskClaimResponseDTO>>builder()
                        .serviceName("task-claim")
                        .outputParameterizedTypeReference(new ParameterizedTypeReference<CommonResponse<TaskClaimResponseDTO>>() {
                        })
                        .headers(headers)
                        .requestBody(requestData)
                        .build();

        return hostIntegrationHelper.integrate(integrationDTO);
    }

    public CommonResponse<Void> saveTask(TaskRouteDTO requestData) throws WorkflowSDKException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HostIntegrationDTO<TaskRouteDTO, CommonResponse<Void>> integrationDTO =
                HostIntegrationDTO.<TaskRouteDTO, CommonResponse<Void>>builder()
                        .serviceName("task-save")
                        .outputParameterizedTypeReference(new ParameterizedTypeReference<CommonResponse<Void>>() {
                        })
                        .headers(headers)
                        .requestBody(requestData)
                        .build();

        return hostIntegrationHelper.integrate(integrationDTO);
    }

    public CommonResponse<List<TaskTypeResponseDTO>> getType(String path, String userId) throws WorkflowSDKException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("path", path);
        queryParams.put("user-id", userId);

        HostIntegrationDTO<Void, CommonResponse<List<TaskTypeResponseDTO>>> integrationDTO =
                HostIntegrationDTO.<Void, CommonResponse<List<TaskTypeResponseDTO>>>builder()
                        .serviceName("task-get-type")
                        .outputParameterizedTypeReference(new ParameterizedTypeReference<CommonResponse<List<TaskTypeResponseDTO>>>() {
                        })
                        .headers(headers)
                        .queryParams(queryParams)
                        .build();

        return hostIntegrationHelper.integrate(integrationDTO);
    }

    public CommonResponse<List<WorkflowDTO.WorkflowStage>> getStatus(String path, String userId) throws WorkflowSDKException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("path", path);
        queryParams.put("user-id", userId);

        HostIntegrationDTO<Void, CommonResponse<List<WorkflowDTO.WorkflowStage>>> integrationDTO =
                HostIntegrationDTO.<Void, CommonResponse<List<WorkflowDTO.WorkflowStage>>>builder()
                        .serviceName("task-get-status")
                        .outputParameterizedTypeReference(new ParameterizedTypeReference<CommonResponse<List<WorkflowDTO.WorkflowStage>>>() {
                        })
                        .headers(headers)
                        .queryParams(queryParams)
                        .build();

        return hostIntegrationHelper.integrate(integrationDTO);
    }

}
