package dc.icdc.workflow.repository;

import dc.icdc.workflow.model.entity.StageConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StageConfigRepository extends JpaRepository<StageConfig, String> {

    @Query("SELECT s.stageCode FROM StageConfig s")
    List<String> findAllStageCodes();

    @Query("SELECT s FROM StageConfig s WHERE s.stageCode =:code")
    StageConfig findByCode(@Param("code") String code);

}