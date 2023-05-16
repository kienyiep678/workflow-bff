package dc.icdc.workflow.controller;

import dc.icdc.workflow.exception.MsWorkflowException;
import dc.icdc.workflow.helper.WorkflowConstant;
import dc.icdc.lib.common.advice.CommonResponseBody;
import dc.icdc.workflow.model.dto.TaskTypeDTO;
import dc.icdc.workflow.model.dto.TaskTypePreviewDTO;
import dc.icdc.workflow.service.TaskTypeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@CommonResponseBody
@RequestMapping("/config/task-type")
public class TaskTypeConfigController {

    private final TaskTypeConfigService taskTypeConfigService;

    @Autowired
    public TaskTypeConfigController(TaskTypeConfigService taskTypeConfigService) {
        this.taskTypeConfigService = taskTypeConfigService;
    }

    @GetMapping("/list/{page}")
    public TaskTypePreviewDTO getAllTaskTypeConfigDTO(@PathVariable int page) {
        TaskTypePreviewDTO taskTypePreviewDTO = taskTypeConfigService.findMtTaskTypeConfigDTO(page);

        if (taskTypePreviewDTO == null) {
            throw new MsWorkflowException(HttpStatus.BAD_REQUEST.toString(), WorkflowConstant.TaskTypeApiKey.TASK_CODE_NOT_FOUND, HttpStatus.BAD_REQUEST);
        } else {
            return taskTypePreviewDTO;
        }

    }

    @GetMapping("/get/{code}")
    public TaskTypeDTO getTaskTypeConfigByCode(@PathVariable String code) {
        TaskTypeDTO taskTypeDTO = taskTypeConfigService.findMtTaskTypeByCode(code);

        if (taskTypeDTO == null) {
            throw new MsWorkflowException(HttpStatus.BAD_REQUEST.toString(), WorkflowConstant.TaskTypeApiKey.TASK_CODE_NOT_FOUND, HttpStatus.BAD_REQUEST);
        } else {
            return taskTypeDTO;
        }
    }

    @PutMapping("/update")
    public TaskTypeDTO updateTaskTypeConfig(@RequestBody TaskTypeDTO taskType) {
        TaskTypeDTO taskConfig = taskTypeConfigService.updateMtTaskTypeConfig(taskType);

        if (taskConfig == null) {
            throw new MsWorkflowException(String.valueOf(HttpStatus.BAD_REQUEST.value()), WorkflowConstant.TaskTypeApiKey.TASK_CODE_NOT_FOUND, HttpStatus.BAD_REQUEST);
        } else {
            return taskConfig;
        }
    }

    @PostMapping("/add")
    public TaskTypeDTO addNewTaskTypeConfig(@RequestBody TaskTypeDTO taskType) {
        TaskTypeDTO taskConfig = taskTypeConfigService.createTaskTypeConfig(taskType);

        if(taskConfig == null) {
            throw new MsWorkflowException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Task code active and exist", HttpStatus.BAD_REQUEST);
        } else {
            return taskConfig;
        }
    }

    @DeleteMapping("/delete/{code}")
    public String deleteTaskTypeConfig(@PathVariable String code) {
        String deletedTaskCode = taskTypeConfigService.deleteTaskTypeConfig(code);

        if (deletedTaskCode == null) {
            throw new MsWorkflowException(String.valueOf(HttpStatus.BAD_REQUEST.value()), WorkflowConstant.TaskTypeApiKey.TASK_CODE_NOT_FOUND, HttpStatus.BAD_REQUEST);
        } else {
            return taskTypeConfigService.deleteTaskTypeConfig(code);
        }
    }

}
