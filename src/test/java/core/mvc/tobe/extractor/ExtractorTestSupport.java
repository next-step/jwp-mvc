package core.mvc.tobe.extractor;

import core.mvc.tobe.ParameterInfo;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

abstract class ExtractorTestSupport {

    private RequestParameterExtractor extractor;

    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        extractor = getExtractor();

        request = getRequest();
        response = new MockHttpServletResponse();
    }

    abstract RequestParameterExtractor getExtractor();

    HttpServletRequest getRequest() {
        return new MockHttpServletRequest();
    }

    boolean isSupport(final ParameterInfo parameterInfo) {
        return extractor.isSupport(parameterInfo);
    }

    Object extract(final ParameterInfo parameterInfo) {
        return extractor.extract(parameterInfo, request, response);
    }

    ParameterInfo getParameterInfo(final Class<?> clazz,
                                   final String methodName,
                                   final Class<?>... parameterTypes) {
        try {
            final Method targetMethod = clazz.getMethod(methodName, parameterTypes);
            final Parameter parameter = targetMethod.getParameters()[0];

            return new ParameterInfo(parameter, "value", "/{value}");
        } catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
