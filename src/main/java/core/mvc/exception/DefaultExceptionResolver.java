package core.mvc.exception;

import core.mvc.ModelAndView;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public enum DefaultExceptionResolver implements ExceptionResolver{

    NO_HANDLER_FOUND(NoHandlerFoundException.class) {
        @Override
        public ModelAndView resolveException(ServletException ex, HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
            return null;
        }
    },
    INTERNAL_SERVER_ERROR(ServletException.class) {
        @Override
        public ModelAndView resolveException(ServletException ex, HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
            return null;
        }
    };

    private final Class<? extends ServletException> type;

    DefaultExceptionResolver(Class<? extends ServletException> type) {
        this.type = type;
    }

    public static DefaultExceptionResolver from(ServletException ex) {
        return Arrays.stream(values())
                .filter(defaultExceptionResolver -> defaultExceptionResolver.type.equals(ex.getClass()))
                .findFirst()
                .orElseGet(() -> DefaultExceptionResolver.INTERNAL_SERVER_ERROR);
    }
}
