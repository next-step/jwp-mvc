package core.mvc.resolver;

import core.mvc.MethodParameter;
import core.mvc.tobe.TestUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PrimitiveWrapperArgumentResolverTest {

    private PrimitiveWrapperArgumentResolver primitiveWrapperArgumentResolver;

    @BeforeEach
    void setUp() {
        primitiveWrapperArgumentResolver = new PrimitiveWrapperArgumentResolver();
    }

    @DisplayName("지원하는 메서드 파라미터")
    @Test
    void 지원하는메서드파라미터() {
        MethodParameter methodParameter = new MethodParameter("testParam", int.class, null, null);
        assertThat(primitiveWrapperArgumentResolver.supports(methodParameter)).isTrue();
    }

    @DisplayName("지원하지 않는 메서드 파라미터")
    @Test
    void 지원하지않는메서드파라미터() {
        MethodParameter methodParameter = new MethodParameter("testParam", TestUser.class, null, null);
        assertThat(primitiveWrapperArgumentResolver.supports(methodParameter)).isFalse();
    }
}