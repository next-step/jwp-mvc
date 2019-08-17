package core.mvc;

import core.mvc.handler.HandlerMapping;
import core.mvc.mapping.MappingRegistry;
import core.mvc.mapping.MyController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HandlerMappingRegistryTest {

    private MockHttpServletRequest request;
    private MockHandlerMapping mockMapping;
    private MyController controller = new MyController();

    @BeforeEach
    void setup() {
        request = new MockHttpServletRequest();
        mockMapping = new MockHandlerMapping(controller);
    }

    @DisplayName("입력받은 Mapping 을 초기화 한다.")
    @Test
    void init() throws Exception {
        MappingRegistry registry = new MappingRegistry(mockMapping);

        assertThat(mockMapping.initialize).isEqualTo(true);
    }

    @Test
    void getHandler() throws Exception {
        MappingRegistry registry = new MappingRegistry(mockMapping);

        assertThat(registry.getHandler(request)).isEqualTo(controller);
    }

    @Test
    void getHandler_Handler가_없으면_에러() throws Exception {
        mockMapping = new MockHandlerMapping(null);
        MappingRegistry registry = new MappingRegistry(mockMapping);

        assertThatThrownBy(() -> registry.getHandler(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static class MockHandlerMapping implements HandlerMapping {

        private boolean initialize = false;
        private final Object handler;

        private MockHandlerMapping(Object handler) {
            this.handler = handler;
        }

        @Override
        public void initialize() {
            this.initialize = true;
        }

        @Override
        public Object getHandler(HttpServletRequest request) {
            return this.handler;
        }
    }

}