package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.mvc.ModelAndView;
import core.mvc.tobe.resolver.CommandObjectMethodArgumentResolver;
import core.mvc.tobe.resolver.SimpleDataTypeMethodArgumentResolver;
import core.mvc.tobe.resolver.HandlerMethodArgumentResolver;
import core.mvc.tobe.resolver.PathVariableMethodArgumentResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;


import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class HandlerMethodArgumentResolverTest {
    private static final Logger logger = LoggerFactory.getLogger(HandlerMethodArgumentResolverTest.class);

    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Test
    void string() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String userId = "javajigi";
        String password = "password";
        request.addParameter("userId", userId);
        request.addParameter("password", password);

        Class clazz = TestUserController.class;
        Method method = getMethod("create_string", clazz.getDeclaredMethods());
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Object[] values = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            String parameterName = parameterNames[i];
            logger.debug("parameter : {}", parameterName);
            values[i] = request.getParameter(parameterName);
        }

        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);
        assertThat(mav.getObject("userId")).isEqualTo(userId);
        assertThat(mav.getObject("password")).isEqualTo(password);
    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }


    @DisplayName("파라미터가 커맨드 객체인 경우, 커맨드 객체에 값을 셋팅해서 반환")
    @Test
    void test_resolveCommandObject() throws Exception {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        String userId = "crystal";
        String password = "password";
        String age = "26";
        request.addParameter("userId", userId);
        request.addParameter("password", password);
        request.addParameter("age", age);

        MethodParameter methodParameter = new MethodParameter("testUser", TestUser.class);

        HandlerMethodArgumentResolver resolver = new CommandObjectMethodArgumentResolver();
        // when // then
        boolean supports = resolver.supports(methodParameter);
        assertThat(supports).isTrue();

        Object arg = resolver.resolveArgument(methodParameter, request, response);
        assertThat(arg).isInstanceOf(TestUser.class);

        TestUser testUser = (TestUser) arg;
        assertThat(testUser.getUserId()).isEqualTo("crystal");
        assertThat(testUser.getPassword()).isEqualTo("password");
        assertThat(testUser.getAge()).isEqualTo(26);
    }

    @DisplayName("DataParser로 파싱 가능한 파라미터인 경우, 해당 데이터를 반환")
    @Test
    void test_resolveBasicDataType() throws Exception {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        String userId = "crystal";
        request.addParameter("userId", userId);

        MethodParameter methodParameter = new MethodParameter("userId", String.class);

        HandlerMethodArgumentResolver resolver = new SimpleDataTypeMethodArgumentResolver();
        // when // then
        boolean supports = resolver.supports(methodParameter);
        assertThat(supports).isTrue();

        Object arg = resolver.resolveArgument(methodParameter, request, response);
        assertThat(arg).isInstanceOf(String.class);

        assertThat((String) arg).isEqualTo("crystal");
    }

    @DisplayName("@PathVariable 이 붙은 파라미터의 경우, 요청url에서 추출하여 반환")
    @Test
    void test_resolvePathVariable() throws Exception {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/1");

        Class clazz = TestUserController.class;
        Method method = getMethod("show_pathvariable", clazz.getDeclaredMethods());

        MethodParameters methodParameters = MethodParameters.from(method);
        MethodParameter methodParameter = methodParameters.getParameters().get(0);

        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        final PathPatternParser ppp = new PathPatternParser();
        ppp.setMatchOptionalTrailingSeparator(true);
        PathPattern pp = ppp.parse(annotation.value());

        HandlerMethodArgumentResolver resolver = new PathVariableMethodArgumentResolver(pp);
        // when
        long id = (long) resolver.resolveArgument(methodParameter, request, response);
        // then
        assertThat(id).isEqualTo(1);
    }
}
