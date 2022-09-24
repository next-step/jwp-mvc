package core.mvc.tobe.resolver;

import core.mvc.tobe.TestUserController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestAndResponseArgumentResolverTest {

    private final RequestArgumentResolver httpRequestArgumentResolver = new RequestArgumentResolver();
    private final ResponseArgumentResolver httpResponseArgumentResolver = new ResponseArgumentResolver();

    @Test
    @DisplayName("Argument HttpServlet Request/Response 지원 여부 테스트")
    public void supportsParameter() throws NoSuchMethodException {
        Method method = TestUserController.class.getDeclaredMethod("index", HttpServletRequest.class, HttpServletResponse.class);
        HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

        assertThat(httpRequestArgumentResolver.supportsParameter(methodParameters[0])).isTrue();
        assertThat(httpResponseArgumentResolver.supportsParameter(methodParameters[1])).isTrue();
    }

    @Test
    @DisplayName("HttpServlet Request/Response Argument 변환 테스트")
    public void resolveArgument() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Method method = TestUserController.class.getDeclaredMethod("index", HttpServletRequest.class, HttpServletResponse.class);
        HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

        assertThat(httpRequestArgumentResolver.resolveArgument(methodParameters[0], request, response)).isEqualTo(request);
        assertThat(httpResponseArgumentResolver.resolveArgument(methodParameters[1], request, response)).isEqualTo(response);
    }
}
