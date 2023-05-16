package dc.icdc.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StageConfigPreviewDTO {
    private String stageCode;
    private String stageName;
    private String lastModifiedBy;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastDateModified;
}
