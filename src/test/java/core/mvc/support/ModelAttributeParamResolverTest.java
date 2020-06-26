package core.mvc.support;

import core.annotation.web.ModelAttribute;
import core.annotation.web.RequestMapping;
import core.mvc.handler.HandlerExecution;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelAttributeParamResolverTest {

    static ModelAttributeResolver resolver;
    static ModelAttributeResolverTestController controller;

    @BeforeAll
    static void setUp() {
        resolver = new ModelAttributeResolver();
        controller = new ModelAttributeResolverTestController();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "skipAnnotation:name:email:name:email",
            "withAnnotation:name:email:name:email",
    }, delimiter = ':')
    @DisplayName("핸들러의 파라미터가 객체타입일 경우 ModelAttribute를 명시여부와 관련 없이 정상적으로 매핑된다")
    void all(String methodName, String nameValue, String emailValue, String expectedName, String expectedEmail) throws NoSuchMethodException {
        // given
        final Map<String, String> params = new HashMap<>();
        params.put("name", nameValue);
        params.put("email", emailValue);

        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameters(params);

        Method method = controller.getClass().getDeclaredMethod(methodName, RequestDto.class);

        final List<MethodParameter> methodParameters = createMethodParameters(methodName);
        final MethodParameter methodParameter = methodParameters.get(0);

        // when
        final RequestDto result = (RequestDto) resolver.resolve(methodParameter, request, response);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(expectedName);
        assertThat(result.getEmail()).isEqualTo(expectedEmail);
    }

    private List<MethodParameter> createMethodParameters(String methodName) throws NoSuchMethodException {
        final Method method = ModelAttributeResolverTestController.class.getDeclaredMethod(methodName, RequestDto.class);
        final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

        final String[] names = nameDiscoverer.getParameterNames(method);
        final Class<?>[] types = method.getParameterTypes();
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        final PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        PathPattern pathPattern = pp.parse(method.getAnnotation(RequestMapping.class).value());

        List result = new ArrayList<>();

        for (int i = 0; i < names.length; i++) {
            result.add(new MethodParameter(names[i], types[i], Arrays.asList(parameterAnnotations[i]), pathPattern));
        }

        return Collections.unmodifiableList(result);
    }

    @Test
    @DisplayName("객체 타입의 파라미터의 필드 중 입력 받지 못한 필드가 존재할 경우 null이 주입된다")
    void fieldNull() throws NoSuchMethodException {
        // given
        final String nameValue = "name";
        final String methodName = "skipAnnotation";
        final Map<String, String> params = new HashMap<>();
        params.put("name", nameValue);

        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameters(params);

        final List<MethodParameter> methodParameters = createMethodParameters(methodName);
        final MethodParameter methodParameter = methodParameters.get(0);

        // when
        final RequestDto result = (RequestDto) resolver.resolve(methodParameter, request, response);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(nameValue);
        assertThat(result.getEmail()).isEqualTo(null);
    }

    @Test
    @DisplayName("binding=false인 @ModelAttribute 를 가진 파라미터의 경우 모든 필드가 null인 객체가 주입된다")
    void bindingFalse() throws NoSuchMethodException {
        // given
        final String methodName = "withAnnotationNoBinding";
        final Map<String, String> params = new HashMap<>();
        params.put("name", "name");
        params.put("email", "email");

        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameters(params);

        final List<MethodParameter> methodParameters = createMethodParameters(methodName);
        final MethodParameter methodParameter = methodParameters.get(0);

        // when
        final RequestDto result = (RequestDto) resolver.resolve(methodParameter, request, response);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(null);
        assertThat(result.getEmail()).isEqualTo(null);
    }

}

class ModelAttributeResolverTestController {

    @RequestMapping("/skip")
    RequestDto skipAnnotation(RequestDto requestDto) {
        return requestDto;
    }

    @RequestMapping("/with")
    RequestDto withAnnotation(@ModelAttribute RequestDto requestDto) {
        return requestDto;
    }

    @RequestMapping("/noBind")
    RequestDto withAnnotationNoBinding(@ModelAttribute(binding = false) RequestDto requestDto) {
        return requestDto;
    }

}

class RequestDto {
    private String name;
    private String email;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    private RequestDto() {
    }
}