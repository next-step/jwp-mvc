package core.mvc.exception;

import javax.servlet.http.HttpServletRequest;

public class NotFoundException extends RuntimeException {

    public NotFoundException(HttpServletRequest request) {
        super(String.format(
                "Method : %s, Request URI : %s",
                request.getMethod(),
                request.getRequestURI()
        ));
    }
}
