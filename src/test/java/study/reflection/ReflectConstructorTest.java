package study.reflection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectConstructorTest {

    private Constructor<MyObject> myObjectConstructor;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        myObjectConstructor = MyObject.class.getConstructor(int.class, long.class);
    }

    @Test
    @DisplayName("getName() 호출 시 Fully Qualified Name을 가져올 수 있다")
    void getName() {
        final String expected = "study.reflection.MyObject";

        final String result = myObjectConstructor.getName();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("getModifiers() 호출 시 접근제한자를 숫자로 반환한다")
    void getModifiers() {
        final int expected = Modifier.PUBLIC;

        final int result = myObjectConstructor.getModifiers();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("getParameterTypes() 호출 시 생성자 파라미터의 데이터 타입을 반환한다")
    void getParameterTypes() {
        final Class[] expected = new Class[]{int.class, long.class};

        final Class[] result = myObjectConstructor.getParameterTypes();

        assertThat(result).containsExactlyInAnyOrder(expected);
    }

}
