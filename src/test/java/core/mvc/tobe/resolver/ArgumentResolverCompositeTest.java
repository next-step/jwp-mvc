package core.mvc.tobe.resolver;

import core.mvc.tobe.MyController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class ArgumentResolverCompositeTest {

    @DisplayName("인자를 올바르게 합치는지 확인")
    @Test
    void getArguments() throws NoSuchMethodException {

        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "dowon");

        Class<MyController> clazz = MyController.class;
        Method method = clazz.getDeclaredMethod("findUserId", String.class);

        // when
        ArgumentResolverComposite argumentResolverComposite = new ArgumentResolverComposite(method);
        Object[] arguments = argumentResolverComposite.getArguments(request);

        // then
        assertThat(arguments)
                .containsOnly("dowon");
    }
}