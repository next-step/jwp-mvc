package next.reflection;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Stream.of(clazz.getDeclaredMethods());
        // TODO Junit3Test에서 test로 시작하는 메소드 실행
    }
}
