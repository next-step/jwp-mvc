package core.mvc.tobe.argumentResolver;

public class LongArgumentResolver extends AbstractArgumentResolver {

    @Override
    public boolean supportsParameter(Class<?> parameterType) {
        return parameterType.equals(long.class) || parameterType.equals(Long.class);
    }
}
