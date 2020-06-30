package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Class<Junit3Test> clazz = Junit3Test.class;
        for(Method method : clazz.getDeclaredMethods()){
            if(method.getName().startsWith("test")) {
                method.invoke(clazz.newInstance());
            }
        }
    }
}
