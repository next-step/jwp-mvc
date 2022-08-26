package core.mvc.tobe.argumentResolver;

public class IntegerArgumentResolver extends AbstractArgumentResolver {
    @Override
    public boolean supportsParameter(Class<?> parameterType) {
        return parameterType.equals(int.class) || parameterType.equals(Integer.class);
    }
}
