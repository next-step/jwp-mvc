package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit4TestRunner {

    @Test
    @DisplayName("@MyTest 어노테이션이 설정되어 있는 메서드를 실행한다.")
    public void run() {
        Class<Junit4Test> clazz = Junit4Test.class;

        Method[] methods = clazz.getDeclaredMethods();
        Constructor<?> defaultConstructor = clazz.getConstructors()[0];

        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .forEach(it -> executeMethod(defaultConstructor, it));
    }

    private void executeMethod(Constructor<?> defaultConstructor, Method it) {
        try {
            it.invoke(defaultConstructor.newInstance());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
