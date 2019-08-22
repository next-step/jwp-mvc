package core.mvc.tobe;

import core.di.factory.MethodParameter;
import core.mvc.HandlerMethodArgumentResolver;
import core.mvc.WebRequest;

import javax.servlet.http.HttpServletRequest;

public class TestUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter methodParameter) {
        return methodParameter.getType().isAssignableFrom(TestUser.class);
    }

    @Override
    public Object resolve(MethodParameter methodParameter, WebRequest webRequest) {
        HttpServletRequest request = webRequest.getRequest();
        return new TestUser(request.getParameter("userId")
                , request.getParameter("password")
                , Integer.parseInt(request.getParameter("age"))
        );
    }
}
