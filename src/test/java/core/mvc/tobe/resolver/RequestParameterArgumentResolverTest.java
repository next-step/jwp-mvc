package core.mvc.tobe.resolver;

import core.mvc.tobe.MyController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.assertj.core.api.Assertions.assertThat;

class RequestParameterArgumentResolverTest {

    @DisplayName("Request parameter 매핑")
    @Test
    void resolve() throws NoSuchMethodException {

        // given
        ArgumentResolver argumentResolver = new RequestParameterArgumentResolver();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "dowon");

        Class<MyController> clazz = MyController.class;
        Method method = clazz.getDeclaredMethod("findUserId", String.class);
        Parameter[] parameters = method.getParameters();
        int index = 0;

        // when
        Object args = argumentResolver.resolve(request, method, parameters[index], index);

        // then
        assertThat(args)
                .isEqualTo("dowon");
    }
}