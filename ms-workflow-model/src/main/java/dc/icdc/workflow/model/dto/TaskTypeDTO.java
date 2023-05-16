package dc.icdc.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskTypeDTO {
    private String taskTypeCode;
    private String taskName;
    private String taskDescription;
    private Integer numberOfApproval;
    private boolean isActive;
    private boolean isDelete;
    private String createdBy;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated;
    private String updatedBy;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateUpdated;
}
