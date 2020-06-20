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

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PrimitiveTypeHandlerMethodArgumentResolverTest {

    private static final HandlerMethodArgumentResolver resolver = new PrimitiveTypeHandlerMethodArgumentResolver();
    private static final Logger log = LoggerFactory.getLogger(PrimitiveTypeHandlerMethodArgumentResolverTest.class);

    @DisplayName("기본 타입 리졸버는 어노테이션이 붙어 있지 않는 경우 지원한다.")
    @Test
    void test_support_condition() throws Exception {
        final Method method = DummyController.class.getDeclaredMethod("primitive1", String.class, String.class);
        final MethodSignature methodSignature = new MethodSignature(method);
        final MethodParameter userIdParam = methodSignature.getMethodParameters().get(0);
        final MethodParameter passwordParam = methodSignature.getMethodParameters().get(1);
        assertTrue(resolver.supportsParameter(userIdParam));
        assertTrue(resolver.supportsParameter(passwordParam));
    }

    @Controller
    public static class DummyController {
        @RequestMapping(value = "/users", method = RequestMethod.POST)
        public ModelAndView primitive1(String userId, String password) {
            return null;
        }

        @RequestMapping(value = "/users", method = RequestMethod.POST)
        public ModelAndView primitive2(long id, int age) {
            return null;
        }
    }
}