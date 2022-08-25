package next.support.resolver;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.mvc.tobe.HandlerExecution;
import core.web.view.ModelAndView;
import next.support.mapper.ObjectMapperFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArgumentResolver {

    private final static ArgumentResolver argumentResolver = new ArgumentResolver();
    private final static Integer HANDLER_ARGUMENT_ANNOTATION_COUNT_LIMIT = 1;
    private final static String URL_PATH_SEPARATOR = "/";
    private final static String PATH_VARIABLE_BRACKET_OPEN = "{";
    private final static String PATH_VARIABLE_BRACKET_CLOSE = "}";

    private ArgumentResolver() {

    }

    public static ArgumentResolver getInstance() {
        return argumentResolver;
    }

    public ModelAndView invokeHandler(HandlerSpec handlerSpec) throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) handlerSpec.getHandler();
        Method handler = handlerExecution.getMethod();

        List<Object> parameters = this.getParameters(handler.getParameters(), handlerSpec);
        return handlerExecution.handle(parameters);
    }

    public List<Object> getParameters(Parameter[] parameters, HandlerSpec handlerSpec) {
        return Arrays.stream(parameters).map(parameter -> {
            Annotation parameterAnnotation = this.getAnnotation(parameter);
            try {
                return this.getParameterSpec(parameterAnnotation, parameter, handlerSpec);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

    }

    private Object getParameterSpec(Annotation parameterAnnotation, Parameter parameter, HandlerSpec handlerSpec) throws JsonProcessingException {
        HttpServletRequest httpServletRequest = handlerSpec.getHttpServletRequest();
        Class<?> parameterClass = parameter.getType();
        String parameterName = parameter.getName();

        if (parameterAnnotation.getClass().getName().equals("RequestBody")) {
            String data = ObjectMapperFactory.getObjectMapper().writeValueAsString(httpServletRequest.getParameterMap());
            return ObjectMapperFactory.getObjectMapper().readValue(data, parameterClass);
        }

        if (parameterAnnotation.getClass().getName().equals("PathVariable")) {
            String requestUri = httpServletRequest.getRequestURI();
            String handlerUri = handlerSpec.getHandlerKey().getUrl();
            return this.getPathVariable(requestUri, handlerUri, parameterName);
        }

        if (parameterAnnotation.getClass().getName().equals("HttpServletRequest")) {
            return httpServletRequest;
        }

        return this.getConvertedParamter(parameterClass, httpServletRequest.getParameter(parameterName));
    }

    private Object getConvertedParamter(Class<?> parameterClass, String value) {
        if (parameterClass.equals(int.class)) {
            return Integer.parseInt(value);
        }

        return value;
    }

    private Annotation getAnnotation(Parameter parameter) {
        Annotation[] annotations = parameter.getDeclaredAnnotations();
        if (annotations.length > HANDLER_ARGUMENT_ANNOTATION_COUNT_LIMIT) {
            throw new IllegalArgumentException();
        }

        return Arrays.stream(parameter.getDeclaredAnnotations()).findFirst().orElse(null);
    }

    private String getPathVariable(String requestUri, String handlerUri, String targetParameter) {
        List<String> seperatedUrl = Arrays.stream(handlerUri.split(URL_PATH_SEPARATOR))
                .collect(Collectors.toList());
        int index = seperatedUrl.indexOf(PATH_VARIABLE_BRACKET_OPEN + targetParameter + PATH_VARIABLE_BRACKET_CLOSE);

        return requestUri.split(URL_PATH_SEPARATOR)[index];
    }

}










