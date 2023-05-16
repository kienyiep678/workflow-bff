package dc.icdc.workflow.controller;

import dc.icdc.workflow.exception.MsWorkflowException;
import dc.icdc.lib.common.advice.CommonResponseBody;
import dc.icdc.workflow.model.dto.StageConfigDTO;
import dc.icdc.workflow.model.dto.StageConfigPreviewDTO;
import dc.icdc.workflow.service.StageConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@CommonResponseBody
@RequestMapping("/config/stage")
public class StageConfigController {

    private final StageConfigService stageConfigService;

    @Autowired
    public StageConfigController(StageConfigService stageConfigService) {
        this.stageConfigService = stageConfigService;
    }

    @GetMapping("/list")
    public List<StageConfigPreviewDTO> getAllStagePreviewConfig() {
        try {
            return stageConfigService.findMtStageConfigPreview();
        } catch (Exception e) {
            throw new MsWorkflowException(HttpStatus.BAD_REQUEST.toString(), e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/{code}")
    public StageConfigDTO getStageConfigByCode(@PathVariable String code){
        try {
            StageConfigDTO stageConfigDTO = stageConfigService.findMtStageConfigByCode(code);

            if(stageConfigDTO == null) {
                throw new MsWorkflowException(HttpStatus.BAD_REQUEST.toString(), "Data not found", HttpStatus.BAD_REQUEST);
            } else {
                return stageConfigDTO;
            }
        } catch (Exception e) {
            throw new MsWorkflowException(HttpStatus.BAD_REQUEST.toString(), "Data not found", HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/update-stage")
    public StageConfigDTO updateStageConfig(@RequestBody StageConfigDTO stageConfigDTO) {
        StageConfigDTO stageConfig = stageConfigService.updateMtStageConfig(stageConfigDTO);

        if(stageConfig == null) {
            throw new MsWorkflowException(HttpStatus.BAD_REQUEST.toString(), "Data incorrect", HttpStatus.BAD_REQUEST);
        } else {
           return stageConfig;
        }
    }

}
