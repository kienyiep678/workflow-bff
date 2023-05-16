package dc.icdc.workflow.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dc.icdc.workflow.helper.WorkflowConstant;
import dc.icdc.workflow.model.dto.SimplifiedTaskDTO;
import dc.icdc.workflow.model.dto.TaskDetailDTO;
import dc.icdc.workflow.model.dto.TaskRouteDTO;
import dc.icdc.workflow.model.dto.TaskUrlDTO;
import dc.icdc.workflow.model.entity.TaskItem;
import dc.icdc.workflow.model.entity.TaskType;
import dc.icdc.workflow.model.entity.TaskUrl;
import dc.icdc.workflow.repository.TaskItemRepository;
import dc.icdc.workflow.repository.TaskTypeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskListServiceTest {

    public static final String YANDEV = "Yandev";
    public static final String TECH_PROFILE = "TECH_PROFILE";
    @Mock
    private TaskItemRepository taskItemRepository;

    @Mock
    private TaskTypeRepository taskTypeRepository;

    @InjectMocks
    private TaskListService taskListService;

    // Create a Pageable object with the appropriate parameters
    private static Pageable pageable;

    private static List<TaskItem> taskItemList;

    @BeforeAll
    private static void initObj()
    {
        pageable = PageRequest.of(0, 10);
        List<TaskUrl> urlJson = Arrays.asList(
                TaskUrl.builder().url("http://localhost:8083/task/testget").urlType(TaskUrlDTO.TaskUrlType.APPROVE_URL.getValue()).method(HttpMethod.GET).build(),
                TaskUrl.builder().url("http://localhost:8083/task/testpost").urlType(TaskUrlDTO.TaskUrlType.REJECT_URL.getValue()).method(HttpMethod.POST).build(),
                TaskUrl.builder().url("http://localhost:8083/task/testview").urlType(TaskUrlDTO.TaskUrlType.VIEW_URL.getValue()).method(HttpMethod.GET).build()
        );
        taskItemList = Arrays.asList(
                TaskItem.builder().id(UUID.randomUUID()).camundaProcessInstanceId(UUID.randomUUID().toString())
                        .referenceNo(String.valueOf(1000001)).name("Create Technical Profile -- 1")
                        .description("Create technical profile for personal 1.")
                        .taskUrlJsonObject(urlJson).currentStageCode(WorkflowConstant.WorkflowStageCd.PENDING_APPROVAL)
                        .taskTypeCode(TECH_PROFILE).currentHolderUserId(YANDEV).build(),
                TaskItem.builder().id(UUID.randomUUID()).camundaProcessInstanceId(UUID.randomUUID().toString())
                        .referenceNo(String.valueOf(1000002)).name("Create Technical Profile -- 2")
                        .description("Create technical profile for personal 2.")
                        .taskUrlJsonObject(urlJson).currentStageCode(WorkflowConstant.WorkflowStageCd.PENDING_APPROVAL)
                        .taskTypeCode(TECH_PROFILE).currentHolderUserId(YANDEV).build(),
                TaskItem.builder().id(UUID.randomUUID()).camundaProcessInstanceId(UUID.randomUUID().toString())
                        .referenceNo(String.valueOf(1000002)).name("Create Technical Profile --3")
                        .description("Create technical profile for personal 3.")
                        .taskUrlJsonObject(urlJson).currentStageCode(WorkflowConstant.WorkflowStageCd.TASK_CREATION)
                        .taskTypeCode(TECH_PROFILE).currentHolderUserId(YANDEV).build()
        );
        List<TaskUrlDTO> taskUrlDTO = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        TaskDetailDTO taskDetailDTO = TaskDetailDTO.builder()
                .id(UUID.randomUUID())
                .referenceNo("RF10000010")
                .taskTypeCode(TECH_PROFILE)
                .currentHolderUserId(YANDEV)
                .name("Create technical profile -- 10")
                .description("Create technical profile for person 10 by yandev")
               // .taskUrlJsonObject(mapper.convertValue(urlJson,TaskUrlDTO.class))

                .build();
    }

    @BeforeEach
    private void init()
    {



//        when(taskItemRepository.findAll()).thenReturn(
//                Arrays.asList(
//                        TaskItem.builder().id(UUID.randomUUID()).camundaProcessInstanceId(UUID.randomUUID().toString())
//                                .referenceNo(String.valueOf(1000001)).name("Create Technical Profile -- 1")
//                                .description("Create technical profile for personal 1.")
//                                .taskUrlJsonObject(urlJson).currentStageCode(WorkflowConstant.WorkflowStageCd.PENDING_APPROVAL)
//                                .taskTypeCode(TECH_PROFILE.getValue()).currentHolderUserId(YANDEV).build(),
//                        TaskItem.builder().id(UUID.randomUUID()).camundaProcessInstanceId(UUID.randomUUID().toString())
//                                .referenceNo(String.valueOf(1000002)).name("Create Technical Profile -- 2")
//                                .description("Create technical profile for personal 2.")
//                                .taskUrlJsonObject(urlJson).currentStageCode(WorkflowConstant.WorkflowStageCd.PENDING_APPROVAL)
//                                .taskTypeCode(TECH_PROFILE.getValue()).currentHolderUserId(YANDEV).build(),
//                        TaskItem.builder().id(UUID.randomUUID()).camundaProcessInstanceId(UUID.randomUUID().toString())
//                                .referenceNo(String.valueOf(1000002)).name("Create Technical Profile --3")
//                                .description("Create technical profile for personal 3.")
//                                .taskUrlJsonObject(urlJson).currentStageCode(WorkflowConstant.WorkflowStageCd.TASK_CREATION)
//                                .taskTypeCode(TECH_PROFILE.getValue()).currentHolderUserId(YANDEV).build()
//
//                )
//        );
//
//        when(taskItemRepository.getPool("Creating Technical Profile"
//                , WorkflowConstant.WorkflowStageCd.PENDING_APPROVAL
//                ,"TF99999999"
//                ,"TECH_PROFILE"
//                ,pageable)).thenReturn(
//                Arrays.asList(
//                        TaskItem.builder().id(UUID.randomUUID()).camundaProcessInstanceId(UUID.randomUUID().toString())
//                                .referenceNo(String.valueOf(1000001)).name("Create Technical Profile -- 1")
//                                .description("Create technical profile for personal 1.")
//                                .taskUrlJsonObject(urlJson).currentStageCode(WorkflowConstant.WorkflowStageCd.PENDING_APPROVAL)
//                                .taskTypeCode(TECH_PROFILE.getValue()).currentHolderUserId(YANDEV).build(),
//                        TaskItem.builder().id(UUID.randomUUID()).camundaProcessInstanceId(UUID.randomUUID().toString())
//                                .referenceNo(String.valueOf(1000002)).name("Create Technical Profile -- 2")
//                                .description("Create technical profile for personal 2.")
//                                .taskUrlJsonObject(urlJson).currentStageCode(WorkflowConstant.WorkflowStageCd.PENDING_APPROVAL)
//                                .taskTypeCode(TECH_PROFILE.getValue()).currentHolderUserId(YANDEV).build(),
//                        TaskItem.builder().id(UUID.randomUUID()).camundaProcessInstanceId(UUID.randomUUID().toString())
//                                .referenceNo(String.valueOf(1000002)).name("Create Technical Profile --3")
//                                .description("Create technical profile for personal 3.")
//                                .taskUrlJsonObject(urlJson).currentStageCode(WorkflowConstant.WorkflowStageCd.TASK_CREATION)
//                                .taskTypeCode(TECH_PROFILE.getValue()).currentHolderUserId(YANDEV).build()
//
//                )
//        );
//
//        when(taskItemRepository.countInTaskPool("Creating Technical Profile"
//                , WorkflowConstant.WorkflowStageCd.PENDING_APPROVAL
//                ,"TF99999999"
//                ,"TECH_PROFILE"
//        )).thenReturn(3);


    }

    @Test
    void testGetContentByTaskId() throws Exception {

        when(taskItemRepository.findById(any(UUID.class))).thenReturn(
                Optional.of(TaskItem.builder().id(UUID.randomUUID()).taskTypeCode("TECH_PROFILE").actionType(0).referenceNo(String.valueOf(1000000)).currentStageCode(WorkflowConstant.WorkflowStageCd.PENDING_APPROVAL).build())
        );
        when(taskTypeRepository.findById(any(String.class))).thenReturn(
                Optional.of(TaskType.builder().taskTypeCode(TECH_PROFILE).taskName("Technical Profile").isActive(true).build())
        );
        TaskDetailDTO contentByTaskId = taskListService.getContentByTaskId("fc3d1eb7-f605-4d90-b072-8c1be65ff757", false);
        verify(taskItemRepository).findById(any(UUID.class));


    }

//    @Test
//    void getListOfTasks() {
//    }
//
//    @Test
//    void calcCountOfPages() {
//    }
//
//    @Test
//    void getSortByAbbreviation() {
//    }
//
//    @Test
//    void getListOfStage() {
//    }
//
//    @Test
//    void getListOfType() {
//    }
}

