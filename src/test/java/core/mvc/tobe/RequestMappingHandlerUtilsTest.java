package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Set;

public class RequestMappingHandlerUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(RequestMappingHandlerUtilsTest.class);

    @Test
    public void getMethodWithRequestMappingAnnotation() {
        RequestMappingHandlerUtils helper = new RequestMappingHandlerUtils(MyController.class);

        Set<Method> methods = helper.getMethodsAnnotatedWith(RequestMapping.class);
        methods.stream()
                .forEach(m -> {
                    logger.debug(m.getName());
                });

    }

    @Test
    public void getAnnotationValue() {
        RequestMappingHandlerUtils helper = new RequestMappingHandlerUtils(MyController.class);
        Set<Method> methods = helper.getMethodsAnnotatedWith(RequestMapping.class);

        methods.stream()
                .forEach(m -> {
                    RequestMapping r = m.getAnnotation(RequestMapping.class);
                    logger.debug("value: {}, method: {}", r.value(), r.method());
                });

    }
}
