package dc.icdc.workflow.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskClaimRequestDTO {

    @NotNull(message = "User ID is missing")
    private String userId;
    @NotEmpty(message = "List of tasks to be claimed is empty")
    private List<String> listToClaim;

}
