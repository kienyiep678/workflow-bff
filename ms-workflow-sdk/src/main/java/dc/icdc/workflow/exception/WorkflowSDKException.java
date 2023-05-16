package dc.icdc.workflow.exception;

import lombok.Getter;

@Getter
public class WorkflowSDKException extends RuntimeException{

    public WorkflowSDKException(String errorMessage){
        super(errorMessage);
    }
}
