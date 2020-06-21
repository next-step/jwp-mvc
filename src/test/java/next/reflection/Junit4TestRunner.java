package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

@DisplayName("Junit4TestRunner 관련 테스트")
public class Junit4TestRunner {

    @DisplayName("Junit4Test에서 @MyTest애노테이션으로 설정되어 있는 메소드를 자동으로 실행한다.")
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        Constructor<?> constructor = clazz.getConstructor();
        Method[] declaredMethods = clazz.getDeclaredMethods();

        Arrays.stream(declaredMethods)
                .filter(declaredMethod -> declaredMethod.isAnnotationPresent(MyTest.class))
                .forEach(methodMyTestAnnotated -> {
                    try {
                        methodMyTestAnnotated.invoke(constructor.newInstance());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
