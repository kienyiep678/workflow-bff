package dc.icdc.workflow.repository;

import dc.icdc.workflow.model.entity.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskHistoryRepository extends JpaRepository<TaskHistory, UUID> {

    /* Query to get list of task history by task id in ascending order.*/
    @Query("SELECT h FROM TaskHistory h WHERE h.taskId = ?1 ORDER BY h.dateStart , h.dateEnd ASC")
    List<TaskHistory> findByTaskId(UUID taskId);
}
