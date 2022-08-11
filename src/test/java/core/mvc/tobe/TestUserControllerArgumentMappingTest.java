package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테스트 유저 컨트롤러 인자 매핑")
class TestUserControllerArgumentMappingTest {

    private static final Class<TestUserController> USER_CONTROLLER_CLASS = TestUserController.class;
    private static final TestUserController USER_CONTROLLER = new TestUserController();

    @Test
    @DisplayName("문자열 인자 매핑")
    void create_string() throws Exception {
        //given
        Method method = USER_CONTROLLER_CLASS.getMethod("create_string", String.class, String.class);

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        String userId = "userId";
        String password = "password";
        servletRequest.addParameters(Map.of("userId", userId, "password", password));
        //when
        ModelAndView result = new HandlerExecution(USER_CONTROLLER, method)
                .handle(servletRequest, new MockHttpServletResponse());
        //then
        assertThat(result)
                .extracting(find("userId"), find("password"))
                .containsExactly(userId, password);
    }

    @Test
    @DisplayName("primitive 인자 매핑")
    void create_int_long() throws Exception {
        //given
        Method method = USER_CONTROLLER_CLASS.getMethod("create_int_long", Long.TYPE, Integer.TYPE);

        long oneId = 1L;
        int tenAge = 10;
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.addParameters(Map.of("id", String.valueOf(oneId), "age", String.valueOf(tenAge)));
        //when
        ModelAndView result = new HandlerExecution(USER_CONTROLLER, method)
                .handle(servletRequest, new MockHttpServletResponse());
        //then
        assertThat(result)
                .extracting(find("id"), find("age"))
                .containsExactly(oneId, tenAge);
    }

    @Test
    @DisplayName("TestUser 객체 매핑")
    void create_javabean() throws Exception {
        Method method = USER_CONTROLLER_CLASS.getMethod("create_javabean", TestUser.class);

        String oneId = "1";
        String password = "password";
        int tenAge = 10;
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.addParameters(Map.of("userId", oneId, "password", password, "age", String.valueOf(tenAge)));
        //when
        ModelAndView result = new HandlerExecution(USER_CONTROLLER, method)
                .handle(servletRequest, new MockHttpServletResponse());
        //then
        assertThat(result)
                .extracting(find("testUser"))
                .isEqualTo(new TestUser(oneId, password, tenAge));
    }

    @Test
    @DisplayName("경로 변수 인자 매핑")
    void show_pathvariable() throws Exception {
        //given
        Method method = USER_CONTROLLER_CLASS.getMethod("show_pathvariable", Long.TYPE);

        long oneId = 1;
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI(String.format("/users/%d", oneId));
        //when
        ModelAndView result = new HandlerExecution(USER_CONTROLLER, method)
                .handle(servletRequest, new MockHttpServletResponse());
        //then
        assertThat(result)
                .extracting(find("id"))
                .isEqualTo(oneId);
    }

    private Function<ModelAndView, Object> find(String key) {
        return model -> model.getObject(key);
    }
}
