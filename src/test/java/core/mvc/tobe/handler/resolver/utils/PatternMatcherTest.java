package core.mvc.tobe.handler.resolver.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PatternMatcherTest {
    private PatternMatcher patternMatcher = new PatternMatcher();

    @DisplayName("입력받은 url이 urlPattern의 패턴과 일치하면 true 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "/users, /users/, true",
            "/users, /users, true",
            "/users/{id}, /users/2, true",
            "/users/{id}, /users/, false",
            "/users/{id}/posts/{postId}, /users/1/posts/1, true",
    })
    void matches(String urlPattern, String targetUrl, boolean expecteed) {
        boolean actual = patternMatcher.matches(urlPattern, targetUrl);
        assertThat(actual).isEqualTo(expecteed);
    }

    @DisplayName("패턴과 일치하는 경우, urlPattern에서 템플릿으로 설정한 변수의 값을 저장한 맵을 얻는다.")
    @Test
    void getUrlVariables() {
        Map<String, String> actual = patternMatcher.getUrlVariables("/users/{user}/posts/{postId}", "/users/jordy/posts/1");
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(Map.of(
                        "user", "jordy",
                        "postId", "1"
                ));
    }
}