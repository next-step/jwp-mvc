package util;

import core.util.PathPatternUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PathPatternUtilTest {

    @Test
    void matchPathPattern() {
        assertTrue(PathPatternUtil.isUrlMatch("/user/{id}", "/user/jun"));
        assertFalse(PathPatternUtil.isUrlMatch("/users/{name}", "/user/juns"));
        assertTrue(PathPatternUtil.isUrlMatch("/cars/{type}/jun", "/cars/auto/jun"));
    }

    @Test
    void getUrlValue() {
        assertThat(PathPatternUtil.getUriValue("/user/{id}", "/user/jun", "id")).isEqualTo("jun");
        assertThat(PathPatternUtil.getUriValue("/cars/{type}/jun", "/cars/auto/jun", "type")).isEqualTo("auto");
    }

}
