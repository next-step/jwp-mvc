package core.mvc.tobe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HandlerExecutionTest {
    private Class<TestHandlerController> clazz;

    @BeforeEach
    void setUp() {
        clazz = TestHandlerController.class;
    }

    @DisplayName("생성 확인")
    @Test
    void create_execution() throws Exception {
        Method method = clazz.getDeclaredMethod("execution", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = HandlerExecution.of(clazz, method);
        assertThat(handlerExecution).isNotNull();
    }

    @DisplayName("Method가 null일 경우 실패 확인")
    @Test
    void failed_execution_case1() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> {
            HandlerExecution.of(null, null);
        });
    }

    @DisplayName("Return Type이 다른 경우 실패 확인")
    @Test
    void failed_execution_case2() throws Exception {
        Method method = clazz.getDeclaredMethod("otherExecution");
        assertThrows(UnsupportedOperationException.class, () -> {
            HandlerExecution.of(clazz, method);
        });
    }


    @DisplayName("Parameter Type이 다른 경우 실패 확인")
    @Test
    void failed_execution_case3() throws Exception {
        Method method = clazz.getDeclaredMethod("otherExecution");
        assertThrows(UnsupportedOperationException.class, () -> {
            HandlerExecution.of(clazz, method);
        });
    }
}