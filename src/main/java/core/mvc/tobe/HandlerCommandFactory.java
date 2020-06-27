package core.mvc.tobe;

import core.mvc.asis.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.InvalidParameterException;

public class HandlerCommandFactory {

    public static HandlerCommand create(final Object handler, final HttpServletRequest request, final HttpServletResponse response) throws InvalidParameterException {
        if (handler instanceof Controller) {
            return new RequestHandlerCommand(request, response);
        } else if (handler instanceof HandlerExecution) {
            return new AnnotationHandlerCommand(request, response);
        }

        throw new InvalidParameterException();
    }
}
