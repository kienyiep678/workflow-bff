package dc.icdc.workflow.service;

import dc.icdc.workflow.model.dto.WorkflowDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkflowDataCacheServiceTest {

    @Mock
    private WorkflowDataService workflowDataService;
    @InjectMocks
    private WorkflowDataCacheService workflowDataCacheService;

    private WorkflowDataCacheService workflowDataCacheServiceSpy;

    private static List<WorkflowDTO.WorkflowStage> mockWorkflowStageList;

    private static List<WorkflowDTO.WorkflowStage> mockWorkflowStageListWithoutAction;

    private static List<WorkflowDTO> mockedList;


    @BeforeAll
    static void setup(){

        mockedList = new ArrayList<>();
        //workflowDataCacheServiceSpy = Mockito.spy(workflowDataCacheService);

        WorkflowDTO.WorkflowStage.WorkflowStageAction mockActionSubmit = new WorkflowDTO.WorkflowStage.WorkflowStageAction("Submit For Approval", "SUBMIT");
        WorkflowDTO.WorkflowStage.WorkflowStageAction mockActionCancel = new WorkflowDTO.WorkflowStage.WorkflowStageAction("Cancel", "CANCEL");
        List<WorkflowDTO.WorkflowStage.WorkflowStageAction> mockWorkflowStageActionList = new ArrayList<>();
        mockWorkflowStageActionList.add(mockActionSubmit);
        mockWorkflowStageActionList.add(mockActionCancel);
        WorkflowDTO.WorkflowStage mockTaskCreationStage = new WorkflowDTO.WorkflowStage("Task Creation", "TASK_CREATION", mockWorkflowStageActionList);

        WorkflowDTO.WorkflowStage.WorkflowStageAction mockActionApprove = new WorkflowDTO.WorkflowStage.WorkflowStageAction("Approve", "APPROVE");
        WorkflowDTO.WorkflowStage.WorkflowStageAction mockActionReject = new WorkflowDTO.WorkflowStage.WorkflowStageAction("Reject", "REJECT");
        List<WorkflowDTO.WorkflowStage.WorkflowStageAction> mockWorkflowStageActionList2 = new ArrayList<>();
        mockWorkflowStageActionList2.add(mockActionApprove);
        mockWorkflowStageActionList2.add(mockActionReject);
        WorkflowDTO.WorkflowStage mockPendingApprovalStage = new WorkflowDTO.WorkflowStage("Pending Approval", "PENDING_APPROVAL", mockWorkflowStageActionList2);

        mockWorkflowStageList = new ArrayList<>();
        mockWorkflowStageList.add(mockTaskCreationStage);
        mockWorkflowStageList.add(mockPendingApprovalStage);

        WorkflowDTO mockWorkflowDTO = new WorkflowDTO(1, mockWorkflowStageList);

        mockedList.add(mockWorkflowDTO);

        WorkflowDTO.WorkflowStage mockTaskCreationStageWithoutAction = new WorkflowDTO.WorkflowStage("Task Creation", "TASK_CREATION", null);
        WorkflowDTO.WorkflowStage mockPendingApprovalStageWithoutAction = new WorkflowDTO.WorkflowStage("Pending Approval", "PENDING_APPROVAL", null);

        mockWorkflowStageListWithoutAction = new ArrayList<>();
        mockWorkflowStageListWithoutAction.add(mockTaskCreationStageWithoutAction);
        mockWorkflowStageListWithoutAction.add(mockPendingApprovalStageWithoutAction);
    }

//    @Test
//    void getCachedListOfFullStageOfEveryVersion() {
//    }
//
//    @Test
//    void getCachedListOfFullStageNameOnly() {
//    }

    @Test
    void getFullStageListPerVersionTest() {
        workflowDataCacheServiceSpy = Mockito.spy(workflowDataCacheService);
        when(workflowDataCacheServiceSpy.getCachedListOfFullStageOfEveryVersion()).thenReturn(mockedList);

        List<WorkflowDTO.WorkflowStage> actualResultList = assertDoesNotThrow(() ->workflowDataCacheServiceSpy.getFullStageListPerVersion(1));
        assertIterableEquals(mockWorkflowStageList, actualResultList);
    }

    @Test
    void getActionListPerStage() {
        workflowDataCacheServiceSpy = Mockito.spy(workflowDataCacheService);
        when(workflowDataCacheServiceSpy.getCachedListOfFullStageOfEveryVersion()).thenReturn(mockedList);

        WorkflowDTO.WorkflowStage expectedResult = mockWorkflowStageList.stream().filter(x -> x.stageCode.equals("TASK_CREATION")).findFirst().get();
        WorkflowDTO.WorkflowStage actualResult = assertDoesNotThrow(() -> workflowDataCacheServiceSpy.getActionListPerStage("TASK_CREATION", 1));
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getStageNameByCode() {
        workflowDataCacheServiceSpy = Mockito.spy(workflowDataCacheService);
        when(workflowDataCacheServiceSpy.getCachedListOfFullStageNameOnly()).thenReturn(mockWorkflowStageListWithoutAction);
        String expectedResult = mockWorkflowStageListWithoutAction.stream().filter(x -> x.stageCode.equals("TASK_CREATION")).findFirst().get().getStageName();
        String actualResult = assertDoesNotThrow(() -> workflowDataCacheServiceSpy.getStageNameByCode("TASK_CREATION"));
        assertEquals(expectedResult, actualResult);
    }
}