package dc.icdc.workflow.service;

import dc.icdc.lib.common.model.dto.CommonResponse;
import dc.icdc.lib.hi.helper.HostIntegrationHelper;
import dc.icdc.lib.hi.model.dto.HostIntegrationDTO;
import dc.icdc.workflow.exception.WorkflowSDKException;
import dc.icdc.workflow.model.dto.TaskTypeDTO;
import dc.icdc.workflow.model.dto.TaskTypePreviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class TaskTypeConfigSDKService {

    @Autowired
    private HostIntegrationHelper hostIntegrationHelper;

    public CommonResponse<TaskTypePreviewDTO> getTaskTypeListHost(int page) throws WorkflowSDKException {

        // set path variable
        Map<String,Object> pathVariables = new HashMap<>();
        pathVariables.put("page", page);

        // build integration dto (request type,response type)
        HostIntegrationDTO<Void,CommonResponse<TaskTypePreviewDTO>> integrationDTO =
                HostIntegrationDTO.<Void, CommonResponse<TaskTypePreviewDTO>>builder()
                        .serviceName("config-task-type-list")
                        .outputParameterizedTypeReference(new ParameterizedTypeReference<CommonResponse<TaskTypePreviewDTO>>() {})
                        .pathVariables(pathVariables)
                        .build();

        return hostIntegrationHelper.integrate(integrationDTO);
    }

    public CommonResponse<TaskTypeDTO> getTaskTypeByCodeHost(String code) throws WorkflowSDKException{

        // set path variable
        Map<String,Object> pathVariables = new HashMap<>();
        pathVariables.put("code", code);

        // build integration dto
        HostIntegrationDTO<Void,CommonResponse<TaskTypeDTO>> integrationDTO =
                HostIntegrationDTO.<Void, CommonResponse<TaskTypeDTO>>builder()
                        .serviceName("config-task-type-get")
                        .outputParameterizedTypeReference(new ParameterizedTypeReference<CommonResponse<TaskTypeDTO>>() {})
                        .pathVariables(pathVariables)
                        .build();

        return hostIntegrationHelper.integrate(integrationDTO);
    }

    public CommonResponse<TaskTypeDTO> updateTaskTypeConfigHost(TaskTypeDTO taskType) throws WorkflowSDKException{

        // set http headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // build integration dto
        HostIntegrationDTO<TaskTypeDTO,CommonResponse<TaskTypeDTO>> integrationDTO =
                HostIntegrationDTO.<TaskTypeDTO, CommonResponse<TaskTypeDTO>>builder()
                        .serviceName("config-task-type-update")
                        .outputParameterizedTypeReference(new ParameterizedTypeReference<CommonResponse<TaskTypeDTO>>() {})
                        .headers(headers)
                        .requestBody(taskType)
                        .build();

        return hostIntegrationHelper.integrate(integrationDTO);
    }

    public CommonResponse<TaskTypeDTO> addNewTaskTypeConfigHost(TaskTypeDTO taskType) throws WorkflowSDKException{

        // set http headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // build integration dto
        HostIntegrationDTO<TaskTypeDTO,CommonResponse<TaskTypeDTO>> integrationDTO =
                HostIntegrationDTO.<TaskTypeDTO, CommonResponse<TaskTypeDTO>>builder()
                        .serviceName("config-task-type-add")
                        .outputParameterizedTypeReference(new ParameterizedTypeReference<CommonResponse<TaskTypeDTO>>() {})
                        .headers(headers)
                        .requestBody(taskType)
                        .build();

        return hostIntegrationHelper.integrate(integrationDTO);
    }

    public CommonResponse<String> deleteTaskTypeConfigHost(String code) throws WorkflowSDKException{

        // set path variable
        Map<String,Object> pathVariables = new HashMap<>();
        pathVariables.put("code", code);

        // build integration dto
        HostIntegrationDTO<Void,CommonResponse<String>> integrationDTO =
                HostIntegrationDTO.<Void, CommonResponse<String>>builder()
                        .serviceName("config-task-type-delete")
                        .outputParameterizedTypeReference(new ParameterizedTypeReference<CommonResponse<String>>() {})
                        .pathVariables(pathVariables)
                        .build();

        return hostIntegrationHelper.integrate(integrationDTO);
    }
}