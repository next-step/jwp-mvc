package core.mvc.tobe.handler.resolver;

public class StringTypeArgumentResolver extends AbstractSimpleTypeArgumentResolver {

    @Override
    public boolean support(NamedParameter parameter) {
        return String.class.isAssignableFrom(parameter.getType());
    }

    @Override
    Object resolveInternal(String value) {
        return value;
    }
}
