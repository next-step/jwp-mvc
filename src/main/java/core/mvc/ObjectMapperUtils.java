package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ObjectMapperUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public <T> T convertValue(Object object, Class<T> clazz) {
        return mapper.convertValue(object, clazz);
    }
}
