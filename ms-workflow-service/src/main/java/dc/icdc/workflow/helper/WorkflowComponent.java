package dc.icdc.workflow.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:workflow.properties")
public class WorkflowComponent {

    @Autowired
    private Environment env;

    public String getProperty(String key) {
        String value = env.getProperty(key);
        if (value == null) {
            return key;
        }
        return value;
    }


}
