package core.mvc.tobe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HandlerExecutionTest {

    private Class<?> clazz;

    @BeforeEach
    void setUp() {
        clazz = TestUserController.class;
    }

    @Test
    void name() throws Exception {
        HandlerExecution handlerExecution = new HandlerExecution(
                clazz.newInstance(),
                clazz.getDeclaredMethod("createString", String.class, String.class));

    }
}