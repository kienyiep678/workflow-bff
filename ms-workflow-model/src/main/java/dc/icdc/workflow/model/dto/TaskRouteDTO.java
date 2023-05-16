package dc.icdc.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import dc.icdc.workflow.helper.ValidateCreateTask;
import dc.icdc.workflow.helper.ValidateRouteTask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRouteDTO {

    @NotEmpty(message = "User ID is null")
    private String userId;

    @NotEmpty(message = "Task Create Type is null", groups = ValidateCreateTask.class)
    private String taskCreateType;

    @NotEmpty(message = "Action is null", groups = ValidateRouteTask.class)
    private String action;

    @NotNull(message = "Task Type is null", groups = ValidateCreateTask.class)
    private TaskType taskTypeCode;

    @Valid
    @NotEmpty(message = "Task URL JSON Object is null", groups = ValidateCreateTask.class)
    private List<TaskUrlDTO> taskUrlJsonObject;

    @JsonProperty("taskId")
    @NotNull(message = "Task ID is null", groups = ValidateRouteTask.class)
    private UUID id;

    @JsonProperty("taskName")
    @NotEmpty(message = "Task Name is null", groups = ValidateCreateTask.class)
    private String name;

    @JsonProperty("taskDescription")
    private String description;
    private String assignedUserId;
    private String remarkMessage;
    private String referenceNo;
    private String errorMessage;

    public enum TaskType{
        TECH_PROFILE("TECH_PROFILE"),
        CALENDAR("CALENDAR");
        private final String value;

        TaskType(final String newValue) {
            value = newValue;
        }

        public String getValue() { return value; }

        @JsonCreator
        public TaskType convertJSONtoTaskType(String value) {
            return TaskType.valueOf(value);
        }

        @JsonValue
        public String getTaskTypeJSON() {
            return value;
        }
    }
}