package dc.icdc.bff.msworkflowservice.service;

import dc.icdc.lib.common.model.dto.CommonResponse;
import dc.icdc.workflow.exception.WorkflowSDKException;
import dc.icdc.workflow.model.dto.TaskTypeDTO;
import dc.icdc.workflow.model.dto.TaskTypePreviewDTO;
import dc.icdc.workflow.service.TaskTypeConfigSDKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BffTaskTypeConfigService {

    @Autowired
    private TaskTypeConfigSDKService taskTypeConfigSDKService;

    public CommonResponse<TaskTypePreviewDTO> getTaskTypeConfigList(int page) throws WorkflowSDKException {

        return taskTypeConfigSDKService.getTaskTypeListHost(page);
    }

    public CommonResponse<TaskTypeDTO> getTaskTypeByCode(String code) throws WorkflowSDKException{

        return taskTypeConfigSDKService.getTaskTypeByCodeHost(code);
    }

    public CommonResponse<TaskTypeDTO> updateTaskType(TaskTypeDTO taskType) throws WorkflowSDKException{

        return taskTypeConfigSDKService.updateTaskTypeConfigHost(taskType);
    }

    public CommonResponse<TaskTypeDTO> addNewTaskTypeConfig(TaskTypeDTO taskType) throws WorkflowSDKException{

        return taskTypeConfigSDKService.addNewTaskTypeConfigHost(taskType);
    }

    public CommonResponse<String> deleteTaskTypeConfig(String code) throws WorkflowSDKException{

        return taskTypeConfigSDKService.deleteTaskTypeConfigHost(code);
    }
}
