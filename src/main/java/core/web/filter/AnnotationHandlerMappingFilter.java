package core.web.filter;

import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class AnnotationHandlerMappingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMappingFilter.class);

    private AnnotationHandlerMapping annotationHandlerMapping;

    @Override
    public void init(FilterConfig filterConfig) {
        annotationHandlerMapping = new AnnotationHandlerMapping("next.controller");
        annotationHandlerMapping.initialize();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HandlerExecution handlerExecution = annotationHandlerMapping.getHandler((HttpServletRequest) request);
        if (handlerExecution == null) {
            chain.doFilter(request, response);
            return;
        }
        executeHandler(request, response, handlerExecution);
    }

    private void executeHandler(ServletRequest request, ServletResponse response, HandlerExecution handlerExecution) {
        try {
            handlerExecution.handle((HttpServletRequest) request, (HttpServletResponse) response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }
}
