package dc.icdc.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDetailDTO {

    @JsonProperty("taskId")
    private UUID id;
    @JsonProperty("taskReferenceNo")
    private String referenceNo;
    @JsonProperty("taskName")
    private String name;
    private String description;
    @Valid
    private List<TaskUrlDTO> taskUrlJsonObject;
    private String stageName;
    private String currentStageCode;
    private String taskTypeName;
    private String taskTypeCode;
    private String currentHolderUserId;
    private String actionBy;
    private Integer actionType;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateAction;
    private String updatedBy;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateUpdated;
    private String createdBy;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated;
    private String remarkMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<WorkflowDTO.WorkflowStage.WorkflowStageAction> actionList;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TaskHistoryDTO> history;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String processExceptionMessage;

    private boolean isEndStage;

}

