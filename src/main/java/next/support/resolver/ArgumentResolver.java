package next.support.resolver;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestBody;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import core.web.view.ModelAndView;
import next.support.mapper.ObjectMapperFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class ArgumentResolver {

    private final static ArgumentResolver argumentResolver = new ArgumentResolver();
    private final static Integer HANDLER_ARGUMENT_ANNOTATION_COUNT_LIMIT = 1;

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
        return Arrays.stream(parameters)
                .map(parameter -> {
                    Annotation parameterAnnotation = this.getAnnotation(parameter);
                    try {
                        return this.getConvertedParameter(parameterAnnotation, parameter, handlerSpec);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

    }

    private Object getConvertedParameter(Annotation parameterAnnotation, Parameter parameter, HandlerSpec handlerSpec) throws JsonProcessingException {
        HttpServletRequest httpServletRequest = handlerSpec.getHttpServletRequest();
        Class<?> parameterClass = parameter.getType();
        String parameterName = parameter.getName();

        if (parameterClass.getName().equals("javax.servlet.http.HttpServletRequest")) {
            return httpServletRequest;
        }

        if (Objects.isNull(parameterAnnotation)) {
            return this.getConvertedParamter(parameterClass, httpServletRequest.getParameter(parameterName));
        }

        if (parameterAnnotation.annotationType().equals(RequestBody.class)) {
            Map<String, String> parametersFromRequest = this.getParametersFromRequest(httpServletRequest, parameterClass);
            String data = ObjectMapperFactory.getObjectMapper().writeValueAsString(parametersFromRequest);
            return ObjectMapperFactory.getObjectMapper().readValue(data, parameterClass);
        }

        if (parameterAnnotation.annotationType().equals(PathVariable.class)) {
            String requestUri = httpServletRequest.getRequestURI();
            String handlerUri = AnnotationHandlerMapping.getInstance().getHandlerOriginUri(requestUri);
            return this.getPathVariable(requestUri, handlerUri, parameterName);
        }

        throw new IllegalArgumentException();
    }

    private Map<String, String> getParametersFromRequest(HttpServletRequest httpServletRequest, Class<?> parameterClass) {
        List<String> keys = Arrays.stream(parameterClass.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList());

        Map<String, String> parametersFromRequest = new HashMap<>();
        keys.forEach(key -> {
            String value = httpServletRequest.getParameter(key);
            parametersFromRequest.put(key, value);
        });

        return parametersFromRequest;
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

        return Arrays.stream(parameter.getDeclaredAnnotations())
                .findFirst()
                .orElse(null);
    }

    private String getPathVariable(String requestUri, String handlerUri, String targetParameter) {
        return PathAnalyzer.getVariables(handlerUri, requestUri, targetParameter);
    }

}










