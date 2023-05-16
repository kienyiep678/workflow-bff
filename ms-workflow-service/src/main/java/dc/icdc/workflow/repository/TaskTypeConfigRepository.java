package dc.icdc.workflow.repository;

import dc.icdc.workflow.model.entity.TaskTypeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskTypeConfigRepository extends JpaRepository<TaskTypeConfig, UUID> {
    @Query("SELECT s FROM TaskTypeConfig s WHERE s.taskType.taskTypeCode =:code")
    TaskTypeConfig findByCode(@Param("code") String code);

}
