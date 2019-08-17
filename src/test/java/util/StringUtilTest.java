package util;

import core.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilTest {

    @Test
    void upperFirstChar() {
        String name = "name";
        String firstUpperName = "Name";

        assertThat(name).isNotEqualTo(firstUpperName);
        assertThat(StringUtil.upperFirstChar(name)).isEqualTo(firstUpperName);
    }

    @Test
    void lastIndexString() {
        String handlebars = "handlebars.hbs";
        final String extension = StringUtils.substringAfterLast(handlebars, ".");
        assertThat(extension).isEqualTo("hbs");
    }

}
