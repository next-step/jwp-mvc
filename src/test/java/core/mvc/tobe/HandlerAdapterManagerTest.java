package core.mvc.tobe;

import core.mvc.Environment;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.ServletException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class HandlerAdapterManagerTest {

    @Test
    void handlerAdapterManager() throws ServletException {
        Properties properties = new Properties();
        properties.setProperty("component.basepackage", "next.mock");

        Environment environment = new Environment();
        environment.setConfig(properties);

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/mock/qna");

        HandlerAdapterManager manager = new HandlerAdapterManager(environment);
        final HandlerAdapter handler = manager.getHandler(request);

        assertThat(handler).isExactlyInstanceOf(AnnotationHandlerMappingAdapter.class);
    }

}
