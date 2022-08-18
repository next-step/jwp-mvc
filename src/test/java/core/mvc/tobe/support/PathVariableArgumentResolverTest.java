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

public class PathVariableArgumentResolverTest {

	private final ArgumentResolver argumentResolver = new PathVariableArgumentResolver();

	@Test
	@DisplayName("@PathVariable 어노테이션 선언 하지 않을 경우, 지원 실패 테스트")
	public void notSupportsParameter() throws NoSuchMethodException {
		Method method = TestUserController.class.getDeclaredMethod("create_javabean", TestUser.class);
		HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

		assertThat(argumentResolver.supportsParameter(methodParameters[0])).isFalse();
	}

	@Test
	@DisplayName("@PathVariable 어노테이션 선언한 경우, 지원 성공 테스트")
	public void supportsParameter() throws NoSuchMethodException {
		Method method = TestUserController.class.getDeclaredMethod("show_pathvariable", long.class);
		HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

		assertThat(argumentResolver.supportsParameter(methodParameters[0])).isTrue();
	}

	@Test
	@DisplayName("Argument 변환 테스트")
	public void resolveStringTypeArgument() throws NoSuchMethodException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/users/1");

		Method method = TestUserController.class.getDeclaredMethod("show_pathvariable", long.class);
		HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

		assertThat(argumentResolver.resolveArgument(methodParameters[0], request, response)).isEqualTo(1L);
	}
}
