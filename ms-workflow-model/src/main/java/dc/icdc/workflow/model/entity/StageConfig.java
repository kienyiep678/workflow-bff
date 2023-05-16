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
@Table(name = "tbl_mt_stage_config", schema = "maintenance")
public class StageConfig {
    @Id
    @Column(name = "stage_cd")
    private String stageCode;

    @Column(name="stage_desc")
    private String stageDescription;

    @Column(name = "user_role_cd")
    private String userRoleCode;

    @Column(name = "is_active")
    private boolean isActive;

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