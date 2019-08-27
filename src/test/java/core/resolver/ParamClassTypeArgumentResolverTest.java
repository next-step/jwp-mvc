package core.resolver;

import core.annotation.web.RequestMapping;
import core.di.factory.MethodParameter;
import core.di.factory.ParameterNameDiscoverUtils;
import core.mvc.WebRequest;
import next.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ParamClassTypeArgumentResolverTest {

    private static final String TEST_NAME = "circlee";
    private static final String TEST_EMAIL = "circlee7@gmail.com";
    private static final String USER_ID = "userId";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final Class<?> RESOLVE_TYPE = User.class;
    private static final HandlerMethodArgumentResolver argumentResolver = new ParamClassTypeArgumentResolver();

    @RequestMapping()
    public void testMethod(User user) { }

    @DisplayName("ParamClassTypeArgumentResolver 테스트")
    @Test
    public void resolverTest() throws Exception {
        Method method = this.getClass().getMethod("testMethod", RESOLVE_TYPE);
        List<MethodParameter> methodParameters = ParameterNameDiscoverUtils.toMethodParameters(method);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(USER_ID, TEST_NAME);
        request.setParameter(PASSWORD, TEST_NAME);
        request.setParameter(NAME, TEST_NAME);
        request.setParameter(EMAIL, TEST_EMAIL);
        MockHttpServletResponse response = new MockHttpServletResponse();
        WebRequest webRequest = WebRequest.of(method.getAnnotation(RequestMapping.class), request, response);

        Object resolveValue = argumentResolver.resolve(methodParameters.get(0), webRequest);
        assertThat(resolveValue).isInstanceOf(RESOLVE_TYPE);
        User resolveUser = (User) RESOLVE_TYPE.cast(resolveValue);
        assertThat(resolveUser.getUserId()).isEqualTo(TEST_NAME);
        assertThat(resolveUser.getPassword()).isEqualTo(TEST_NAME);
        assertThat(resolveUser.getName()).isEqualTo(TEST_NAME);
        assertThat(resolveUser.getEmail()).isEqualTo(TEST_EMAIL);
    }
}
