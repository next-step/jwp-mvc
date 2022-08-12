package core.mvc.tobe;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class ModelAttributeMethodArgumentResolverTest extends ArgumentResolverTest {

    private final ArgumentResolver argumentResolver = new ModelAttributeMethodArgumentResolver();

    @DisplayName("핸들러 메서드의 객체 타입 인자를 매핑한다.")
    @Test
    void resolveArgument() throws Exception {
        Class<TestUserController> clazz = TestUserController.class;
        Method method = clazz.getMethod("create_javabean", TestUser.class);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "user");
        request.addParameter("password", "1234");
        request.addParameter("age", "30");
        MockHttpServletResponse response = new MockHttpServletResponse();

        MethodParameter[] methodParameters = getMethodParameters(method);
        List<Object> argumentValues = Arrays.stream(methodParameters)
            .map(parameter -> argumentResolver.resolveArgument(parameter, request, response))
            .collect(Collectors.toList());

        TestUser testUser = (TestUser) argumentValues.get(0);
        assertThat(testUser.getUserId()).isEqualTo("user");
        assertThat(testUser.getPassword()).isEqualTo("1234");
        assertThat(testUser.getAge()).isEqualTo(30);
    }
}
