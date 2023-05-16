package dc.icdc.workflow.service.camunda;

import com.fasterxml.jackson.databind.ObjectMapper;
import dc.icdc.workflow.helper.WorkflowConstant;
import dc.icdc.workflow.model.dto.TaskUrlDTO;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.inject.Named;
import java.util.List;
import java.util.Optional;

@Named
@Slf4j
public class ExecutionServiceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        try{
            String taskDecisionVal = (String) delegateExecution.getVariable(WorkflowConstant.WorkflowVariableKey.TASK_DECISION);
            String strJSON = (String) delegateExecution.getVariable(WorkflowConstant.WorkflowVariableKey.TASK_JSON);

            if(taskDecisionVal == null || taskDecisionVal.isEmpty()){
                taskDecisionVal = (String) delegateExecution.getVariable(WorkflowConstant.WorkflowVariableKey.ACTION);
                delegateExecution.setVariable(WorkflowConstant.WorkflowVariableKey.TASK_DECISION, taskDecisionVal);
            }

            ObjectMapper mapper = new ObjectMapper();

            List<TaskUrlDTO> jsonObject = mapper.readValue(strJSON , mapper.getTypeFactory().constructCollectionType(List.class, TaskUrlDTO.class));

            Optional<TaskUrlDTO> result;

            if(taskDecisionVal.equals(WorkflowConstant.WorkflowAction.APPROVE)){
                result = jsonObject.stream().filter(x -> x.getUrlType() == TaskUrlDTO.TaskUrlType.APPROVE_URL).findFirst();
            } else {
                result = jsonObject.stream().filter(x -> x.getUrlType() == TaskUrlDTO.TaskUrlType.REJECT_URL).findFirst();
            }
            if(result.isPresent()) {

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                String jsonBody = null;
                if (result.get().getData() != null) {
                    jsonBody = mapper.writeValueAsString(result.get().getData());
                }
                HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
                ResponseEntity<String> response = restTemplate.exchange(result.get().getUrl(), result.get().getMethod(), entity, String.class);

                if (response.getStatusCode() != HttpStatus.OK) {
                    throw new BpmnError(WorkflowConstant.CamundaKey.PROCESS_EXECUTION_EXCEPTION_ERROR, String.format("Error in executing api: Status Code: %s", response.getStatusCode()));
                }
            } else {
                if(taskDecisionVal.equals(WorkflowConstant.WorkflowAction.APPROVE)) {
                    throw new BpmnError(WorkflowConstant.CamundaKey.PROCESS_EXECUTION_EXCEPTION_ERROR, String.format("Error in executing api: Url is not found. Type: %s", TaskUrlDTO.TaskUrlType.APPROVE_URL.getValue()));
                }
            }



        }
        catch (BpmnError bp){
            log.error("Process Instance id: " + delegateExecution.getProcessInstanceId() + ", error: " + bp.getMessage());
            throw bp;
        }
        catch (Exception e){

            log.error("Process Instance id: " + delegateExecution.getProcessInstanceId() + ", error: " + e.getMessage());
            throw new BpmnError(WorkflowConstant.CamundaKey.PROCESS_EXECUTION_EXCEPTION_ERROR, "Error in executing api: " + e.getMessage());

        }

    }
}

