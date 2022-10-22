package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit3TestRunner {
    @Test
    @DisplayName("test 로 시작하는 메서드를 실행한다.")
    public void run() {
        Class<Junit3Test> clazz = Junit3Test.class;

        Method[] methods = clazz.getDeclaredMethods();
        Constructor<?> defaultConstructor = clazz.getConstructors()[0];

        Arrays.stream(methods)
                .filter(method -> method.getName().startsWith("test"))
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
