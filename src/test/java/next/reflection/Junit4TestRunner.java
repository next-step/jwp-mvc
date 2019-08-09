package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

public class Junit4TestRunner {

    private static final Logger logger = LoggerFactory.getLogger(Junit4TestRunner.class);

    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Junit4Test junit4Test = clazz.newInstance();
        Stream.of(clazz.getMethods())
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> {
                    logger.debug("method name: {}", method.getName());
                    try {
                        method.invoke(junit4Test);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        logger.error("error : {}", method.getName(), e);
                    }
                });
    }
}
