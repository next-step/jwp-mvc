package core.mvc.resolver;

import org.junit.jupiter.api.BeforeAll;

class HandlerMethodArgumentResolverMapperTest {

    private static HandlerMethodArgumentResolverMapping mapper;

    @BeforeAll
    static void setUp() {
        mapper = new HandlerMethodArgumentResolverMapping();
        mapper.initialize();
    }
}
