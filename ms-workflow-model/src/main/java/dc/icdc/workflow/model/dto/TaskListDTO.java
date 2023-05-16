package dc.icdc.workflow.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskListDTO {
    private List<SimplifiedTaskDTO> taskList;
    private int countOfPages;
}
