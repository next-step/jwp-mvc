package core.resolver;

import javax.servlet.http.HttpServletRequest;

import core.di.factory.MethodParameter;
import core.handler.converter.StringConverters;
import core.mvc.WebRequest;

public class ParamNameArgumentResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supports(MethodParameter methodParameter) {
		return StringConverters.getInstance().supports(methodParameter.getType());
	}

	@Override
	public Object resolve(MethodParameter methodParameter, WebRequest webRequest) {
		Class<?> type = methodParameter.getType();
		HttpServletRequest request = webRequest.getRequest();
		String parameterName = methodParameter.getName();

		return StringConverters.getInstance()
				.getConverter(type)
				.convert(request.getParameter(parameterName));
	}
}
