package core.resolver;

import core.di.factory.MethodParameter;
import core.mvc.WebRequest;

public interface HandlerMethodArgumentResolver {

    boolean supports(MethodParameter methodParameter);

    Object resolve(MethodParameter methodParameter, WebRequest webRequest);
}
