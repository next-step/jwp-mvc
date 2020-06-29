package core.mvc.support;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestParam;
import core.mvc.ModelAndView;
import core.mvc.support.exception.MissingRequestParamException;
import core.mvc.tobe.TestUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import testUtils.NullableConverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class RequestParamResolverTest {

    private static HandlerMethodArgumentResolver resolver;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeAll
    static void setUp() {
        resolver = new RequestParamResolver();
    }

    @BeforeEach
    void setUpEach() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "none:name:bactoria:bactoria",
            "requestParam:name:bactoria:bactoria",
            "requestParamWithValue:username:bactoria:bactoria",
            "requestParamNotRequired:xx:xx:null"
    }, delimiter = ':')
    @DisplayName("RequestParam이 정상적으로 동작한다")
    void all(String methodName, String paramName, String paramValue, @ConvertWith(NullableConverter.class) String expected) throws NoSuchMethodException {
        // given
        final Map<String, String> params = new HashMap<>();
        params.put(paramName, paramValue);

        request.addParameters(params);

        final List<MethodParameter> methodParameters = createMethodParameters(methodName);

        // when
        final Object[] result = methodParameters.stream()
                .filter(p -> resolver.supportParameter(p))
                .map(p -> resolver.resolve(p, request, response))
                .toArray();

        // then
        assertThat(result[0]).isEqualTo(expected);
    }

    @Test
    @DisplayName("Required=true 인 @RequestParam 의 경우 request에 매칭되는 파라미터가 없을 경우 에러를 반환한다")
    void notExistRequired() throws NoSuchMethodException {
        // given
        final String methodName = "requestParamWithValue";
        final Map<String, String> params = new HashMap<>();

        request.addParameters(params);

        final List<MethodParameter> methodParameters = createMethodParameters(methodName);

        // when
        Throwable thrown = catchThrowable(() -> methodParameters.stream()
                .filter(p -> resolver.supportParameter(p))
                .map(p -> resolver.resolve(p, request, response))
                .toArray());

        // then
        assertThat(thrown).isInstanceOf(MissingRequestParamException.class)
                .hasMessageContaining("파라미터(username)의 값이 비어있습니다.");
    }

    private List<MethodParameter> createMethodParameters(String methodName) throws NoSuchMethodException {
        final Method method = RequestParamResolverTestController.class.getDeclaredMethod(methodName, String.class);
        final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

        final String[] names = nameDiscoverer.getParameterNames(method);
        final Class<?>[] types = method.getParameterTypes();
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        final PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        final PathPattern pathPattern = pp.parse(method.getAnnotation(RequestMapping.class).value());

        List result = new ArrayList<>();

        for (int i = 0; i < names.length; i++) {
            result.add(new MethodParameter(names[i], types[i], Arrays.asList(parameterAnnotations[i]), pathPattern));
        }

        return Collections.unmodifiableList(result);
    }

}

class RequestParamResolverTestController {

    @RequestMapping("/none")
    ModelAndView none(String name) {
        return null;
    }

    @RequestMapping("/requestParam")
    ModelAndView requestParam(@RequestParam String name) {
        return null;
    }

    @RequestMapping("/requestParamWithValue")
    ModelAndView requestParamWithValue(@RequestParam("username") String name) {
        return null;
    }

    @RequestMapping("/requestParamNotRequired")
    ModelAndView requestParamNotRequired(@RequestParam(value = "username", required = false) String name) {
        return null;
    }
}