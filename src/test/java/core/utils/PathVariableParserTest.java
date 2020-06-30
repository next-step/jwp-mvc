package core.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

public class PathVariableParserTest {

    @Test
    void matchAndExtract() {
        Map<String, String> variables = PathVariableParser.parsePathVariable("/users/{id}", "/users/1");
        assertThat(variables.get("id")).isEqualTo("1");

        variables = PathVariableParser.parsePathVariable("/{var1}/{var2}", "/foo/bar");
        assertThat(variables.get("var1")).isEqualTo("foo");
        assertThat(variables.get("var2")).isEqualTo("bar");
    }
}
