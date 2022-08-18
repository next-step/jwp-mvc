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

public class RequestParamArgumentResolverTest {

	ArgumentResolver argumentResolver = new RequestParamArgumentResolver();

	@Test
	@DisplayName("@RequestParam 어노테이션 선언 하지 않을 경우, 지원 여부 테스트")
	public void notSupportsParameter() throws NoSuchMethodException {
		Method method = TestUserController.class.getDeclaredMethod("create_javabean", TestUser.class);
		HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

		assertThat(argumentResolver.supportsParameter(methodParameters[0])).isFalse();
	}

	@Test
	@DisplayName("@RequestParam 어노테이션 선언 한 경우, 지원 여부 테스트")
	public void supportsParameter() throws NoSuchMethodException {
		Method method = TestUserController.class.getDeclaredMethod("create_string", String.class, String.class);
		HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

		assertThat(argumentResolver.supportsParameter(methodParameters[0])).isTrue();
		assertThat(argumentResolver.supportsParameter(methodParameters[1])).isTrue();
	}

	@Test
	@DisplayName("String Type Argument 변환 테스트")
	public void resolveStringTypeArgument() throws NoSuchMethodException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.addParameter("userId", "javajigi");
		request.addParameter("password", "password");

		Method method = TestUserController.class.getDeclaredMethod("create_string", String.class, String.class);
		HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

		assertThat(argumentResolver.resolveArgument(methodParameters[0], request, response)).isEqualTo("javajigi");
		assertThat(argumentResolver.resolveArgument(methodParameters[1], request, response)).isEqualTo("password");
	}
}
