package dc.icdc.workflow.helper;

public class WorkflowConstant {

    private WorkflowConstant(){
        // private constructor to prevent instantiation
    }

    public static class General{

        private General(){
            // private constructor to prevent instantiation
        }
        public static final String MAKER_CHECKER_WORKFLOW_KEY = "MAKER_CHECKER_FLOW";

    }

    public static class WorkflowVariableKey{

        private WorkflowVariableKey(){
            // private constructor to prevent instantiation
        }
        public static final String TASK_NAME = "TASK_NAME";
        public static final String TASK_DESCRIPTION = "TASK_DESC";
        public static final String TASK_TYPE = "TASK_TYPE";
        public static final String TASK_JSON = "TASK_JSON";
        public static final String ACTION = "ACTION";
        public static final String APPROVAL_MATRIX_RESULT = "APPROVAL_MATRIX_RESULT";
        public static final String NUMBER_OF_APPROVAL = "NUMBER_OF_APPROVAL";
        public static final String CURRENT_APPROVAL = "CURRENT_APPROVAL";

        public static final String TASK_DECISION = "TASK_DECISION";
    }

    public static class WorkflowAction{

        private WorkflowAction(){
            // private constructor to prevent instantiation
        }

        public static final String SUBMIT_FOR_APPROVAL = "SUBMIT";
        public static final String APPROVE = "APPROVE";
        public static final String REJECT = "REJECT";
        public static final String CANCEL = "CANCEL";

        public static final String RETURN_FROM_APPROVAL = "RETURN_FROM_APPROVAL";

        public static final String RETURN_FROM_EXCEPTION = "RETURN_FROM_EXCEPTION";

        public static final String CLAIM = "CLAIM";


    }

    public static class WorkflowStageCd {

        private WorkflowStageCd(){
            // private constructor to prevent instantiation
        }
        public static final String PENDING_APPROVAL = "PENDING_APPROVAL";
        public static final String TASK_CREATION = "TASK_CREATION";

        public static final String PROCESS_EXCEPTION = "PROCESS_EXCEPTION";


    }

    public static class CamundaKey{

        private CamundaKey(){
            // private constructor to prevent instantiation
        }
        public static final String PROCESS_EXECUTION_EXCEPTION_ERROR = "EXECUTION_EXCEPTION_ERROR";

        public static final String PROCESS_EXECUTION_EXCEPTION_ERROR_MESSAGE = "EXECUTION_EXCEPTION_ERROR_MSG";

    }


    public enum TaskActionType{
        APPROVE_TASK(1),
        REJECT_TASK(2),
        CANCEL_TASK(3);
        private final int value;

        TaskActionType(final int newValue) {
            value = newValue;
        }

        public int getValue() { return value; }
    }

    public static class TaskApiKey{

        private TaskApiKey(){
            // private constructor to prevent instantiation
        }

        // General Task Constant
        public static final String TASK_REFERENCE_NO = "taskReferenceNo";
        public static final String TASK_STAGE_NAME = "taskStageName";

        // additional constants
        public static final String NOT_ASSIGNED_USER = "Not Assigned";
        public static final String SYSTEM_CREATE = "SYSTEM";
        public static final String ACTION = "action";
        public static final String NOT_FOUND = "NOT FOUND";
        public static final String POOL = "pool";
        public static final String PERSONAL_LIST = "personal-list";
        public static final String SEARCH = "search";
        public static final String ROUTE_FLAG = "routeFlag";
        public static final String CLAIM_ACTION_NAME = "Claim";
    }

    public static class TaskTypeApiKey {

        private TaskTypeApiKey(){
            // private constructor to prevent instantiation
        }
        public static final String RESPONSE = "Response";
        public static final String DATA_CODE = "Data code";
        public static final String TASK_CODE_NOT_FOUND = "Task type code not found.";
    }

}
