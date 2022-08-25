package core.mvc.tobe.argumentResolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LongArgumentResolverTest {
    @DisplayName("전달된 파라미터의 타입이 long 타입인지 확인한다.")
    @Test
    void supportParameter() {
        HandlerMethodArgumentResolver resolver = new LongArgumentResolver();
        assertThat(resolver.supportsParameter(long.class)).isTrue();
        assertThat(resolver.supportsParameter(Long.class)).isTrue();
        assertThat(resolver.supportsParameter(Object.class)).isFalse();
    }

    @DisplayName("Integer 인스턴스가 null이 아니면 Object 인스턴스가 결과로 나온다.")
    @Test
    void resolver() {
        HandlerMethodArgumentResolver resolver = new LongArgumentResolver();
        List<Object> expected = new ArrayList<>();
        long value = 3;
        expected.add(value);
        assertThat(resolver.resolveArgument(long.class, expected)).isEqualTo(value);
        assertThat(resolver.resolveArgument(long.class, expected)).isInstanceOf(long.class);
    }
}