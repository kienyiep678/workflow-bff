package dc.icdc.workflow.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_task_history" )
public class TaskHistory {

    // column definition - UUID , make sure database recognize it as uuid
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "task_history_id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID taskHistoryId;

    @Column(name = "task_id", columnDefinition = "uuid")
    private UUID taskId;

    @Column(name = "action_name")
    private String actionCode;

    @Column(name = "stage_from")
    private String stageFrom;

    @Column(name = "stage_to")
    private String stageTo;

    @Column(name = "holder_from_user_id")
    private String holderFromUserId;

    @Column(name = "holder_to_user_id")
    private String holderToUserId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "dt_start" , columnDefinition = "TIMESTAMP")
    private LocalDateTime dateStart;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "dt_end" , columnDefinition = "TIMESTAMP")
    private LocalDateTime dateEnd;
}