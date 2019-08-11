package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Junit4TestRunner {
    private static final Logger logger = LoggerFactory.getLogger(Junit4TestRunner.class);

    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method: methods) {
            logger.debug("method name: " + method.getName());
            logger.debug("get annotation(MyTest.class): " + method.getAnnotation(MyTest.class));
            invoke(clazz, method);
        }

    }

    private void invoke(Class<Junit4Test> clazz, Method method) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (method.isAnnotationPresent(MyTest.class)) {
            method.invoke(clazz.newInstance());
        }
    }
}
