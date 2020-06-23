package core.mvc.tobe;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;

/**
 * Created By kjs4395 on 2020-06-24
 */
public class OriginalParameterBinding implements HandlerMethodHelper {

    @Override
    public Object bindingProcess(Class<?> type, String name, HttpServletRequest request) {
        String requestValue = request.getParameter(name);

        if(type.isPrimitive()) {
            return PrimitiveTypeUtil.castPrimitiveType(type, requestValue);
        }

        return requestValue;

    }
}
