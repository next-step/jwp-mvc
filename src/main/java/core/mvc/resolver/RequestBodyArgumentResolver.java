package core.mvc.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.stream.Stream;

public class RequestBodyArgumentResolver implements ArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Annotation[] annotations = methodParameter.getParameterAnnotations();
        return Stream.of(annotations).anyMatch(annotation -> annotation.annotationType() == RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        try {
            Class<?> parameterType = methodParameter.getParameterType();
            String readLine = request.getReader().readLine();

            Map<String, String> result = getParameterMapfromReadLine(readLine);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.convertValue(result, parameterType);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Map<String, String> getParameterMapfromReadLine(String readLine) {
        Map<String, String> result = Maps.newHashMap();
        for (String param : readLine.split("&")) {
            String pair[] = param.split("=");
            String value = "";
            if (pair.length > 1) {
                value = pair[1];
            }
            result.put(pair[0], value);
        }
        return result;
    }
}
