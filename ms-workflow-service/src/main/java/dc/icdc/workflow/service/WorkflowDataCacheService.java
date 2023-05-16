package dc.icdc.workflow.service;

import dc.icdc.workflow.exception.MsWorkflowException;
import dc.icdc.workflow.model.dto.WorkflowDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class WorkflowDataCacheService {
    private List<WorkflowDTO> cachedListOfFullStageOfEveryVersion;

    private List<WorkflowDTO.WorkflowStage> cachedlistOfFullStageNameOnly;

    private final WorkflowDataService workflowDataService;

    @Autowired
    public WorkflowDataCacheService(WorkflowDataService workflowDataService) {
        this.workflowDataService = workflowDataService;
    }

    //@PostConstruct
    public void initialize() {
        log.debug("Started WorkflowDataCacheService");
        cachedListOfFullStageOfEveryVersion = loadListFromService(); // or load from a file, API, etc.
        cachedlistOfFullStageNameOnly = getListofStage(cachedListOfFullStageOfEveryVersion);
    }

    private List<WorkflowDTO.WorkflowStage> getListofStage(List<WorkflowDTO> cachedList) {

        cachedlistOfFullStageNameOnly = new ArrayList<>();

        cachedList.stream().forEach(x ->
                x.getStageList().stream().forEach(y -> {
                    if(cachedlistOfFullStageNameOnly.isEmpty() || cachedlistOfFullStageNameOnly.stream().noneMatch(z -> z.getStageCode().equals(y.getStageCode()))){
                        cachedlistOfFullStageNameOnly.add(new WorkflowDTO.WorkflowStage(y.getStageName(), y.getStageCode(), null));
                        log.debug("Added into Full Stage Name: Code = {}, Name = {}", y.getStageCode(), y.getStageName());
                    }
                })
        );

        return cachedlistOfFullStageNameOnly;
    }

    @Cacheable("cachedListOfFullStageOfEveryVersion")
    public List<WorkflowDTO> getCachedListOfFullStageOfEveryVersion() {
        return cachedListOfFullStageOfEveryVersion;
    }

    @Cacheable("cachedlistOfFullStageNameOnly")
    public List<WorkflowDTO.WorkflowStage> getCachedListOfFullStageNameOnly() {
        return cachedlistOfFullStageNameOnly;
    }

    public List<WorkflowDTO.WorkflowStage> getFullStageListPerVersion(int version) throws Exception {

        try {
            List<WorkflowDTO> listOfFullStageOfEveryVersion = getCachedListOfFullStageOfEveryVersion();
            Optional<WorkflowDTO> workflowDTO = listOfFullStageOfEveryVersion.stream().filter(x -> x.workflowVersion == version).findFirst();

            if(workflowDTO.isPresent()) {

                List<WorkflowDTO.WorkflowStage> workflowStageListPerVersion = workflowDTO.get().getStageList();

                if (workflowStageListPerVersion != null && !workflowStageListPerVersion.isEmpty()) {

                    return workflowStageListPerVersion;

                } else {

                    throw new MsWorkflowException(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), "The workflow is not found with the given version.", HttpStatus.INTERNAL_SERVER_ERROR);

                }
            } else {

                throw new MsWorkflowException(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), "The workflow is not found with the given version.", HttpStatus.INTERNAL_SERVER_ERROR);

            }

        } catch (Exception e) {

            log.error("error: {}",e.getMessage()); //NOSONAR
            throw e;
        }
    }

    //@Cacheable("myObjectList")
    public WorkflowDTO.WorkflowStage getActionListPerStage(String taskStageCd, int version) throws Exception {

        try {
            List<WorkflowDTO.WorkflowStage> workflowStageList = getFullStageListPerVersion(version);

            Optional<WorkflowDTO.WorkflowStage> workflowStage = workflowStageList.stream().filter(x -> x.getStageCode().equalsIgnoreCase(taskStageCd)).findFirst();

            if(workflowStage.isPresent()) {

                return workflowStage.get();

            } else {

                throw new MsWorkflowException(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), "The action list is not found with the given stage code and version.", HttpStatus.INTERNAL_SERVER_ERROR);

            }

        } catch (Exception e) {

            log.error("error: {}",e.getMessage());
            throw e;
        }
    }

    //@Cacheable("myObjectList")
    public String getStageNameByCode(String taskStageCd) throws Exception {

        try {
            List<WorkflowDTO.WorkflowStage> listOfFullStageNameOnly = getCachedListOfFullStageNameOnly();
            Optional<WorkflowDTO.WorkflowStage> workflowStage = listOfFullStageNameOnly.stream().filter(x -> x.getStageCode().equals(taskStageCd)).findFirst();

            if(workflowStage.isPresent()) {

                return workflowStage.get().getStageName();

            } else {

                throw new MsWorkflowException(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), "The stage code is not found", HttpStatus.INTERNAL_SERVER_ERROR);

            }

        } catch (Exception e) {

            log.error("error: {}",e.getMessage());
            throw e;
        }
    }

    private List<WorkflowDTO> loadListFromService() {
        return workflowDataService.getWorkflowDataForEveryVersion();
    }
}
