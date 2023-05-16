package dc.icdc.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUrlDTO {

    @NotEmpty(message = "Url is empty")
    public String url;
    @NotEmpty(message = "Url Type is empty")
    public TaskUrlType urlType;
    @JsonSerialize(using = HttpMethodSerializer.class)
    public HttpMethod method;
    public Object data;

    public enum TaskUrlType{
        APPROVE_URL("APPROVE_URL"),
        REJECT_URL("REJECT_URL"),
        VIEW_URL("VIEW_URL");
        private final String value;

        TaskUrlType(final String newValue) {
            value = newValue;
        }

        public String getValue() { return value; }

        @JsonCreator
        public TaskUrlType convertJSONtoTaskUrlType(String value){
            return TaskUrlType.valueOf(value);
        }
        @JsonValue
        public String getTaskUrlTypeJSON() {
            return value;
        }
    }

    public static class HttpMethodSerializer extends StdSerializer<HttpMethod> {

        public HttpMethodSerializer() {
            super(HttpMethod.class);
        }

        @Override
        public void serialize(HttpMethod value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value.name());
        }
    }
}

