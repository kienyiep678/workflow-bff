package dc.icdc.workflow.repository;

import dc.icdc.workflow.model.entity.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskTypeRepository extends JpaRepository<TaskType, String> {
    @Query("SELECT s FROM TaskType s WHERE s.isDelete = false")
    List<TaskType> findActiveTask();
}
