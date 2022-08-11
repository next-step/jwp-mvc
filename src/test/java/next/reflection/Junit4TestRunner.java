package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Junit4TestRunner {
    private static final Logger logger = LoggerFactory.getLogger(Junit4TestRunner.class);

    @Test
    public void run() throws Exception {
        Junit4Test junit4Test = new Junit4Test();
        Class<Junit4Test> clazz = Junit4Test.class;
        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행

        Method[] methods = clazz.getDeclaredMethods();

        Predicate<Method> isDeclaredMyTestAnnotation = (method) -> method.isAnnotationPresent(MyTest.class);

        List<Method> onlyDeclaredMyTestAnnotationMethods = Arrays.stream(methods)
                .filter(isDeclaredMyTestAnnotation)
                .collect(Collectors.toList());

        for (Method method : onlyDeclaredMyTestAnnotationMethods) {
            logger.debug("method name : {} ", method.getName());
            method.invoke(junit4Test);
        }
    }
}
