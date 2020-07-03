package core.mvc.tobe.resolver;

public class RequestParameterArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean isSupport() {
        return true;
    }

    @Override
    public Object resolve() {
        return null;
    }
}
