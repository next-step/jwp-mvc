package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static next.reflection.MethodUtils.assertValidMethod;
import static next.reflection.MethodUtils.nonParameterMethodInvoke;

public class Junit3TestRunner {

    @DisplayName("메소드 이름 검색")
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        final Junit3Test junit3Test = (Junit3Test) clazz.getConstructors()[0].newInstance();
        final List<String> expectedNames = asList("test1", "test2");

        Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().startsWith("test"))
                .peek(method -> assertValidMethod(method, expectedNames))
                .forEach(method -> nonParameterMethodInvoke(method, junit3Test));
    }

}
