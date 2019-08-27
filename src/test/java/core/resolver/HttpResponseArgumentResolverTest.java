package core.resolver;

import core.annotation.web.RequestMapping;
import core.di.factory.MethodParameter;
import core.di.factory.ParameterNameDiscoverUtils;
import core.mvc.WebRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpResponseArgumentResolverTest {

    private static final HandlerMethodArgumentResolver argumentResolver = new HttpResponseArgumentResolver();
    private static final Class<?> RESOLVE_TYPE = HttpServletResponse.class;

    @RequestMapping
    public void testMethod(HttpServletResponse response) {

    }

    @DisplayName("HttpResponseArgumentResolver 테스트")
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
        assertThat(RESOLVE_TYPE.cast(resolveValue)).isEqualTo(response);
    }
}
