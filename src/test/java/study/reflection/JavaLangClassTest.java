package study.reflection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class JavaLangClassTest {

    private Class<MyObject> myObjectClass;

    @BeforeEach
    void setUp() {
        myObjectClass = MyObject.class;
    }

    @Test
    @DisplayName("객체에서 클래스 정보를 가져올 수 있다")
    void clazz() {
        final MyObject myObject = new MyObject("");

        final Class<? extends MyObject> result = myObject.getClass();

        assertThat(result).isEqualTo(myObjectClass);
    }

    @Test
    @DisplayName("Class.getModifiers()는 클래스 접근제한자를 반환한다")
    void getModifiers() {
        System.out.println(myObjectClass.getModifiers());
    }

    @Test
    @DisplayName("Class.getFields()는 접근제한자가 public인 필드만 반환한다")
    void getFields() {
        Arrays.stream(myObjectClass.getFields())
                .forEach(f -> System.out.println(f));
    }

    @Test
    @DisplayName("Class.getDeclaredFields()는 모든 필드를 반환한다")
    void getDeclaredFields() {
        Arrays.stream(myObjectClass.getDeclaredFields())
                .forEach(f -> System.out.println(f));
    }

    @Test
    @DisplayName("Class.getConstructors()는 접근제한자가 public인 생성자만 반환한다")
    void getConstructors() {
        Arrays.stream(myObjectClass.getConstructors())
                .forEach(c -> System.out.println(c));
    }

    @Test
    @DisplayName("Class.getConstructors(Class<?>... parameterTypes)는 접근제한자가 public인 생성자 중 파라미터의 타입과 순서가 일치하는 생성자를 반환한다")
    void getConstructorsWithParam() throws NoSuchMethodException {
        Constructor<MyObject> constructor = myObjectClass.getConstructor(int.class, long.class);
        System.out.println(constructor);
    }

    @Test
    @DisplayName("Class.getConstructors(Class<?>... parameterTypes)는 접근제한자가 public인 생성자 중 파라미터의 타입과 순서가 일치하는 생성자가 존재하지 않을 경우 에러를 반환한다")
    void getConstructorsWithParamException() {
        final Throwable thrown = catchThrowable(() -> myObjectClass.getConstructor(int.class, int.class));

        assertThat(thrown)
                .isInstanceOf(NoSuchMethodException.class)
                .hasMessageContaining("study.reflection.MyObject.<init>(int, int)");
    }

    @Test
    @DisplayName("Class.getDeclaredConstructors()는 모든 생성자를 반환한다")
    void getDeclaredConstructors() {
        Arrays.stream(myObjectClass.getDeclaredConstructors())
                .forEach(c -> System.out.println(c));
    }

    @Test
    @DisplayName("Class.getDeclaredConstructors(Class<?>... parameterType)는 모든 생성자 중 파라미터의 타입과 순서가 일치하는 생성자를 반환한다")
    void getDeclaredConstructorsWithParams() throws NoSuchMethodException {
        Constructor<MyObject> constructor = myObjectClass.getDeclaredConstructor(int.class, String.class);
        System.out.println(constructor);
    }

    @Test
    @DisplayName("Class.getDeclaredConstructors(Class<?>... parameterType)는 모든 생성자 중 파라미터의 타입과 순서가 일치하는 생성자가 존재하지 않을 경우 에러를 반환한다")
    void getDeclaredConstructorsWithParamsException() {
        final Throwable thrown = catchThrowable(() -> myObjectClass.getConstructor(int.class, byte.class));

        assertThat(thrown)
                .isInstanceOf(NoSuchMethodException.class)
                .hasMessageContaining("study.reflection.MyObject.<init>(int, byte)");
    }

    @Test
    @DisplayName("Class.getMethods()는 해당 클래스가 상속 받은 메서드를 포함하여 접근제한자가 public인 메서드를 반환한다")
    void getMethods() {
        Arrays.stream(myObjectClass.getMethods())
                .forEach(m -> System.out.println(m));
    }

    @Test
    @DisplayName("Class.getDeclaredMethods()는 해당 클래스에서 구현한 모든 메서드를 반환한다")
    void getDeclaredMethod() {
        Arrays.stream(myObjectClass.getDeclaredMethods())
                .forEach(m -> System.out.println(m));
    }

}
