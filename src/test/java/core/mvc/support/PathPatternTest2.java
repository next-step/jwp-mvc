package core.mvc.support;

import core.mvc.ModelAndView;
import core.mvc.handler.HandlerExecution;
import core.mvc.handlerMapping.AnnotationHandlerMapping;
import core.mvc.tobe.TestUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class PathPatternTest2 {

    private static final String BASE_PACKAGE = "testController";
    private static AnnotationHandlerMapping ahm;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private HandlerMethodArgumentResolverComposite handlerMethodArgumentResolverComposite;

    @BeforeAll
    static void setUp() {
        ahm = new AnnotationHandlerMapping(BASE_PACKAGE);
        ahm.initialize();
    }

    @BeforeEach
    void setUpEach() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        handlerMethodArgumentResolverComposite = new HandlerMethodArgumentResolverComposite();
    }

    @Test
    @DisplayName("핸들러의 파라미터가 @ReuqestParam 을 생략한 String 타입인 경우 Request의 파라미터 값을 받아올 수 있다")
    public void stringParam() throws Exception {
        // given
        request.setRequestURI("/users/a");
        request.setParameter("userId", "bactoria");
        request.setParameter("password", "test");
        request.setMethod("POST");

        final HandlerExecution handler = ahm.getHandler(request);

        handlerMethodArgumentResolverComposite.addResolver(new RequestParamResolver());

        // when
        final ModelAndView mav = handler.handle(request, response, handlerMethodArgumentResolverComposite);

        // then
        assertThat(mav).isNotNull();
        assertThat(mav.getModel().get("userId")).isEqualTo("bactoria");
        assertThat(mav.getModel().get("password")).isEqualTo("test");
    }

    @Test
    @DisplayName("핸들러의 파라미터가 @ReuqestParam 을 생략한 int, long 타입인 경우 Request의 파라미터 값을 받아올 수 있다")
    public void numberParam() throws Exception {
        // given
        request.setRequestURI("/users/b");
        request.setParameter("id", "20");
        request.setParameter("age", "30");
        request.setMethod("POST");

        final HandlerExecution handler = ahm.getHandler(request);

        handlerMethodArgumentResolverComposite.addResolver(new RequestParamResolver());

        // when
        final ModelAndView mav = handler.handle(request, response, handlerMethodArgumentResolverComposite);

        // then
        assertThat(mav).isNotNull();
        assertThat(mav.getModel().get("id")).isEqualTo(20L);
        assertThat(mav.getModel().get("age")).isEqualTo(30);
    }

    @Test
    @DisplayName("핸들러의 파라미터가 객체 타입인 경우 Request의 파라미터 값을 받아올 수 있다")
    public void objectParam() throws Exception {
        // given
        final String userId = "bactoria";
        final String password = "test";
        final int age = 28;
        final TestUser testUser = new TestUser(userId, password, age);

        request.setRequestURI("/users/c");
        request.setParameter("userId", userId);
        request.setParameter("password", password);
        request.setParameter("age", String.valueOf(age));
        request.setMethod("POST");
        final HandlerExecution handler = ahm.getHandler(request);

        handlerMethodArgumentResolverComposite.addResolver(new ModelAttributeResolver());

        // when
        final ModelAndView mav = handler.handle(request, response, handlerMethodArgumentResolverComposite);

        // then
        assertThat(mav).isNotNull();
        assertThat(mav.getModel().get("testUser")).isEqualTo(testUser);
    }

    @Test
    @DisplayName("핸들러의 파라미터에 @PathVariable 이 존재할 경우 path에서 지정한 패턴에 일치하는 값을 받아올 수 있")
    public void pathParam() throws Exception {
        // given
        final long id = 37;
        request.setRequestURI("/users/" + id);
        request.setMethod("GET");

        final HandlerExecution handler = ahm.getHandler(request);

        handlerMethodArgumentResolverComposite.addResolver(new PathVariableResolver());

        // when
        final ModelAndView mav = handler.handle(request, response, handlerMethodArgumentResolverComposite);

        // then
        assertThat(mav).isNotNull();
        assertThat(mav.getModel().get("id")).isEqualTo(id);
    }

}