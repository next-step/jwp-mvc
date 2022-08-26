package core.mvc.tobe.argumentResolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class IntegerArgumentResolverTest {
    @DisplayName("전달된 파라미터의 타입이 int 타입인지 확인한다.")
    @Test
    void supportParameter() {
        HandlerMethodArgumentResolver resolver = new IntegerArgumentResolver();
        assertThat(resolver.supportsParameter(int.class)).isTrue();
        assertThat(resolver.supportsParameter(Integer.class)).isTrue();
        assertThat(resolver.supportsParameter(Object.class)).isFalse();
    }

    @DisplayName("Integer 인스턴스가 null이 아니면 Object 인스턴스가 결과로 나온다.")
    @Test
    void resolver() {
        HandlerMethodArgumentResolver resolver = new IntegerArgumentResolver();
        List<Object> expected = new ArrayList<>();
        int value = 3;
        expected.add(value);
        assertThat(resolver.resolveArgument(Object.class, expected)).isEqualTo(value);
    }
}