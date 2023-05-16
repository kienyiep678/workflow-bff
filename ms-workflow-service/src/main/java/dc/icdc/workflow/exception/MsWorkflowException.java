package dc.icdc.workflow.exception;

import dc.icdc.lib.common.exception.BaseException;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

@Getter
public class MsWorkflowException extends BaseException {


    public MsWorkflowException(String errorCode, String errorMessage,  List<Serializable> errorDetails, HttpStatus status)
    {
        super(errorCode, errorMessage, errorDetails, status);
    }

    public MsWorkflowException(String errorCode, String errorMessage, HttpStatus status)
    {
        super(errorCode, errorMessage, null, status);
    }

}
