package core.mvc.tobe;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ControllerScannerTest {
    private ControllerScanner controllerScanner;

    @BeforeEach
    void setUp() {
        Object[] scanPackage = Arrays.array("core.mvc.tobe");
        controllerScanner = new ControllerScanner(scanPackage);
    }

    @Test
    @DisplayName("Controller 어노테이션이 붙은 class의 객체를 찾는지 확인한다.")
    void getControllers() {
        Set<Class<?>> controllers = controllerScanner.getControllers();
        Class<?>[] expected = getClassExpected();

        assertThat(controllers).containsAnyOf(expected);
    }

    @Test
    @DisplayName("Controller 어노테이션이 붙은 class의 객체가 생성되었는지 확인한다.")
    void instantiateControllers() {
        Map<Class<?>, Object> instantiateControllers = controllerScanner.getInstantiateControllerMap();
        Class<?>[] expected = getClassExpected();

        for (Class<?> aClass : instantiateControllers.keySet()) {
            assertThat(instantiateControllers.get(aClass)).isInstanceOfAny(expected);
        }
    }

    private Class<?>[] getClassExpected() {
        return Arrays.array(MyController.class);
    }

}
