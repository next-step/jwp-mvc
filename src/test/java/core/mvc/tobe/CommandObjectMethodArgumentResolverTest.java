package core.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;


import static org.assertj.core.api.Assertions.assertThat;

class CommandObjectMethodArgumentResolverTest {

    @DisplayName("파라미터가 커맨드 객체인 경우, 커맨드 객체에 값을 셋팅해서 반환")
    @Test
    void test_resolve() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String userId = "crystal";
        String password = "password";
        String age = "26";
        request.addParameter("userId", userId);
        request.addParameter("password", password);
        request.addParameter("age", age);

        MethodParameter methodParameter = new MethodParameter("testUser", TestUser.class);
        CommandObjectMethodArgumentResolver resolver = new CommandObjectMethodArgumentResolver();
        // when // then
        boolean supports = resolver.supports(methodParameter);
        assertThat(supports).isTrue();

        Object arg = resolver.resolveArgument(methodParameter, request);
        assertThat(arg).isInstanceOf(TestUser.class);

        TestUser testUser = (TestUser) arg;
        assertThat(testUser.getUserId()).isEqualTo("crystal");
        assertThat(testUser.getPassword()).isEqualTo("password");
        assertThat(testUser.getAge()).isEqualTo(26);
    }
}