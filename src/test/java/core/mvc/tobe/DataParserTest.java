package core.mvc.tobe;

import core.mvc.utils.DataParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

public class DataParserTest {

    @DisplayName("int형 데이터에 적합한 파서 찾기")
    @Test
    void test_staticFactoryMethod() {
        // given
        // when
        DataParser parser = DataParser.from(int.class);
        // then
        assertThat(parser == DataParser.INTEGER_TYPE).isTrue();
    }

    @DisplayName("String형을 int형으로 데이터 파싱 수행")
    @Test
    void test_parse() {
        // given
        // when
        DataParser parser = DataParser.from(int.class);
        try {
            Object arg = parser.parse("26");
            // then
            assertThat((int) arg).isEqualTo(26);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("파싱 불가능한 타입의 경우 false 반환")
    @Test
    void test_supports() {
        // given

        // when
        boolean result = DataParser.supports(TestUser.class);
        // then
        assertThat(result).isFalse();
    }
}
