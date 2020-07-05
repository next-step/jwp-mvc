package core.mvc.tobe;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class ArgumentResolverTest {

    @Test
    void resolve() throws NoSuchMethodException {

        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "dowon");

        Class<MyController> clazz = MyController.class;
        Method method = clazz.getDeclaredMethod("findUserId", String.class);

        // when
        Object[] args = ArgumentResolver.resolve(request, method);

        // then
        assertThat(args)
                .containsOnly("dowon");
    }
}