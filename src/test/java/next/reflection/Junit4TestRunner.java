package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static next.reflection.MethodUtils.assertValidMethod;
import static next.reflection.MethodUtils.nonParameterMethodInvoke;

public class Junit4TestRunner {
    @DisplayName("애노테이션 메소드 검색")
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        final Junit4Test junit4Test = (Junit4Test) clazz.getConstructors()[0].newInstance();
        final List<String> expectedNames = asList("one", "two", "four", "five", "six");

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .peek(method -> assertValidMethod(method, expectedNames))
                .forEach(method -> nonParameterMethodInvoke(method, junit4Test));
    }


}
