package dc.icdc.workflow.repository;

import dc.icdc.workflow.helper.CustomRepository;
import dc.icdc.workflow.model.entity.TaskItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface TaskItemRepository extends CustomRepository<TaskItem, UUID> {

    /* Query to find all tasks projected by MiniTaskItemDTO.*/
    @Query("SELECT e FROM TaskItem e")
    List<TaskItem> findTasks();

    /* Query to find all tasks that can be claimed.*/
    @Query("SELECT e FROM TaskItem e WHERE LOWER(e.name) LIKE CONCAT('%',LOWER(?1),'%') " +
            "AND LOWER(e.currentStageCode) LIKE CONCAT('%',LOWER(?2),'%') " +
            "AND LOWER(e.referenceNo) LIKE CONCAT('%',LOWER(?3),'%') " +
            "AND LOWER(e.taskTypeCode) LIKE CONCAT('%',LOWER(?4),'%') " +
            "AND e.currentHolderUserId IS NULL AND e.isEndStage = FALSE")
    List<TaskItem> getPool(String taskName , String status , String referenceNo  , String taskType, Pageable pageable);

    /* Query to get number of tasks that can be claimed.*/
    @Query("SELECT COUNT(e) FROM TaskItem e WHERE LOWER(e.name) LIKE CONCAT('%',LOWER(?1),'%') " +
            "AND LOWER(e.currentStageCode) LIKE CONCAT('%',LOWER(?2),'%') " +
            "AND LOWER(e.referenceNo) LIKE CONCAT('%',LOWER(?3),'%') " +
            "AND LOWER(e.taskTypeCode) LIKE CONCAT('%',LOWER(?4),'%') " +
            "AND e.currentHolderUserId IS NULL AND e.isEndStage = FALSE")
    int countInTaskPool(String taskName , String status, String referenceNo, String taskType);

    /* Query to get tasks by name, status, task type or reference number.*/
    @Query("SELECT e FROM TaskItem e WHERE LOWER(e.name) LIKE CONCAT('%',LOWER(?1),'%') " +
            "AND LOWER(e.currentStageCode) LIKE CONCAT('%',LOWER(?2),'%') " +
            "AND LOWER(e.referenceNo) LIKE CONCAT('%',LOWER(?3),'%') " +
            "AND LOWER(e.taskTypeCode) LIKE CONCAT('%',LOWER(?4),'%')")
    List<TaskItem> findByNameStatusReferenceNo(String taskName , String status , String referenceNo , String taskType , Pageable pageable);

    /* Query to get number of tasks by name, status, task type or reference number.*/
    @Query("SELECT COUNT(e) FROM TaskItem e WHERE LOWER(e.name) LIKE CONCAT('%',LOWER(?1),'%') " +
            "AND LOWER(e.currentStageCode) LIKE CONCAT('%',LOWER(?2),'%') " +
            "AND LOWER(e.referenceNo) LIKE CONCAT('%',LOWER(?3),'%') " +
            "AND LOWER(e.taskTypeCode) LIKE CONCAT('%',LOWER(?4),'%') ")
    int countInFindByNameStatusReferenceNo(String taskName , String status, String referenceNo, String taskType);

    /* Query to get list of tasks by a current holder's user id.*/
    @Query("SELECT e FROM TaskItem e WHERE e.currentHolderUserId = ?1 " +
            "AND LOWER(e.name) LIKE CONCAT('%',LOWER(?2),'%') " +
            "AND LOWER(e.currentStageCode) LIKE CONCAT('%',LOWER(?3),'%') " +
            "AND LOWER(e.referenceNo) LIKE CONCAT('%',LOWER(?4),'%') " +
            "AND LOWER(e.taskTypeCode) LIKE CONCAT('%',LOWER(?5),'%') " +
            "AND e.isEndStage = FALSE")
    List<TaskItem> findByCurrentHolderUserId(String holderId, String taskName , String status , String referenceNo , String taskType , Pageable pageable);

    /* Query to get number of tasks by a current holder's user id.*/
    @Query("SELECT COUNT(e) FROM TaskItem e WHERE e.currentHolderUserId = ?1 " +
            "AND LOWER(e.name) LIKE CONCAT('%',LOWER(?2),'%') " +
            "AND LOWER(e.currentStageCode) LIKE CONCAT('%',LOWER(?3),'%') " +
            "AND LOWER(e.referenceNo) LIKE CONCAT('%',LOWER(?4),'%') " +
            "AND LOWER(e.taskTypeCode) LIKE CONCAT('%',LOWER(?5),'%') " +
            "AND e.isEndStage = FALSE")
    int countInFindByCurrentHolderUserId(String holderId, String taskName , String status , String referenceNo , String taskType);

    @Query("SELECT DISTINCT e.currentStageCode FROM TaskItem e")
    List<String> findAllExistStageCode();
    @Query("SELECT DISTINCT e.currentStageCode FROM TaskItem e WHERE e.currentHolderUserId IS NULL AND e.isEndStage = FALSE")
    List<String> findAllExistStageCodePool();
    @Query("SELECT DISTINCT e.currentStageCode FROM TaskItem e WHERE e.currentHolderUserId = ?1 " +
            "AND e.isEndStage = FALSE")
    List<String> findAllExistStageCodeByCurrentHolder(String holderId);

    @Query("SELECT DISTINCT e.taskTypeCode FROM TaskItem e")
    List<String> findAllExistTypeCode();
    @Query("SELECT DISTINCT e.taskTypeCode FROM TaskItem e WHERE e.currentHolderUserId IS NULL AND e.isEndStage = FALSE")
    List<String> findAllExistTypeCodePool();
    @Query("SELECT DISTINCT e.taskTypeCode FROM TaskItem e WHERE e.currentHolderUserId = ?1 " +
            "AND e.isEndStage = FALSE")
    List<String> findAllExistTypeCodeByCurrentHolder(String holderId);

    public Optional<TaskItem> findByCamundaProcessInstanceId(String camundaProcessInstanceId);
    public Optional<TaskItem> findById(String id);
}