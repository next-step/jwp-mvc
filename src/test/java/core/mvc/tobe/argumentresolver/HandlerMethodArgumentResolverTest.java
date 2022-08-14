package core.mvc.tobe.argumentresolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import core.mvc.tobe.TestUser;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class HandlerMethodArgumentResolverTest extends AbstractMethodArgumentResolverTest {

    private final HandlerMethodArgumentResolver handlerMethodArgumentResolver = new HandlerMethodArgumentResolver();

    @DisplayName("메소드의 파라미터 타입의 Object 배열을 반환한다")
    @Test
    void returns_an_object_array_of_parameter_types_of_method() {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "admin");
        request.addParameter("password", "pass");

        final Method method = getMethodOfTestUserController("create_string");

        // when
        final Object[] actual = handlerMethodArgumentResolver.resolve(method, request);

        // then
        assertThat(actual).containsExactly("admin", "pass");

    }

    @DisplayName("메소드의 파라미터가 없으면 빈 Object 배열을 반환한다")
    @Test
    void returns_an_empty_object_array_of_does_not_have_parameters_method() {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "admin");
        request.addParameter("password", "pass");

        final Method method = getMethodOfTestUserController("notParameters");

        // when
        final Object[] actual = handlerMethodArgumentResolver.resolve(method, request);

        // then
        assertThat(actual).isEmpty();

    }

    @DisplayName("메소드의 파라미터가 원시 타입인 Object 배열을 반환한다")
    @Test
    void returns_an_object_array_of_primitive_types_of_method() {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("id", String.valueOf(Long.MAX_VALUE));
        request.addParameter("age", String.valueOf(Integer.MAX_VALUE));

        final Method method = getMethodOfTestUserController("create_int_long");

        // when
        final Object[] actual = handlerMethodArgumentResolver.resolve(method, request);

        // then
        assertAll(
            () -> assertThat(actual[0]).isEqualTo(Long.MAX_VALUE),
            () -> assertThat(actual[1]).isEqualTo(Integer.MAX_VALUE)
        );
    }

    @DisplayName("메소드의 파라미터가 wrapper 타입인 Object 배열을 반환한다")
    @Test
    void returns_an_object_array_of_wrapper_types_of_method() {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("id", String.valueOf(Long.MAX_VALUE));
        request.addParameter("age", String.valueOf(Integer.MAX_VALUE));

        final Method method = getMethodOfTestUserController("create_wrapper_int_long");

        // when
        final Object[] actual = handlerMethodArgumentResolver.resolve(method, request);

        // then
        assertAll(
            () -> assertThat(actual[0]).isEqualTo(Long.MAX_VALUE),
            () -> assertThat(actual[1]).isEqualTo(Integer.MAX_VALUE)
        );
    }

    @DisplayName("메소드의 파라미터가 커스텀 클래스 타입인 Object 배열을 반환한다")
    @Test
    void returns_an_object_array_of_custom_class_types_of_method() {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "admin");
        request.addParameter("password", "pass");

        final Method method = getMethodOfTestUserController("create_javabean");

        // when
        final Object[] resolved = handlerMethodArgumentResolver.resolve(method, request);
        final TestUser actual = (TestUser) resolved[0];

        // then
        assertAll(
            () -> assertThat(resolved[0]).isInstanceOf(TestUser.class),
            () -> assertThat(actual.getUserId()).isEqualTo("admin"),
            () -> assertThat(actual.getPassword()).isEqualTo("pass"),
            () -> assertThat(actual.getAge()).isZero()
        );
    }

    @DisplayName("PathVariable 애너테이션이 적용된 파라미터를 타입에 맞춰 Object 배열을 반환한다")
    @Test
    void returns_an_object_array_of_parameters_with_path_variable_types_of_method() {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/100");

        final Method method = getMethodOfTestUserController("show_pathvariable");

        // when
        final Object[] actual = handlerMethodArgumentResolver.resolve(method, request);

        // then
        assertAll(
            () -> assertThat(actual).hasSize(1),
            () -> assertThat(actual[0]).isEqualTo(100L)
        );
    }

    @DisplayName("메소드의 파라미터가 HttpServletRequest 타입인 Object 배열을 반환한다")
    @Test
    void returns_an_object_array_of_http_servlet_request_type_of_method() {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest();

        final Method method = getMethodOfTestUserController("httpServletRequest");

        // when
        final Object[] actual = handlerMethodArgumentResolver.resolve(method, request);

        // then
        assertAll(
            () -> assertThat(actual).hasSize(1),
            () -> assertThat(actual[0]).isInstanceOf(HttpServletRequest.class),
            () -> assertThat(actual[0]).isEqualTo(request)
        );
    }

}
