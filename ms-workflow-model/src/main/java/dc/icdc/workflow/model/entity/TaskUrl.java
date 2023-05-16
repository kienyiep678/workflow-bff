package dc.icdc.workflow.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskUrl {

    public String url;
    public String urlType;
    public HttpMethod method;
    public Object data;

}
