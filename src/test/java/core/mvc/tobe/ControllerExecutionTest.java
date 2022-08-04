package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import core.mvc.ModelAndView;
import core.mvc.asis.LegacyController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class ControllerExecutionTest {

    @DisplayName("컨트롤러 실행 결과 리다이렉트 ModelAndView를 반환한다")
    @Test
    void redirect_response() throws Exception {
        // given
        final LegacyController legacyController = new LegacyController();
        final ControllerExecution controllerExecution = new ControllerExecution(legacyController);

        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();

        // given
        final ModelAndView actual = controllerExecution.handle(request, response);

        // then
        final ModelAndView expected = new ModelAndView(new JspView("redirect:/users/loginForm"));

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("컨트롤러 실행 결과 모델과 함께 페이지 이동 ModelAndView를 반환한다")
    @Test
    void ok_response_with_model_object() throws Exception {
        // given
        final LegacyController legacyController = new LegacyController();
        final ControllerExecution controllerExecution = new ControllerExecution(legacyController);

        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("userId", "there");
        final MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        final ModelAndView actual = controllerExecution.handle(request, response);

        // then
        final ModelAndView expected = new ModelAndView(new JspView("/home.jsp"));
        expected.addObject("welcome", "hi there");

        assertThat(actual).isEqualTo(expected);
    }

}
