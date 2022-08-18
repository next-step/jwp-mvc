package core.mvc.tobe.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import core.mvc.tobe.TestUserController;

public class HttpServletArgumentResolverTest {

	private HttpRequestArgumentResolver httpRequestArgumentResolver = new HttpRequestArgumentResolver();
	private HttpResponseArgumentResolver httpResponseArgumentResolver = new HttpResponseArgumentResolver();

	@Test
	@DisplayName("Argument HttpServlet Request/Response 지원 여부 테스트")
	public void supportsParameter() throws NoSuchMethodException {
		Method method = TestUserController.class.getDeclaredMethod("index", HttpServletRequest.class, HttpServletResponse.class);
		HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

		assertThat(httpRequestArgumentResolver.supportsParameter(methodParameters[0])).isTrue();
		assertThat(httpResponseArgumentResolver.supportsParameter(methodParameters[1])).isTrue();
	}

	@Test
	@DisplayName("HttpServlet Request/Response Argument 변환 테스트")
	public void resolveArgument() throws NoSuchMethodException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		Method method = TestUserController.class.getDeclaredMethod("index", HttpServletRequest.class, HttpServletResponse.class);
		HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

		assertThat(httpRequestArgumentResolver.resolveArgument(methodParameters[0], request, response)).isEqualTo(request);
		assertThat(httpResponseArgumentResolver.resolveArgument(methodParameters[1], request, response)).isEqualTo(response);
	}
}
