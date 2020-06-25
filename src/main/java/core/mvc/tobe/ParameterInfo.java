package core.mvc.tobe;

import org.apache.commons.lang3.ClassUtils;
import org.springframework.web.util.pattern.PathPattern;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Created By kjs4395 on 2020-06-25
 */
public class ParameterInfo {

    private Class<?> type;

    private String valueName;

    private PathPattern pathPattern;

    private boolean isAnnotated;

    public ParameterInfo(Class<?> type, String name, PathPattern pathPattern, Annotation[] annotations) {
        this.type = type;
        this.valueName = name;
        this.pathPattern = pathPattern;
        this.isAnnotated = annotations.length > 0;
    }

    public Object resolveRequestValue(String value) {
        if (this.type.isPrimitive()) {
            return PrimitiveTypeUtil.castPrimitiveType(type, value);
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
        return ClassUtils.isPrimitiveOrWrapper(this.type);
    }

    public boolean isAnnotated() {
        return isAnnotated;
    }
}
