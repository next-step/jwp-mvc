package core.mvc.tobe.argumentResolver;

import core.mvc.tobe.TestUser;

public class TestUserArgumentResolver extends AbstractArgumentResolver {

    @Override
    public boolean supportsParameter(Class<?> parameterType) {
        return parameterType.equals(TestUser.class);
    }
}
