package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author : yusik
 * @date : 2019-08-13
 */
public class HandlerKeyTest {

    private static final Logger logger = LoggerFactory.getLogger(HandlerKeyTest.class);

    @DisplayName("핸들러키 equals 테스트")
    @Test
    void equalsTest() {
        String[] urls1 = new String[] {"/test1", "/test2"};
        RequestMethod[] methods1 = new RequestMethod[] {RequestMethod.GET, RequestMethod.DELETE};
        HandlerKey handlerKey1 = new HandlerKey(urls1, methods1);

        String[] urls2 = new String[] {"/test1", "/test2"};
        RequestMethod[] methods2 = new RequestMethod[] {RequestMethod.GET, RequestMethod.DELETE};
        HandlerKey handlerKey2 = new HandlerKey(urls2, methods2);

        logger.debug("handlerKey1: {}", handlerKey1);
        logger.debug("handlerKey2: {}", handlerKey2);
        assertEquals(handlerKey1, handlerKey2);
    }
    
    @DisplayName("핸들러키 URL이 다른 경우")
    @Test
    void notEqualsTestInUrl() {
        String[] urls1 = new String[] {"/test1", "/test2"};
        RequestMethod[] methods1 = new RequestMethod[] {RequestMethod.GET, RequestMethod.DELETE};
        HandlerKey handlerKey1 = new HandlerKey(urls1, methods1);

        String[] urls2 = new String[] {"/test1", "/test3"};
        RequestMethod[] methods2 = new RequestMethod[] {RequestMethod.GET, RequestMethod.DELETE};
        HandlerKey handlerKey2 = new HandlerKey(urls2, methods2);

        logger.debug("handlerKey1: {}", handlerKey1);
        logger.debug("handlerKey2: {}", handlerKey2);
        assertNotEquals(handlerKey1, handlerKey2);
    }

    @DisplayName("핸들러키 method가 다른 경우")
    @Test
    void notEqualsTestInMethod() {
        String[] urls1 = new String[] {"/test1", "/test2"};
        RequestMethod[] methods1 = new RequestMethod[] {RequestMethod.GET, RequestMethod.POST};
        HandlerKey handlerKey1 = new HandlerKey(urls1, methods1);

        String[] urls2 = new String[] {"/test1", "/test2"};
        RequestMethod[] methods2 = new RequestMethod[] {RequestMethod.GET, RequestMethod.TRACE};
        HandlerKey handlerKey2 = new HandlerKey(urls2, methods2);

        logger.debug("handlerKey1: {}", handlerKey1);
        logger.debug("handlerKey2: {}", handlerKey2);
        assertNotEquals(handlerKey1, handlerKey2);
    }
}
