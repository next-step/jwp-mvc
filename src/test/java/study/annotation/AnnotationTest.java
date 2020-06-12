package study.annotation;

import core.annotation.web.RequestMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("어노테이션 학습 테스트")
public class AnnotationTest {
    @Test
    @DisplayName("값이 할당 되어있는지 아닌지 확인")
    void checkValue() throws NoSuchMethodException {
        RequestMapping requestMapping = AnnotatedClass.class
                .getDeclaredMethod("test")
                .getDeclaredAnnotation(RequestMapping.class);

        System.out.println(requestMapping.method().length);
        System.out.println(requestMapping.value());
    }

    private static class AnnotatedClass {
        @RequestMapping("val")
        public void test() {

        }
    }
}
