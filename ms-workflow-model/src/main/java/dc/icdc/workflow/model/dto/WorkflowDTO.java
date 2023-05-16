package dc.icdc.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowDTO {

    public int workflowVersion;

    public List<WorkflowStage> stageList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkflowStage {

        public String stageName;

        public String stageCode;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public List<WorkflowStageAction> actionList;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class WorkflowStageAction {

            public String actionName;
            public String actionValue;
        }

    }

}