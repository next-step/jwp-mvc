package core.mvc.tobe.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import core.mvc.tobe.TestUserController;

public class SimpleTypeArgumentResolverTest {
	private final ArgumentResolver argumentResolver = new SimpleTypeArgumentResolver();

	@Test
	@DisplayName("Simple Type 인 경우, 지원 성공 테스트")
	public void supportsParameter() throws NoSuchMethodException {
		Method method = TestUserController.class.getDeclaredMethod("create_string2", String.class, String.class);
		HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

		assertThat(argumentResolver.supportsParameter(methodParameters[0])).isTrue();
		assertThat(argumentResolver.supportsParameter(methodParameters[1])).isTrue();
	}

	@Test
	@DisplayName("Simple Type 인 경우, 지원 성공 테스트2")
	public void supportsParameter2() throws NoSuchMethodException {
		Method method = TestUserController.class.getDeclaredMethod("create_int_long2", long.class, int.class);
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

		Method method = TestUserController.class.getDeclaredMethod("create_string2", String.class, String.class);
		HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

		assertThat(argumentResolver.resolveArgument(methodParameters[0], request, response)).isEqualTo("javajigi");
		assertThat(argumentResolver.resolveArgument(methodParameters[1], request, response)).isEqualTo("password");
	}

	@Test
	@DisplayName("Primitive Type Argument 변환 테스트")
	public void resolvePrimitiveTypeArgument() throws NoSuchMethodException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.addParameter("id", "1");
		request.addParameter("age", "12");

		Method method = TestUserController.class.getDeclaredMethod("create_int_long", long.class, int.class);
		HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

		assertThat(argumentResolver.resolveArgument(methodParameters[0], request, response)).isEqualTo(1L);
		assertThat(argumentResolver.resolveArgument(methodParameters[1], request, response)).isEqualTo(12);
	}
}
