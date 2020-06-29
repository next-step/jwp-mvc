package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit3TestRunner {
    private static final Logger logger = LoggerFactory.getLogger(Junit3TestRunner.class);

    @DisplayName("요구사항 2 - test로 시작하는 메소드 실행")
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Junit3Test junit3Test = new Junit3Test();

        Method[] methods = clazz.getDeclaredMethods();

        /*for(Method method: methods){
            logger.debug("method : {}", method.getName());
            if(method.getName().startsWith("test"))
                method.invoke(junit3Test,null);
        }*/

        Arrays.stream(methods)
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> {
                    try {
                        method.invoke(junit3Test, null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });

    }
}
