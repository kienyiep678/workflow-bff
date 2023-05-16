package dc.icdc.workflow.service.camunda;

import dc.icdc.workflow.helper.WorkflowConstant;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named
@Slf4j
public class ApprovalServiceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        try {
            boolean result = false;
            Integer numberOfApproval = (Integer) delegateExecution.getVariable(WorkflowConstant.WorkflowVariableKey.NUMBER_OF_APPROVAL);


            Integer currentApproval = (Integer) delegateExecution.getVariable(WorkflowConstant.WorkflowVariableKey.CURRENT_APPROVAL);
            currentApproval += 1;


            if(currentApproval.equals(numberOfApproval)) {
                result = true;
            }

            delegateExecution.setVariable(WorkflowConstant.WorkflowVariableKey.APPROVAL_MATRIX_RESULT, result);
            delegateExecution.setVariable(WorkflowConstant.WorkflowVariableKey.CURRENT_APPROVAL, currentApproval);

        }
        catch (Exception e) {

            log.error("Process Instance id: " + delegateExecution.getProcessInstanceId() + ", error: " + e.getMessage());
            throw e;
        }

    }
}

