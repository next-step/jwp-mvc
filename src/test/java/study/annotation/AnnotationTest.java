package study.annotation;

import core.annotation.web.RequestMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("어노테이션 학습 테스트")
public class AnnotationTest {
    @Test
    @DisplayName("값이 할당 되어있는지 아닌지 확인")
    void checkValue() throws NoSuchMethodException {
        RequestMapping requestMapping = AnnotatedClass.class
                .getDeclaredMethod("test")
                .getDeclaredAnnotation(RequestMapping.class);

        assertThat(requestMapping.method()).isEmpty();
        assertThat(requestMapping.value()).isNotNull();
    }

    private static class AnnotatedClass {
        @RequestMapping("val")
        public void test() {

        }
    }
}
