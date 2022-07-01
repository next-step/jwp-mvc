package core.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArrayUtilsTest {

    @Nested
    @DisplayName("isEmpty 빈 배열인지 확인한다")
    class isEmpty {
        @Test
        void 빈_배열인_경우_true() {
            boolean empty = ArrayUtils.isEmpty(new Object[]{});
            assertThat(empty).isTrue();
        }

        @Test
        void 배열에_값이_있는_경우_false() {
            boolean empty = ArrayUtils.isEmpty(new Object[]{1});
            assertThat(empty).isFalse();
        }
    }
}
