package core.mvc.tobe.argumentResolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StringArgumentResolverTest {

    @DisplayName("전달된 파라미터의 타입이 String 확인한다.")
    @Test
    void supportParameter() {
        HandlerMethodArgumentResolver resolver = new StringArgumentResolver();
        assertThat(resolver.supportsParameter(String.class)).isTrue();
        assertThat(resolver.supportsParameter(Integer.class)).isFalse();
    }

    @DisplayName("String 타입의 문자열을 입력하면 String 타입의 문자열이 결과로 나온다.")
    @Test
    void resolver() {
        HandlerMethodArgumentResolver resolver = new StringArgumentResolver();
        List<Object> expected = new ArrayList<>();
        expected.add("argument");
        assertThat(resolver.resolveArgument(String.class, expected)).isEqualTo("argument");
        assertThat(resolver.resolveArgument(String.class, expected)).isInstanceOf(String.class);
    }
}