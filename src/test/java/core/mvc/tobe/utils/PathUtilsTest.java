package core.mvc.tobe.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class PathUtilsTest {

    @DisplayName("매칭되는 경로 확인 시 true를 반환한다.")
    @CsvSource({
            "/users/{id},/users/1",
            "/{id},/1",
            "/{id},/users"
    })
    @ParameterizedTest
    void matchesTrue(final String pattern,
                     final String path) {
        assertThat(PathUtils.matches(pattern, path)).isTrue();
    }

    @DisplayName("매칭이 안되는 경로 확인 시 false를 반환한다.")
    @CsvSource({
            "/,/1",
            "/{id},/",
            "/users/1/{id},/users/1",
            "/users/{id},/users/1/items",
    })
    @ParameterizedTest
    void matchesFalse(final String pattern,
                      final String path) {
        assertThat(PathUtils.matches(pattern, path)).isFalse();
    }

    @DisplayName("매칭되는 경로 1개 파싱 시 매칭되는 정보를 반환한다.")
    @CsvSource({
            "/users/{id},/users/1",
            "/{id},/1",
            "/{id},/users"
    })
    @ParameterizedTest
    void parseVariablesSingle(final String pattern,
                              final String path) {
        assertThat(PathUtils.parse(pattern, path)).hasSize(1);
    }

    @DisplayName("매칭되는 경로 여러 개 파싱 시 매칭되는 정보를 반환한다.")
    @CsvSource({
            "/users/{id}/items/{itemId},/users/1/items/1",
            "/{id}/{id2},/1/2",
            "/{path}/{id},/users/1"
    })
    @ParameterizedTest
    void parseVariablesMulti(final String pattern,
                             final String path) {
        assertThat(PathUtils.parse(pattern, path)).hasSize(2);
    }

    @DisplayName("매칭되는 경로가 없을 경우 빈 값을 반환한다.")
    @CsvSource({
            "/,/1",
            "/{id},/",
            "/users/1/{id},/users/1",
            "/users/{id},/users/1/items",
    })
    @ParameterizedTest
    void parseVariablesFail(final String pattern,
                            final String path) {
        assertThat(PathUtils.parse(pattern, path)).isEmpty();
    }

    @DisplayName("매칭되는 경로가 없을 경우 값 조회 시 비어있다.")
    @CsvSource({
            "/,/1",
            "/{id},/",
            "/users/1/{id},/users/1",
            "/users/{id},/users/1/items",
    })
    @ParameterizedTest
    void parseFail(final String pattern,
                   final String path) {
        assertThat(PathUtils.parse(pattern, path, "id")).isNotPresent();
    }
}