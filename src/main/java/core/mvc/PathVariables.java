package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.Map;

@Getter
public class PathVariables {
    private final ObjectMapper mapper = new ObjectMapper();

    private Map<String, String> pathVariables;

    public PathVariables(String path, String requestUri) {
        if (path != null) {
            this.pathVariables = PathPatternUtils.getUriVariables(path, requestUri);
        }
    }

    public <T> Object get(String name, Class<T> clazz) {
        return mapper.convertValue(this.pathVariables.get(name), clazz);
    }
}
