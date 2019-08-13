package core.mvc;

public abstract class AbstractHandlerAdapter implements HandlerAdapter{

    private Class<?> clazz;

    public AbstractHandlerAdapter(Class<?> clazz) {
        this.clazz = clazz;

    }
    @Override
    public boolean supports(Object handler) {
        assert handler!= null;
        return clazz.isAssignableFrom(handler.getClass());
    }
}
