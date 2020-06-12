package study.reflection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectMethodTest {

    private Method sumMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        sumMethod = MyObject.class.getDeclaredMethod("sum", int.class, int.class);
    }

    @Test
    @DisplayName("getName() 호출 시 메서드 이름을 반환한다")
    void getName() {
        final String expected = "sum";

        final String result = sumMethod.getName();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("getModifiers() 호출 시 접근제한자를 숫자로 반환한다")
    void getModifiers() {
        final int expected = Modifier.PRIVATE;

        final int result = sumMethod.getModifiers();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("getParameterTypes() 호출 시 생성자 파라미터의 데이터 타입을 반환한다")
    void getParameterTypes() {
        final Class[] expected = new Class[]{int.class, int.class};

        final Class[] result = sumMethod.getParameterTypes();

        assertThat(result).containsExactlyInAnyOrder(expected);
    }

}
