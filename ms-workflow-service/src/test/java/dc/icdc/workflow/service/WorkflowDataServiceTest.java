package dc.icdc.workflow.service;

import dc.icdc.workflow.helper.WorkflowConstant;
import dc.icdc.workflow.model.dto.WorkflowDTO;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.history.event.HistoricActivityInstanceEventEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WorkflowDataServiceTest {

    @Autowired
    private WorkflowDataService workflowDataService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    private String processInstanceId;

    @BeforeEach
    void setupTestCase(){

        Map<String, Object> variables = null;
        variables = new HashMap<>();
        variables.put(WorkflowConstant.WorkflowVariableKey.TASK_NAME, "Test Task Name");
        variables.put(WorkflowConstant.WorkflowVariableKey.TASK_DESCRIPTION, "Test Task Description");
        variables.put(WorkflowConstant.WorkflowVariableKey.TASK_TYPE, "Test Task Type");
        //variables.put(WorkflowConstant.WorkflowVariableKey.TASK_JSON, null);
//        variables.put(WorkflowConstant.WorkflowVariableKey.ACTION, "");
//        variables.put(WorkflowConstant.WorkflowVariableKey.APPROVAL_MATRIX_RESULT, false);
//        variables.put(WorkflowConstant.WorkflowVariableKey.NUMBER_OF_APPROVAL, 1);
//        variables.put(WorkflowConstant.WorkflowVariableKey.CURRENT_APPROVAL, 0);
//        variables.put(WorkflowConstant.WorkflowVariableKey.TASK_DECISION, "");

        // Start the process instance and set some variables
        processInstanceId = runtimeService.startProcessInstanceByKey(WorkflowConstant.General.MAKER_CHECKER_WORKFLOW_KEY, variables).getId();

//        // Wait for the process instance to complete
//        assertThat(processInstance(processInstanceId)).isEnded();
//
//        // Verify that the task service was called
//        verify(taskService, times(1)).complete(any());

        // Delete the process instance
//        runtimeService.deleteProcessInstance(processInstanceId, "test completed");
    }

    @AfterEach
    void removeTestCase(){
        // Delete the process instance
        runtimeService.deleteProcessInstance(processInstanceId, "Test Completed");
        historyService.deleteHistoricProcessInstance(processInstanceId);
    }

    @Test
    void getWorkflowDataForEveryVersionTest() {
        List<WorkflowDTO> workflowDTOList = workflowDataService.getWorkflowDataForEveryVersion();
        assertNotNull(workflowDTOList);
    }

    @Test
    void getLatestStageActivityInstanceTest() {
        HistoricActivityInstanceEventEntity activityInstance = workflowDataService.getLatestStageActivityInstance(processInstanceId);
        assertEquals("TASK_CREATION", activityInstance.getActivityId());
    }

    @Test
    void getTaskVersionByProcessInstanceIdTest() {
        // To test the retrieved version for a specific task should be at least version 1
        int version = workflowDataService.getTaskVersionByProcessInstanceId(processInstanceId);
        assertNotEquals(0, version);
    }

    @Test
    void getTaskVariableTest() {
        // To test the retrieved variable is correct
        String taskNameTest = (String)workflowDataService.getTaskVariable(processInstanceId, WorkflowConstant.WorkflowVariableKey.TASK_NAME);
        assertEquals("Test Task Name", taskNameTest);
    }

    @Test
    void getLatestVersionTest() {
        // To test the retrieved latest version of the workflow process should be at least version 1
        int version = workflowDataService.getLatestVersion();
        assertNotEquals(0, version);
    }
}