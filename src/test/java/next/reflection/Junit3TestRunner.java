package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Junit3TestRunner {
    private static final Logger logger = LoggerFactory.getLogger(Junit3TestRunner.class);
    public static final String INVOKE_START_METHOD_NAME = "test";

    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        // TODO Junit3Test에서 test로 시작하는 메소드 실행

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method: methods) {
            String methodName = method.getName();
            logger.debug("method name: " + methodName);
            invokeLikeMethodName(clazz, method, INVOKE_START_METHOD_NAME);
        }
    }

    private void invokeLikeMethodName(Class<Junit3Test> clazz, Method method, String methodName) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (methodName.indexOf(methodName) > -1) {
            method.invoke(clazz.newInstance());
        }
    }
}
