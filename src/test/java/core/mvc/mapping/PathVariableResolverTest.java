package core.mvc.mapping;

import core.mvc.resolver.PathVariableResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PathVariableResolverTest {

    @DisplayName("requestMappingPath 에 {} 가 있으면 일치하는 키와 값을 가져온다.")
    @Test
    void resolve() throws Exception {
        String requestMappingPath = "/root/{id}/path/{name}";
        String requestUri = "/root/123/path/name!!!";

        Map<String, String> pathVariables = PathVariableResolver.getPathVariables(requestMappingPath, requestUri);

        assertThat(pathVariables.get("id")).isEqualTo("123");
        assertThat(pathVariables.get("name")).isEqualTo("name!!!");
    }

    @DisplayName("requestMappingPath 에 {} 가 있지만, requestUri 가 패턴과 일치하지 않으면 빈 값을 반환한다.")
    @Test
    void resolve1() throws Exception {
        String requestMappingPath = "/root/{id}/path/{name}";
        String requestUri = "/root/123/path";

        Map<String, String> pathVariables = PathVariableResolver.getPathVariables(requestMappingPath, requestUri);

        assertThat(pathVariables).isEmpty();
    }

    @DisplayName("requestMapping 에 {} 가 없으면 빈 값이 반환된다.")
    @Test
    void resolve2() throws Exception {
        String requestMappingPath = "/root/123/path/name!!!";
        String requestUri = "/root/123/path/name!!!";

        Map<String, String> pathVariables = PathVariableResolver.getPathVariables(requestMappingPath, requestUri);

        assertThat(pathVariables).isEmpty();
    }
}