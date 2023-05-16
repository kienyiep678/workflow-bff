package dc.icdc.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskClaimResponseDTO {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ClaimResponseDTO> success;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ClaimResponseDTO> fail;
}
