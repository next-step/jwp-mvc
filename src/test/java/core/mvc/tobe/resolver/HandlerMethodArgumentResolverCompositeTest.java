package core.mvc.tobe.resolver;

import core.mvc.tobe.TestUserController;
import core.mvc.tobe.resolver.method.MethodParameter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;

class HandlerMethodArgumentResolverCompositeTest {


    @Test
    @DisplayName("path variable resolver 찾기")
    void find_pathVariable() throws Exception {
        HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/users/123");
        MockHttpServletResponse response = new MockHttpServletResponse();

        Class<TestUserController> controller = TestUserController.class;
        Method method = controller.getDeclaredMethod("show_pathvariable", long.class);
        PathArgumentResolver pathArgumentResolver = new PathArgumentResolver();
        MethodParameter methodParameter = new MethodParameter(method, 0);
        Assertions.assertThat(pathArgumentResolver.supportsParameter(methodParameter)).isTrue();
        Object value = composite.resolveArgument(methodParameter, request, response);
        Assertions.assertThat(value).isEqualTo("123");
    }
}
