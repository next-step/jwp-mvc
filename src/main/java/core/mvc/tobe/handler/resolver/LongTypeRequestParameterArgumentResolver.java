package core.mvc.tobe.handler.resolver;

public class LongTypeRequestParameterArgumentResolver extends AbstractSimpleTypeRequestParameterArgumentResolver {

    @Override
    public boolean support(NamedParameter parameter) {
        return Long.TYPE.isAssignableFrom(parameter.getType()) ||
                Long.class.isAssignableFrom(parameter.getType());
    }

    @Override
    Object resolveInternal(String value) {
        return Long.parseLong(value);
    }
}
