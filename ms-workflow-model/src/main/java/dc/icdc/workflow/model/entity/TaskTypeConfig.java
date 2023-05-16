package dc.icdc.workflow.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_mt_task_typ_config", schema = "maintenance")
public class TaskTypeConfig {

    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID taskTypeConfigId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mt_task_typ_cd")
    private TaskType taskType;

    @Column(name = "num_of_approver")
    private int numberOfApproval;

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
