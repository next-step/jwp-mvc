package core.mvc.tobe.argumentResolver;

import core.mvc.tobe.ParameterTypeEnum;

import java.util.List;

public class StringArgumentResolver extends AbstractArgumentResolver {

    @Override
    public boolean supportsParameter(Class<?> parameterType) {
        return parameterType.equals(String.class);
    }
}
