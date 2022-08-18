package core.mvc.tobe.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import core.mvc.tobe.TestUser;
import core.mvc.tobe.TestUserController;

public class BeanTypeArgumentResolverTest {
	private final ArgumentResolver argumentResolver = new BeanTypeArgumentResolver();

	@Test
	@DisplayName("Bean Type 인 경우, 지원 성공 테스트")
	public void supportsParameter() throws NoSuchMethodException {
		Method method = TestUserController.class.getDeclaredMethod("create_javabean", TestUser.class);
		HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

		assertThat(argumentResolver.supportsParameter(methodParameters[0])).isTrue();
	}

	@Test
	@DisplayName("Bean Type 이 아닌 경우, 지원 실패 테스트")
	public void notSupportsParameter() throws NoSuchMethodException {
		Method method = TestUserController.class.getDeclaredMethod("create_string2", String.class, String.class);
		HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

		assertThat(argumentResolver.supportsParameter(methodParameters[0])).isFalse();
		assertThat(argumentResolver.supportsParameter(methodParameters[1])).isFalse();
	}

	@Test
	@DisplayName("Bean Type 이 아닌 경우, 지원 실패 테스트2")
	public void notSupportsParameter2() throws NoSuchMethodException {
		Method method = TestUserController.class.getDeclaredMethod("create_int_long2", long.class, int.class);
		HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

		assertThat(argumentResolver.supportsParameter(methodParameters[0])).isFalse();
		assertThat(argumentResolver.supportsParameter(methodParameters[1])).isFalse();
	}

	@Test
	@DisplayName("Bean Type Argument 변환 테스트")
	public void resolveBeanTypeArgument() throws NoSuchMethodException {
		TestUser expect = new TestUser("javajigi", "password", 10);

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		request.addParameter("userId", expect.getUserId());
		request.addParameter("password", expect.getPassword());
		request.addParameter("age", String.valueOf(expect.getAge()));

		Method method = TestUserController.class.getDeclaredMethod("create_javabean", TestUser.class);
		HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

		assertThat(argumentResolver.resolveArgument(methodParameters[0], request, response)).isEqualTo(expect);
	}
}
