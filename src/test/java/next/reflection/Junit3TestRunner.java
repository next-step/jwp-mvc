package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Junit3TestRunner {

    @DisplayName("메서드 접두사가 test인 메서드만 실행시킨다.")
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Constructor<Junit3Test> constructor = clazz.getConstructor();

        List<Method> availMethods = Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName().startsWith("test"))
                .collect(Collectors.toList());

        for (Method method : availMethods) {
            method.invoke(constructor.newInstance());
        }
    }
}
