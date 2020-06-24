package core.mvc.support;

import core.annotation.web.ModelAttribute;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.mock.web.MockHttpServletRequest;
import testUtils.NullableConverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ModelAttributeParamResolverTest {

    static ModelAttributeResolver resolver;

    @BeforeAll
    static void setUp() {
        resolver = new ModelAttributeResolver();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "skipAnnotation:name:email:name:email",
            "withAnnotation:name:email:name:email",
    }, delimiter = ':')
    void all(String methodName, String nameValue, String emailValue, String expectedName, String expectedEmail) throws NoSuchMethodException {
        // given
        final Map<String, String> params = new HashMap<>();
        params.put("name", nameValue);
        params.put("email", emailValue);

        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameters(params);

        final List<MethodParameter> methodParameters = createMethodParameters(methodName);
        final MethodParameter methodParameter = methodParameters.get(0);

        // when
        final RequestDto result = (RequestDto) resolver.resolve(methodParameter, request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(expectedName);
        assertThat(result.getEmail()).isEqualTo(expectedEmail);
    }

    private List<MethodParameter> createMethodParameters(String methodName) throws NoSuchMethodException {
        final Method method = ModelAttributeResolverTestController.class.getDeclaredMethod(methodName, RequestDto.class);
        final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

        String[] names = nameDiscoverer.getParameterNames(method);
        Class<?>[] types = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        List result = new ArrayList<>();

        for (int i = 0; i < names.length; i++) {
            result.add(new MethodParameter(names[i], types[i], Arrays.asList(parameterAnnotations[i])));
        }

        return Collections.unmodifiableList(result);
    }

    @Test
    @DisplayName("입력받지 못한 필드는 null이 주입된다")
    void fieldNull() throws NoSuchMethodException {
        // given
        final String nameValue = "name";
        final String methodName = "skipAnnotation";
        final Map<String, String> params = new HashMap<>();
        params.put("name", nameValue);

        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameters(params);

        final List<MethodParameter> methodParameters = createMethodParameters(methodName);
        final MethodParameter methodParameter = methodParameters.get(0);

        // when
        final RequestDto result = (RequestDto) resolver.resolve(methodParameter, request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(nameValue);
        assertThat(result.getEmail()).isEqualTo(null);
    }

    @Test
    @DisplayName("binding이 false인 경우에는 필드가 비어있는 객체가 주입된다")
    void bindingFalse() throws NoSuchMethodException {
        // given
        final String methodName = "withAnnotationNoBinding";
        final Map<String, String> params = new HashMap<>();
        params.put("name", "name");
        params.put("email", "email");

        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameters(params);

        final List<MethodParameter> methodParameters = createMethodParameters(methodName);
        final MethodParameter methodParameter = methodParameters.get(0);

        // when
        final RequestDto result = (RequestDto) resolver.resolve(methodParameter, request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(null);
        assertThat(result.getEmail()).isEqualTo(null);
    }

}

class ModelAttributeResolverTestController {

    RequestDto skipAnnotation(RequestDto requestDto) {
        return requestDto;
    }

    RequestDto withAnnotation(@ModelAttribute RequestDto requestDto) {
        return requestDto;
    }

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