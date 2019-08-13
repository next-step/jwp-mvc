package core.mvc.tobe.support;

import core.mvc.tobe.MethodParameter;
import core.mvc.tobe.mock.MockRequestParam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class ArgumentResolverTest {

    List<ArgumentResolver> argumentResolvers;

    @BeforeEach
    void setUp() {
        argumentResolvers = asList(
                new HttpRequestArgumentResolver(),
                new HttpResponseArgumentResolver(),
                new RequestParamArgumentResolver()
        );
    }

    @DisplayName("Request Type Argument Resolver Test")
    @Test
    void requestResolveArguments() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        MethodParameter mp = new MethodParameter(HttpServletRequest.class, new Annotation[0]);
        Object result = findArgumentResolver(mp, request, response);

        assertThat(result).isEqualTo(request);
    }

    @DisplayName("Response Type Argument Resolver Test")
    @Test
    void responseResolveArguments() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        MethodParameter mp = new MethodParameter(HttpServletResponse.class, new Annotation[0]);
        Object result = findArgumentResolver(mp, request, response);

        assertThat(result).isEqualTo(response);
    }

    @DisplayName("RequestParam Argument Resolver Test")
    @ParameterizedTest(name = "MethodParameter: {0}")
    @MethodSource("sampleMethodParameter")
    void RequestParamResolveArguments(MethodParameter mp, Object expectedResult) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setParameter("name", expectedResult.toString());

        Object result = findArgumentResolver(mp, request, response);

        assertThat(result).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> sampleMethodParameter() {
        return Stream.of(
                Arguments.of(new MethodParameter(String.class, new Annotation[]{new MockRequestParam()}), "expectedResult"),
                Arguments.of(new MethodParameter(Integer.class, new Annotation[]{new MockRequestParam()}), 50)
        );
    }

    private Object findArgumentResolver(MethodParameter mp, HttpServletRequest request, HttpServletResponse response) {
        for (ArgumentResolver argumentResolver : argumentResolvers) {
            if (argumentResolver.supports(mp)) {
                return argumentResolver.resolveArgument(mp, request, response);
            }
        }

        throw new IllegalArgumentException("does not have argumentResolver");
    }

}
