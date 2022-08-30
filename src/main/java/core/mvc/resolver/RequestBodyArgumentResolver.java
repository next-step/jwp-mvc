package core.mvc.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestBodyArgumentResolver implements ArgumentResolver {
    @Override
    public boolean supports(RequestParameter requestParameter) {
        Annotation[] parameterAnnotations = requestParameter.getAnnotations();
        return Arrays.stream(parameterAnnotations)
                .anyMatch(annotation -> annotation.annotationType() == RequestBody.class);
    }

    @Override
    public Object resolveArgument(RequestParameter requestParameter, HttpServletRequest request, HttpServletResponse response) {
        try {
            Class<?> parameterClazz = requestParameter.getClassType();
            String readLine = request.getReader().readLine();

            Map<String, String> result = getParameterMapfromReadLine(readLine);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.convertValue(result, parameterClazz);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Illegal Request Parameter");
        }
    }

    private Map<String, String> getParameterMapfromReadLine(String readLine) {
        Map<String, String> result = new HashMap<String, String>();
        for(String param : readLine.split("&")) {
            String pair[] = param.split("=");
            result.put(pair[0], getValue(pair));

        }
        return result;
    }

    private String getValue(String[] pair) {
        String value = "";
        if (pair.length > 1) {
            value = pair[1];
        }
        return value;
    }
}
