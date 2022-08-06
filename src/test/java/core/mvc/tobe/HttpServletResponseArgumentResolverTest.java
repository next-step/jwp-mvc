package core.mvc.tobe;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import next.controller.HomeController;

class HttpServletResponseArgumentResolverTest extends ArgumentResolverTest {

    private final ArgumentResolver argumentResolver = new HttpServletResponseArgumentResolver();

    @DisplayName("핸들러 메서드의 HttpServletResponse 인자를 매핑한다.")
    @Test
    void resolveArgument() throws Exception {
        Class<HomeController> clazz = HomeController.class;
        Method method = clazz.getMethod("home", HttpServletRequest.class, HttpServletResponse.class);
        MethodParameter[] methodParameters = getMethodParameters(method);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        List<Object> argumentValues = Arrays.stream(methodParameters)
            .map(parameter -> argumentResolver.resolveArgument(parameter, request, response))
            .collect(Collectors.toList());

        assertThat(argumentValues).contains(response);
    }
}
