package core.mvc.tobe;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.JspHandleView;
import core.mvc.ModelAndView;
import core.mvc.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HandlerExecution implements AnnotationController {
    private static final Logger logger = LoggerFactory.getLogger(HandlerExecution.class);

    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    Class<?> clazz;
    Method executionMethod;

    public HandlerExecution() {
    }

    public HandlerExecution(Class<?> clazz, Method executionMethod) {
        this.clazz = clazz;
        this.executionMethod = executionMethod;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Class<?> returnObjectType = executionMethod.getReturnType();

        try {
            Object returnObject = initInvokeMethod(request);//executionMethod.invoke(clazz.newInstance(), request, response);
            if (returnObjectType.equals(String.class)) {
                String viewName = (String) returnObject;
                View views = new JspHandleView(viewName);
                return new ModelAndView(views);
            }

            if (returnObjectType.equals(ModelAndView.class)) {
                return (ModelAndView) returnObject;
            }
        }catch (IllegalAccessException e) {
            logger.error("IllegalAccessException", e);
            throw new IllegalAccessException("잘못된 접근입니다.");
        } catch (Exception e) {
            logger.error("Exception", e);
            throw new InstantiationException("메소드를 생성중 실패하였습니다.");
        }
        return null;
    }

    private Object initInvokeMethod(HttpServletRequest request) throws Exception{
        return executionMethod.invoke(clazz.newInstance(),
                getParameterValue(request, executionMethod));
    }

    public Object[] getParameterValue(HttpServletRequest request, Method method) throws Exception{
        Map<String, String> variables = getVariableParam(request, method);

        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Class<?>[] parameterType = method.getParameterTypes();
        Object[] values = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            if(!isClass(parameterType[i])){
                String parameterName = parameterNames[i];
                logger.debug("parameter : {}", parameterName);

                Object paramValue = variables.containsKey(parameterName)
                        ? variables.get(parameterName)
                        : request.getParameter(parameterName);
                values[i] = getPrimitiveType(parameterType[i], paramValue);
            }else{
                values[i] = initClassParameter(request, parameterType[i]);
            }
        }
        return values;
    }

    public Object getPrimitiveType(Class<?> typeClass, Object object){
        if (typeClass.equals(int.class)) {
            return Integer.parseInt(object.toString());
        }
        if (typeClass.equals(long.class)) {
            return Long.parseLong(object.toString());
        }
        if (typeClass.equals(String.class)){
            return String.valueOf(object);
        }
        if (typeClass.equals(Double.class)){
            return Double.parseDouble(object.toString());
        }
        if (typeClass.equals(HttpServletRequest.class)){
            return object;
        }
        if (typeClass.equals(HttpServletResponse.class)){
            return object;
        }

        return null;
    }

    public boolean isClass(Class<?> typeClass){
        if (typeClass.equals(int.class)) {
            return false;
        }
        if (typeClass.equals(long.class)) {
            return false;
        }
        if (typeClass.equals(String.class)){
            return false;
        }
        if (typeClass.equals(Double.class)){
            return false;
        }

        return true;
    }

    public Object initClassParameter(HttpServletRequest request, Class<?> clazz) throws Exception{
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Object parameterObjects = getNewInstance(request, clazz, constructor);
            if (parameterObjects != null) return parameterObjects;
        }

        return null;
    }

    public Object getNewInstance(HttpServletRequest request, Class<?> clazz, Constructor constructor) throws Exception{
        String[] parameterNames = nameDiscoverer.getParameterNames(constructor);
        Class[] parameterTypes = constructor.getParameterTypes();
        Object[] parameterObjects = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            if(isClass(parameterTypes[i])){
                parameterObjects[i] = initClassParameter(request, parameterTypes[i]);
            }else{
                parameterObjects[i] = getPrimitiveType(parameterTypes[i],
                        request.getParameter(parameterNames[i]));
            }
        }
        return constructor.newInstance(parameterObjects);
    }

    private Map<String, String> getVariableParam(HttpServletRequest request, Method method){
        Annotation requestMapping = method.getAnnotation(RequestMapping.class);
        String pathUrl = ((RequestMapping) requestMapping).value();
        if(pathUrl.contains("{") && pathUrl.contains("}")){
            String requestPath = request.getServletPath();
            return parse(pathUrl)
                    .matchAndExtract(toPathContainer(requestPath))
                    .getUriVariables();
        }

        return new HashMap<>();
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

}
