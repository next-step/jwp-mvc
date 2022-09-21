package core.mvc.resolver;

import org.junit.jupiter.api.BeforeAll;

class HandlerMethodArgumentResolverMapperTest {

    private static HandlerMethodArgumentResolverMapper mapper;

    @BeforeAll
    static void setUp() {
        mapper = new HandlerMethodArgumentResolverMapper();
        mapper.initialize();
    }
}
