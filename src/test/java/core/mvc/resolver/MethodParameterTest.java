package core.mvc.resolver;

import core.annotation.web.RequestParam;
import core.mvc.exception.InvalidParameterAnnotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MethodParameterTest {

    @DisplayName("Annotation 을 지원할 경우 supportAnnotation 이 true 이다.")
    @Test
    void supportAnnotation_true() {
        assertThat(
                new MethodParameter(
                        null, String.class, new Annotation[]{new MockRequestParam()}, ""
                ).supportAnnotation(RequestParam.class)
        ).isTrue();
    }

    @DisplayName("Annotation 을 지원하지 않는 경우 supportAnnotation 이 false 이다.")
    @Test
    void supportAnnotation_false() {
        assertThat(
                new MethodParameter(
                        null, String.class, new Annotation[]{new MockRequestParam()}, ""
                ).supportAnnotation(MockRequestParam.class)
        ).isFalse();
    }

    @DisplayName("지원하는 Annotation 을 가져올 수 있다.")
    @Test
    void getAnnotation_success() {
        Annotation annotation = new MockRequestParam();
        assertThat(
                new MethodParameter(
                        null, String.class, new Annotation[]{annotation}, ""
                ).getAnnotation(RequestParam.class)
        ).isEqualTo(annotation);
    }

    @DisplayName("지원하지 않는 Annotation 을 가져올 경우 InvalidParameterAnnotation 예외가 발생한다.")
    @Test
    void getAnnotation_fail() {
        assertThatThrownBy(
                () -> new MethodParameter(
                        null, String.class, new Annotation[]{new MockRequestParam()}, ""
                ).getAnnotation(MockRequestParam.class)
        ).isInstanceOf(InvalidParameterAnnotation.class);
    }

    @DisplayName("파라미터가 Integer 타입일 경우, resolveArgument 도 Integer 를 반환한다.")
    @Test
    void isInteger_true() {
        assertAll(
                () -> assertThat(
                        new MethodParameter(
                                null, Integer.class, new Annotation[]{new MockRequestParam()}, ""
                        ).resolveArgument(1)
                ).isInstanceOf(Integer.class),
                () -> assertThat(
                        new MethodParameter(
                                null, int.class, new Annotation[]{new MockRequestParam()}, ""
                        ).resolveArgument(2)
                ).isInstanceOf(Integer.class)
        );
    }

    @DisplayName("파라미터가 String 타입일 경우, resolveArgument 도 String 을 반환한다.")
    @Test
    void isInteger_false() {
        assertThat(
                new MethodParameter(
                        null, String.class, new Annotation[]{new MockRequestParam()}, ""
                ).resolveArgument("argument")
        ).isInstanceOf(String.class);
    }
}
