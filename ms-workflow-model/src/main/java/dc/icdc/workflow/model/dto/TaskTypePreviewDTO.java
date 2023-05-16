package dc.icdc.workflow.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskTypePreviewDTO {
    private List<TaskTypePreviewDetailDTO> taskTypePreviewList;
    private int countOfPage;
}
