package dc.icdc.workflow.service;

import dc.icdc.workflow.helper.WorkflowConstant;
import dc.icdc.workflow.model.dto.TaskTypePreviewDTO;
import dc.icdc.workflow.model.entity.TaskType;
import dc.icdc.workflow.repository.TaskTypeRepository;
import dc.icdc.workflow.model.dto.TaskTypeDTO;
import dc.icdc.workflow.model.dto.TaskTypePreviewDetailDTO;
import dc.icdc.workflow.model.entity.TaskTypeConfig;
import dc.icdc.workflow.repository.TaskTypeConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class TaskTypeConfigService {
    private static final String USER_NAME = "ADMIN";

    private final TaskTypeConfigRepository taskTypeConfigRepository;

    private final TaskTypeRepository taskTypeRepository;

    private final ModelMapper map = new ModelMapper();

    @Autowired
    public TaskTypeConfigService(TaskTypeConfigRepository taskTypeConfigRepository, TaskTypeRepository taskTypeRepository) {
        this.taskTypeConfigRepository = taskTypeConfigRepository;
        this.taskTypeRepository = taskTypeRepository;
    }

    public TaskTypePreviewDTO findMtTaskTypeConfigDTO(int requestedPage) {
        log.info("Client request for task type list.");

        TaskTypePreviewDTO result = new TaskTypePreviewDTO();
        List<TaskTypePreviewDetailDTO> taskTypePreviewDetailDTOList = new ArrayList<>();

        try {
            List<TaskType> data = taskTypeRepository.findActiveTask();

            int count;

            int totalPage = (data.size()/10)+1;
            if(data.size() % 10 == 0) {
                totalPage = data.size()/10;
            }

            int firstData = (requestedPage-1)*10;
            if((data.size()-firstData)<10) {
                count = data.size()-firstData;
            } else {
                count = 10;
            }

            int lastData = firstData+count;

            log.info("total data: " + data.size());
            log.info("total page: " + totalPage);
            log.info("first data index: " + firstData);
            log.info("last data index: " + lastData);

            for(int i=firstData; i<lastData; i++) {
                TaskTypePreviewDetailDTO taskTypePreviewDetailDTO = map.map(data.get(i), TaskTypePreviewDetailDTO.class);

                if (data.get(i).getUpdatedBy() == null) {
                    taskTypePreviewDetailDTO.setLastModifiedBy(data.get(i).getCreatedBy());
                } else {
                    taskTypePreviewDetailDTO.setLastModifiedBy(data.get(i).getUpdatedBy());
                }

                if (data.get(i).getDateUpdated() == null) {
                    taskTypePreviewDetailDTO.setLastDateModified(data.get(i).getDateCreated());
                } else {
                    taskTypePreviewDetailDTO.setLastDateModified(data.get(i).getDateUpdated());
                }

                taskTypePreviewDetailDTOList.add(taskTypePreviewDetailDTO);
            }

            result.setCountOfPage(totalPage);
            result.setTaskTypePreviewList(taskTypePreviewDetailDTOList);

            log.info("Response contains with list sent.");
            log.debug(WorkflowConstant.TaskTypeApiKey.RESPONSE + ": " + result);

        } catch (Exception e) {
            log.error("Failed to retrieve data.");
        }

        return result;
    }

    public TaskTypeDTO findMtTaskTypeByCode(String code) {
        log.info("Client request for task type content.");

        if(taskTypeConfigRepository.findByCode(code) != null) {
            TaskTypeConfig taskTypeConfig = taskTypeConfigRepository.findByCode(code);
            Optional<TaskType> taskType = taskTypeRepository.findById(code);

            TaskTypeDTO taskTypeDTO = map.map(taskTypeConfig, TaskTypeDTO.class);

            if(taskType.isPresent()) {
                TaskType result = taskType.get();
                taskTypeDTO.setTaskDescription(result.getTaskDescription());
                taskTypeDTO.setActive(result.isActive());
                taskTypeDTO.setDelete(result.isDelete());
                log.debug(String.valueOf(result));
            }

            if(taskTypeConfig.getDateUpdated() == null) {
                taskTypeDTO.setDateUpdated(taskTypeConfig.getDateCreated());
            } else {
                taskTypeDTO.setDateUpdated(taskTypeConfig.getDateUpdated());
            }

            if(taskTypeConfig.getUpdatedBy() == null) {
                taskTypeDTO.setUpdatedBy(taskTypeConfig.getCreatedBy());
            } else {
                taskTypeDTO.setUpdatedBy(taskTypeConfig.getUpdatedBy());
            }

            log.info("Response contains task type content.");
            log.debug(WorkflowConstant.TaskTypeApiKey.RESPONSE + ": " + taskTypeDTO);
            return taskTypeDTO;
        } else {
            log.info("Failed to retrieve data.");
            log.debug(WorkflowConstant.TaskTypeApiKey.DATA_CODE + ": " + code);
            return null;
        }

    }

    public TaskTypeDTO updateMtTaskTypeConfig(TaskTypeDTO taskType) {
        log.info("Client request for update task type.");

        try {
            TaskTypeDTO result;
            TaskTypeConfig taskTypeConfigResult = taskTypeConfigRepository.findByCode(taskType.getTaskTypeCode());
            Optional<TaskType> taskTypOpt = taskTypeRepository.findById(taskType.getTaskTypeCode());

            if (taskTypOpt.isPresent()) {
                TaskType taskTypeResult = taskTypOpt.get();

                taskTypeResult.setTaskName(taskType.getTaskName());
                taskTypeResult.setTaskDescription(taskType.getTaskDescription());
                taskTypeResult.setActive(taskType.isActive());
                taskTypeResult.setUpdatedBy(USER_NAME);
                taskTypeResult.setDateUpdated(LocalDateTime.now());
                taskTypeRepository.save(taskTypeResult);
            }

            taskTypeConfigResult.setNumberOfApproval(taskType.getNumberOfApproval());
            taskTypeConfigResult.setUpdatedBy(USER_NAME);
            taskTypeConfigResult.setDateUpdated(LocalDateTime.now());
            taskTypeConfigRepository.save(taskTypeConfigResult);

            log.info("Respond contains updated task type.");
            log.debug(WorkflowConstant.TaskTypeApiKey.RESPONSE + ": " + taskTypeConfigResult);

            result = map.map(taskTypeConfigResult, TaskTypeDTO.class);
            result.setNumberOfApproval(taskType.getNumberOfApproval());

            return result;
        } catch (Exception e){
            log.info("Failed to update data.");
            log.debug(WorkflowConstant.TaskTypeApiKey.DATA_CODE + ": " + taskType.getTaskTypeCode());
            return null;
        }

    }

    public TaskTypeDTO createTaskTypeConfig(TaskTypeDTO taskType) {
        TaskTypeDTO result;
        TaskTypeConfig taskTypeConfigResult;
        TaskType taskTypeResult = new TaskType();

        log.info("Client request for create task type.");

        try {
            if(taskTypeConfigRepository.findByCode(taskType.getTaskTypeCode()) != null) {
                log.info("Checking if task type exist.");

                taskTypeConfigResult = taskTypeConfigRepository.findByCode(taskType.getTaskTypeCode());
                Optional<TaskType> taskType1 = taskTypeRepository.findById(taskType.getTaskTypeCode());

                if(taskType1.isPresent()){
                    taskTypeResult = map.map(taskType1.get(), TaskType.class);

                    if(!taskTypeResult.isDelete()) {
                        log.error("Task type is active.");
                        return null;
                    }

                    taskTypeResult.setTaskName(taskType.getTaskName());
                    taskTypeResult.setTaskDescription(taskType.getTaskDescription());
                    taskTypeResult.setUpdatedBy(USER_NAME);
                    taskTypeResult.setDateUpdated(LocalDateTime.now());
                }

                taskTypeConfigResult.setUpdatedBy(USER_NAME);
                taskTypeConfigResult.setDateUpdated(LocalDateTime.now());

            } else {
                log.info("Creating new task type.");

                taskTypeResult = map.map(taskType, TaskType.class);
                taskTypeConfigResult = map.map(taskType, TaskTypeConfig.class);

                UUID uuid = UUID.randomUUID();
                taskTypeConfigResult.setTaskTypeConfigId(uuid);
                taskTypeConfigResult.setTaskType(taskTypeResult);
            }

            taskTypeResult.setDelete(false);
            taskTypeResult.setActive(true);

            taskTypeResult.setCreatedBy(USER_NAME);
            taskTypeResult.setDateCreated(LocalDateTime.now());

            taskTypeConfigResult.setCreatedBy(USER_NAME);
            taskTypeConfigResult.setDateCreated(LocalDateTime.now());
            taskTypeConfigResult.setNumberOfApproval(taskType.getNumberOfApproval());

            taskTypeRepository.save(taskTypeResult);
            taskTypeConfigRepository.save(taskTypeConfigResult);

            log.info("Response contains created task type.");
            log.debug(WorkflowConstant.TaskTypeApiKey.RESPONSE + ": " + taskTypeConfigResult);

            result = map.map(taskTypeResult, TaskTypeDTO.class);
            result.setNumberOfApproval(taskType.getNumberOfApproval());

            return result;
        } catch (Exception e) {
            log.error("Failed to create task type.");
            return null;
        }

    }

    public String deleteTaskTypeConfig(String code) {
        log.info("Client request for delete task type.");

        Optional<TaskType> taskTypOpt = taskTypeRepository.findById(code);

        if (taskTypOpt.isPresent()) {
            TaskType taskTypeResult = taskTypOpt.get();

            if(!taskTypeResult.isDelete()) {
                taskTypeResult.setDelete(true);
                taskTypeResult.setActive(false);
                taskTypeRepository.save(taskTypeResult);

                log.info("Response contains deleted task type.");
                log.debug(WorkflowConstant.TaskTypeApiKey.RESPONSE + ": " + taskTypeResult);
                return code;
            } else {
                log.info("Task type already deleted.");
                return null;
            }

        } else {
            log.info("Failed to delete data.");
            log.debug(WorkflowConstant.TaskTypeApiKey.DATA_CODE + ": " + code);
            return null;
        }
    }
}