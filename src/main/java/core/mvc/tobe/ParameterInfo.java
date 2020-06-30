package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.util.pattern.PathPattern;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created By kjs4395 on 2020-06-25
 */
public class ParameterInfo {
    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private Class<?> type;

    private String valueName;

    private PathPattern pathPattern;

    private boolean isAnnotated;

    public ParameterInfo(Method method, int parameterIdx) {
        this.type = method.getParameterTypes()[parameterIdx];
        this.valueName = nameDiscoverer.getParameterNames(method)[parameterIdx];
        this.pathPattern = PathPatternUtil.getPathPattern(method.getDeclaredAnnotation(RequestMapping.class).value());
        this.isAnnotated = method.getParameterAnnotations()[parameterIdx].length > 0;

    }

    public Object resolveRequestValue(String value) {
        if (this.type.isPrimitive()) {
            return PrimitiveTypeUtil.getValue(type, value);
        }
        return value;
    }

    public Map<String, String> getUrlVariables(HttpServletRequest request) {
        return pathPattern.matchAndExtract(PathPatternUtil.toPathContainer(request.getRequestURI())).getUriVariables();
    }

    public String getValueName() {
        return valueName;
    }

    public Class<?> getType() {
        return this.type;
    }

    public boolean isOriginalType() {

        if(ClassUtils.isPrimitiveOrWrapper(this.type)) {
            return true;
        }

        return String.class.getName().equalsIgnoreCase(type.getName());
    }

    public boolean isAnnotated() {
        return isAnnotated;
    }
}
