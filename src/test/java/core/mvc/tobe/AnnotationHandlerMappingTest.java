package core.mvc.tobe;

import com.google.common.collect.Lists;
import core.annotation.web.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationHandlerMappingTest {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMappingTest.class);

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        handlerMapping.initialize();
    }

    @Test
    public void getHandler() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/findUserId");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }

    @Test
    void initialize() {
        List<HandlerKey> handlerKeys = getExpected();
        Set<HandlerKey> actualKeys = handlerMapping.getHandlerKeys();

        assertThat(actualKeys).isNotEmpty();
        for (HandlerKey handlerKey : actualKeys) {
            logger.debug("actual handler key : {}", handlerKey);
            assertThat(handlerKey).isIn(handlerKeys);
        }
    }

    private List<HandlerKey> getExpected() {
        HandlerKey findUserHandlerKey = new HandlerKey("/users/findUserId", RequestMethod.GET);
        HandlerKey usersHandlerKey = new HandlerKey("/users", RequestMethod.GET);
        HandlerKey crateUserHandlerKey = new HandlerKey("/users", RequestMethod.POST);

        return Lists.newArrayList(findUserHandlerKey, usersHandlerKey, crateUserHandlerKey);
    }

    @Test
    void isHandlerKeyPresent() {
        assertThat(handlerMapping.isHandlerKeyPresent("/users", RequestMethod.POST)).isTrue();
    }
}
