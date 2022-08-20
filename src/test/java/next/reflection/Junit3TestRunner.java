package next.reflection;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Junit3TestRunner {
    private static final Logger logger = LoggerFactory.getLogger(Junit3TestRunner.class);

    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Junit3Test junit3Test = clazz.getDeclaredConstructor().newInstance();
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> methodsStartWithTest = Stream.of(methods)
                .filter(method -> StringUtils.startsWith(method.getName(), "test"))
                .collect(Collectors.toList());

        for (Method method : methodsStartWithTest) {
            logger.info("{}", method.getName());
            method.invoke(junit3Test);
        }

    }
}
