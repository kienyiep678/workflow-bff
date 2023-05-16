package dc.icdc.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimResponseDTO {
    private String referenceNo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String responseMessage;
}