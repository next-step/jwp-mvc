package core.mvc.tobe;

import org.springframework.web.util.pattern.PathPattern;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class AnnotationParameterHelper implements HandlerMethodHelper{
    @Override
    public Object bindingProcess(Class<?> type, String name, PathPattern pathPattern,HttpServletRequest request) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Map<String, String> values  = pathPattern.matchAndExtract(PathPatternUtil.toPathContainer(request.getRequestURI())).getUriVariables();

        String value = values.get(name);

        if(type.isPrimitive()) {
            return PrimitiveTypeUtil.castPrimitiveType(type,value);
        }
        return value;
    }
}
