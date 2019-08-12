package next.reflection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class Junit3TestRunner {

    private static final String PREFIX_METHOD = "test";
    public static final int PRIVATE_MODIFIER = 2;

    @DisplayName("메소드 이름이 test로 시작하는 메소드 실행에 성공한다")
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test instance = clazz.newInstance();

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith(PREFIX_METHOD)) {
                invoke(instance, method);
            }
        }
    }

    @DisplayName("private 접근 제어자를 가진 메소드 실행 시 에러발생")
    @Test
    void run_whenMethodPrivate_thenException() {
        Class<Junit3Test> junit3TestClass = Junit3Test.class;
        String declaredMethodName = "testPrivate";

        Assertions.assertThatIllegalStateException()
                .isThrownBy(() -> invoke(junit3TestClass.newInstance(),
                        junit3TestClass.getDeclaredMethod(declaredMethodName)));
    }

    private void invoke(Junit3Test instance, Method method) throws Exception {
        if (PRIVATE_MODIFIER == method.getModifiers()) {
            throw new IllegalStateException("private method는 생성할 수 없습니다.");
        }
        method.invoke(instance);
    }
}
