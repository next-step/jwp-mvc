package core.resolver;

import core.annotation.web.RequestMapping;
import core.di.factory.MethodParameter;
import core.di.factory.ParameterNameDiscoverUtils;
import core.mvc.WebRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestArgumentResolverTest {

    private static final HandlerMethodArgumentResolver argumentResolver = new HttpRequestArgumentResolver();
    private static final Class<?> RESOLVE_TYPE = HttpServletRequest.class;

    @RequestMapping
    public void testMethod(HttpServletRequest request) {

    }

    @DisplayName("HttpRequestArgumentResolver 테스트")
    @Test
    public void resolverTest() throws Exception {
        Method method = this.getClass().getMethod("testMethod", RESOLVE_TYPE);
        List<MethodParameter> methodParameters = ParameterNameDiscoverUtils.toMethodParameters(method);
        MethodParameter methodParameter = methodParameters.get(0);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        WebRequest webRequest = WebRequest.of(method.getAnnotation(RequestMapping.class), request, response);

        Object resolveValue = argumentResolver.resolve(methodParameter, webRequest);
        assertThat(resolveValue).isInstanceOf(RESOLVE_TYPE);
        assertThat(RESOLVE_TYPE.cast(resolveValue)).isEqualTo(request);
    }
}
