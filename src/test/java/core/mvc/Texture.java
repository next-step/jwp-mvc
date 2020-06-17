package core.mvc;

import core.mvc.param.extractor.context.ContextValueExtractor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class Texture {
    public static MockHttpServletRequest request = new MockHttpServletRequest();

    static {
        Map<Class<?>, Object> context = new HashMap<>();
        context.put(HttpServletRequest.class, request);
        context.put(HttpServletResponse.class, new MockHttpServletResponse());
        context.put(HttpSession.class, new MockHttpSession());

        request.setAttribute(ContextValueExtractor.CONTEXTS, context);
    }
}
