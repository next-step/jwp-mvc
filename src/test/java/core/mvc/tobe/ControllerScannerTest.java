package core.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {
    @DisplayName("core.mvc.tobe 패키지에서 @Controller 어노테이션이 붙은 컨트롤러 스캐닝")
    @Test
    void test_scanController() {
        // given
        ControllerScanner sc = new ControllerScanner("core.mvc.tobe");
        // when
        Set<Class<?>> controllers = sc.scan();
        // then
        assertThat(controllers).hasSize(1);
        assertThat(controllers.contains(MyController.class)).isTrue();
    }

    @DisplayName("@RequestMapping 어노테이션이 붙은 메소드 스캐닝")
    @Test
    void test_scanMethodsWithRequestMapping() {
        // given
        ControllerScanner sc = new ControllerScanner("core.mvc.tobe");
        sc.scan();

        Class<MyController> clazz = MyController.class;
        try {
            Method findUserId = clazz.getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
            Method save = clazz.getDeclaredMethod("save", HttpServletRequest.class, HttpServletResponse.class);
            // when
            Set<Method> methodsWithRequestMapping = sc.getMethodsWithRequestMapping(clazz);
            // then
            assertThat(methodsWithRequestMapping).containsExactlyInAnyOrder(findUserId, save);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}