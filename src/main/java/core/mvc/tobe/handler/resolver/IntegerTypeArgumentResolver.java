package core.mvc.tobe.handler.resolver;

public class IntegerTypeArgumentResolver extends AbstractSimpleTypeArgumentResolver {

    @Override
    public boolean support(NamedParameter parameter) {
        return Integer.TYPE.isAssignableFrom(parameter.getType()) ||
                Integer.class.isAssignableFrom(parameter.getType());
    }

    @Override
    Object resolveInternal(String value) {
        return Integer.parseInt(value);
    }
}
