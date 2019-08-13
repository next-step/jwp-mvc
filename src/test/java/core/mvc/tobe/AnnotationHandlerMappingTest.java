package core.mvc.tobe;

import com.google.common.collect.Lists;
import core.annotation.web.RequestMethod;
import core.mvc.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("/users/findUserId api에서 리턴되는 jsp 파일을 확인한다")
    public void getHandler() throws Exception {
        String givenRequestUrl = "/users/findUserId";
        String expectedViewName = "/users/show.jsp";

        MockHttpServletRequest request = new MockHttpServletRequest("GET", givenRequestUrl);
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        View view = execution.handle(request, response).getView();

        assertThat(view.getViewName()).isEqualTo(expectedViewName);
    }

    @Test
    @DisplayName("core.mvc.tobe package 중 Controller annotation이 붙은 method가 올바로 등록되었는지 확인한다.")
    void initialize() {
        List<HandlerKey> expectedKeys = getExpected();
        Set<HandlerKey> actualKeys = handlerMapping.getHandlerKeys();

        assertThat(actualKeys).containsAll(expectedKeys);
    }

    private List<HandlerKey> getExpected() {
        HandlerKey findUserHandlerKey = new HandlerKey("/users/findUserId", RequestMethod.GET);
        HandlerKey usersHandlerKey = new HandlerKey("/users", RequestMethod.GET);
        HandlerKey crateUserHandlerKey = new HandlerKey("/users", RequestMethod.POST);

        return Lists.newArrayList(findUserHandlerKey, usersHandlerKey, crateUserHandlerKey);
    }

    @Test
    @DisplayName("handlerMapping에 맵핑된 키값 중 POST /users 가 있는지 확인한다.")
    void isHandlerKeyPresent() {
        assertThat(handlerMapping.isHandlerKeyPresent("/users", RequestMethod.POST)).isTrue();
    }
}
