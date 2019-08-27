package core.resolver;

import core.annotation.web.RequestMapping;
import core.di.factory.MethodParameter;
import core.di.factory.ParameterNameDiscoverUtils;
import core.mvc.WebRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathVariableArgumentResolverTest {

    private static final String REQUEST_MAPPING_PATH = "/rooms/{grade}/{no}";
    private static final String REQUEST_PATH = "/rooms/SWEET/104";
    private static final HandlerMethodArgumentResolver argumentResolver = new PathVariableArgumentResolver();

    @RequestMapping(REQUEST_MAPPING_PATH)
    public void testMethod(int no, String grade) {

    }

    @DisplayName("PathVariableArgumentResolver 테스트")
    @Test
    public void resolverTest() throws Exception {
        Method method = this.getClass().getMethod("testMethod", int.class, String.class);
        List<MethodParameter> methodParameters = ParameterNameDiscoverUtils.toMethodParameters(method);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", REQUEST_PATH);
        MockHttpServletResponse response = new MockHttpServletResponse();
        WebRequest webRequest = WebRequest.of(method.getAnnotation(RequestMapping.class), request, response);

        resolverTestParam(webRequest, methodParameters.get(0), Integer.class, 104);
        resolverTestParam(webRequest, methodParameters.get(1), String.class, "SWEET");
    }

    private void resolverTestParam(WebRequest webRequest, MethodParameter methodParameter, Class<?> clazz, Object expectValue) {
        Object resolveValue = argumentResolver.resolve(methodParameter, webRequest);
        assertThat(resolveValue).isInstanceOf(clazz);
        assertThat(clazz.cast(resolveValue)).isEqualTo(expectValue);
    }
}
