package core.mvc.tobe;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.pattern.PathPattern;

import static org.assertj.core.api.Assertions.assertThat;

class PathPatternUtilTest {
    @Test
    void name() {
        PathPattern pp = PathPatternUtil.parse("/users/profile/{id}");
        assertThat(pp.matches(PathPatternUtil.toPathContainer("/users/profile/ninjasul"))).isTrue();
        assertThat(pp.matches(PathPatternUtil.toPathContainer("/users/profile"))).isFalse();
    }
}