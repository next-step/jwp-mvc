package study.reflection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectFieldTest {

    private Field field;

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        field = MyObject.class.getDeclaredField("c");
    }

    @Test
    @DisplayName("getName() 호출 시 필드 이름을 반환한다")
    void getName() {
        final String expected = "c";

        final String result = field.getName();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("getModifiers() 호출 시 접근제한자를 숫자로 반환한다")
    void getModifiers() {
        final int expected = Modifier.PRIVATE;

        final int result = field.getModifiers();

        assertThat(result).isEqualTo(expected);
    }

}
