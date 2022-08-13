package core.mvc.tobe.handler.resolver;

import core.mvc.tobe.TestUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class BeanTypeArgumentResolverTest {
    private static final NamedParameter BEAN_TYPE_PARAMETER;
    private static final NamedParameter OTHER_TYPE_PARAMETER;

    private static final NamedParameter MULTIPLE_CONSTRUCTOR_TYPE_PARAMETER;
    private static final NamedParameter COMPLEX_OBJECT_TYPE_PARAMETER;

    private final BeanTypeArgumentResolver argumentResolver = new BeanTypeArgumentResolver(
            new LocalVariableTableParameterNameDiscoverer(),
            new CompositeSimpleTypeArgumentResolver(
                    List.of(
                            new StringTypeRequestParameterArgumentResolver(),
                            new IntegerTypeRequestParameterArgumentResolver(),
                            new LongTypeRequestParameterArgumentResolver()
                    )
            )
    );

    static {
        Method testClassMethod = TestClass.class.getDeclaredMethods()[0];
        Parameter[] parameters = testClassMethod.getParameters();

        BEAN_TYPE_PARAMETER = new NamedParameter(parameters[0], "testUser");
        OTHER_TYPE_PARAMETER = new NamedParameter(parameters[1], "simple");
        MULTIPLE_CONSTRUCTOR_TYPE_PARAMETER = new NamedParameter(parameters[2], "multipleConstructorClass");
        COMPLEX_OBJECT_TYPE_PARAMETER = new NamedParameter(parameters[3], "notStringAndNumberConstructor");

    }

    @DisplayName("1개의 생성자만 가지고 있고, 생성자의 인자가 String/숫자형 타입으로 이뤄져있으면 true")
    @ParameterizedTest
    @MethodSource("provideForSupport")
    void support(NamedParameter parameter, boolean expected) {
        assertThat(argumentResolver.support(parameter)).isEqualTo(expected);
    }

    public static Stream<Arguments> provideForSupport() {
        return Stream.of(
                arguments(BEAN_TYPE_PARAMETER, true),
                arguments(OTHER_TYPE_PARAMETER, false),
                arguments(MULTIPLE_CONSTRUCTOR_TYPE_PARAMETER, false),
                arguments(COMPLEX_OBJECT_TYPE_PARAMETER, false)
        );
    }

    @DisplayName("requestParameter 값들을 생성자 매개변수에 할당한다.")
    @Test
    void resolve() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "userId");
        request.addParameter("password", "password");
        request.addParameter("age", "20");

        Object actual = argumentResolver.resolve(BEAN_TYPE_PARAMETER, request, new MockHttpServletResponse());

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(new TestUser(
                        "userId",
                        "password",
                        20
                ));
    }

    private static class TestClass {
        public void test(TestUser testUser,
                         int simple,
                         MultipleConstructorClass multipleConstructorClass,
                         NotStringAndNumberConstructor notStringAndNumberConstructor) {

        }
    }

    private static class MultipleConstructorClass {
        public MultipleConstructorClass() {
        }

        public MultipleConstructorClass(int some) {
        }
    }

    private static class NotStringAndNumberConstructor {
        private TestUser testUser;

        public NotStringAndNumberConstructor(TestUser testUser) {
            this.testUser = testUser;
        }
    }
}
