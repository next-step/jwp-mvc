package study.reflections;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.MyController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @DisplayName("Java Reflection API를 통해 Parameter 이름 조회시, arg0, arg1,... 등 argN 값으로 조회된다.")
    @Test
    void getParameterNames_return_argN() throws NoSuchMethodException {
        Class<MyController> myControllerClass = MyController.class;

        Method findUserId = myControllerClass.getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);

        List<String> result = Arrays.stream(findUserId.getParameters())
                .map(parameter -> parameter.getName())
                .collect(Collectors.toList());

        assertThat(result).containsExactly("arg0", "arg1");
    }

    @DisplayName("Java Reflection API를 통해 Parameter 타입을 조회된다.")
    @Test
    void getParameter_type_return_argN() throws NoSuchMethodException {
        Class<MyController> myControllerClass = MyController.class;

        Method findUserId = myControllerClass.getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);

        assertThat(findUserId.getParameterTypes()).contains(HttpServletRequest.class, HttpServletResponse.class);
    }
}
