package next.reflection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static next.reflection.MethodUtils.nonParameterMethodInvoke;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = (Junit4Test) clazz.getConstructors()[0].newInstance();
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getAnnotation(MyTest.class) != null)
                .forEach(method -> nonParameterMethodInvoke(method, junit4Test));
    }
}
