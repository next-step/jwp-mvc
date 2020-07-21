package core.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created By kjs4395 on 7/21/20
 */
public class PathPatternUtilTest {

    @Test
    @DisplayName("PathPatern parse 테스트ㅅ")
    void getPathPatternTest() {
        PathPatternParser parser = new PathPatternParser();
        PathPattern actual = PathPatternUtil.getPathPattern("/users/{id}");
        PathPattern expected = parser.parse("/users/{id}");

        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("pathVariable가져오는지 테스트")
    void toPathContainerTest() {
        PathPattern pathPattern = PathPatternUtil.getPathPattern("/users/{id}");
        PathContainer pathContainer = PathPatternUtil.toPathContainer("/users/1");

        String pathVariable = pathPattern.matchAndExtract(pathContainer).getUriVariables().get("id");

        assertEquals(pathVariable, "1");
    }
}
