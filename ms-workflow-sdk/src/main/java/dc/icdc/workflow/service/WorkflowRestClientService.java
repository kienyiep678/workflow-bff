package dc.icdc.workflow.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dc.icdc.workflow.constant.WorkflowSDKConstant;
import dc.icdc.workflow.exception.WorkflowSDKException;
import dc.icdc.workflow.model.dto.TaskRouteDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
@PropertySource("classpath:workflow-sdk.properties")
public class WorkflowRestClientService {

    @Autowired
    private Validator validator;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private Environment env;

    public String createWorkflowTaskFromSystem(TaskRouteDTO taskCreateRequestDTO) throws WorkflowSDKException, JsonProcessingException {

        taskCreateRequestDTO.setTaskCreateType(WorkflowSDKConstant.General.CREATE_BY_SYSTEM);
        Set<ConstraintViolation<TaskRouteDTO>> violations = validator.validate(taskCreateRequestDTO);

        ObjectMapper mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<TaskRouteDTO> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage() + "; ");
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TaskRouteDTO> entity = new HttpEntity<>(taskCreateRequestDTO, headers);

        String url = env.getProperty("rest.service.workflow.createtask.url");
        String method = env.getProperty("rest.service.workflow.createtask.method");

        if ((url != null && !url.isEmpty()) && (method != null && !method.isEmpty())) {

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.valueOf(method), entity, String.class);

            if (response.getStatusCode() == HttpStatusCode.valueOf(200)) {

                JsonNode jsonResult = mapper.readTree(response.getBody());
                String taskReferenceNo = jsonResult.get("data").get("taskReferenceNo").asText();

                if (taskReferenceNo != null && !taskReferenceNo.isEmpty()) {
                    return taskReferenceNo;
                } else {
                    throw new WorkflowSDKException("Task reference No is empty.");
                }

            } else {
                throw new WorkflowSDKException(String.format("Error in executing API, Status Code: %s.", response.getStatusCode().toString()));
            }
        } else {
            throw new WorkflowSDKException("API Configuration missing from properties file.");
        }
    }
}