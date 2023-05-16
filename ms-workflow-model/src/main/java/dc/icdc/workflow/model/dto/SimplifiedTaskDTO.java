package dc.icdc.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimplifiedTaskDTO
{
    @JsonProperty("taskId")
    private UUID id;
    @JsonProperty("taskReferenceNo")
    private String referenceNo;
    @JsonProperty("taskName")
    private String name;
    private String taskTypeName;
    private String taskTypeCode;
    private String createdBy;
    private String stageName;
    private String currentStageCode;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateUpdated;
}