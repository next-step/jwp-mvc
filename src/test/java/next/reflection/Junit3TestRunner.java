package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.getName().startsWith("test"))
                .forEach(s -> {
                    try {
                        s.invoke(clazz.newInstance());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                });
    }
}
