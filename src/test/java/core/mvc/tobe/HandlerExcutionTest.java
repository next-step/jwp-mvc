package core.mvc.tobe;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class HandlerExcutionTest {

    @Test
    public void create() throws Exception {
        Class clazz = MyController.class;

        Method method = clazz.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);

        HandlerExecution execution = new HandlerExecution(clazz, method);

        assertThat(execution).isNotEqualTo(null);
    }
}
