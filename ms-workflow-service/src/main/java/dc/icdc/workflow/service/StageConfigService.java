package dc.icdc.workflow.service;

import dc.icdc.workflow.model.entity.StageConfig;
import dc.icdc.workflow.repository.StageConfigRepository;
import dc.icdc.workflow.model.dto.StageConfigDTO;
import dc.icdc.workflow.model.dto.StageConfigPreviewDTO;
import dc.icdc.workflow.model.dto.WorkflowDTO;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class StageConfigService {

    private static final String USER_NAME = "ADMIN";

    private StageConfigRepository stageConfigRepository;

    private WorkflowDataCacheService workflowDataCacheService;

    private WorkflowDataService workflowDataService;

    private ModelMapper map = new ModelMapper();

    @Autowired
    public StageConfigService(StageConfigRepository stageConfigRepository, WorkflowDataCacheService workflowDataCacheService, WorkflowDataService workflowDataService) {
        this.stageConfigRepository = stageConfigRepository;
        this.workflowDataCacheService = workflowDataCacheService;
        this.workflowDataService = workflowDataService;
    }

    public String findStageName(String code) throws Exception {
        String result = "";

        List<WorkflowDTO.WorkflowStage> getFullStageListPerVersion = workflowDataCacheService.getFullStageListPerVersion(workflowDataService.getLatestVersion());

        for (WorkflowDTO.WorkflowStage stage : getFullStageListPerVersion) {
            if (stage.getStageCode().equals(code)) {
                result = stage.getStageName();
            }
        }

        return result;
    }

    public JSONArray convertJsontoArray(String toBeJson) {
        JSONArray stringResult = new JSONArray();

        if (toBeJson.contains(";")) {
            String[] roleSplitted = toBeJson.split(";");
            for (String word : roleSplitted) {
                stringResult.put(word);
            }
        } else {
            stringResult.put(toBeJson);
        }

        return stringResult;
    }

    public List<StageConfigPreviewDTO> findMtStageConfigPreview() {
        log.info("Client request for update task type.");
        List<StageConfigPreviewDTO> listStageConfig = new ArrayList<>();

        try {
            for (String code : stageConfigRepository.findAllStageCodes()) {
                StageConfigPreviewDTO stageConfigPreviewDTO = new StageConfigPreviewDTO();

                StageConfig stageConfig = stageConfigRepository.findByCode(code);
                stageConfigPreviewDTO.setStageCode(code);

                String stageName = findStageName(stageConfig.getStageCode());
                stageConfigPreviewDTO.setStageName(stageName);

                if (stageConfig.getUpdatedBy() == null) {
                    stageConfigPreviewDTO.setLastModifiedBy(stageConfig.getCreatedBy());
                } else {
                    stageConfigPreviewDTO.setLastModifiedBy(stageConfig.getUpdatedBy());
                }

                if (stageConfig.getDateUpdated() == null) {
                    stageConfigPreviewDTO.setLastDateModified(stageConfig.getDateCreated());
                } else {
                    stageConfigPreviewDTO.setLastDateModified(stageConfig.getDateUpdated());
                }

                listStageConfig.add(stageConfigPreviewDTO);
            }

            log.info("Response contains with list sent.");
            log.debug("Response: " + listStageConfig.toString()); //NOSONAR
        } catch (Exception e) {
            log.error("Failed to retrieve data.");
        }

        return listStageConfig;
    }

    public StageConfigDTO findMtStageConfigByCode(String code) throws Exception {

        if(stageConfigRepository.findByCode(code) != null) {
            StageConfig stageConfig = stageConfigRepository.findByCode(code);

            StageConfigDTO stageConfigDTO = map.map(stageConfig, StageConfigDTO.class);

            String stageName = findStageName(stageConfigDTO.getStageCode());
            stageConfigDTO.setStageName(stageName);

            String toBeJson = stageConfig.getUserRoleCode();
            JSONArray stringResult = convertJsontoArray(toBeJson);
            stageConfigDTO.setUserRoleCode(stringResult.toList());

            if (stageConfig.getDateCreated() != null) {
                stageConfigDTO.setDateCreated(stageConfig.getDateCreated());
            }

            if (stageConfig.getDateUpdated() != null) {
                stageConfigDTO.setDateUpdated(stageConfig.getDateUpdated());
            }

            log.info("Response contains stage content.");
            log.debug("Response: " + stageConfig.toString());
            return stageConfigDTO;
        } else {
            log.info("Failed to retrieve data.");
            log.debug("Data code: " + code);
            return null;
        }

    }

    public StageConfigDTO updateMtStageConfig(StageConfigDTO stageConfigDTO) {
        StageConfigDTO result;

        try {
            boolean first = true;
            StringBuilder userCode = new StringBuilder();
            StageConfig stageConfig = stageConfigRepository.findByCode(stageConfigDTO.getStageCode());

            String temp = stageConfigDTO.getUserRoleCode().toString();
            temp = temp.substring(1, temp.length() - 1);
            String[] toArray = temp.split(",");

            for (String array : toArray) {
                if (first) {
                    first = false;
                } else {
                    userCode.append(";");
                }
                userCode.append(array.trim());
            }

            stageConfig.setUserRoleCode(userCode.toString());
            stageConfig.setStageDescription(stageConfigDTO.getStageDescription());
            stageConfig.setUpdatedBy(USER_NAME);
            stageConfig.setDateUpdated(LocalDateTime.now());

            stageConfigRepository.save(stageConfig);

            log.info("Response contains updated stage.");
            log.debug("Response: " + stageConfig.toString());

            result = map.map(stageConfig, StageConfigDTO.class);
            result.setStageName(findStageName(stageConfig.getStageCode()));
            result.setUserRoleCode(stageConfigDTO.getUserRoleCode());

            return result;
        } catch (Exception e){
            log.info("Failed to update data.");
            log.debug("Data code: " + stageConfigDTO.getStageCode());
            return null;
        }
    }

}