//@RunWith(MockitoJUnitRunner.class)
//class TaskListServiceTest {
//
//    @Mock
//    private TaskItemRepository myRepository;
//
//    @Mock
//    TaskTypeRepository taskTypeRepository;
//
//    @InjectMocks
//    private TaskListService myService;
//
//    @BeforeEach
//    void init()
//    {
//        myRepository = Mockito.mock(TaskItemRepository.class);
//        taskTypeRepository = Mockito.mock(TaskTypeRepository.class);
//        myService = Mockito.mock(TaskListService.class);
//    }
//
//    @Test
//    void testMyServiceMethod() {
//        // Set up the mock behavior for the repository method
//
//        //when(taskItemRepository.findById(any(String.class))).thenReturn(
////                Optional.of(TaskItem.builder().id(UUID.randomUUID()).actionType(0).referenceNo(String.valueOf(1000000)).build())
////        );
////        when(taskTypeRepository.findById(any(String.class))).thenReturn(
////                Optional.of(TaskType.builder().taskTypeCode("TECH_PROFILE").taskName("Technical Profile").isActive(true).build())
////        );
//        when(myRepository.findById("1L")).thenReturn(Optional.of(TaskItem.builder().id(UUID.randomUUID()).actionType(0).referenceNo(String.valueOf(1000000)).build()));
//
//
//        when(taskTypeRepository.findById(any(String.class))).thenReturn(
//                Optional.of(TaskType.builder().taskTypeCode("TECH_PROFILE").taskName("Technical Profile").isActive(true).build())
//        );
//
//        when(myService.getContentByTaskId("sad" , false)).thenReturn(
//                new TaskDetailDTO()
//        );
//
//        // Call the method under test
//        TaskDetailDTO result = myService.getContentByTaskId("sad" , false);
//
//
//
//    }
//}
