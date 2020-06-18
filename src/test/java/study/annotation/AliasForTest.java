package study.annotation;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("@AliasFor 학습 테스트")
public class AliasForTest {

    @Test
    @DisplayName("Alias for")
    void test() throws NoSuchMethodException {
        RequestParam annotation = AliasTest.class
                .getDeclaredMethod("testMethod", String.class)
                .getParameters()[0]
                .getAnnotation(RequestParam.class);

        System.out.println(annotation);
        System.out.println(annotation.name());
        System.out.println(annotation.value());

        assertThat(annotation.name()).isEqualTo("test");
        assertThat(annotation.value()).isEqualTo("test");
    }

    private class AliasTest {
        void testMethod(@RequestParam("test") String test) {}
    }
}
