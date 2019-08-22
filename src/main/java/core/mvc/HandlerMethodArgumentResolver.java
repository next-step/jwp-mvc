package core.mvc;

import core.di.factory.MethodParameter;

public interface HandlerMethodArgumentResolver {

    boolean supports(MethodParameter methodParameter);

    Object resolve(MethodParameter methodParameter, WebRequest webRequest);
}
