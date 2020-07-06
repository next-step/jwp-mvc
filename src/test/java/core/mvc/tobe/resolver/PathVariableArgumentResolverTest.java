package core.mvc.tobe.resolver;

import core.mvc.tobe.MyController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.assertj.core.api.Assertions.assertThat;

class PathVariableArgumentResolverTest {

    @DisplayName("Path variable 매핑")
    @Test
    void resolve() throws NoSuchMethodException {

        // given
        ArgumentResolver argumentResolver = new PathVariableArgumentResolver();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users/1");

        Class<MyController> clazz = MyController.class;
        Method method = clazz.getDeclaredMethod("findUserId2", String.class);
        Parameter[] parameters = method.getParameters();
        int index = 0;

        // when
        Object args = argumentResolver.resolve(request, method, parameters[index], index);

        // then
        assertThat(args)
                .isEqualTo("1");
    }
}