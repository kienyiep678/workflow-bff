package dc.icdc.workflow.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_task" )
@TypeDef(name = "json", typeClass = JsonStringType.class)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class TaskItem {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "cmd_proc_inst_id")
    private String camundaProcessInstanceId;

    @Column(name = "ref_no")
    private String referenceNo;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Type(type = "jsonb")
    @Column(name = "url_json_obj", columnDefinition = "jsonb")
    private List<TaskUrl> taskUrlJsonObject;

    @Column(name = "cur_stage_cd")
    private String currentStageCode;

    @Column(name = "mt_task_typ_cd")
    private String taskTypeCode;

    @Column(name = "cur_holder_user_id")
    private String currentHolderUserId;

    @Column(name = "action_by")
    private String actionBy;

    @Column(name = "action_typ")
    private Integer actionType;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "dt_action", columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAction;

    @Column(name = "updated_by")
    private String updatedBy;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "dt_updated", columnDefinition = "TIMESTAMP")
    private LocalDateTime dateUpdated;

    @Column(name = "created_by")
    private String createdBy;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "dt_created", columnDefinition = "TIMESTAMP")
    private LocalDateTime dateCreated;

    @Column(name = "remark_msg")
    private String remarkMessage;

    @Column(name = "is_end_stage")
    private boolean isEndStage;

    @Column(name = "err_msg")
    private String errorMessage;
}