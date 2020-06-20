package core.mvc.support.resolvers;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.support.MethodParameter;
import core.mvc.support.MethodSignature;
import core.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrimitiveTypeHandlerMethodArgumentResolverTest {

    private static final HandlerMethodArgumentResolver resolver = new PrimitiveTypeHandlerMethodArgumentResolver();
    private static final Logger log = LoggerFactory.getLogger(PrimitiveTypeHandlerMethodArgumentResolverTest.class);

    @DisplayName("기본 타입 리졸버는 어노테이션이 붙어 있지 않는 경우 지원한다.")
    @Test
    void test_support_condition() throws Exception {
        final Method method = DummyController.class
                .getDeclaredMethod("primitive1", String.class, String.class, long.class);
        final MethodSignature methodSignature = new MethodSignature(method);
        final MethodParameter userIdParam = methodSignature.getMethodParameters().get(0);
        final MethodParameter passwordParam = methodSignature.getMethodParameters().get(1);
        assertTrue(resolver.supportsParameter(userIdParam));
        assertTrue(resolver.supportsParameter(passwordParam));
    }

    @DisplayName("인자를 잘 가지고 오는지 테스트한다.")
    @Test
    void test_resolve() throws Exception {
        final Method method = DummyController.class
                .getDeclaredMethod("primitive1", String.class, String.class, long.class);
        final MethodSignature methodSignature = new MethodSignature(method);
        final MethodParameter userIdParam = methodSignature.getMethodParameters().get(0);
        final MethodParameter passwordParam = methodSignature.getMethodParameters().get(1);
        final MethodParameter ageParam = methodSignature.getMethodParameters().get(2);

        final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        final String answerId = "hyeyoom";
        request.addParameter("userId", answerId);
        final String answerPassword = "1234abcd";
        request.addParameter("password", answerPassword);
        final long answerAge = 100;
        request.addParameter("age", String.valueOf(answerAge));

        final String userId = (String) resolver.resolveArgument(userIdParam, request);
        final String password = (String) resolver.resolveArgument(passwordParam, request);
        final long age = (long) resolver.resolveArgument(userIdParam, request);

        assertEquals(userId, answerId);
        assertEquals(password, answerPassword);
        assertEquals(age, answerAge);
    }

    @Controller
    public static class DummyController {
        @RequestMapping(value = "/users", method = RequestMethod.POST)
        public ModelAndView primitive1(String userId, String password, long age) {
            return null;
        }
    }
}