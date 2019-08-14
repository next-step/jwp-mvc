package util;

import core.util.StringUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringUtilTest {

    @Test
    void upperFirstChar() {
        String name = "name";
        String firstUpperName = "Name";

        assertThat(name).isNotEqualTo(firstUpperName);
        assertThat(StringUtil.upperFirstChar(name)).isEqualTo(firstUpperName);

    }

}
