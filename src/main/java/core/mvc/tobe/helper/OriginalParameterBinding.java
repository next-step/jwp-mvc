package core.mvc.tobe.helper;

import core.mvc.tobe.ParameterInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * Created By kjs4395 on 2020-06-24
 */
public class OriginalParameterBinding implements HandlerMethodHelper {

    @Override
    public boolean support(ParameterInfo parameterInfo) {
        return !parameterInfo.isAnnotated() && parameterInfo.isOriginalType();
    }

    @Override
    public Object bindingProcess(ParameterInfo parameterInfo, HttpServletRequest request) {
        return parameterInfo.resolveRequestValue(request.getParameter(parameterInfo.getValueName()));
    }
}
