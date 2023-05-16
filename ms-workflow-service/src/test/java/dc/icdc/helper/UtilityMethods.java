package dc.icdc.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.InputStream;

@UtilityClass
public class UtilityMethods {

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    public <T> T mapJsonFileToDTO(String filename, Class<T> returnType) throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Resource resource = resourceLoader.getResource("classpath:input/"+filename+".json");
        InputStream inputStream = resource.getInputStream();
        T obj = mapper.readValue(inputStream, returnType);
        return obj;
    }
}
