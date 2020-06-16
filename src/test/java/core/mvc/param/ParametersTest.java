package core.mvc.param;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("파라미터 여러개를 표현하기 위함")
class ParametersTest {

    @Test
    @DisplayName("method 를 하나 받으면 함수 내에 존재하는 파라미터를 가지고 parameters 객체를 만든다.")
    void constructor() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("x", "1");
        request.addParameter("y", "2");

        Method method = MethodTestClass.class
                .getDeclaredMethods()[0];

        Parameters parameters = new Parameters(method);

        Object[] values = parameters.extractValues(request);
        assertThat(values[0]).isEqualTo(1);
        assertThat(values[1]).isEqualTo(2);
    }

    public static class MethodTestClass {
        public int test(int x, int y) {
            return x + y;
        }
    }

}