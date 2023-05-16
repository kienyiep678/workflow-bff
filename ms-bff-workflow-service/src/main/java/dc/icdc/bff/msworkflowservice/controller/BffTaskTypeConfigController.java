package dc.icdc.bff.msworkflowservice.controller;

import dc.icdc.bff.msworkflowservice.service.BffTaskTypeConfigService;
import dc.icdc.lib.common.model.dto.CommonResponse;
import dc.icdc.workflow.model.dto.TaskTypeDTO;
import dc.icdc.workflow.model.dto.TaskTypePreviewDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/bff/config")
@Slf4j
public class BffTaskTypeConfigController {

    @Autowired
    private BffTaskTypeConfigService bffTaskTypeConfigService;

    @GetMapping(value = "/task-type/list/{page}")
    public CommonResponse<TaskTypePreviewDTO> getTaskTypeList(@PathVariable int page) {
        return bffTaskTypeConfigService.getTaskTypeConfigList(page);
    }

    @GetMapping(value = "/task-type/get/{code}")
    public CommonResponse<TaskTypeDTO> getTaskTypeByCode(@PathVariable String code) {
        return bffTaskTypeConfigService.getTaskTypeByCode(code);
    }

    @PutMapping(value = "/task-type/update")
    public CommonResponse<TaskTypeDTO> updateTaskType(@RequestBody TaskTypeDTO taskType) {
        return bffTaskTypeConfigService.updateTaskType(taskType);
    }

    @PostMapping(value = "/task-type/add")
    public CommonResponse<TaskTypeDTO> addNewTaskType(@RequestBody TaskTypeDTO taskType) {
        return bffTaskTypeConfigService.addNewTaskTypeConfig(taskType);
    }

    @DeleteMapping(value = "/task-type/delete/{code}")
    public CommonResponse<String> deleteTaskTypeConfig(@PathVariable String code) {
        return bffTaskTypeConfigService.deleteTaskTypeConfig(code);
    }
}
