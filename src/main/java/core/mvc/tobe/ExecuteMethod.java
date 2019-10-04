package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ExecuteMethod {

    private static final Logger logger = LoggerFactory.getLogger(HandlerExecution.class);
    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();


    public Object[] getParameterValue(HttpServletRequest request, Method method) throws Exception {
        Map<String, String> variables = getVariableParam(request, method);
        RequestParamToMap(request, variables);

        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Class<?>[] parameterType = method.getParameterTypes();
        Object[] values = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            boolean checkClass = isClass(parameterType[i]);

            if (!checkClass) {
                String parameterName = parameterNames[i];
                logger.debug("parameter : {}", parameterName);

                Object paramValue = variables.containsKey(parameterName)
                        ? variables.get(parameterName)
                        : request.getParameter(parameterName);
                values[i] = getPrimitiveType(parameterType[i], paramValue).orElse("");
            }

            if(checkClass){
                values[i] = initClassParameter(variables, parameterType[i]).orElse("");
            }
        }
        return values;
    }

    public Optional<Object> getPrimitiveType(Class<?> typeClass, Object object) {
        if (typeClass.equals(int.class)) {
            return Optional.ofNullable(Integer.parseInt(object.toString()));
        }
        if (typeClass.equals(long.class)) {
            return Optional.ofNullable(Long.parseLong(object.toString()));
        }
        if (typeClass.equals(String.class)) {
            return Optional.ofNullable(String.valueOf(object));
        }
        if (typeClass.equals(Double.class)) {
            return Optional.ofNullable(Double.parseDouble(object.toString()));
        }
        if (typeClass.equals(HttpServletRequest.class)) {
            return Optional.ofNullable(object);
        }
        if (typeClass.equals(HttpServletResponse.class)) {
            return Optional.ofNullable(object);
        }

        return Optional.empty();
    }

    public boolean isClass(Class<?> typeClass) {
        if (typeClass.equals(int.class)) {
            return false;
        }
        if (typeClass.equals(long.class)) {
            return false;
        }
        if (typeClass.equals(String.class)) {
            return false;
        }
        if (typeClass.equals(Double.class)) {
            return false;
        }

        return true;
    }

    public Optional<Object> initClassParameter(Map<String, String> variables, Class<?> clazz) throws Exception {
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Object parameterObjects = getNewInstance(variables, clazz, constructor);
            if (parameterObjects != null) {
                return Optional.ofNullable(parameterObjects);
            }
        }

        return Optional.empty();
    }

    public Object getNewInstance(Map<String, String> variables, Class<?> clazz, Constructor constructor) throws Exception {
        String[] parameterNames = nameDiscoverer.getParameterNames(constructor);
        Class[] parameterTypes = constructor.getParameterTypes();
        Object[] parameterObjects = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            boolean isClass = isClass(parameterTypes[i]);

            if (isClass) {
                parameterObjects[i] = initClassParameter(variables, parameterTypes[i]).orElse("");
            }

            if(!isClass) {
                parameterObjects[i] = getPrimitiveType(parameterTypes[i],
                        variables.get(parameterNames[i])).orElse("");
            }
        }
        return constructor.newInstance(parameterObjects);
    }

    private Map<String, String> getVariableParam(HttpServletRequest request, Method method) {
        Annotation requestMapping = method.getAnnotation(RequestMapping.class);
        String pathUrl = ((RequestMapping) requestMapping).value();

        Map<String, String> variableMap = new HashMap<>();

        if (pathUrl.contains("{") && pathUrl.contains("}")) {
            String requestPath = request.getServletPath();
            variableMap = parse(pathUrl)
                    .matchAndExtract(toPathContainer(requestPath))
                    .getUriVariables();

            Map<String, String> returnMap = new HashMap<>();
            returnMap.putAll(variableMap);
            return returnMap;
        }

        return variableMap;
    }

    private PathPattern parse(String path) {
        PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(path);
    }

    private static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }

    private void RequestParamToMap(HttpServletRequest request, Map<String, String> variableMap){
        Enumeration<String> requestParam = request.getParameterNames();
        while (requestParam.hasMoreElements()){
            String requestKey = requestParam.nextElement();
            String requestValue = request.getParameter(requestKey);
            variableMap.put(requestKey, requestValue);
        }
    }

}
