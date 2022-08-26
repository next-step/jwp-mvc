package core.mvc.tobe.argumentResolver;

import core.mvc.tobe.TestUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TestUserArgumentResolverTest {

    @DisplayName("전달된 파라미터의 타입이 TestUser인지 확인한다.")
    @Test
    void supportParameter() {
        HandlerMethodArgumentResolver resolver = new TestUserArgumentResolver();
        assertThat(resolver.supportsParameter(TestUser.class)).isTrue();
        assertThat(resolver.supportsParameter(Integer.class)).isFalse();
    }

    @DisplayName("TestUser 인스턴스를 입력하면 TestUser 인스턴스가 결과로 나온다.")
    @Test
    void resolver() {
        HandlerMethodArgumentResolver resolver = new TestUserArgumentResolver();
        List<Object> expected = new ArrayList<>();
        TestUser testUser = new TestUser("userId", "passoword", 20);
        expected.add(testUser);
        assertThat(resolver.resolveArgument(Object.class, expected)).isEqualTo(testUser);
    }
}