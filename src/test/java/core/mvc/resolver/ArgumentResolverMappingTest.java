package core.mvc.resolver;

import core.mvc.exception.ArgumentResolverException;
import core.mvc.exception.NoSuchArgumentResolverException;
import core.mvc.tobe.TestUserController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArgumentResolverMappingTest {
    private static ArgumentResolverMapping argumentResolverMapping;

    @BeforeAll
    static void setUp() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        argumentResolverMapping = new ArgumentResolverMapping();
        argumentResolverMapping.init();
    }


    @DisplayName("HttpServletRequest와 HttpServletResponse 매개변수를 주입할 수 있다.")
    @Test
    void injectHttpServletXXXX() throws ArgumentResolverException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Class<TestUserController> clazz = TestUserController.class;

        Method method = getMethod("httpServletXXXX", clazz.getDeclaredMethods());

        Object[] resolve = argumentResolverMapping.resolve(method, request, response);

        assertThat(resolve).hasSize(2);
        assertThat(resolve[0]).isEqualTo(request);
        assertThat(resolve[1]).isEqualTo(response);
    }

    @DisplayName("아무 애노테이션이 없는 파라미터를 요구하는 타입에 맞게 주입할 수 있다.")
    @Test
    void injectNormalArgument() throws ArgumentResolverException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("id", "123");
        request.addParameter("age", "45");

        MockHttpServletResponse response = new MockHttpServletResponse();
        Class<TestUserController> clazz = TestUserController.class;

        Method method = getMethod("create_int_long", clazz.getDeclaredMethods());

        Object[] values = argumentResolverMapping.resolve(method, request, response);

        assertThat(values).hasSize(2);
        assertThat(values[0]).isEqualTo(123L).isInstanceOf(Long.class);
        assertThat(values[1]).isEqualTo(45).isInstanceOf(Integer.class);
    }

    @DisplayName("ArgumentResolverMapping 컨테이너는")
    @Nested
    class Describe_ArgumentResolverMapping {

        @DisplayName("속성을 지정하지 않은 경우")
        @Nested
        class Context_without_attribute {
            @DisplayName("파라미터 이름으로 경로에서 인수를 찾아 주입한다.")
            @Test
            void injectParameterByParameterName() throws ArgumentResolverException {
                MockHttpServletRequest request = new MockHttpServletRequest();
                request.setRequestURI("/users/123");

                MockHttpServletResponse response = new MockHttpServletResponse();
                Class<TestUserController> clazz = TestUserController.class;

                Method method = getMethod("show_pathvariable", clazz.getDeclaredMethods());

                Object[] values = argumentResolverMapping.resolve(method, request, response);

                assertThat(values).hasSize(1);
                assertThat(values[0]).isEqualTo(123L).isInstanceOf(Long.class);
            }
        }

        @DisplayName("속성을 지정한 경우")
        @Nested
        class Context_with_attribute {

            @DisplayName("name속성을 지정하면 해당 속성으로 경로에서 인수를 찾아 주입한다.")
            @Test
            void injectParameterByNameAttribute() throws ArgumentResolverException {
                MockHttpServletRequest request = new MockHttpServletRequest();
                request.setRequestURI("/users/123/catsbi/35");

                MockHttpServletResponse response = new MockHttpServletResponse();
                Class<TestUserController> clazz = TestUserController.class;

                Method method = getMethod("show_multiple_pathvariables", clazz.getDeclaredMethods());

                Object[] values = argumentResolverMapping.resolve(method, request, response);

                assertThat(values).hasSize(3);
                assertThat(values[0]).isEqualTo(123L);
            }

            @DisplayName("required 속성이 true인 경우 경로에서 인수가 존재할 경우 찾아 주입한다.")
            @Test
            void injectParameterByRequireTrue() throws ArgumentResolverException {
                MockHttpServletRequest request = new MockHttpServletRequest();
                request.setRequestURI("/users/123/catsbi/35");

                MockHttpServletResponse response = new MockHttpServletResponse();
                Class<TestUserController> clazz = TestUserController.class;

                Method method = getMethod("show_multiple_pathvariables", clazz.getDeclaredMethods());

                Object[] values = argumentResolverMapping.resolve(method, request, response);

                assertThat(values).hasSize(3);
                assertThat(values[1]).isEqualTo("catsbi");
            }

            @DisplayName("required 속성이 true인 경우 경로에서 인수가 없을 경우 예외가 발생한다.")
            @Test
            void injectParameterByRequireTrueAndNotFound() {
                MockHttpServletRequest request = new MockHttpServletRequest();
                request.setRequestURI("/users");

                MockHttpServletResponse response = new MockHttpServletResponse();
                Class<TestUserController> clazz = TestUserController.class;

                Method method = getMethod("show_pathvariable", clazz.getDeclaredMethods());

                assertThatThrownBy(()-> argumentResolverMapping.resolve(method, request, response))
                        .isInstanceOf(NoSuchArgumentResolverException.class);
            }
        }

    }

    @DisplayName("PathVariable 의 속성에 따라 파라미터를 다른 방식으로 주입할 수 있다.")
    @Test
    void injectPathVariableParameterWithNameAttribute() {

    }




    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }
}
