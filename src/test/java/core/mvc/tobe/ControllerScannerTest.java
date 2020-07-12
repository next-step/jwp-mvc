package core.mvc.tobe;

import core.mvc.exception.ReflectionsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class ControllerScannerTest {
    @DisplayName("core.mvc.tobe 패키지에서 @Controller 어노테이션이 붙은 컨트롤러 스캐닝")
    @Test
    void test_scanController() {
        // given
        try {
            ControllerScanner sc = new ControllerScanner("core.mvc.tobe");
            // when
            sc.scan();
            // then
            assertThat(sc.size()).isEqualTo(1);
        } catch (ReflectionsException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("@RequestMapping 어노테이션이 붙은 메소드 스캐닝")
    @Test
    void test_scanMethodsWithRequestMapping() {
        // given
        try {
            ControllerScanner sc = new ControllerScanner("core.mvc.tobe");

            Class<MyController> clazz = MyController.class;
            Method findUserId = clazz.getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
            Method save = clazz.getDeclaredMethod("save", HttpServletRequest.class, HttpServletResponse.class);
            // when
            Map<HandlerKey, HandlerExecution> result = sc.scan();
            // then
            assertThat(result).hasSize(2);
        } catch (NoSuchMethodException | ReflectionsException e) {
            throw new RuntimeException(e);
        }
    }

}