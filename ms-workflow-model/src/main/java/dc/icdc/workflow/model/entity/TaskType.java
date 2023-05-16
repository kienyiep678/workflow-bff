package dc.icdc.workflow.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_mt_task_typ", schema = "maintenance")
public class TaskType {
    @Id
    @Column(name = "cd", nullable = false)
    private String taskTypeCode;

    @Column(name = "name")
    private String taskName;

    @Column(name = "description")
    private String taskDescription;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_delete")
    private boolean isDelete;

    @Column(name = "created_by")
    private String createdBy;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "dt_created", columnDefinition = "TIMESTAMP")
    private LocalDateTime dateCreated;

    @Column(name = "updated_by")
    private String updatedBy;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "dt_updated", columnDefinition = "TIMESTAMP")
    private LocalDateTime dateUpdated;
}
