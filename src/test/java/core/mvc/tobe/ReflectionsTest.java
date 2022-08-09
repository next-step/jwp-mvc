package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import org.junit.jupiter.api.Test;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionsTest {

    private Reflections reflections = new Reflections("core.mvc.tobe");

    @Test
    void getTypesAnnotatedWith() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        assertThat(controllerClasses).contains(MyController.class);
    }

    @Test
    void getAllMethods() {
        Class<MyController> myControllerClass = MyController.class;

        Set<Method> collect = ReflectionUtils.getAllMethods(myControllerClass, ReflectionUtils.withAnnotation(RequestMapping.class));
        assertThat(collect).contains(MyController.class.getDeclaredMethods());
    }
}
