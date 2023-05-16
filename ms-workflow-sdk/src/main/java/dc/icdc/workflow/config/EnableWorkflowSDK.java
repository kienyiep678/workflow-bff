package dc.icdc.workflow.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({WorkflowSDKConfig.class})
public @interface EnableWorkflowSDK {
}
