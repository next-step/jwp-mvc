package core.mvc.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReflectionUtilTest {

    @DisplayName("T 클래스로 생성한 인스턴스의 타입은 T여야 함.")
    @Test
    void test_instantiate_class() throws Exception {
        final Object instance = ReflectionUtil.instantiateClass(Dummy.class);
        assertThat(instance)
                .isInstanceOf(Dummy.class);
    }

    @DisplayName("기본 생성자가 없는 클래스를 인스턴스화 하는 경우 UnableToCreateInstanceException 발생")
    @Test
    void test_unable_to_create_instance() {
        assertThatThrownBy(() -> ReflectionUtil.instantiateClass(DummyWithOutNoArgsConstructor.class))
                .isInstanceOf(UnableToCreateInstanceException.class);
    }

    static class Dummy {
        // no-op
    }

    static class DummyWithOutNoArgsConstructor {
        private String data;

        public DummyWithOutNoArgsConstructor(String data) {
            this.data = data;
        }
    }
}