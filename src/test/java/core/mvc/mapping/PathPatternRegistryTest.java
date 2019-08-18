package core.mvc.mapping;

import core.mvc.resolver.PathPatternRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.pattern.PathPattern;

import static org.assertj.core.api.Assertions.assertThat;

class PathPatternRegistryTest {

    private PathPatternRegistry pathPatternRegistry;

    @BeforeEach
    void setup() {
        this.pathPatternRegistry = new PathPatternRegistry();
    }

    @Test
    void getPathPattern() throws Exception {
        String uri = "/path";

        PathPattern pathPattern1 = pathPatternRegistry.getPathPattern(uri);
        PathPattern pathPattern2 = pathPatternRegistry.getPathPattern(uri);

        assertThat(pathPattern1).isNotNull();
        assertThat(pathPattern2).isNotNull();

        assertThat(pathPattern1).isSameAs(pathPattern2);
    }
}