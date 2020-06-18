package core.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class PathPatternTest {
    @Test
    @DisplayName("정렬에 대한 테스트")
    void compareTo() {
        PathPattern users = parse("/users");
        PathPattern usersSpecificId = parse("/users/id");
        PathPattern usersId = parse("/users/{id}");

        List<PathPattern> pathPatterns = Arrays.asList(users, usersSpecificId, usersId);

        System.out.println("BEFORE");
        Collections.shuffle(pathPatterns);
        pathPatterns.forEach(System.out::println);

        System.out.println("AFTER");
        Collections.sort(pathPatterns);
        pathPatterns.forEach(System.out::println);
    }

    @Test
    @DisplayName("트리맵에서 정렬 및 찾기 테스트")
    void sortWithTreeMap() {
        PathPattern users = parse("/users");
        PathPattern usersSpecificId = parse("/users/id");
        PathPattern usersId = parse("/users/{id}");

        Map<PathPattern, Object> map = new TreeMap<>();
        map.put(users, users);
        map.put(usersSpecificId, usersSpecificId);
        map.put(usersId, usersId);

        PathContainer pathContainer = toPathContainer("/users/id");

        PathPattern result = map.keySet()
                .stream()
                .filter(pathPattern -> pathPattern.matches(pathContainer))
                .findFirst()
                .orElse(null);

        assertThat(result).isEqualTo(usersSpecificId);
    }

    @Test
    void match() {
        PathPattern pp = parse("/users/{id}");
        assertThat(pp.matches(toPathContainer("/users/1"))).isTrue();
        assertThat(pp.matches(toPathContainer("/users"))).isFalse();
    }

    @Test
    void matchNoPattern() {
        PathPattern pp = parse("/users");
        assertThat(pp.matches(toPathContainer("/users/1"))).isFalse();
        assertThat(pp.matches(toPathContainer("/users"))).isTrue();
        assertThat(pp.matches(toPathContainer("/users/"))).isTrue();
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
