package study;

import org.apache.commons.lang3.ClassUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TypeTest {

    @Test
    public void primitiveTest() {
        assertThat(long.class.isPrimitive()).isTrue();
        assertThat(Long.class.isPrimitive()).isFalse();

        assertThat(ClassUtils.isPrimitiveOrWrapper(long.class)).isTrue();
        assertThat(ClassUtils.isPrimitiveOrWrapper(Long.class)).isTrue();
    }
}
