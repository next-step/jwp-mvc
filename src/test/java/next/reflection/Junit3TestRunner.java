package next.reflection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static next.reflection.MethodUtils.nonParameterMethodInvoke;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        final Junit3Test junit3Test = (Junit3Test) clazz.getConstructors()[0].newInstance();

        Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> nonParameterMethodInvoke(method, junit3Test));
    }
}
