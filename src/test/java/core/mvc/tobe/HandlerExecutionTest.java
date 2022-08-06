package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import core.mvc.ModelAndView;
import java.lang.reflect.Method;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class HandlerExecutionTest {

    @DisplayName("handler를 실행한 후 ModelAndView를 반환한다.")
    @Test
    void handle_should_be_returned_model_and_view() throws Exception {
        // given
        final MyController myController = MyController.class.getConstructor().newInstance();
        final Method method = MyController.class.getMethod("welcome", HttpServletRequest.class, HttpServletResponse.class);

        final HandlerExecutable handlerExecution = new HandlerExecution(myController, method);

        // when
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/welcome");
        final MockHttpServletResponse response = new MockHttpServletResponse();

        final ModelAndView actual = handlerExecution.handle(request, response);

        // then
        final ModelAndView expected = new ModelAndView(new JspView("/home.jsp"));
        expected.addObject("hi", "there");

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Handler가 실행가능한 상태인지 확인한다")
    @ParameterizedTest
    @MethodSource
    void executable(Object handler, Method method, boolean expected) {
        final HandlerExecutable handlerExecution = new HandlerExecution(handler, method);

        final boolean actual = handlerExecution.executable();

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> executable() throws Exception {
        final MyController myController = MyController.class.getConstructor().newInstance();
        final Method method = MyController.class.getMethod("welcome", HttpServletRequest.class, HttpServletResponse.class);

        return Stream.of(
            Arguments.of(null, null, false),
            Arguments.of(myController, method, true)
        );
    }
}
