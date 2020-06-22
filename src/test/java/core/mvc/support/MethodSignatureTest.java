package core.mvc.support;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

class MethodSignatureTest {

    private static final Logger log = LoggerFactory.getLogger(MethodSignatureTest.class);

    @Test
    void test_extract_signature_from_method() throws Exception {

    }

    @DisplayName("실험용 테스트")
    @Test
    void test_test() throws Exception {
        final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        final Method method = MethodSignatureTestObject.class.getDeclaredMethod(
                "multipleAnnotations", long.class, String.class, DummyData.class);

        log.info("method parameter count: {}", method.getParameterCount());
        final String[] parameterNames = nameDiscoverer.getParameterNames(method);
        final Parameter[] parameters = method.getParameters();
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < parameters.length; i++) {
            String name = parameterNames[i];
            System.out.println(name + "(" + parameters[i].getType() + ")");
            final Annotation[] annotatedParameters = parameterAnnotations[i];
            System.out.println(annotatedParameters.length);
            System.out.println("==============================");
        }

    }

    public static class MethodSignatureTestObject {

        public void multipleAnnotations(
                @RequestParam @PathVariable long id,
                String notAnnotated,
                DummyData data) {
            // no-op
        }
    }

    public static class DummyData {
        private long id;
        private String name;
        private String message;
    }
}