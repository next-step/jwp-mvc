package next.controller;

import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static core.mvc.asis.DispatcherServlet.DEFAULT_PACKAGE;

public class AnnotationController {

    private static final AnnotationHandlerMapping handlerMapping;

    static {
        handlerMapping = new AnnotationHandlerMapping(DEFAULT_PACKAGE);
        handlerMapping.initialize();
    }

    public static ModelAndView execute(HttpServletRequest request) {
        return handlerMapping.handle(request, new MockHttpServletResponse());
    }

    public static ModelAndView execute(HttpServletRequest request, HttpServletResponse response) {
        return handlerMapping.handle(request, response);
    }

    public static ModelAndView execute(String method, String url) {
        MockHttpServletRequest request = new MockHttpServletRequest(method, url);
        return handlerMapping.handle(request, new MockHttpServletResponse());
    }
}

