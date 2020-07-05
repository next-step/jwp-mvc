package core.mvc.tobe;

import org.junit.jupiter.api.Test;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PathPatternTest {

    @Test
    void matchByNoneDepSpring() {
        String pattern = "/users/{id}/abc/{detail}";
        final String[] split = pattern.split("/");

        final List<String> pathNames = Arrays.stream(split)
                .filter(splitString -> !splitString.isEmpty())
                .filter(word -> word.contains("}"))
                .map(word -> word.substring(0, word.length() - 1))
                .map(word -> word.substring(1))
                .collect(Collectors.toList());

        assertThat(pathNames.size()).isEqualTo(2);
    }

    @Test
    void getValueByMatch() {
        String pattern = "/users/{id}/abc/{detail}";
        String uri = "/users/1/abc/2";

        final String[] split = pattern.split("/");

        final List<String> pathNames = Arrays.stream(split)
                .filter(splitString -> !splitString.isEmpty())
                .filter(word -> !word.contains("}"))
                .collect(Collectors.toList());

        final String[] strings = uri.split("/");
        final List<String> collect = Arrays.stream(strings)
                .filter(splitString -> !splitString.isEmpty())
                .filter(s -> !pathNames.contains(s))
                .collect(Collectors.toList());

        assertThat(collect.size()).isEqualTo(2);
    }

    @Test
    void match() {
        PathPattern pp = parse("/users/{id}");
        assertThat(pp.matches(toPathContainer("/users/1"))).isTrue();
        assertThat(pp.matches(toPathContainer("/users"))).isFalse();
    }

    @Test
    void matchAndExtract() {
        Map<String, String> variables = parse("/users/{id}")
                .matchAndExtract(toPathContainer("/users/1")).getUriVariables();
        assertThat(variables.get("id")).isEqualTo("1");

        variables = parse("/{var1}/{var2}")
                .matchAndExtract(toPathContainer("/foo/bar")).getUriVariables();
        assertThat(variables.get("var1")).isEqualTo("foo");
        assertThat(variables.get("var2")).isEqualTo("bar");
    }

    private PathPattern parse(String path) {
        PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(path);
    }

    private static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }
}
